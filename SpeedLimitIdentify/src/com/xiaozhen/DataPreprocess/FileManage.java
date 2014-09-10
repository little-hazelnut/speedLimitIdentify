package com.xiaozhen.DataPreprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManage {  
	   
	 /** 
	  * @param path 
	  *            文件路径 
	  * @param suffix 
	  *            后缀名, 为空则表示所有文件 
	  * @param isdepth 
	  *            是否遍历子目录 
	  * @return list 
	  * 		      返回一个file对象的列表
	  */  
	 public static List<File> getListFiles(String path, String suffix, boolean isdepth) {  
		 List<File> Files = new ArrayList<File>();  
		 File file = new File(path);  
		 return FileManage.listFile(Files, file, suffix, isdepth);  
	 }  
	  
	 private static List<File> listFile(List<File> Files, File f, String suffix, boolean isdepth) {  
		 // 若是目录, 采用递归的方法遍历子目录    
		 if (f.isDirectory()) {  
			 File[] t = f.listFiles();  
			 for (int i = 0; i < t.length; i++) {  
				 if (isdepth || t[i].isFile()){  
					 listFile(Files, t[i], suffix, isdepth);  
				 }
			 }     
		 }else{ 
			 String filePath = f.getAbsolutePath();     
			 if (!suffix.equals("")) {  
				 int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引  
				 String tempsuffix = "";  
				 if (begIndex != -1) {  
					 tempsuffix = filePath.substring(begIndex + 1, filePath.length());  
					 if (tempsuffix.equals(suffix)) {  
						 Files.add(f);  
					 }  
				 }  
			 }else{  
				 Files.add(f);  
			 }  
		} 
	  	return Files;  
	 }  
}  