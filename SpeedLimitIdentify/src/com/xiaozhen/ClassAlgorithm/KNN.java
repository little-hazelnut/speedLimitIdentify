package com.xiaozhen.ClassAlgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import com.xiaozhen.model.HeapSort;

public class KNN {
	int[][] int_sample_matrix = null;
	double[][] double_sample_matrix = null;
	int[] int_class_obj = null;
	double[] double_class_obj = null;
	int labels[] = null;
	int k = 0;
	
	public KNN(int[][] int_sample_matrix,int[] labels,int[] class_obj,int k){
		if(int_sample_matrix.length != labels.length)
			System.out.println("error : sample matrix rownum must equal labels array num!");
		this.int_sample_matrix = int_sample_matrix;
		this.int_class_obj = class_obj;
		this.labels = labels;
		this.k = k;
	}
	
	public KNN(double[][] double_sample_matrix,int[] labels,double[] class_obj,int k){
		if(double_sample_matrix.length != labels.length)
			System.out.println("error : sample matrix rownum must equal labels array num!");
		this.double_sample_matrix = double_sample_matrix;
		this.double_class_obj = class_obj;
		this.labels = labels;
		this.k = k;
	}
	
	public int load(){
		if(k<=0){
			System.out.println("error: k<=0");
			System.exit(-1);
		}
		if(int_sample_matrix.length < k)
			k = int_sample_matrix.length;
		int[] dist_heap = new int[k]; 	//构建一个距离数组
		int[] dist_index = new int[k];	//构建一个样本索引数组，与距离数组是值-键的关系
		if(int_sample_matrix != null)
			for(int i = 0;i<int_sample_matrix.length;i++){
				if(i<k - 1){//插入数据没有超过堆大小
					dist_heap[i] = this.euclidean_distance(int_sample_matrix[i], int_class_obj);
					dist_index[i] = i;
				}else if(i == k - 1){
					dist_heap[i] = this.euclidean_distance(int_sample_matrix[i], int_class_obj);
					dist_index[i] = i;
					HeapSort.makeMaxHeap(dist_heap, k,dist_index);
				}else{
					int distance = this.euclidean_distance(int_sample_matrix[i], int_class_obj);
					if(dist_heap[0] > distance){
						dist_heap[0] = distance;
						dist_index[0] = i;  
					}
					HeapSort.maxHeapFixdown(dist_heap, 0, k,dist_index);
				}	
			}
		
		HeapSort.MaxheapsortToascendArray(dist_heap, k, dist_index);
		Map<Integer,Integer> labelsMap = new HashMap<Integer,Integer>();
		int num = 0;
		for(int i =0;i<k;i++){
			System.out.print(dist_index[i]+",");
			System.out.print(dist_heap[i]+",");
			System.out.println(labels[dist_index[i]]);
			if(labelsMap.containsKey(labels[dist_index[i]])){
				num = labelsMap.get(labels[dist_index[i]]);
				num++;
				labelsMap.put(labels[dist_index[i]], num);
			}else
				labelsMap.put(labels[dist_index[i]], 1);
		}
		Set<Map.Entry<Integer,Integer>> entrys = labelsMap.entrySet();
		Iterator<Map.Entry<Integer, Integer>> it = entrys.iterator();
		int MaxLabelsNum = 0;
		int MaxLabels = 0;
		Map.Entry<Integer, Integer> entry = null;
		while(it.hasNext()){
			entry = it.next();	
			if(entry.getValue() > MaxLabelsNum){
				MaxLabelsNum = entry.getValue();
				MaxLabels = entry.getKey();
			}
		}
		System.out.println("road classification: "+MaxLabels);
		return MaxLabels;
	}
	
	public int load_double_euc(){
		if(k<=0){
			System.out.println("error: k<=0");
			System.exit(-1);
		}
		if(double_sample_matrix.length < k)
			k = double_sample_matrix.length;
		double[] dist_heap = new double[k]; 	//构建一个距离数组
		int[] dist_index = new int[k];	//构建一个样本索引数组，与距离数组是值-键的关系
		if(double_sample_matrix != null)
			for(int i = 0;i<double_sample_matrix.length;i++){
				if(i<k - 1){//插入数据没有超过堆大小
					dist_heap[i] = this.euclidean_distance(double_sample_matrix[i], double_class_obj);
					dist_index[i] = i;
				}else if(i == k - 1){
					dist_heap[i] = this.euclidean_distance(double_sample_matrix[i], double_class_obj);
					dist_index[i] = i;
					HeapSort.makeMaxHeap(dist_heap, k,dist_index);
				}else{
					double distance = this.euclidean_distance(double_sample_matrix[i], double_class_obj);
					if(dist_heap[0] > distance){
						dist_heap[0] = distance;
						dist_index[0] = i;  
					}
					HeapSort.maxHeapFixdown(dist_heap, 0, k,dist_index);
				}	
			}
		
		HeapSort.MaxheapsortToascendArray(dist_heap, k, dist_index);
		Map<Integer,Integer> labelsMap = new HashMap<Integer,Integer>();
		int num = 0;
		for(int i =0;i<k;i++){
			System.out.print(dist_index[i]+",");
			System.out.print(dist_heap[i]+",");
			System.out.println(labels[dist_index[i]]);
			if(labelsMap.containsKey(labels[dist_index[i]])){
				num = labelsMap.get(labels[dist_index[i]]);
				num++;
				labelsMap.put(labels[dist_index[i]], num);
			}else
				labelsMap.put(labels[dist_index[i]], 1);
		}
		Set<Map.Entry<Integer,Integer>> entrys = labelsMap.entrySet();
		Iterator<Map.Entry<Integer, Integer>> it = entrys.iterator();
		int MaxLabelsNum = 0;
		int MaxLabels = 0;
		Map.Entry<Integer, Integer> entry = null;
		while(it.hasNext()){
			entry = it.next();	
			if(entry.getValue() > MaxLabelsNum){
				MaxLabelsNum = entry.getValue();
				MaxLabels = entry.getKey();
			}
		}
		System.out.println("road classification: "+MaxLabels);
		return MaxLabels;
	}
	
