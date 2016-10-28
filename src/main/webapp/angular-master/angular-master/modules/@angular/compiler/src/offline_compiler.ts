/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */

import {SchemaMetadata} from '@angular/core';

import {AnimationCompiler} from './animation/animation_compiler';
import {AnimationParser} from './animation/animation_parser';
import {CompileDirectiveMetadata, CompileIdentifierMetadata, CompileNgModuleMetadata, CompilePipeMetadata, CompileProviderMetadata, StaticSymbol, createHostComponentMeta} from './compile_metadata';
import {DirectiveNormalizer} from './directive_normalizer';
import {DirectiveWrapperCompileResult, DirectiveWrapperCompiler} from './directive_wrapper_compiler';
import {ListWrapper, MapWrapper} from './facade/collection';
import {Identifiers, resolveIdentifier, resolveIdentifierToken} from './identifiers';
import {CompileMetadataResolver} from './metadata_resolver';
import {NgModuleCompiler} from './ng_module_compiler';
import {OutputEmitter} from './output/abstract_emitter';
import * as o from './output/output_ast';
import {CompiledStylesheet, StyleCompiler} from './style_compiler';
import {TemplateParser} from './template_parser/template_parser';
import {ComponentFactoryDependency, DirectiveWrapperDependency, ViewCompileResult, ViewCompiler, ViewFactoryDependency} from './view_compiler/view_compiler';

export class SourceModule {
  constructor(public fileUrl: string, public moduleUrl: string, public source: string) {}
}

// Returns all the source files and a mapping from modules to directives
export function analyzeNgModules(
    programStaticSymbols: StaticSymbol[], options: {transitiveModules: boolean},
    metadataResolver: CompileMetadataResolver): {
  ngModuleByPipeOrDirective: Map<StaticSymbol, CompileNgModuleMetadata>,
  files: Array<{srcUrl: string, directives: StaticSymbol[], ngModules: StaticSymbol[]}>
} {
  const {
    ngModules: programNgModules,
    pipesAndDirectives: programPipesOrDirectives,
  } = _extractModulesAndPipesOrDirectives(programStaticSymbols, metadataResolver);

  const moduleMetasByRef = new Map<any, CompileNgModuleMetadata>();

  programNgModules.forEach(modMeta => {
    if (options.transitiveModules) {
      // For every input modules add the list of transitively included modules
      modMeta.transitiveModule.modules.forEach(
          modMeta => { moduleMetasByRef.set(modMeta.type.reference, modMeta); });
    } else {
      moduleMetasByRef.set(modMeta.type.reference, modMeta);
    }
  });

  const ngModuleMetas = MapWrapper.values(moduleMetasByRef);
  const ngModuleByPipeOrDirective = new Map<StaticSymbol, CompileNgModuleMetadata>();
  const ngModulesByFile = new Map<string, StaticSymbol[]>();
  const ngDirectivesByFile = new Map<string, StaticSymbol[]>();
  const filePaths = new Set<string>();

  // Looping over all modules to construct:
  // - a map from file to modules `ngModulesByFile`,
  // - a map from file to directives `ngDirectivesByFile`,
  // - a map from directive/pipe to module `ngModuleByPipeOrDirective`.
  ngModuleMetas.forEach((ngModuleMeta) => {
    const srcFileUrl = ngModuleMeta.type.reference.filePath;
    filePaths.add(srcFileUrl);
    ngModulesByFile.set(
        srcFileUrl, (ngModulesByFile.get(srcFileUrl) || []).concat(ngModuleMeta.type.reference));

    ngModuleMeta.declaredDirectives.forEach((dirMeta: CompileDirectiveMetadata) => {
      const fileUrl = dirMeta.type.reference.filePath;
      filePaths.add(fileUrl);
      ngDirectivesByFile.set(
          fileUrl, (ngDirectivesByFile.get(fileUrl) || []).concat(dirMeta.type.reference));
      ngModuleByPipeOrDirective.set(dirMeta.type.reference, ngModuleMeta);
    });

    ngModuleMeta.declaredPipes.forEach((pipeMeta: CompilePipeMetadata) => {
      const fileUrl = pipeMeta.type.reference.filePath;
      filePaths.add(fileUrl);
      ngModuleByPipeOrDirective.set(pipeMeta.type.reference, ngModuleMeta);
    });
  });

  // Throw an error if any of the program pipe or directives is not declared by a module
  const symbolsMissingModule =
      programPipesOrDirectives.filter(s => !ngModuleByPipeOrDirective.has(s));

  if (symbolsMissingModule.length) {
    const messages = symbolsMissingModule.map(
        s => `Cannot determine the module for class ${s.name} in ${s.filePath}!`);
    throw new Error(messages.join('\n'));
  }

  const files: {srcUrl: string, directives: StaticSymbol[], ngModules: StaticSymbol[]}[] = [];

  filePaths.forEach((srcUrl) => {
    const directives = ngDirectivesByFile.get(srcUrl) || [];
    const ngModules = ngModulesByFile.get(srcUrl) || [];
    files.push({srcUrl, directives, ngModules});
  });

  return {
      // map directive/pipe to module
      ngModuleByPipeOrDirective,
      // list modules and directives for every source file
      files,
  };
}

