package plot.axis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AxisAlgebra {
	
	public static final IAxisAlgebraStrategy LOG = new AxisLogStrategy();
	public static final IAxisAlgebraStrategy LINEAR = new AxisLinearStrategy();
	
	
	/**
	 * ���log����
	 */
	private static class AxisLogStrategy implements IAxisAlgebraStrategy {

		@Override
		public List<Double> calibrateBigScale(double minValue, double maxValue) {
			
			List<Double> res = new ArrayList<Double>();
			res.add(minValue);
			//ȡλ��
			String minStr = "" + (int)minValue;
			String maxStr = "" + (int)maxValue;
			//���λ����ȣ������ж��м䲻���������̶�
			if(minStr.length() == maxStr.length()) {
				//��ӽ��
			}
			//���λ������
			else {
				//��Сֵ���ұߵ�һ���̶�ȡ10��λ����
				int minBigger = (int) Math.pow(10, minStr.length());
				//���ֵ����ߵ�һ����ȡ10��(λ�� - 1)��
				int maxSmaller = (int) Math.pow(10, maxStr.length() - 1);
				//��������������
				if(minBigger == maxSmaller) {
					//��ӽ��
					res.add((double)minBigger);
				}
				//���������������
				else {
					
					res.add((double)minBigger);
					//Ѱ���м�������̶�
					String s1 = minBigger + "";
					String s2 = maxSmaller + "";
					int diff = s2.length() - s1.length();
					for (int i = 1; i < diff; i++) {
						res.add(minBigger * Math.pow(10, i));
					}
					//��ӽ��
					if(maxSmaller != maxValue) {
						//�п��ܴ�����ȵ������ͨ�����ж��Ա����ظ����
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
			//�������䣬Ѱ�������ڵ�С�̶�
			for (int i = 1; i < bsValues.size(); i++) {
				double lowd = bsValues.get(i - 1);
				double upd = bsValues.get(i);
				int low = (int)lowd;
				int up = (int)upd;
				int lowMask = 0;
				int upMask = 0;
				//ȡλ��
				String lowStr = low + "";
				String upStr = up + "";
				//���λ����ȣ�ȡ��һλ
				if(lowStr.length() == upStr.length()) {
					lowMask = Integer.valueOf("" + lowStr.charAt(0));
					upMask = Integer.valueOf("" + upStr.charAt(0));
				}
				//���λ������
				else {
					//������ȡ��һλ
					lowMask = Integer.valueOf("" + lowStr.charAt(0));
					//������ȡǰ��λ
					upMask = Integer.valueOf("" + upStr.charAt(0) + upStr.charAt(1));
				}
				//��ӽ��
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
			//��logֵ
			List<Double> logs = new ArrayList<>();
			Iterator<Double> iter = bsValues.iterator();
			while(iter.hasNext()) {
				double d = iter.next();
				logs.add(Math.log10(d));
			}
			//��log���ر�
			double rate = 0.0;
			rate = (double)length / (logs.get(logs.size() - 1) - logs.get(0));
			//������
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
			//��logֵ
			List<Double> logs = new ArrayList<>();
			Iterator<Double> iter = ssValues.iterator();
			while(iter.hasNext()) {
				double d = iter.next();
				logs.add(Math.log10(d));
			}
			//��log���ر�
			double rate = 0.0;
			rate = (double)length / (Math.log10(maxValue) - Math.log10(minValue));
			//������
			Iterator<Double> iter2 = logs.iterator();
			while(iter2.hasNext()) {
				double d = iter2.next();
				res.add((int) (rate * (d - Math.log10(minValue))));
			}
			
			return res;
		}
		
	}
	
	/**
	 * ���linear����
	 */
	private static class AxisLinearStrategy implements IAxisAlgebraStrategy {

		@Override
		public List<Double> calibrateBigScale(double minValue, double maxValue) {
			if(minValue > maxValue) {
				return null;
			}
			List<Double> res = new ArrayList<Double>();
			int min = 0;	//����minValue������
			int max = 0;	//����maxValue������
			min = (int) minValue;
			max = (int) maxValue;
			
			/*
			 * ����λ��ȷ��min��max��ͷ��ֵ
			 */
			String minStr = "";	//����min���ַ�����ʽ
			String maxStr = "";	//����max���ַ�����ʽ
			int minHead = 0;	//����minͷ��
			int maxHead = 0;	//����maxͷ��
			
			minStr = "" + min;
			maxStr = "" + max;
			// ���λ������
			if(minStr.length() != maxStr.length()) {
				/*
				 * minHeadȡmin�����λ
				 * maxHeadȡmax�����λ�ʹθ�λ
				 */
				minHead = Integer.valueOf("" + minStr.charAt(0));
				maxHead = Integer.valueOf("" + maxStr.charAt(0) + maxStr.charAt(1));
			} // ���λ�����
			else {
				// �������0-9�����С�ڵ���10
				if(minStr.length() == 1 || max - min <= 10) {
					// ֱ��ȡԭֵ
					minHead = min;
					maxHead = max;
				} 
				// �������λ�������ϻ���������10
				else {
					// ȡ���λ�ʹθ�λ
					minHead = Integer.valueOf("" + minStr.charAt(0) + minStr.charAt(1));
					maxHead = Integer.valueOf("" + maxStr.charAt(0) + maxStr.charAt(1));
				}
			}
			
			/*
			 * ȷ��ͷ����ԭֵ�ı���
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
			 * ȷ������
			 */
			int cur = min;
			int step = 0;
			if (maxHead - minHead < 5) {
				//��������Ϊ1�ı���
				step = 1 * times;
				while ((max - min) / step > 4) {
					//����Ե�ǰ��������������ķ�������4�Ļ������󲽳�
					step += 1 * times;
				}
				//�����������䣬Ѱ�ҵ�һ���̶�ֵ
				for(int i = 0; i < max; i += step) {
					if(cur < i) {
						cur = i;
						break;
					}
				}
				//��ӽ��
				for (; cur < max; cur += step) {
					res.add((double)cur);
				}
			}
			else if (maxHead - minHead < 9) {
				//��������Ϊ2�ı���
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
				//��������Ϊ5�ı���
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
			//���Բ��Բ�������
			return null;
		}

		@Override
		public List<Integer> pixelateBigScale(List<Double> bsValues, int length) {
			
			List<Integer> bsDistance = new ArrayList<Integer>();
			double rate = 0.0;	//�������
			
			rate = (length) / (bsValues.get(bsValues.size() - 1) - bsValues.get(0));
			for(double ele : bsValues) {
				if(ele != bsValues.get(bsValues.size() - 1)) {
					bsDistance.add((int) (rate * (ele - bsValues.get(0))));
				}
			}
			//���һ��Ԫ�ص�����ֵ����������������ת�������������
			bsDistance.add(length);
			
			//����ظ��ԣ�����ظ����һ������
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
			//���Բ��Բ�������
			return null;
		}
		
	}

}
