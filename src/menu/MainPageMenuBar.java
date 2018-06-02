package menu;

import java.lang.reflect.Method;

import mainPage.MainPageController;

public class MainPageMenuBar extends MenuBarX {
	
	private MainPageController controller;
	
	public MainPageMenuBar(MainPageController controller) {
		this.controller = controller;
	}

	@Override
	public String getTableName() {
		return "MainPageMenuBar";
	}

	@Override
	public void invoke(String command) {
		try {
			Method fun = MainPageController.class.getMethod(command);
			fun.invoke(controller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		// 不需要定制
	}

}
