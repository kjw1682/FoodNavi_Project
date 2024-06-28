package com.demo.config;

import java.io.File;

public class PathConfig {
	public static String path = System.getProperty("user.dir")+ File.separator;
	
	public static String realPath(String file) {
		return path + file;		
	}

	public static String intelliJPath = "FoodNavi\\src\\main\\resources\\static\\assets\\foodimages\\";

	public static String eclipsePath = "src\\main\\resources\\static\\assets\\foodimages\\";

	public static String existsPath = realPath(intelliJPath.substring(0, intelliJPath.length() - 1));

	public static boolean isExistsPath() {
		File file = new File(existsPath);
		boolean exists = file.exists();

		return  exists;
	}
}
