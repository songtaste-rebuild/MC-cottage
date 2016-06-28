package com.songtaste.service.impl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.songtaste.base.BaseService;
import com.songtaste.service.MusicService;
import com.songtaste.utils.Result;
import com.songtaste.utils.SongtasteUtils;

/*
 * this programe writted by micro
 */
@Service
public class MusicServiceImpl extends BaseService implements MusicService {
	
	private static final Logger log = Logger.getLogger(MusicService.class);

	public Result downloadMusicByOneUrl(String url) {
		Result result = new Result();
		log.debug("download music by one url : " + url);
		try {
			SongtasteUtils.downloadFromSongtastePageUrl(url);
			result.setSuccess(true);
		} catch (Exception ex) {
			result.setSuccess(false).setErrorMsg(ex.getMessage());
			log.error("download false");
		}
		return result;
	}

	public Result downloadMusicByOnePage(String url) {
		Result result = new Result();
		log.debug("downloadMusicByOnePage, url : " + url);
		try {
			SongtasteUtils.downloadByPage(url);
			result.setSuccess(true);
		} catch (Exception ex) {
			result.setSuccess(false).setErrorMsg(ex.getMessage());
			log.error("download false");
		}
		return result;
	}

}