export class OfflineCompiler {
  private _animationParser = new AnimationParser();
  private _animationCompiler = new AnimationCompiler();

  constructor(
      private _metadataResolver: CompileMetadataResolver,
      private _directiveNormalizer: DirectiveNormalizer, private _templateParser: TemplateParser,
      private _styleCompiler: StyleCompiler, private _viewCompiler: ViewCompiler,
      private _dirWrapperCompiler: DirectiveWrapperCompiler,
      private _ngModuleCompiler: NgModuleCompiler, private _outputEmitter: OutputEmitter,
      private _localeId: string, private _translationFormat: string) {}

  clearCache() {
    this._directiveNormalizer.clearCache();
    this._metadataResolver.clearCache();
  }

  compileModules(staticSymbols: StaticSymbol[], options: {transitiveModules: boolean}):
      Promise<SourceModule[]> {
    const {ngModuleByPipeOrDirective, files} =
        analyzeNgModules(staticSymbols, options, this._metadataResolver);

    const sourceModules = files.map(
        file => this._compileSrcFile(
            file.srcUrl, ngModuleByPipeOrDirective, file.directives, file.ngModules));

    return Promise.all(sourceModules)
        .then((modules: SourceModule[][]) => ListWrapper.flatten(modules));
  }

  private _compileSrcFile(
      srcFileUrl: string, ngModuleByPipeOrDirective: Map<StaticSymbol, CompileNgModuleMetadata>,
      directives: StaticSymbol[], ngModules: StaticSymbol[]): Promise<SourceModule[]> {
    const fileSuffix = _splitTypescriptSuffix(srcFileUrl)[1];
    const statements: o.Statement[] = [];
    const exportedVars: string[] = [];
    const outputSourceModules: SourceModule[] = [];

    // compile all ng modules
    exportedVars.push(
        ...ngModules.map((ngModuleType) => this._compileModule(ngModuleType, statements)));

    // compile directive wrappers
    exportedVars.push(...directives.map(
        (directiveType) => this._compileDirectiveWrapper(directiveType, statements)));

    // compile components
    return Promise
        .all(directives.map((dirType) => {
          const compMeta = this._metadataResolver.getDirectiveMetadata(<any>dirType);
          if (!compMeta.isComponent) {
            return Promise.resolve(null);
          }
          const ngModule = ngModuleByPipeOrDirective.get(dirType);
          if (!ngModule) {
            throw new Error(
                `Internal Error: cannot determine the module for component ${compMeta.type.name}!`);
          }

          return Promise
              .all([compMeta, ...ngModule.transitiveModule.directives].map(
                  dirMeta => this._directiveNormalizer.normalizeDirective(dirMeta).asyncResult))
              .then((normalizedCompWithDirectives) => {
                const [compMeta, ...dirMetas] = normalizedCompWithDirectives;
                _assertComponent(compMeta);

                // compile styles
                const stylesCompileResults = this._styleCompiler.compileComponent(compMeta);
                stylesCompileResults.externalStylesheets.forEach((compiledStyleSheet) => {
                  outputSourceModules.push(
                      this._codgenStyles(srcFileUrl, compiledStyleSheet, fileSuffix));
                });

                // compile components
                exportedVars.push(
                    this._compileComponentFactory(compMeta, fileSuffix, statements),
                    this._compileComponent(
                        compMeta, dirMetas, ngModule.transitiveModule.pipes, ngModule.schemas,
                        stylesCompileResults.componentStylesheet, fileSuffix, statements));
              });
        }))
        .then(() => {
          if (statements.length > 0) {
            const srcModule = this._codegenSourceModule(
                srcFileUrl, _ngfactoryModuleUrl(srcFileUrl), statements, exportedVars);
            outputSourceModules.unshift(srcModule);
          }
          return outputSourceModules;
        });
  }

