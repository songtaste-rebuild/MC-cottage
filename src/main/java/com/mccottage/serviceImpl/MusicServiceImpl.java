package com.mccottage.serviceImpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mccottage.base.BaseService;
import com.mccottage.entity.Music;
import com.mccottage.entity.MusicExample;
import com.mccottage.service.MusicService;
import com.mccottage.utils.Result;
import com.mccottage.utils.SongtasteUtils;

@Service
public class MusicServiceImpl extends BaseService implements MusicService {

	private static final Logger log = Logger.getLogger(MusicService.class);

	public Result<Object> downloadMusicByOneUrl(String url) {
		Result<Object> result = new Result<Object>();
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

	public Result<Object> downloadMusicByOnePage(String url) {
		Result<Object> result = new Result<Object>();
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


	public Result<List<Music>> searchMusicBySelective(MusicExample musicExample) {
		Result<List<Music>> result = new Result<List<Music>>();
		try {
			List<Music> musicList = musicMapper.selectByExample(musicExample);
			if (musicList != null && musicList.size() > 0) {
				result.setContext(musicList);
			}
		} catch (Exception ex) {
			log.error("search Music by selective error, message : " + ex.getMessage());
			return Result.getError("search error");
		}
		return result.setSuccess(true);
	}

}
