package com.mccottage.service;

import com.mccottage.entity.MusicType;

/**
 * 
 * @author micro
 * @date 2016/7/2
 */
public interface MusicTypeService {
	// music add type
	public boolean addMusicType(MusicType musicType);
	
	// delete type
	public boolean deleteMusicType(Long musicTypeId);
}
