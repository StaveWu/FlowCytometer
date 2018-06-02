package device;

public enum CommDeviceType {
	
	SERIALPORT("SerialPort"),
	USB("USB");
	
	private String s;
	
	CommDeviceType(String s) {
		this.s = s;
	}
	
	@Override
	public String toString() {
		return s;
	}

}
