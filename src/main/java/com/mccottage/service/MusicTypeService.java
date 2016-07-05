package com.mccottage.service;

import com.mccottage.entity.MusicType;

/**
 * 
 * @author mapc
 * MusicTypeService.java
 * 2016年7月1日
 * @Description
 */
public interface MusicTypeService {
	// music add type
	public boolean addMusicType(MusicType musicType);
	
	// delete type
	public boolean deleteMusicType(Long musicTypeId);
}
