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
		notifyStatusChanged();
	}
	
	/**
	 * 控制硬件停止采样
	 * @throws Exception 
	 */
	public void stopSampling() throws Exception {
		device.write(encode("StopSampling"));
		
		isOnSampling = false;
		notifyStatusChanged();
	}
	
	/**
	 * 调节电压
	 * @param message
	 * @throws Exception
	 */
	public void changeVoltage(List<?> param) throws Exception {
		device.write(encode("ChangeVoltage", param));
	}
	
	/**
	 * 复位下位机
	 * @throws Exception
	 */
	public void resetSystem() throws Exception {
		device.write(encode("ResetSystem"));
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
	
	public void notifyStatusChanged() {
		observers.stream().forEach(e -> e.statusChanged(isOnSampling));
	}
	
	public void notifyDataAvailable(Map<String, Double> data) {
		observers.stream().forEach(e -> e.dataAvailable(data));
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
				notifyDataAvailable(decode(device.read()));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new RuntimeException("数据读取错误！");
			}
		}
		
	}
	
	
	//////////////// CommDataParser //////////////
	
	private byte[] encode(String cmd) {
		return encode(cmd, null);
	}
	
	private byte[] encode(String cmd, List<?> param) {
		// 格式：CMD:CH1,CH2,CH3
		String partB = "";
		if (param != null) {
			for (Object ele : param) {
				partB += ele;
				partB += ",";
			}
			partB = partB.substring(0, partB.length() - 1);
		}
		return (cmd + ":" + partB + "\r\n").getBytes();
	}
	
	private Map<String, Double> decode(byte[] data) {
		// 格式：CH1:XXX_CH2:XXX_CH3:XXX
		Map<String, Double> res = new LinkedHashMap<>();
		String dataStr = new String(data);
		for (String ele : dataStr.split("\r\n")) {
			if (ele.length() == 0) {
				continue;
			}
			for (String ele2 : ele.split("_")) {
				String[] kv = ele2.split(":");
				res.put(kv[0], Double.valueOf(kv[1]));
			}
		}
		return res;
	}

}
