package spectrum;

import java.util.List;
import java.util.Map;

import spectrum.wave.IWavePolicy;

public interface IIntensityCalculator {
	
	/**
	 * ��ȡӫ��ǿ��
	 * @param data
	 * @param threshold
	 * @param wp
	 * @return ���λ��-ǿ��ֵ
	 */
	Map<Integer, Integer> getIntensities(List<Double> data, int threshold, IWavePolicy wp);

}
