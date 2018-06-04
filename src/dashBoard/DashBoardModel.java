package dashBoard;

import device.CommDeviceType;
import device.ICommDevice;
import device.SerialTool;
import mainPage.FCMSettings;

public class DashBoardModel {
	
	private ICommDevice device;
	
	public DashBoardModel() {
		
	}
	
	/**
	 * 控制硬件开始采样
	 * @throws Exception
	 */
	public void startSampling() throws Exception {
		confirmCurrentSelectedDevice();
		String message = "开始采样";
		device.write(message.getBytes());
	}
	
	/**
	 * 控制硬件停止采样
	 * @throws Exception 
	 */
	public void stopSampling() throws Exception {
		confirmCurrentSelectedDevice();
		String message = "停止采样";
		device.write(message.getBytes());
	}
	
	private void confirmCurrentSelectedDevice() {
		CommDeviceType currentSelected = FCMSettings.getCommDeviceType();
		if (currentSelected == CommDeviceType.SERIALPORT)  {
			device = SerialTool.getInstance();
		}
		else if (currentSelected == CommDeviceType.USB) {
			
		}
	}

}
