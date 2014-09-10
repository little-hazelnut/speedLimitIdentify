package com.xiaozhen.DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

//提取每个样本的特征值

public class DataProcess {
	
	String src = null;
	String dest = null;
	private int MIN_SPEED = 5;
	private int MAX_SPEED = 150;
	private int SPEED_LABEL = 40;
	
	public DataProcess(String src,String dest){
		this.src = src;
		this.dest = dest;
		
	}
	
	public void init(){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest,true)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		List<File> files = FileManage.getListFiles(src, "txt", true);
		String str = null;	
		int speed = 0;			//数据点速度
		int hour = 0;			//数据点时间
		String[] info = null;	//每个样本中每行数据的信息
		BufferedReader br = null;
		File file = null;
		String filename = null;
		
		for(int i = 0;i<files.size();i++){
			try {
				file = files.get(i);
				filename = file.getName().substring(0, file.getName().lastIndexOf('.'));
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));	
				int[][] time_series = new int[24][MAX_SPEED];  			//时间序列速度信息
				int flag = 0;											//样本包含有效数据总数
				int[] time_data_size = new int[24]; 					//样本每个时间序列包含有效数据数量
				int[] speed_frequency = new int[MAX_SPEED];				//样本总体速度频数
				
				try {
					while((str = br.readLine())!= null){
						info = str.split(",");
						if(info.length != 16)
							continue;
						speed = Integer.parseInt(info[6]);
						hour = Integer.parseInt(info[12]);
						if(speed>MIN_SPEED&&speed<MAX_SPEED){
							flag++;
							time_data_size[hour]++;
							time_series[hour][speed]++;
							speed_frequency[speed]++;
						}
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(flag == 0)
					return;
				int sum = 0;											//样本总体速度总和
				int time_series_sum = 0;								//每个时间序列速度总和
				int mspeed = 0;											//总体平均瞬时速度
				int standard_dev = 0;									//总体标准差
				int mode = 0;											//众数
				int time_series_mspeed[] = new int[24];					//每个小时段平均瞬时速度
				for(int j = 0;j<24;j++){
					time_series_sum = 0;
					for(int k = MIN_SPEED+1;k<MAX_SPEED;k++){
						time_series_sum = time_series_sum + k*time_series[j][k];
					}
					if(time_data_size[j]!=0)
						time_series_mspeed[j] = time_series_sum/time_data_size[j];
					sum = sum + time_series_sum;
				}
				mspeed = sum/flag;
				int[] s_15_85_95 = this.get_s15_s85_s95(speed_frequency, flag);
				//Arrays.sort(time_series_mspeed);
				System.out.print(filename+"|");
				bw.write(filename+",");
				for(int j = 0;j<24;j++){
					System.out.print(time_series_mspeed[j]+",");
					bw.write(time_series_mspeed[j]+",");
				}
				System.out.print("|"+mspeed+",");
				standard_dev = standard_dev(time_series,mspeed);
				System.out.print(standard_dev+"|");
				System.out.print(s_15_85_95[0]+","+s_15_85_95[1]+","+s_15_85_95[2]+",");
				System.out.print(s_15_85_95[1] - s_15_85_95[0]+"|");
				mode = this.getMode_afterMeanSpeed(speed_frequency, mspeed);
				System.out.print(mode+"|");
				System.out.println(SPEED_LABEL);
				bw.write(mspeed+","+standard_dev+","+s_15_85_95[0]+","+s_15_85_95[1]+","+s_15_85_95[2]+","+(s_15_85_95[1] - s_15_85_95[0])+","+mode+","+SPEED_LABEL);
				bw.newLine();
				//------------------------绘制速度频率图像特征图----------------------------------
				//int[][] speed_freMat = this.speed_frequency_matrix(speed_frequency);
				//int[][] speed_freMat = creat_2D_matrix(speed_frequency);
				//printMatrix(speed_freMat);
				//System.out.println("==========================");
				//this.matrix_gray(speed_freMat);
				//printMatrix(speed_freMat);
				//speed_freMat = CreatGrayImage.mat_boost(speed_freMat, 10);
				//CreatGrayImage.creatImage(speed_freMat,filename+".jpg");
				//-------------------------------------------------------------------------
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//求样本标准差
	public int standard_dev(int[][] array,int mean_value){
		int N = 0;
		int sum = 0;
		for(int i = 0;i<array.length;i++)
			for(int j =0;j<array[i].length;j++){
				if(array[i][j]!=0){
					sum +=(j - mean_value)*(j - mean_value)*array[i][j];
					N += array[i][j];
				}
			}
		return (int) Math.sqrt(sum/N);
	}
	
	//求超过平均速度的众数
	public int getMode_afterMeanSpeed(int[] speed_frequency_array,int mean_speed){
		int mode = 0;
		int mode_speed = 0;
		for(int i = mean_speed;i<speed_frequency_array.length;i++){
			if(speed_frequency_array[i] > mode){
				mode = speed_frequency_array[i];
				mode_speed = i;
			}
		}
		return mode_speed;
	}
	
	//计算15位车速和85位、95位车速,返回数组{s15,s85，s95}
	public int[] get_s15_s85_s95(int[] array,int N){ 
		int[] result = new int[3];
		int N_15 = N*15/100;
		int N_85 = N*85/100;
		int N_95 = N*95/100;
		int sum = 0;
		for(int i = 0;i<array.length;i++){
			sum = sum+array[i];
			if(result[0]==0){
				if(sum>N_15)
					result[0] = i;
			}else{
				if(result[1] == 0){
					if(sum>N_85)
						result[1] = i;
				}else{ 
					if(result[2] == 0)
						if(sum > N_95)
							result[2] = i;
				}
			}
			
		}
		return result;
	}
	
	//速度频率数组转化为速度频率矩阵
	public int[][] speed_frequency_matrix(int[] array){
		int[][] matrix_array = new int[14][14];
		int row = 0;
		int col = 0;
		int max_value = 0;
		for(int i = 0;i<array.length;i++){
			row = i/14;
			col = i%14;
			matrix_array[row][col] = array[i];
			if(array[i]>max_value)
				max_value = array[i];
		}
		matrix_array[13][13] = max_value;	//将速度频数中最大的数值放在矩阵尾部
		return matrix_array;
	}
	
	//速度频率矩阵，转化为灰度矩阵,原始矩阵最后一个值为最大值
	public void matrix_gray(int[][] array){
		int max_value = array[13][13];
		if (max_value == 0)
			return;
		array[13][13] = 0;
		for(int i = 0;i<array.length;i++)
			for(int j = 0;j<array[i].length;j++){
				array[i][j] = (int)(((float)array[i][j]/max_value)*255);
			}
	}
	
	//打印矩阵
	public void printMatrix(int[][] array){
		for(int i = 0;i<array.length;i++){
			for(int j = 0;j<array[0].length;j++){
				System.out.print(array[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	//创建二维速度频率矩阵
	public int[][] creat_2D_matrix(int[] array){
		int[][] matrix = new int[array.length][array.length];
		int x_pow = 0;
		int y_pow = 0;
		for(int i = 0;i<array.length;i++)
			for(int j = 0;j<array.length;j++){
				x_pow = array[i]*array[i];
				y_pow = array[j]*array[j];
				if(x_pow > y_pow)
					matrix[i][j] = y_pow;
				else
					matrix[i][j] = x_pow;
			}
		return matrix;
	}
	
	/*
	public static void main(String[] args){
		DataProcess dp = new DataProcess("C:/Users/lin/Desktop/道路限速实验/实验代码/洋中路（限速40）","C:/Users/lin/Desktop/test/stan_data.txt");
		dp.init();
	}
	*/

}
