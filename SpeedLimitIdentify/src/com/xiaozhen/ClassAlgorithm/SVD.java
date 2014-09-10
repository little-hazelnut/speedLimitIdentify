package com.xiaozhen.ClassAlgorithm;

import com.xiaozhen.jama.Matrix;
import com.xiaozhen.jama.SingularValueDecomposition;



public class SVD {
	Matrix matrix = null;
	int[] labelArray = null;
	double scale = 0;
	Matrix U = null;
	Matrix S = null;
	Matrix V = null;
	double[][] V_array = null;
	
	public SVD(Matrix matrix,int[] labelArray,double scale){
		this.matrix = matrix;
		this.labelArray = labelArray;
		this.scale = scale;
	}

	public void init(){
		System.out.println("SVD begin: ");
		Matrix matrix_T = matrix.transpose();
		matrix_T.print(2,2);
		SingularValueDecomposition s = matrix_T.svd();
		//System.out.print("U = ");
		U = s.getU();
		//U.print(9, 6);
		System.out.print("Sigma = ");
		S = s.getS();
		//S.print(2, 2);
		//System.out.print("V = ");
		V = s.getV();
		//V.print(2, 2);
		System.out.println(s.rank());
		Matrix svalues = new Matrix(s.getSingularValues(), 1);
	    svalues.print(2, 2);
	    
	    //选取降维s_k
	    double SingularValuesSum = 0;
	    for(int i = 0;i<s.getSingularValues().length;i++){
	    	SingularValuesSum += s.getSingularValues()[i];
	    }
	    double SingularValuesCount = 0;
	    SingularValuesSum = SingularValuesSum*scale;
	    int s_k = 0;
	    for(int i = 0;i<s.getSingularValues().length;i++){
	    	SingularValuesCount += s.getSingularValues()[i];
	    	if(SingularValuesCount>SingularValuesSum){
	    		s_k = i;
	    		break;
	    	}
	    }
	    System.out.println("k = "+(s_k+1));
	    //重构矩阵
	    U = U.getMatrix(0, U.getRowDimension()-1, 0, s_k);
	    S = S.getMatrix(0, s_k, 0, s_k);
	    V = V.getMatrix(0, V.getRowDimension()-1, 0, s_k);
	    V_array = V.getArrayCopy();
	}
	
	public int class_obj(double[] class_obj){
		if(V_array == null){
			System.out.println("SVD must init");
			System.exit(-1);
		}
		Matrix class_obj_matrix = new Matrix(class_obj,1);
	    class_obj_matrix = class_obj_matrix.times(U).times(S.inverse());
	    class_obj_matrix.print(2, 2);
	    double[][] class_obj_matrix_array = class_obj_matrix.getArrayCopy();
	    KNN knn = new KNN(V_array,labelArray,class_obj_matrix_array[0],3);
	    return knn.load_double_euc();
	    //return knn.load_double_cos();    在限速识别中余弦距离准确率不如欧式距离
	}
	

}
