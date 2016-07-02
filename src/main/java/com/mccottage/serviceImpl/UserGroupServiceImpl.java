package com.mccottage.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.mccottage.base.BaseService;
import com.mccottage.entity.UserGroup;
import com.mccottage.entity.UserGroupExample;
import com.mccottage.service.UserGroupService;

/*
 * this programe writted by micro
 */
public class UserGroupServiceImpl extends BaseService implements
		UserGroupService {

	public boolean addUserGroup(UserGroup userGroup) {
		log.debug("addUserGroup : " + userGroup);
		try {
			userGroupMapper.insert(userGroup);
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

	public boolean deleteUserGroup(Long userGroupId) {
		log.debug("delete userGroup : groupId = " + userGroupId);
		try {
			userGroupMapper.deleteByPrimaryKey(userGroupId);
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

	public List<UserGroup> searchUserGroup(UserGroupExample userGroupExample) {
		log.debug("searchUserGroup");
		try {
			List<UserGroup> userGroupList = userGroupMapper.selectByExample(userGroupExample);
			return userGroupList;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return new ArrayList<UserGroup>();
	}

	public boolean updateUserGroup(UserGroup userGroup) {
		log.debug("modify userGroup info : userGroup");
		try {
			// if property == null not set
			userGroupMapper.updateByPrimaryKeySelective(userGroup);
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

	public UserGroup selectUserGroupById(Long userGroupId) {
		log.debug("search userGroup by id : " + userGroupId);
		try {
			return userGroupMapper.selectByPrimaryKey(userGroupId);
		} catch (Exception ex) {
			log.error("error message " + ex.getMessage());
		}
		return null;
	}
}
