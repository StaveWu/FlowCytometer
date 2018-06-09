package mainPage;

public class Application {
	
	// 主程序入口
	public static void main(String[] args) {
		if (FCMSettings.bootSpaceChooserBox()) {
			new WorkSpaceChooserBox();
		} else {
			new MainView();
		}
	}

}
