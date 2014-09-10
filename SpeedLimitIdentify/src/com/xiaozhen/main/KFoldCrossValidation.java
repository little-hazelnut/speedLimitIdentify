package com.xiaozhen.main;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xiaozhen.ClassAlgorithm.KNN;
import com.xiaozhen.ClassAlgorithm.SVD;
import com.xiaozhen.jama.Matrix;
import com.xiaozhen.model.SampleCollection;

public class KFoldCrossValidation {
	SampleCollection sac = null;	//样本总量
	int k = 0; 			//k折
	
	public KFoldCrossValidation(SampleCollection sac,int k){
		this.sac = sac;
		this.k = k;
	}

	public void init(){
		if(sac == null)
			return;
		Map<Integer,List<int[]>> labelMap = new HashMap<Integer,List<int[]>>();
		int[][] sample_array = sac.sample_array;
		int[] label_array = sac.labels_array;
		for(int i = 0;i<sample_array.length;i++){
			if(labelMap.containsKey(label_array[i])){
				labelMap.get(label_array[i]).add(sample_array[i]);
			}else{
				List<int[]> list = new LinkedList<int[]>();
				list.add(sample_array[i]);
				labelMap.put(label_array[i],list);
			}
		}
		
		Set<Integer> labelSet = labelMap.keySet();
		Iterator<Integer> label_it = labelSet.iterator(); 
		List<int[]> testList = new LinkedList<int[]>();
		List<Integer> testlabel = new LinkedList<Integer>();
		List<int[]> tranList = new LinkedList<int[]>();
		List<Integer> tranlabel = new LinkedList<Integer>();
		
		int labelnum = 0;
		int kfold = 0;
		List<int[]> tempList;
		while(label_it.hasNext()){
			labelnum = label_it.next();
			tempList = labelMap.get(labelnum);
			kfold = tempList.size()/k;
			for(int i = 0;i<kfold;i++){
				testList.add(tempList.get(i));
				testlabel.add(labelnum);
			}
			for(int i = kfold;i<tempList.size();i++){
				tranList.add(tempList.get(i));
				tranlabel.add(labelnum);
			}
		}
		
		int[][] test_array = new int[testList.size()][]; 
		int[] test_label = new int[testlabel.size()];
		int[][] tran_array = new int[tranList.size()][]; 
		int[] tran_label = new int[tranlabel.size()];
		testList.toArray(test_array);
		tranList.toArray(tran_array);
		for(int i = 0;i<testlabel.size();i++)
			test_label[i] = testlabel.get(i);
		for(int i = 0;i<tranlabel.size();i++)
			tran_label[i] = tranlabel.get(i);
		
		for(int i = 0;i<test_array.length;i++){
			System.out.print(i+" : "+Arrays.toString(test_array[i])+",");
			System.out.println(test_label[i]);
		}
		System.out.println("=========================================================================================");
		for(int i = 0;i<tran_array.length;i++){
			System.out.print(i+" : "+Arrays.toString(tran_array[i])+",");
			System.out.println(tran_label[i]);
		}
		
		//test_knn(test_array,test_label,tran_array,tran_label);
		test_svd_knn(test_array,test_label,tran_array,tran_label);
		
		
		
	}
	
	public void test_knn(int[][] test_array,int[] test_label,int[][] tran_array,int[] tran_label){
		int[] class_obj = null;
		double right_flag = 0;
		int knn_k = 1;
		int knn_result = 0;
		for(int i = 0;i<test_array.length;i++){
			class_obj = test_array[i];
			KNN knn = new KNN(tran_array,tran_label,class_obj,knn_k);
			//System.out.println("oringal label: "+test_label[i]);
			knn_result = knn.load();
			if(knn_result == test_label[i])
				right_flag++;
			System.out.println("====================================");
		}
		System.out.println("right rate: "+ right_flag/test_array.length);
	}
	
	public void test_svd_knn(int[][] test_array,int[] test_label,int[][] tran_array,int[] tran_label){
		int[] class_obj = null;
		double right_flag = 0;
		int svd_knn_result = 0;
		double[][] tran_array_double = new double[tran_array.length][tran_array[0].length];
		for(int i = 0;i<tran_array.length;i++)
			for(int j = 0;j<tran_array[i].length;j++)
				tran_array_double[i][j] = tran_array[i][j];
		Matrix matrix = new Matrix(tran_array_double);
		SVD svd = new SVD(matrix,tran_label,0.95);
		svd.init();
		for(int i = 0;i<test_array.length;i++){
			class_obj = test_array[i];
			double[] class_obj_double = new double[class_obj.length];
			for(int j = 0;j<class_obj_double.length;j++)
				class_obj_double[j] = class_obj[j];
			//System.out.println("oringal label: "+test_label[i]);
			svd_knn_result = svd.class_obj(class_obj_double);
			if(svd_knn_result == test_label[i])
				right_flag++;
			System.out.println("====================================");
		}
		System.out.println("right rate: "+ right_flag/test_array.length);
	}
	
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		Main ma = new Main("E:/论文啊/道路限速识别论文资料/testdata/stan_data.txt");
		SampleCollection sac  = ma.load_data();
		new KFoldCrossValidation(sac,25).init();
		System.out.println("time spent: "+ (System.currentTimeMillis() - time)+" ms");
	}
}
