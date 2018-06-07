package paramSettings;

import java.util.ArrayList;
import java.util.List;

public class Spectrum {
	
	private String name;
	private int threshold;
	private String ahw = "A";
	
	private List<Double> data = new ArrayList<>(100);
	private IIntensityCalculator calculator;
	
	public void addData(double d) {
		data.add(d);
	}
	
	public double[] getIntensities() {
		return calculator.getIntensities(data, threshold, ahw);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getAhw() {
		return ahw;
	}

	public void setAhw(String ahw) {
		this.ahw = ahw;
	}
	
	

}
