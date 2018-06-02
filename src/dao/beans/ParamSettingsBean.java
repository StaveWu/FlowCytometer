package dao.beans;

public class ParamSettingsBean {
	
	/**
	 * 参数名
	 */
	private String paramName;
	
	/**
	 * 电压值
	 */
	private int voltage;
	
	/**
	 * 阈值
	 */
	private int threshold;
	
	private boolean isA;
	
	private boolean isH;
	
	private boolean isW;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
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

	public boolean isA() {
		return isA;
	}

	public void setA(boolean isA) {
		this.isA = isA;
	}

	public boolean isH() {
		return isH;
	}

	public void setH(boolean isH) {
		this.isH = isH;
	}

	public boolean isW() {
		return isW;
	}

	public void setW(boolean isW) {
		this.isW = isW;
	}


}
