package plot.axis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AxisAlgebra {
	
	public static final IAxisAlgebraStrategy LOG = new AxisLogStrategy();
	public static final IAxisAlgebraStrategy LINEAR = new AxisLinearStrategy();
	
	
	/**
	 * 轴的log策略
	 */
	private static class AxisLogStrategy implements IAxisAlgebraStrategy {

		@Override
		public List<Double> calibrateBigScale(double minValue, double maxValue) {
			
			List<Double> res = new ArrayList<Double>();
			res.add(minValue);
			//取位数
			String minStr = "" + (int)minValue;
			String maxStr = "" + (int)maxValue;
			//如果位数相等，可以判断中间不存在其他刻度
			if(minStr.length() == maxStr.length()) {
				//添加结果
			}
			//如果位数不等
			else {
				//最小值的右边第一个刻度取10的位数幂
				int minBigger = (int) Math.pow(10, minStr.length());
				//最大值的左边第一个数取10的(位数 - 1)幂
				int maxSmaller = (int) Math.pow(10, maxStr.length() - 1);
				//如果这两个数相等
				if(minBigger == maxSmaller) {
					//添加结果
					res.add((double)minBigger);
				}
				//如果这两个数不等
				else {
					
					res.add((double)minBigger);
					//寻找中间的其他刻度
					String s1 = minBigger + "";
					String s2 = maxSmaller + "";
					int diff = s2.length() - s1.length();
					for (int i = 1; i < diff; i++) {
						res.add(minBigger * Math.pow(10, i));
					}
					//添加结果
					if(maxSmaller != maxValue) {
						//有可能存在相等的情况，通过该判断以避免重复添加
						res.add((double)maxSmaller);
					}
				}
			}
			res.add(maxValue);
			return res;
			
		}

		@Override
		public List<Double> calibrateSmallScale(List<Double> bsValues) {
			
			if(bsValues == null) {
				return null;
			}
			List<Double> res = new ArrayList<Double>();
			//遍历区间，寻找区间内的小刻度
			for (int i = 1; i < bsValues.size(); i++) {
				double lowd = bsValues.get(i - 1);
				double upd = bsValues.get(i);
				int low = (int)lowd;
				int up = (int)upd;
				int lowMask = 0;
				int upMask = 0;
				//取位数
				String lowStr = low + "";
				String upStr = up + "";
				//如果位数相等，取第一位
				if(lowStr.length() == upStr.length()) {
					lowMask = Integer.valueOf("" + lowStr.charAt(0));
					upMask = Integer.valueOf("" + upStr.charAt(0));
				}
				//如果位数不等
				else {
					//下区间取第一位
					lowMask = Integer.valueOf("" + lowStr.charAt(0));
					//上区间取前两位
					upMask = Integer.valueOf("" + upStr.charAt(0) + upStr.charAt(1));
				}
				//添加结果
				for (int j = 1; j < upMask - lowMask; j++) {
					double d = (lowMask + j) * Math.pow(10, lowStr.length() - 1);
					res.add(d);
				}
			}
			return res;
		}

		@Override
		public List<Integer> pixelateBigScale(List<Double> bsValues, int length) {
			
			List<Integer> res = new ArrayList<>();
			//求log值
			List<Double> logs = new ArrayList<>();
			Iterator<Double> iter = bsValues.iterator();
			while(iter.hasNext()) {
				double d = iter.next();
				logs.add(Math.log10(d));
			}
			//求log像素比
			double rate = 0.0;
			rate = (double)length / (logs.get(logs.size() - 1) - logs.get(0));
			//计算点距
			Iterator<Double> iter2 = logs.iterator();
			while(iter2.hasNext()) {
				double d = iter2.next();
				res.add((int) (rate * (d - logs.get(0))));
			}
			
			return res;
		}

		@Override
		public List<Integer> pixelateSmallScale(List<Double> ssValues, double minValue, 
				double maxValue, int length) {
			
			List<Integer> res = new ArrayList<>();
			//求log值
			List<Double> logs = new ArrayList<>();
			Iterator<Double> iter = ssValues.iterator();
			while(iter.hasNext()) {
				double d = iter.next();
				logs.add(Math.log10(d));
			}
			//求log像素比
			double rate = 0.0;
			rate = (double)length / (Math.log10(maxValue) - Math.log10(minValue));
			//计算点距
			Iterator<Double> iter2 = logs.iterator();
			while(iter2.hasNext()) {
				double d = iter2.next();
				res.add((int) (rate * (d - Math.log10(minValue))));
			}
			
			return res;
		}
		
	}
	
	/**
	 * 轴的linear策略
	 */
	private static class AxisLinearStrategy implements IAxisAlgebraStrategy {

		@Override
		public List<Double> calibrateBigScale(double minValue, double maxValue) {
			if(minValue > maxValue) {
				return null;
			}
			List<Double> res = new ArrayList<Double>();
			int min = 0;	//保存minValue的整型
			int max = 0;	//保存maxValue的整型
			min = (int) minValue;
			max = (int) maxValue;
			
			/*
			 * 根据位数确定min和max的头部值
			 */
			String minStr = "";	//保存min的字符串格式
			String maxStr = "";	//保存max的字符串格式
			int minHead = 0;	//保存min头部
			int maxHead = 0;	//保存max头部
			
			minStr = "" + min;
			maxStr = "" + max;
			// 如果位数不等
			if(minStr.length() != maxStr.length()) {
				/*
				 * minHead取min的最高位
				 * maxHead取max的最高位和次高位
				 */
				minHead = Integer.valueOf("" + minStr.charAt(0));
				maxHead = Integer.valueOf("" + maxStr.charAt(0) + maxStr.charAt(1));
			} // 如果位数相等
			else {
				// 如果都是0-9或相差小于等于10
				if(minStr.length() == 1 || max - min <= 10) {
					// 直接取原值
					minHead = min;
					maxHead = max;
				} 
				// 如果是两位数及以上或者相差大于10
				else {
					// 取最高位和次高位
					minHead = Integer.valueOf("" + minStr.charAt(0) + minStr.charAt(1));
					maxHead = Integer.valueOf("" + maxStr.charAt(0) + maxStr.charAt(1));
				}
			}
			
			/*
			 * 确定头部与原值的倍数
			 */
			int times = 0;
			int diff = maxStr.length() - ("" + maxHead).length();
			if(diff > 0) {
				times = (int) Math.pow(10, diff);
			}
			else {
				times = 1;
			}
			
			res.add((double)min);
			
			/*
			 * 确定步长
			 */
			int cur = min;
			int step = 0;
			if (maxHead - minHead < 5) {
				//初定步长为1的倍数
				step = 1 * times;
				while ((max - min) / step > 4) {
					//如果以当前步长划分轴区间的份数大于4的话，增大步长
					step += 1 * times;
				}
				//遍历步长区间，寻找第一个刻度值
				for(int i = 0; i < max; i += step) {
					if(cur < i) {
						cur = i;
						break;
					}
				}
				//添加结果
				for (; cur < max; cur += step) {
					res.add((double)cur);
				}
			}
			else if (maxHead - minHead < 9) {
				//初定步长为2的倍数
				step = 2 * times;
				while ((max - min) / step > 4) {
					step += 2 * times;
				}
				for(int i = 0; i < max; i += step) {
					if(cur < i) {
						cur = i;
						break;
					}
				}
				for (; cur < max; cur += step) {
					res.add((double)cur);
				}
			}
			else {
				//初定步长为5的倍数
				step = 5 * times;
				while ((max - min) / step > 4) {
					step += 5 * times;
				}
				for(int i = 0; i < max; i += step) {
					if(min < i) {
						cur = i;
						break;
					}
				}
				for (; cur < max; cur += step) {
					res.add((double)cur);
				}
			}
			res.add((double)max);
			
			return res;
		}

		@Override
		public List<Double> calibrateSmallScale(List<Double> bsValues) {
			//线性策略不做划分
			return null;
		}

		@Override
		public List<Integer> pixelateBigScale(List<Double> bsValues, int length) {
			
			List<Integer> bsDistance = new ArrayList<Integer>();
			double rate = 0.0;	//换算比率
			
			rate = (length) / (bsValues.get(bsValues.size() - 1) - bsValues.get(0));
			for(double ele : bsValues) {
				if(ele != bsValues.get(bsValues.size() - 1)) {
					bsDistance.add((int) (rate * (ele - bsValues.get(0))));
				}
			}
			//最后一个元素单独赋值，消除因数据类型转换而产生的误差
			bsDistance.add(length);
			
			//检查重复性，如果重复则错开一个像素
			int prev = -1;
			for (int i = 0; i < bsDistance.size(); i++) {
				int ele = bsDistance.get(i);
				if(ele == prev) {
					bsDistance.set(i, ele + 1);
				}
				prev = ele;
			}
			return bsDistance;
		}

		@Override
		public List<Integer> pixelateSmallScale(List<Double> ssValues, double minValue, 
				double maxValue, int length) {
			//线性策略不做划分
			return null;
		}
		
	}

}
