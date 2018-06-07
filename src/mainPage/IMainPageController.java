package mainPage;

public interface IMainPageController {
	
	// ������
	void loadParamSettings();
	
	void loadWorkSheet();
	
	void loadTube();
	
	void saveTube();
	
	void saveWorkSheet();
	
	void saveParamSettings();
	
	// ͨѶ�豸
	void changeVoltage(int voltage);
	
	void startSampling();
	
	void stopSampling();

}
