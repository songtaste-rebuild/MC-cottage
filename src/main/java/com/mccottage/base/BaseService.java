package com.mccottage.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.mccottage.dao.AlbumMapper;
import com.mccottage.dao.MusicAlbumRelationMapper;
import com.mccottage.dao.MusicMapper;
import com.mccottage.dao.MusicTypeMapper;
import com.mccottage.dao.PermissionMapper;
import com.mccottage.dao.RoleMapper;
import com.mccottage.dao.RolePermissionRelationMapper;
import com.mccottage.dao.UserGroupMapper;
import com.mccottage.dao.UserGroupRelationMapper;
import com.mccottage.dao.UserMapper;

/**
 * 
 * @author mapc BaseService.java 2016 6 20
 * @Description 业务层基础支持类
 */
public abstract class BaseService {
	
	@Autowired
	protected MusicMapper musicMapper;
	
	@Autowired
	protected AlbumMapper albumMapper;
	
	@Autowired
	protected MusicAlbumRelationMapper musicAlbumRelationMapper;
	
	@Autowired
	protected MusicTypeMapper  musicTypeMapper;
	
	@Autowired
	protected PermissionMapper permissionMapper;
	
	@Autowired
	protected RoleMapper roleMapper;
	
	@Autowired
	protected RolePermissionRelationMapper rolePermissionRelationMapper;
	
	@Autowired
	protected UserGroupMapper userGroupMapper;
	
	@Autowired
	protected UserGroupRelationMapper userGroupRelationMapper;
	
	@Autowired
	protected UserMapper userMapper;

}
