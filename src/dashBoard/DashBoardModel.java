package dashBoard;

import device.CommDeviceType;
import device.ICommDevice;
import device.SerialTool;
import mainPage.FCMSettings;

public class DashBoardModel {
	
	private ICommDevice device;
	
	public DashBoardModel() {}
	
	/**
	 * 控制硬件开始采样
	 * @throws Exception
	 */
	public void startSampling() throws Exception {
		confirmCurrentSelectedDevice();
		device.open();
	}
	
	/**
	 * 控制硬件停止采样
	 */
	public void stopSampling() {
		device.close();
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
