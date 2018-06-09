package spectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spectrum.wave.IWavePolicy;

public class Spectrum {
	
	private String name;
	private int channelId;
	private int threshold;
	private IWavePolicy wavePolicy;
	
	private List<Double> data = new ArrayList<>(100);
	private IIntensityCalculator calculator;
	
	public Spectrum() {
		calculator = new IntensityCalculator();
	}
	
	public void addData(double d) {
		data.add(d);
	}
	
	public Map<Integer, Integer> getIntensities() {
		return calculator.getIntensities(data, threshold, wavePolicy);
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

	public IWavePolicy getWavePolicy() {
		return wavePolicy;
	}

	public void setWavePolicy(IWavePolicy wavePolicy) {
		this.wavePolicy = wavePolicy;
	}
	

}
