package com.xiaozhen.DataPreprocess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreatGrayImage {

	public static void creatImage(int[][] matrix,String destfile){
		int width = matrix[0].length;
		int height = matrix.length;
		BufferedImage grayImage=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);//无符号 byte 灰度级图像
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++){
			  grayImage.setRGB(j,i,matrix[i][j]*500);	//读入所有像素，转换图像信号,使其灰度化
			}
		File file = new File("C:/Users/lin/Desktop/test/"+destfile);
		try {
			ImageIO.write(grayImage, "jpg", file);	//在原路径下输出拉伸后的图像
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//放大矩阵,times为放大倍数
	public static int[][] mat_boost(int[][] matrix,int times){
		int row = matrix.length;
		int col = matrix[0].length;
		int[][] result = new int[row*times][col*times];
		int new_rbegin = 0;
		int new_rend = 0;
		int new_cbegin = 0;
		int new_cend = 0;
		for(int i = 0;i<row;i++)
			for(int j = 0;j<col;j++){
				new_rbegin = times*i;
				new_cbegin = times*j;
				new_rend = new_rbegin + times;
				new_cend = new_cbegin + times;
				for(int k = new_rbegin;k<new_rend;k++)
					for(int z = new_cbegin;z<new_cend;z++){
						result[k][z] = matrix[i][j];
					}
			}
		return result;
	}
	
	/*
	public static void main(String[] args){
		int[][] matrix = new int[14][14];
		for(int i = 0;i<14;i++)
			for(int j = 0;j<14;j++){
				matrix[i][j] = i*j;
			}
		matrix = mat_boost(matrix,50);
		CreatGrayImage.creatImage(matrix,"test.jpg");
	}
	*/
	
	
}
