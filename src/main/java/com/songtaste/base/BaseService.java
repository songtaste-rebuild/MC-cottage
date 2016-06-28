package com.songtaste.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.songtaste.dao.AlbumMapper;
import com.songtaste.dao.MusicAlbumRelationMapper;
import com.songtaste.dao.MusicMapper;
import com.songtaste.dao.MusicTypeMapper;
import com.songtaste.dao.PermissionMapper;
import com.songtaste.dao.RoleMapper;
import com.songtaste.dao.RolePermissionRelationMapper;
import com.songtaste.dao.UserGroupMapper;
import com.songtaste.dao.UserGroupRelationMapper;
import com.songtaste.dao.UserMapper;

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
