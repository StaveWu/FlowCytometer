package spectrum;

import java.util.List;
import java.util.Map;

import spectrum.wave.IWavePolicy;

public interface IIntensityCalculator {
	
	/**
	 * 获取荧光强度
	 * @param data
	 * @param threshold
	 * @param wp
	 * @return 尖峰位置-强度值
	 */
	Map<Integer, Integer> getIntensities(List<Double> data, int threshold, IWavePolicy wp);

}
