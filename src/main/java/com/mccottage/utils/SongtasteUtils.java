package com.mccottage.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/*
 * this programe writted by micro
 */
public class SongtasteUtils {

	private static String strUrl, st_songid, t;

	private static String musicName;

	private static final String outFilePath = "H:" + File.separator
			+ "songtaste download" + File.separator + "page11" + File.separator;

	// 文件后缀
	public static final String FILE_SUFFIX = ".mp3";

	// 网站
	public static final String SONGTASTE = "http://www.songtaste.com";

	public static final Logger log = Logger.getLogger(SongtasteUtils.class);

	// 歌曲页面下载
	public static boolean downloadByPage(String pageUrl) throws Exception {
		try {
			String htmlContext = sendRequest(pageUrl, true);

			// 获取到所有歌曲url列表
			List<String> urlStringList = new ArrayList<String>();

			while (htmlContext.indexOf("<td class=\"singer\"><a href='") != -1) {
				int start = htmlContext
						.indexOf("<td class=\"singer\"><a href='")
						+ "<td class=\"singer\"><a href='".length();
				int end = start + "/song/278510/".length();
				urlStringList.add(htmlContext.substring(start, end));
				htmlContext = htmlContext.substring(start);
			}
			log.debug("获取到页面所有音乐的url:" + urlStringList);
			int successNum = 0;
			for (String url : urlStringList) {
				if (downloadFromSongtastePageUrl(SONGTASTE + url)) {
					successNum++;
				} else {
					log.error("download false by url : " + SONGTASTE + url);
				}
			}
			log.debug("成功下载歌曲：" + successNum + "首");
		} catch (Exception e) {
			throw new Exception("download false");
		}
		return true;
	}

	// 收藏夹页面下载
	public static boolean downloadFromSongtastePageUrl(String url) throws Exception {
		try {

			String htmlContext = sendRequest(url, false);

			setDuoMiUrlBySongtasteHtml(htmlContext);

			String duomiUrl = getDuomiUrl();

			download(duomiUrl);
		} catch (Exception ex) {
			log.error("下载收藏页失败");
			throw new Exception("download false");
		}
		return true;
	}

	private static String getDuomiUrl() throws Exception {
		String requestDuomiUrl = "http://www.songtaste.com" + "/time.php?str="
				+ strUrl + "&sid=" + st_songid + "&t=" + t;
		log.debug("request to duomi url :" + requestDuomiUrl);
		return sendRequest(requestDuomiUrl, false);
	}

	// 发送url请求返回context
	public static String sendRequest(String urlString, boolean isCookie)
			throws Exception {
		log.debug("request url : " + urlString);

		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		connection.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connection.setRequestProperty("connection", "keep-alive");
		connection.setRequestProperty("accept", "*/*");

		if (isCookie) {
			String cookie = "bdshare_firstime=1465568036970; PHPSESSID=05d23749f77332d207fdb81edb1d42b4; test=123456; CookName=%E9%B9%8F%E7%B1%B3; CookID=592920; CookPwd=2af7cac204ae7073508e058fb5186d21; CookIcon=592920.jpg; CookDmID=35431489; __utmt=1; Hm_lvt_fd19d6b371be7ce51c3b62b0dac6535e=1466305138; Hm_lpvt_fd19d6b371be7ce51c3b62b0dac6535e=1466337185; valid=1; valid=1; __utma=148846773.1671464521.1466305136.1466305136.1466334568.2; __utmb=148846773.21.10.1466334568; __utmc=148846773; __utmz=148846773.1466305136.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
			connection.setRequestProperty("Cookie", cookie);
			log.debug("send cookie : " + cookie);
		}

		connection.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String oneLine = null;

		StringBuffer htmlContext = new StringBuffer();
		while ((oneLine = reader.readLine()) != null) {
			if (oneLine.length() > 2 && (oneLine.substring(0, 2).equals("//"))) {
				oneLine = oneLine.substring(2);
			}
			htmlContext.append(oneLine).append("\n");
		}

		reader.close();
		return htmlContext.toString();
	}

	// 处理下载任务
	private static void download(String downloadUrl) throws Exception {
		if (downloadUrl.contains("404.html")) {
			throw new Exception("歌曲资源不存在");
		}
		URL url = new URL(downloadUrl);
		log.debug("下载链接为:" + downloadUrl);
		URLConnection connection = url.openConnection();

		connection.connect();
		// 读取字节流写入文件
		InputStream in = connection.getInputStream();
		log.debug("文件尺寸为:" + (double) connection.getContentLength()
				/ (1024 * 1024) + "MB");
		byte[] b = new byte[1024 * 10];

		File file = new File(outFilePath + musicName + FILE_SUFFIX);
		if (file.exists()) {
			log.debug("file exits , download next");
			return;
		}
		FileOutputStream out = new FileOutputStream(file);
		int len;
		int sum = 0;
		int size = connection.getContentLength();
		while ((len = in.read(b)) > 0) {
			out.write(b, 0, len);
			sum += len;
			log.debug("downloading " + (sum * 100) / size + "%");
		}

		in.close();
		out.close();

	}

	// 获取向多米发送请求的url
	private static void setDuoMiUrlBySongtasteHtml(String htmlContext) {
		// 获取3个参数strURL,st_songid,t
		String begainString = "<a href=\"javascript:playmedia1('playicon','player', '";
		int begain = htmlContext.indexOf(begainString);
		begain += begainString.length();
		int end = htmlContext.indexOf(");ListenLog(");

		String[] params = htmlContext.substring(begain, end).replace(" ", "")
				.split(",");

		strUrl = params[0].substring(0, params[0].length() - 1);
		st_songid = params[5].substring(1, params[5].length() - 1);
		t = params[6];

		// 获取歌名
		String name = htmlContext.substring(htmlContext.indexOf("mid_tit")
				+ "mid_tit".length() + 2,
				htmlContext.indexOf("</p>", htmlContext.indexOf("mid_tit")));
		musicName = name;
		log.debug("music name is " + name);
	}
}