	public int load_double_cos(){
		if(k<=0){
			System.out.println("error: k<=0");
			System.exit(-1);
		}
		if(double_sample_matrix.length < k)
			k = double_sample_matrix.length;
		double[] dist_heap = new double[k]; 	//构建一个距离数组
		int[] dist_index = new int[k];	//构建一个样本索引数组，与距离数组是值-键的关系
		if(double_sample_matrix != null)
			for(int i = 0;i<double_sample_matrix.length;i++){
				if(i<k - 1){//插入数据没有超过堆大小
					dist_heap[i] = 1-this.cos_distance(double_sample_matrix[i], double_class_obj);
					dist_index[i] = i;
				}else if(i == k - 1){
					dist_heap[i] = 1-this.cos_distance(double_sample_matrix[i], double_class_obj);
					dist_index[i] = i;
					HeapSort.makeMaxHeap(dist_heap, k,dist_index);
				}else{
					double distance =1-this.cos_distance(double_sample_matrix[i], double_class_obj);
					if(dist_heap[0] > distance){
						dist_heap[0] = distance;
						dist_index[0] = i;  
					}
					HeapSort.maxHeapFixdown(dist_heap, 0, k,dist_index);
				}	
			}
		
		HeapSort.MaxheapsortToascendArray(dist_heap, k, dist_index);
		Map<Integer,Integer> labelsMap = new HashMap<Integer,Integer>();
		int num = 0;
		for(int i =0;i<k;i++){
			System.out.print(dist_index[i]+",");
			System.out.print(dist_heap[i]+",");
			System.out.println(labels[dist_index[i]]);
			if(labelsMap.containsKey(labels[dist_index[i]])){
				num = labelsMap.get(labels[dist_index[i]]);
				num++;
				labelsMap.put(labels[dist_index[i]], num);
			}else
				labelsMap.put(labels[dist_index[i]], 1);
		}
		Set<Map.Entry<Integer,Integer>> entrys = labelsMap.entrySet();
		Iterator<Map.Entry<Integer, Integer>> it = entrys.iterator();
		int MaxLabelsNum = 0;
		int MaxLabels = 0;
		Map.Entry<Integer, Integer> entry = null;
		while(it.hasNext()){
			entry = it.next();	
			if(entry.getValue() > MaxLabelsNum){
				MaxLabelsNum = entry.getValue();
				MaxLabels = entry.getKey();
			}
		}
		System.out.println("road classification: "+MaxLabels);
		return MaxLabels;
	}
	
	//欧式距离计算
	private double euclidean_distance(double[] a,double[] b){
		double sum = 0;
		if(a.length != b.length)
			return -1; 
		for(int i = 0;i<a.length;i++){
			sum += (a[i]- b[i])*(a[i] - b[i]);
		}
		
		return Math.sqrt(sum);
	}
	
	//余弦距离计算
	private double cos_distance(double[] a,double[] b){
		double sum = 0;
		double a_value = 0;
		double b_value = 0;
		if(a.length != b.length)
			return -1;
		for(int i = 0;i<a.length;i++){
			sum += a[i]*b[i];
			a_value+= a[i]*a[i];
			b_value+= b[i]*b[i];
		}
		return sum/(Math.sqrt(a_value)*Math.sqrt(b_value));
	}
				
	private int euclidean_distance(int[] a,int[] b){
		int sum = 0;
		if(a.length != b.length)
			return -1;
		for(int i = 0;i<a.length;i++){
			sum += (a[i]- b[i])*(a[i] - b[i]);
		}
		
		return (int)Math.sqrt(sum);
	}	
	
	/*
	public static void main(String[] args) {
		int[][] int_sample_matrix = {
			{1,2},
			{1,1},
			{1,0},
			{0,1},
			{1,6},
			{1,3}
		};
		int[] labels = {1,1,5,2,5,8};
		int[] class_obj = {1,1};
		int k = 3;
		KNN knn = new KNN(int_sample_matrix,labels,class_obj,k);
		knn.load();
	}
	*/
}
