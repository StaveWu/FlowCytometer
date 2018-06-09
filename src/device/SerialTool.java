package device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import mainPage.FCMSettings;

public class SerialTool implements ICommDevice {
	
	private static SerialTool instance;
	
	private SerialPort serialPort;
	
	private SerialPortEventListener listener;
	
	private SerialTool() {}
	
	public static SerialTool getInstance() {
		if (instance == null) {
			instance = new SerialTool();
		}
		return instance;
	}
	
	public List<String> getPortNames() {
		List<String> res = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier ele = (CommPortIdentifier) portIdentifiers.nextElement();
			res.add(ele.getName());
		}
		return res;
	}

	@Override
	public void open() throws Exception {
		String portname = FCMSettings.getSerialPortName();
		int baudrate = FCMSettings.getBaudRate();
		
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portname);
		CommPort commPort = portIdentifier.open(portname, 2000);
		
		if (commPort instanceof SerialPort) {
			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(
					baudrate, 
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1, 
					SerialPort.PARITY_NONE);
			if (listener != null) {
				hookListener(listener);
			}
		}
	}

	@Override
	public boolean isOpen() {
		return serialPort != null;
	}

	@Override
	public byte[] read() throws Exception {
		if (serialPort == null) {
			throw new NoSuchPortException();
		}
		InputStream in = null;
		byte[] res = null;
		try {
			in = serialPort.getInputStream();
	    	int bufflength = in.available();	//获取buffer里的数据长度
	        
	    	while (bufflength != 0) {                             
	            res = new byte[bufflength];		//初始化byte数组为buffer中数据的长度
	            in.read(res);
	            bufflength = in.available();
	    	}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			}
			catch (Exception e1) {
				throw e1;
			}
		}
		return res;
	}

	@Override
	public void write(byte[] data) throws Exception {
		if (serialPort == null) {
			throw new NoSuchPortException();
		}
		OutputStream out = null;
        try {
        	out = serialPort.getOutputStream();
            out.write(data);
            out.flush();
            
        } 
        catch (IOException e) {
        	throw e;
        } 
        finally {
        	try {
        		if (out != null) {
        			out.close();
        			out = null;
        		}				
			} catch (IOException e) {
				throw e;
			}
        }
	}

	@Override
	public void close() {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}
	
	public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException {
		this.listener = listener;
		if (serialPort != null) {
			hookListener(listener);
		}
	}
	
	private void hookListener(SerialPortEventListener listener) throws TooManyListenersException {
		serialPort.addEventListener(listener);
		serialPort.notifyOnDataAvailable(true);
	}
	
	public void removeEventListener() {
		if (serialPort != null) {
			serialPort.removeEventListener();
		}
	}

	@Override
	public void addDeviceListener(Object listener) throws Exception {
		addEventListener((SerialPortEventListener) listener);
	}

	@Override
	public void removeDeviceListener(Object listener) {
		removeEventListener();
	}

}
