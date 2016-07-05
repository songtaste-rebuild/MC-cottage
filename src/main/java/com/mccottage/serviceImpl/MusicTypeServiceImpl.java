package com.mccottage.serviceImpl;

import org.apache.log4j.Logger;

import com.mccottage.base.BaseService;
import com.mccottage.entity.MusicType;
import com.mccottage.service.MusicTypeService;

/**
 * 
 * @author micro
 *
 */
public class MusicTypeServiceImpl extends BaseService implements
		MusicTypeService {
	
	private static final Logger log = Logger.getLogger(MusicTypeService.class);

	public boolean addMusicType(MusicType musicType) {
		log.debug("addMusicType : " + musicType);
		try {
			musicTypeMapper.insert(musicType);
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

	public boolean deleteMusicType(Long musicTypeId) {
		log.debug("deleteMusicType : " + musicTypeId);
		try {
			musicTypeMapper.deleteByPrimaryKey(musicTypeId);
			return true;
		} catch (Exception ex) {
			log.error("error message : " + ex.getMessage());
		}
		return false;
	}

}
