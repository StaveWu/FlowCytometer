package paramSettings;

public class Channel {
	
	private int index;
	private String name;
	private int voltage;
	private int threshold;
	private String waveCalculateMethod;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVoltage() {
		return voltage;
	}
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
	public int getThreshold() {
		return threshold;
	}
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	public String getWaveCalculateMethod() {
		return waveCalculateMethod;
	}
	public void setWaveCalculateMethod(String waveCalculateMethod) {
		this.waveCalculateMethod = waveCalculateMethod;
	}
	
	

}
