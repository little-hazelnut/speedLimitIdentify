package com.xiaozhen.DataPreprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Test {
	public static void main(String[] args){
		/*
		String src = "C:/Users/lin/Desktop/道路限速实验/实验代码/S1531福州机场高速北三环_新店出口-上浦岭桥段（限速120）/车道1/20131201s1531FZairport_gs_xdsp1.txt";
		int flag = 0;
		int speed = 0;
		int sum = 0;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
			String str = null;
			try {
				while((str = bw.readLine())!= null){
					String[] info = str.split(",");
					if(info.length != 16)
						continue;
					speed = Integer.parseInt(info[6]);
					if(speed>5&&speed<150){
						sum += speed;
						flag++;
					}
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(flag);
		System.out.println(sum/flag);
	*/
		long time = System.currentTimeMillis();
		int k = 1;
		int N = 1000000;
		int[][] sample_matrix = new int[N][];
		int[] label = new int[N];
		int[] temp_arr = {45, 45, 44, 43, 42, 42, 28, 13, 12, 43, 52, 31, 30};
		for(int i = 0;i<sample_matrix.length;i++){
			sample_matrix[i] = temp_arr;
		}
		int[] test_arr = {43, 43, 42, 41, 41, 40, 26, 14, 9, 41, 52, 32, 38};
		for(int i = 0;i<sample_matrix.length;i++){
			label[i] = euclidean_distance(sample_matrix[i],test_arr);
		}
		Arrays.sort(label);
		/*
		for(int i = 0;i<sample_matrix.length;i++)
			for(int j = i+1;j<sample_matrix.length;j++){
				if(label[i]>label[j]){
					int temp = label[j];
					label[j] = label[i];
					label[i] = temp;
				}
			}
		*/
		System.out.println("time spent: "+ (System.currentTimeMillis() - time)+" ms");
	}
	
	public static int euclidean_distance(int[] a,int[] b){
		int sum = 0;
		if(a.length != b.length)
			return -1;
		for(int i = 0;i<a.length;i++){
			sum += (a[i]- b[i])*(a[i] - b[i]);
		}
		
		return (int)Math.sqrt(sum);
	}
}