  private _compileModule(ngModuleType: StaticSymbol, targetStatements: o.Statement[]): string {
    const ngModule = this._metadataResolver.getNgModuleMetadata(ngModuleType);
    const providers: CompileProviderMetadata[] = [];

    if (this._localeId) {
      providers.push(new CompileProviderMetadata({
        token: resolveIdentifierToken(Identifiers.LOCALE_ID),
        useValue: this._localeId,
      }));
    }

    if (this._translationFormat) {
      providers.push(new CompileProviderMetadata({
        token: resolveIdentifierToken(Identifiers.TRANSLATIONS_FORMAT),
        useValue: this._translationFormat
      }));
    }

    const appCompileResult = this._ngModuleCompiler.compile(ngModule, providers);

    appCompileResult.dependencies.forEach((dep) => {
      dep.placeholder.name = _componentFactoryName(dep.comp);
      dep.placeholder.moduleUrl = _ngfactoryModuleUrl(dep.comp.moduleUrl);
    });

    targetStatements.push(...appCompileResult.statements);
    return appCompileResult.ngModuleFactoryVar;
  }

  private _compileDirectiveWrapper(directiveType: StaticSymbol, targetStatements: o.Statement[]):
      string {
    const dirMeta = this._metadataResolver.getDirectiveMetadata(directiveType);
    const dirCompileResult = this._dirWrapperCompiler.compile(dirMeta);

    targetStatements.push(...dirCompileResult.statements);
    return dirCompileResult.dirWrapperClassVar;
  }

  private _compileComponentFactory(
      compMeta: CompileDirectiveMetadata, fileSuffix: string,
      targetStatements: o.Statement[]): string {
    const hostMeta = createHostComponentMeta(compMeta);
    const hostViewFactoryVar =
        this._compileComponent(hostMeta, [compMeta], [], [], null, fileSuffix, targetStatements);
    const compFactoryVar = _componentFactoryName(compMeta.type);
    targetStatements.push(
        o.variable(compFactoryVar)
            .set(o.importExpr(resolveIdentifier(Identifiers.ComponentFactory), [o.importType(
                                                                                   compMeta.type)])
                     .instantiate(
                         [
                           o.literal(compMeta.selector),
                           o.variable(hostViewFactoryVar),
                           o.importExpr(compMeta.type),
                         ],
                         o.importType(
                             resolveIdentifier(Identifiers.ComponentFactory),
                             [o.importType(compMeta.type)], [o.TypeModifier.Const])))
            .toDeclStmt(null, [o.StmtModifier.Final]));
    return compFactoryVar;
  }

  private _compileComponent(
      compMeta: CompileDirectiveMetadata, directives: CompileDirectiveMetadata[],
      pipes: CompilePipeMetadata[], schemas: SchemaMetadata[], componentStyles: CompiledStylesheet,
      fileSuffix: string, targetStatements: o.Statement[]): string {
    const parsedAnimations = this._animationParser.parseComponent(compMeta);
    const parsedTemplate = this._templateParser.parse(
        compMeta, compMeta.template.template, directives, pipes, schemas, compMeta.type.name);
    const stylesExpr = componentStyles ? o.variable(componentStyles.stylesVar) : o.literalArr([]);
    const compiledAnimations =
        this._animationCompiler.compile(compMeta.type.name, parsedAnimations);
    const viewResult = this._viewCompiler.compileComponent(
        compMeta, parsedTemplate, stylesExpr, pipes, compiledAnimations);
    if (componentStyles) {
      targetStatements.push(..._resolveStyleStatements(componentStyles, fileSuffix));
    }
    compiledAnimations.forEach(
        entry => { entry.statements.forEach(statement => { targetStatements.push(statement); }); });
    targetStatements.push(..._resolveViewStatements(viewResult));
    return viewResult.viewFactoryVar;
  }

