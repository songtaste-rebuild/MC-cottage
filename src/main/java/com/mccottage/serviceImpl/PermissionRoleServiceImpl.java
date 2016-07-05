package com.mccottage.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mccottage.base.BaseService;
import com.mccottage.entity.Permission;
import com.mccottage.entity.Role;
import com.mccottage.entity.RolePermissionRelation;
import com.mccottage.entity.RolePermissionRelationExample;
import com.mccottage.entity.User;
import com.mccottage.service.PermissionRoleService;

public class PermissionRoleServiceImpl extends BaseService implements PermissionRoleService {

	private static final Logger log = Logger.getLogger(PermissionRoleService.class);

	public Role getRoleByUserId(Long userId) {
		log.debug("getRoleByUserId = " + userId);
		try {
			User user = userMapper.selectByPrimaryKey(userId);
			Long roleId;
			if ((roleId = user.getRoleId()) != null) {
				log.error("user is illagel");
			} else {
				return roleMapper.selectByPrimaryKey(roleId);
			}
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return null;
	}

	public List<Permission> getPermissionByRole(Long roleId) {
		log.debug("getPermissionByRole roleId = " + roleId);
		try {
			RolePermissionRelationExample rolePermissionRelationExample = new RolePermissionRelationExample();
			rolePermissionRelationExample.or().andRoleIdEqualTo(roleId);
			// 获取关系集合
			List<RolePermissionRelation> rolePermissionRelationList = rolePermissionRelationMapper
					.selectByExample(rolePermissionRelationExample);

			List<Permission> permissionList = new ArrayList<Permission>();
			// 获取所有权限
			for (RolePermissionRelation rpr : rolePermissionRelationList) {
				permissionList.add(permissionMapper.selectByPrimaryKey(rpr.getPermissionId()));
			}
			return permissionList;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return null;
	}

}
