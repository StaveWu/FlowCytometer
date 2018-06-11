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
	

}