  private _codgenStyles(
      fileUrl: string, stylesCompileResult: CompiledStylesheet, fileSuffix: string): SourceModule {
    _resolveStyleStatements(stylesCompileResult, fileSuffix);
    return this._codegenSourceModule(
        fileUrl, _stylesModuleUrl(
                     stylesCompileResult.meta.moduleUrl, stylesCompileResult.isShimmed, fileSuffix),
        stylesCompileResult.statements, [stylesCompileResult.stylesVar]);
  }

  private _codegenSourceModule(
      fileUrl: string, moduleUrl: string, statements: o.Statement[],
      exportedVars: string[]): SourceModule {
    return new SourceModule(
        fileUrl, moduleUrl,
        this._outputEmitter.emitStatements(moduleUrl, statements, exportedVars));
  }
}

function _resolveViewStatements(compileResult: ViewCompileResult): o.Statement[] {
  compileResult.dependencies.forEach((dep) => {
    if (dep instanceof ViewFactoryDependency) {
      const vfd = <ViewFactoryDependency>dep;
      vfd.placeholder.moduleUrl = _ngfactoryModuleUrl(vfd.comp.moduleUrl);
    } else if (dep instanceof ComponentFactoryDependency) {
      const cfd = <ComponentFactoryDependency>dep;
      cfd.placeholder.name = _componentFactoryName(cfd.comp);
      cfd.placeholder.moduleUrl = _ngfactoryModuleUrl(cfd.comp.moduleUrl);
    } else if (dep instanceof DirectiveWrapperDependency) {
      const dwd = <DirectiveWrapperDependency>dep;
      dwd.placeholder.moduleUrl = _ngfactoryModuleUrl(dwd.dir.moduleUrl);
    }
  });
  return compileResult.statements;
}


function _resolveStyleStatements(
    compileResult: CompiledStylesheet, fileSuffix: string): o.Statement[] {
  compileResult.dependencies.forEach((dep) => {
    dep.valuePlaceholder.moduleUrl = _stylesModuleUrl(dep.moduleUrl, dep.isShimmed, fileSuffix);
  });
  return compileResult.statements;
}

function _ngfactoryModuleUrl(dirUrl: string): string {
  const urlWithSuffix = _splitTypescriptSuffix(dirUrl);
  return `${urlWithSuffix[0]}.ngfactory${urlWithSuffix[1]}`;
}

function _componentFactoryName(comp: CompileIdentifierMetadata): string {
  return `${comp.name}NgFactory`;
}

function _stylesModuleUrl(stylesheetUrl: string, shim: boolean, suffix: string): string {
  return shim ? `${stylesheetUrl}.shim${suffix}` : `${stylesheetUrl}${suffix}`;
}

function _assertComponent(meta: CompileDirectiveMetadata) {
  if (!meta.isComponent) {
    throw new Error(`Could not compile '${meta.type.name}' because it is not a component.`);
  }
}

function _splitTypescriptSuffix(path: string): string[] {
  if (path.endsWith('.d.ts')) {
    return [path.slice(0, -5), '.ts'];
  }

  const lastDot = path.lastIndexOf('.');

  if (lastDot !== -1) {
    return [path.substring(0, lastDot), path.substring(lastDot)];
  }

  return [path, ''];
}

// Group the symbols by types:
// - NgModules,
// - Pipes and Directives.
function _extractModulesAndPipesOrDirectives(
    programStaticSymbols: StaticSymbol[], metadataResolver: CompileMetadataResolver) {
  const ngModules: CompileNgModuleMetadata[] = [];
  const pipesAndDirectives: StaticSymbol[] = [];

  programStaticSymbols.forEach(staticSymbol => {
    const ngModule = metadataResolver.getNgModuleMetadata(staticSymbol, false);
    const directive = metadataResolver.getDirectiveMetadata(staticSymbol, false);
    const pipe = metadataResolver.getPipeMetadata(<any>staticSymbol, false);

    if (ngModule) {
      ngModules.push(ngModule);
    } else if (directive) {
      pipesAndDirectives.push(staticSymbol);
    } else if (pipe) {
      pipesAndDirectives.push(staticSymbol);
    }
  });

  return {ngModules, pipesAndDirectives};
}
