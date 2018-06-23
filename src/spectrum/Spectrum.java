package spectrum;

import java.util.ArrayList;
import java.util.List;

public class Spectrum {
	
	private String name;
	private int channelId;
	private int threshold;
	private String wavePolicy;
	
	private List<Double> data = new ArrayList<>();
	
	public Spectrum() {
		
	}
	
	public void addData(double d) {
		data.add(d);
	}
	
	public List<Double> getData() {
		return data;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getWavePolicy() {
		return wavePolicy;
	}

	public void setWavePolicy(String wavePolicy) {
		this.wavePolicy = wavePolicy;
	}
	
	public class Wave implements Comparable<Wave> {
		int waveStart;
		int waveEnd;
		int position;
		
		public Wave(int waveStart, int waveEnd, int position) {
			super();
			this.waveStart = waveStart;
			this.waveEnd = waveEnd;
			this.position = position;
		}
		
		public double getWaveIntensity() {
			if (wavePolicy.equals("A")) {
				int area = 0;
				for (int i = waveStart; i < waveEnd + 1; i++) {
					area += data.get(i);
				}
				return area;
			}
			else if (wavePolicy.equals("H")) {
				return data.get(position);
			}
			else {
				return waveEnd - waveStart;
			}
		}

		@Override
		public int compareTo(Wave o) {
			// �Ƚ�λ��
			double a = this.position - o.position;
			if (a < 0) {
				return -1;
			}
			else if (a > 0)  {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	
	public List<Wave> getWaves() {
		List<Wave> res = new ArrayList<>();
		int waveStart = 0;
		int waveEnd = 0;
		for (int i = 1; i < data.size(); i++) {
			// ��������˴ӱ���ֵС������ֵ�������
			if (data.get(i - 1) <= threshold && data.get(i) > threshold) {
				// ���¿�ʼ��
				waveStart = i - 1;
			}
			// ��������˴ӱ���ֵ�󵽱���ֵС������
			if (data.get(i - 1) >= threshold && data.get(i) < threshold) {
				// ���½�����
				waveEnd = i - 1;
				
				// ȷ����λ�ã��ڷ����ߴ�
				int wavePosition = 0;
				double waveHeight = 0;
				for (int j = waveStart; j < waveEnd + 1; j++) {
					if (waveHeight < data.get(j)) {
						waveHeight = data.get(j);
						wavePosition = j;
					}
				}
				
				// ��ӽ��
				res.add(new Wave(waveStart, waveEnd, wavePosition));
				
				// ��λ��ʼ��ͽ�����
				waveStart = 0;
				waveEnd = 0;
			}
		}
		
		return res;
	}

}
