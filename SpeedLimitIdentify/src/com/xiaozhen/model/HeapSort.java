package com.xiaozhen.model;

public class HeapSort {
	//最大堆
	//从i节点开始向下调整，n为节点总数，0为根节点，i节点的子节点为2*i+1,2*i+2
	//dis_index为堆中元素对应的样本序号
	public static void maxHeapFixdown(int[] a,int i,int n,int[] dist_index){
		int j,temp,temp_index;
		temp = a[i];
		temp_index = dist_index[i];
		j = 2*i+1;
		while(j<n){
			if(j+1<n && a[j] < a[j+1])
				j++;
			if(a[i] > a[j])
				break;
			a[i] = a[j];
			dist_index[i] = dist_index[j];
			i = j;
			j = 2*i +1;
		}
		
		a[i] = temp;
		dist_index[i] = temp_index;
	}
	
	public static void maxHeapFixdown(double[] a,int i,int n,int[] dist_index){
		int j,temp_index;
		double temp;
		temp = a[i];
		temp_index = dist_index[i];
		j = 2*i+1;
		while(j<n){
			if(j+1<n && a[j] < a[j+1])
				j++;
			if(a[i] > a[j])
				break;
			a[i] = a[j];
			dist_index[i] = dist_index[j];
			i = j;
			j = 2*i +1;
		}
		
		a[i] = temp;
		dist_index[i] = temp_index;
	}
	
	public static void makeMaxHeap(int[] a,int n,int[] dist_index){
		for(int i = n/2 - 1;i >= 0;i--){
			maxHeapFixdown(a,i,n,dist_index);
		}
	}
	
	public static void makeMaxHeap(double[] a,int n,int[] dist_index){
		for(int i = n/2 - 1;i >= 0;i--){
			maxHeapFixdown(a,i,n,dist_index);
		}
	}
	
	public static void MaxheapsortToascendArray(int[] a,int n,int[] dist_index){
		for(int i = n-1;i>0;i--){
			int temp = a[0];
			int temp_index = dist_index[0];
			dist_index[0] = dist_index[i];
			dist_index[i] = temp_index;
			a[0] = a[i];
			a[i] = temp;
			maxHeapFixdown(a,0,i,dist_index);
		}
	}
	
	public static void MaxheapsortToascendArray(double[] a,int n,int[] dist_index){
		for(int i = n-1;i>0;i--){
			double temp = a[0];
			int temp_index = dist_index[0];
			dist_index[0] = dist_index[i];
			dist_index[i] = temp_index;
			a[0] = a[i];
			a[i] = temp;
			maxHeapFixdown(a,0,i,dist_index);
		}
	}
	
	
}
