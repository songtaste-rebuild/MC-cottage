package com.songtaste.service;

import com.songtaste.utils.Result;

/*
 * this programe writted by micro
 */
public interface MusicService{
	
	// 按照st音乐详情页面的url下载一首歌
	public Result downloadMusicByOneUrl(String url);
	
	// 按照收藏夹页面下载一页音乐
	public Result downloadMusicByOnePage(String url);
	
}
