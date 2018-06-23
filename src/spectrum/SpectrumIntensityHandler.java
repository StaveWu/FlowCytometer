package spectrum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import spectrum.Spectrum.Wave;

public class SpectrumIntensityHandler implements Runnable {
	
	private SpectrumModel model;
	
	public SpectrumIntensityHandler(SpectrumModel model) {
		this.model = model;
	}

	@Override
	public void run() {
		model.setEvents(calculateIntensities(model.getSpectrums()));
	}
	
	/**
	 * �������й��׵�ǿ�ȶ�
	 * @param spectrums	����
	 * @return	����ǿ�ȶ�
	 */
	public List<Vector<Double>> calculateIntensities(List<Spectrum> spectrums) {
		
//		System.out.println("����ǿ����.." + spectrums.get(0).getData().size());
		
		List<Vector<Double>> res = new ArrayList<>();
		
		Map<Integer, List<Wave>> wavesMap = new HashMap<>();
		for (int i = 0; i < spectrums.size(); i++) {
			wavesMap.put(i, spectrums.get(i).getWaves());
		}
		
		// Ϊ���в�����
		List<WaveInfo> waveInfos = new ArrayList<>();
		for (Integer key : wavesMap.keySet()) {
			for (Wave wave : wavesMap.get(key)) {
				waveInfos.add(new WaveInfo(key, wave));
			}
		}
		
		// �����в�������
		Collections.sort(waveInfos);
		
		// ��ȡ�����Ĳ����Ӧ�Ĺ���id����
		LinkedList<Integer> sequencialId = new LinkedList<>();
		for (WaveInfo ele : waveInfos) {
			sequencialId.add(ele.ownerId);
		}
		
		// ���ÿ���Ϊ��ͬһ��ϸ���Ĳ������ƫ����
		int horizontalShift = 2;
		
		// ѭ�����ǿ�ȶ�
		while (!sequencialId.isEmpty()) {
			Vector<Double> event = new Vector<>();
			
			// ȡ���ж�Ӧ�ĵ�һ������id������
			int baseId = sequencialId.getFirst();
			int basePosition = wavesMap.get(baseId).get(0).position;
			
			// ȡ�������׵ĵ�һ�����壬�Ƚ��Ƿ��������ƫ��������
			for (Integer key : wavesMap.keySet()) {
				if (wavesMap.get(key).size() > 0) {
					Wave wave = wavesMap.get(key).get(0);
					if (wave.position - basePosition <= horizontalShift) {
						// ���ǿ��
						event.add(wave.getWaveIntensity());
						// ɾ���ò���
						wavesMap.get(key).remove(0);
						// ɾ������id�е�һ�γ���key�Ľڵ�
						for (int i = 0; i < sequencialId.size(); i++) {
							if (sequencialId.get(i) == key) {
								sequencialId.remove(i);
								break;
							}
						}
					}
					else {
						event.add(0.0);
					}
				}
				else {
					event.add(0.0);
				}
			}
			res.add(event);
		}
		
		return res;
	}
	
	private class WaveInfo implements Comparable<WaveInfo> {
		
		int ownerId;
		Wave wave;
		
		WaveInfo(int ownerId, Wave wave) {
			super();
			this.ownerId = ownerId;
			this.wave = wave;
		}


		@Override
		public int compareTo(WaveInfo o) {
			return this.wave.compareTo(o.wave);
		}
	}

}
