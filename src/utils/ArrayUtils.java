package utils;

public class ArrayUtils {
	
	public static double getMax(double[] arr) {
		
		if (arr == null || arr.length == 0) {
			return -1;
		}
		double max = arr[0];
		for(int i = 0; i < arr.length; i++) {
			if(max < arr[i])
				max = arr[i];
		}
		return max;
	}
	
	public static int getMax(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		int max = arr[0];
		for(int i = 0; i < arr.length; i++) {
			if(max < arr[i])
				max = arr[i];
		}
		return max;
	}
	
	public static double getMin(double[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		double min = arr[0];
		for(int i = 0; i < arr.length; i++) {
			if(min > arr[i])
				min = arr[i];
		}
		return min;
	}
	
	public static int getMin(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1;
		}
		int min = arr[0];
		for(int i = 0; i < arr.length; i++) {
			if(min > arr[i])
				min = arr[i];
		}
		return min;
	}


}
