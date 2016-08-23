package com.mccottage.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	// 下载文件
	public static boolean getFileFromUrl(String url, String saveLocalUrl) {
		logger.debug("get file from url : " + url);
		URLConnection connection = null;
		try {
			connection = new URL(url).openConnection();
			connection.connect();
			BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
			byte[] byteContent = new byte[input.available()];
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(saveLocalUrl)));
			int data = -1;
			int pos = 0;
			while ((data = input.read()) != -1) {
				pos += data;
				out.write(byteContent, pos, data);
				System.out.println("download speed : " + (pos / input.available()) + "%");
			}
			input.close();
			out.close();
			return true;
		} catch (Exception ex) {
			logger.error("getFileFromUrl error ,stack message : " + ex.getMessage());
		} 
		return false;
	}
	
	// 复制文件
	public static boolean copylocalFile(String fromUrl, String targetUrl) {
		try {
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File(fromUrl)));
			byte[] byteContent = new byte[input.available()];
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(targetUrl)));
			int data = -1;
			int pos = 0;
			while ((data = input.read()) != -1) {
				pos += data;
				out.write(byteContent, pos, data);
				Float speed = ((float)pos * 100/ (float)input.available());
				DecimalFormat format = new DecimalFormat("#0.00");
				System.out.println("download file speed : " + format.format(speed) + "%"); // console log
			}
			input.close();
			out.close();
			logger.debug("download file : " + fromUrl + " success!");
			return true;
		} catch (Exception ex) {
			
		}
		return false;
	}
	
	// test
	public static void main(String[] args) {
		copylocalFile("E:"+ File.separator + "node-v4.4.7-x64.msi", "C:" + File.separator + "node-v4.4.7-x64.msi");
	}
}
