package com.mccottage.service;

import java.util.List;

import com.mccottage.entity.Permission;
import com.mccottage.entity.Role;

/**
 * 
 * @author mapc PermissionRoleService.java 2016年7月5日
 * @Description : 返回需要Result再使用，没必要统一封装成Result,这样更关注在业务上而不是代码上
 */
public interface PermissionRoleService {
	// 获取用户角色
	public Role getRoleByUserId(Long userId);

	// 获取角色权限
	public List<Permission> getPermissionByRole(Long roleId);

}
