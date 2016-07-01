package com.mccottage.service;

import java.util.List;

import com.mccottage.entity.Music;
import com.mccottage.entity.MusicExample;
import com.mccottage.utils.Result;

/*
 * this programe writted by micro
 */
public interface MusicService {

	// 按照st音乐详情页面的url下载一首歌
	public Result<Object> downloadMusicByOneUrl(String url);

	// 按照收藏夹页面下载一页音乐
	public Result<Object> downloadMusicByOnePage(String url);

	// 条件查询音乐
	public Result<List<Music>> searchMusicBySelective(MusicExample musicExample);

	// 将音乐加入到专辑
	public Result<Object> addMusicIntoAlbum(Long[] musicList, Long albumId);

	// 将音乐从专辑删除
	public Result<Object> removeMusicFromAlbum(Long[] musicList, Long ablumId);

	// 新增音乐
	public Result<Music> addMusic(Music music);

	// 删除音乐
	public Result<Object> deleteMusic(Long musicId);
	
	// 查询音乐
	public Result<List<Music>> selectMusicList(MusicExample musicExample);

}
