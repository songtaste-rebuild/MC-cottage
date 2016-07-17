package com.mccottage.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mccottage.base.BaseService;
import com.mccottage.entity.User;
import com.mccottage.entity.UserExample;
import com.mccottage.entity.UserGroupRelation;
import com.mccottage.entity.UserGroupRelationExample;
import com.mccottage.service.UserService;
import com.mccottage.utils.Result;

@Service
public class UserServiceImpl extends BaseService implements UserService{
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	public Result registerUser(User user) {
		log.debug("register user : " + user);
		try {
			userMapper.insert(user);
		} catch (Exception ex) {
			log.error("register user error , message " + ex.getMessage());
			return new Result().getError("register error");
		}
		return new Result().setSuccess(true);
	}

	public Result<User> login(User user) {
		log.debug("user : " + user.getUserName() + "login...");
		Result<User> result = new Result<User>();
		try {
			UserExample userExample = new UserExample();
			userExample.or().andUserNameEqualTo(user.getUserName());
			userExample.or().andPasswordEqualTo(user.getPassword());
			List<User> userList = userMapper.selectByExample(userExample);
			if (userList != null && userList.size() > 0) {
				user = userList.get(0);
				result.setContext(user);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setErrorMsg("用户不存在");
			}
		} catch (Exception ex) {
			log.error("login error : login Info : " + user);
		}
		return result;
	}

	@SuppressWarnings("null")
	public Result<Object> updateUser(User user) {
		log.debug("updateUser params : user " + user);
		Result<Object> result = null;
		try {
			userMapper.updateByPrimaryKeySelective(user);
			result.setSuccess(false);
		} catch (Exception ex) {
			result = Result.getError("update error");
			log.error("update user error ");
		}
		return result;
	}

	public boolean addUserGroup(Long userId, List<Long> userGroupIdList) {
		log.debug("addUserGroup : userId  = " + userId + " userGroupList = " + userGroupIdList);
		try {
			for (Long groupId : userGroupIdList) {
				UserGroupRelation userGroupRelation = new UserGroupRelation();
				userGroupRelation.setCreateTime(new Date());
				userGroupRelation.setGroupId(groupId);
				userGroupRelation.setUseId(userId);
				userGroupRelationMapper.insert(userGroupRelation);
			}
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

	public boolean removeUserFromGroup(Long userId, List<Long> userGroupIdList) {
		log.debug("remove user from group , userId = " + userId + ", userGroupList = " + userGroupIdList);
		try {
			for (Long groupId : userGroupIdList) {
				UserGroupRelationExample userGroupRelationExample = new UserGroupRelationExample();
				userGroupRelationExample.or().andUserIdEqualTo(userId).andGroupIdEqualTo(groupId);
				userGroupRelationMapper.deleteByExample(userGroupRelationExample);
			}
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

	public List<User> searchUserByExample(UserExample userExample) {
		log.debug("searchUser..");
		try {
			List<User> userList = userMapper.selectByExample(userExample);
			return userList;
		} catch (Exception ex) {
			log.error("error Message : " + ex.getMessage());
		}
		return new ArrayList<User>();
	}
}
