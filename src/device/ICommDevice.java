package device;

public interface ICommDevice {
	
	void open() throws Exception;
	
	boolean isOpen();
	
	byte[] read() throws Exception;
	
	void write(byte[] data) throws Exception;
	
	void close();
	
	void addDeviceListener(Object listener) throws Exception;
	
	void removeDeviceListener(Object listener);

}
