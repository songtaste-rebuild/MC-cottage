/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */


import {CompileDirectiveMetadata, CompileIdentifierMetadata, CompileTokenMetadata} from '../compile_metadata';
import {createDiTokenExpression} from '../compiler_util/identifier_util';
import {isPresent} from '../facade/lang';
import {Identifiers, resolveIdentifier} from '../identifiers';
import * as o from '../output/output_ast';

import {CompileView} from './compile_view';

export function getPropertyInView(
    property: o.Expression, callingView: CompileView, definedView: CompileView): o.Expression {
  if (callingView === definedView) {
    return property;
  } else {
    var viewProp: o.Expression = o.THIS_EXPR;
    var currView: CompileView = callingView;
    while (currView !== definedView && isPresent(currView.declarationElement.view)) {
      currView = currView.declarationElement.view;
      viewProp = viewProp.prop('parent');
    }
    if (currView !== definedView) {
      throw new Error(
          `Internal error: Could not calculate a property in a parent view: ${property}`);
    }
    return property.visitExpression(new _ReplaceViewTransformer(viewProp, definedView), null);
  }
}

class _ReplaceViewTransformer extends o.ExpressionTransformer {
  constructor(private _viewExpr: o.Expression, private _view: CompileView) { super(); }
  private _isThis(expr: o.Expression): boolean {
    return expr instanceof o.ReadVarExpr && expr.builtin === o.BuiltinVar.This;
  }

  visitReadVarExpr(ast: o.ReadVarExpr, context: any): any {
    return this._isThis(ast) ? this._viewExpr : ast;
  }
  visitReadPropExpr(ast: o.ReadPropExpr, context: any): any {
    if (this._isThis(ast.receiver)) {
      // Note: Don't cast for members of the AppView base class...
      if (this._view.fields.some((field) => field.name == ast.name) ||
          this._view.getters.some((field) => field.name == ast.name)) {
        return this._viewExpr.cast(this._view.classType).prop(ast.name);
      }
    }
    return super.visitReadPropExpr(ast, context);
  }
}

export function injectFromViewParentInjector(
    token: CompileTokenMetadata, optional: boolean): o.Expression {
  var args = [createDiTokenExpression(token)];
  if (optional) {
    args.push(o.NULL_EXPR);
  }
  return o.THIS_EXPR.prop('parentInjector').callMethod('get', args);
}

export function getViewFactoryName(
    component: CompileDirectiveMetadata, embeddedTemplateIndex: number): string {
  return `viewFactory_${component.type.name}${embeddedTemplateIndex}`;
}

export function createFlatArray(expressions: o.Expression[]): o.Expression {
  var lastNonArrayExpressions: o.Expression[] = [];
  var result: o.Expression = o.literalArr([]);
  for (var i = 0; i < expressions.length; i++) {
    var expr = expressions[i];
    if (expr.type instanceof o.ArrayType) {
      if (lastNonArrayExpressions.length > 0) {
        result =
            result.callMethod(o.BuiltinMethod.ConcatArray, [o.literalArr(lastNonArrayExpressions)]);
        lastNonArrayExpressions = [];
      }
      result = result.callMethod(o.BuiltinMethod.ConcatArray, [expr]);
    } else {
      lastNonArrayExpressions.push(expr);
    }
  }
  if (lastNonArrayExpressions.length > 0) {
    result =
        result.callMethod(o.BuiltinMethod.ConcatArray, [o.literalArr(lastNonArrayExpressions)]);
  }
  return result;
}

export function getHandleEventMethodName(elementIndex: number): string {
  return `handleEvent_${elementIndex}`;
}