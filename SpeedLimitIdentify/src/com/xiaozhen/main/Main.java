package com.xiaozhen.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import com.xiaozhen.ClassAlgorithm.KNN;
import com.xiaozhen.ClassAlgorithm.SVD;
import com.xiaozhen.jama.Matrix;
import com.xiaozhen.model.SampleCollection;

public class Main {
	String src = null;
	List<int[]> sampleList = new ArrayList<int[]>();
	List<Integer> labelList = new ArrayList<Integer>();
	
	public Main(String src){
		this.src = src;
	}
	
	
	public SampleCollection load_data(){
		BufferedReader br = null;
		String str = null;
		String[] info = null;
		SampleCollection sc = null;
		
		try {
			if(src == null)
				System.exit(-1);
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
			while((str = br.readLine())!= null){
				info = str.split(",");
				int[] time_series = new int[24];
				int[] sample = new int[13];
				for(int i = 0;i<24;i++){
					time_series[i] = Integer.parseInt(info[i+1]);
				}
				Arrays.sort(time_series);
				if(time_series[23] == 0){
					continue;
				}else sample[0] = time_series[23];
				
				for(int i = 22;i>17;i--){
					if(time_series[i] == 0)
						sample[23 - i] = sample[0];
					else
						sample[23 - i] = time_series[i];
				}
				
				for(int i = 25;i<32;i++){
					sample[i-19] = Integer.parseInt(info[i]);
				}
				
				labelList.add(Integer.parseInt(info[32]));
				sampleList.add(sample);
			}
		
			int[][] sampleArrays = new int[sampleList.size()][];
			sampleList.toArray(sampleArrays);
			Integer[] labelsArrays = new Integer[labelList.size()];
			labelList.toArray(labelsArrays);
			int[] labelsArray = new int[labelsArrays.length];
			
			for(int i = 0;i<labelsArrays.length;i++){
				labelsArray[i] =  labelsArrays[i];
			}
			
			sc = new SampleCollection(sampleArrays,labelsArray);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			return sc;
		}
	}
	
	public static void main(String[] args){
		long time = System.currentTimeMillis();
		Main ma = new Main("E:/论文啊/道路限速识别论文资料/testdata/stan_data.txt");
		SampleCollection sac  = ma.load_data();
		int[][] sample_array = sac.sample_array;
		int[] label_array = sac.labels_array;
		for(int i = 0;i<sample_array.length;i++){
			System.out.print(i+":"+Arrays.toString(sample_array[i])+",");
			/*
			for(int j = 0;j<sample_array[i].length;j++)
				System.out.print((float)sample_array[i][j]/150+",");
			*/
			System.out.println(label_array[i]);
		}
		/*
		int TestSize = 1000000;
		int[][] sample_array = new int[TestSize][];
		int[] label_array = new int[TestSize];
		int[] test = {56, 55, 55, 55, 55, 55, 53, 19, 35, 73, 85, 38, 54};
		for(int i = 0;i<TestSize;i++){
			sample_array[i] = test;
			label_array[i] = 0;
		}
		*/
		/*knn method
		int k = 5;
		int[] class_obj = {56, 55, 55, 55, 55, 55, 53, 19, 35, 73, 85, 38, 54};
		KNN knn = new KNN(sample_array,label_array,class_obj,k);
		knn.load();
		*/
		double[] class_obj = {34, 32, 31, 30, 30, 29, 26, 12, 13, 40, 48, 27, 31};
		double[][] sample_array_double = new double[sample_array.length][sample_array[0].length];
		for(int i = 0;i<sample_array.length;i++)
			for(int j = 0;j<sample_array[i].length;j++)
				sample_array_double[i][j] = sample_array[i][j];
		Matrix matrix = new Matrix(sample_array_double);
		SVD svd = new SVD(matrix,label_array,0.9);
		svd.init();
		svd.class_obj(class_obj);
		System.out.println("time spent: "+ (System.currentTimeMillis() - time)+" ms");
	}
}
