package mainPage;

public class Application {
	
	// ���������
	public static void main(String[] args) {
		if (FCMSettings.bootSpaceChooserBox()) {
			new WorkSpaceChooserBox();
		} else {
			new MainView();
		}
	}

}
