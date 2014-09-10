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

public class TimeSeriesAnalysis {
	String src = null;
	String dest = null;
	private int MIN_SPEED = 5;
	private int MAX_SPEED = 150;
	
	public TimeSeriesAnalysis(String src,String dest){
		this.src = src;
		this.dest = dest;
		
	}
	
	public void init(){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		String str = null;	
		int speed = 0;			//数据点速度
		int hour = 0;			//数据点时间
		String[] info = null;	//每个样本中每行数据的信息
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(src)));	
			int[][] time_series = new int[24][MAX_SPEED];  			//时间序列速度信息
			int flag = 0;											//样本包含有效数据总数
			int[] time_data_size = new int[24]; 					//样本每个时间序列包含有效数据数量
				
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
					}
				}
				br.close();
				
				for(int i = 0;i<time_series.length;i++)
					for(int j = 0;j<time_series[i].length;j++){
						bw.write(time_series[i][j]+","+i);
						bw.newLine();
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
		
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TimeSeriesAnalysis tsa = new TimeSeriesAnalysis("C:/Users/lin/Desktop/test/20131201JinAnSouthRoad.txt","C:/Users/lin/Desktop/test/20131201JinAnSouthRoad_TimeSeries.txt");
		tsa.init();
	}

}
