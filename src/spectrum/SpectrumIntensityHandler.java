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
	 * 计算所有光谱的强度对
	 * @param spectrums	光谱
	 * @return	所有强度对
	 */
	public List<Vector<Double>> calculateIntensities(List<Spectrum> spectrums) {
		
//		System.out.println("计算强度了.." + spectrums.get(0).getData().size());
		
		List<Vector<Double>> res = new ArrayList<>();
		
		Map<Integer, List<Wave>> wavesMap = new HashMap<>();
		for (int i = 0; i < spectrums.size(); i++) {
			wavesMap.put(i, spectrums.get(i).getWaves());
		}
		
		// 为所有波峰编号
		List<WaveInfo> waveInfos = new ArrayList<>();
		for (Integer key : wavesMap.keySet()) {
			for (Wave wave : wavesMap.get(key)) {
				waveInfos.add(new WaveInfo(key, wave));
			}
		}
		
		// 对所有波峰排序
		Collections.sort(waveInfos);
		
		// 获取排序后的波峰对应的光谱id序列
		LinkedList<Integer> sequencialId = new LinkedList<>();
		for (WaveInfo ele : waveInfos) {
			sequencialId.add(ele.ownerId);
		}
		
		// 设置可认为是同一个细胞的波峰横向偏移量
		int horizontalShift = 2;
		
		// 循环添加强度对
		while (!sequencialId.isEmpty()) {
			Vector<Double> event = new Vector<>();
			
			// 取序列对应的第一个光谱id及波峰
			int baseId = sequencialId.getFirst();
			int basePosition = wavesMap.get(baseId).get(0).position;
			
			// 取其他光谱的第一个波峰，比较是否在允许的偏移量以内
			for (Integer key : wavesMap.keySet()) {
				if (wavesMap.get(key).size() > 0) {
					Wave wave = wavesMap.get(key).get(0);
					if (wave.position - basePosition <= horizontalShift) {
						// 添加强度
						event.add(wave.getWaveIntensity());
						// 删除该波峰
						wavesMap.get(key).remove(0);
						// 删除序列id中第一次出现key的节点
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
