package dashBoard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import device.ICommDevice;
import device.NonConnectionDevice;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class DashBoardModel implements SerialPortEventListener {
	
	private ICommDevice device = new NonConnectionDevice();
	
	private boolean isOnSampling = false;
	
	private List<IDashBoardModelObserver> observers = new ArrayList<>();
	
	public DashBoardModel() {
		
	}
	
	/**
	 * 控制硬件开始采样
	 * @throws Exception
	 */
	public void startSampling(List<?> param) throws Exception {
		device.write(encode("StartSampling", param));
		
		isOnSampling = true;
		notifyObservers();
	}
	
	/**
	 * 控制硬件停止采样
	 * @throws Exception 
	 */
	public void stopSampling(List<?> param) throws Exception {
		device.write(encode("StopSampling", param));
		
		isOnSampling = false;
		notifyObservers();
	}
	
	/**
	 * 调节电压
	 * @param message
	 * @throws Exception
	 */
	public void changeVoltage(List<?> param) throws Exception {
		device.write(encode("ChangeVoltage", param));
	}
	
	public boolean isOnSampling() {
		return isOnSampling;
	}
	
	public void addObserver(IDashBoardModelObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(IDashBoardModelObserver o) {
		observers.remove(o);
	}
	
	public void notifyObservers() {
		observers.stream().forEach(e -> e.statusChanged(isOnSampling));
	}

	public ICommDevice getDevice() {
		return device;
	}

	public void setDevice(ICommDevice device) throws Exception {
		ICommDevice old = this.device;
		old.removeDeviceListener(this);
		this.device = device;
		this.device.addDeviceListener(this);
	}
	
	public boolean isConnected() {
		return device.isOpen();
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Map<String, Double> kvs = decode(device.read());
				System.out.println(kvs);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new RuntimeException("数据读取错误！");
			}
		}
		
	}
	
	
	//////////////// CommDataParser //////////////
	
	private byte[] encode(String cmd, List<?> param) {
		// 格式：CMD:CH1,CH2,CH3
		String partB = "";
		for (Object ele : param) {
			partB += ele;
			partB += ",";
		}
		partB = partB.substring(0, partB.length() - 1);
		return (cmd + ":" + partB + "\r\n").getBytes();
	}
	
	private Map<String, Double> decode(byte[] data) {
		// 格式：FSC:XXX_SSC:XXX_FITC:XXX
		Map<String, Double> res = new LinkedHashMap<>();
		String dataStr = new String(data);
		for (String ele : dataStr.split("_")) {
			String[] kv = ele.split(":");
			res.put(kv[0], Double.valueOf(kv[1]));
		}
		return res;
	}

}
