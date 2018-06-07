package mainPage;

public interface IMainPageController {
	
	// 加载器
	void loadParamSettings();
	
	void loadWorkSheet();
	
	void loadTube();
	
	void saveTube();
	
	void saveWorkSheet();
	
	void saveParamSettings();
	
	// 通讯设备
	void changeVoltage(int voltage);
	
	void startSampling();
	
	void stopSampling();

}
