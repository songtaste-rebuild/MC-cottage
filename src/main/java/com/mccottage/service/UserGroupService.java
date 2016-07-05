package com.mccottage.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.mccottage.entity.UserGroup;
import com.mccottage.entity.UserGroupExample;

/**
 * 
 * @author mapc
 * UserGroupService.java
 * 2016年6月25日
 * @Description
 */
public interface UserGroupService {

	static Logger log = Logger.getLogger(UserGroupService.class);

	// add userGroup
	public boolean addUserGroup(UserGroup userGroup);

	// delete userGroup
	public boolean deleteUserGroup(Long userGroupId);

	// searchUserGroup
	public List<UserGroup> searchUserGroup(UserGroupExample userGroupExample);

	// modify userGroup
	public boolean updateUserGroup(UserGroup userGroup);
	
	// searchUserGroup by id
	public UserGroup selectUserGroupById(Long userGroupId);
}
