package test;

import java.util.List;

import plot.axis.AxisAlgebra;

public class AxisAlgobraTest {
	
	public static void main(String[] args) {
		int min = 1;
		int max = 16000;
		List<Double> bigscale = AxisAlgebra.LOG.calibrateBigScale(min, max);
		List<Double> smallScale = AxisAlgebra.LOG.calibrateSmallScale(bigscale);
		List<Integer> bigdistances = AxisAlgebra.LOG.pixelateBigScale(bigscale, 200);
		List<Integer> smalldistances = AxisAlgebra.LOG.pixelateBigScale(smallScale, 200);
		
		System.out.println(bigscale);
		System.out.println(smallScale);
		System.out.println(bigdistances);
		System.out.println(smalldistances);
	}

}
