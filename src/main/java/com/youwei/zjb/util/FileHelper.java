package com.youwei.zjb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
	public static void main(String[] args){
		listFiles(new File("D:/code/xzye/yycf/src/main/webapp/css"));
	}
	public static List<File> listFiles(File dir) {
		List<File> results = new ArrayList<File>();
		if(dir==null){
			return results;
		}
		for(File child : dir.listFiles()){
			if(child.isFile()){
				results.add(child);
			}else{
				results.addAll(listFiles(child));
			}
		}
		return results;
	}
}
