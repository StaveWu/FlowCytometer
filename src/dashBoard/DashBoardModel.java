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
	 * ����Ӳ����ʼ����
	 * @throws Exception
	 */
	public void startSampling() throws Exception {
		confirmCurrentSelectedDevice();
		String message = "LED1:\r\n";
		String m2 = "data1:\r\n";
		String m3 = "data2:\r\n";
		device.write(m3.getBytes());
	}
	
	/**
	 * ����Ӳ��ֹͣ����
	 * @throws Exception 
	 */
	public void stopSampling() throws Exception {
		confirmCurrentSelectedDevice();
		String message = "ֹͣ����";
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
