package menu;

import java.lang.reflect.Method;

@SuppressWarnings("serial")
public class MainPageMenuBar extends MenuBarX {
	
	private IMainMenuBarCommand mainMenuBarCommand;
	
	public MainPageMenuBar(IMainMenuBarCommand command) {
		this.mainMenuBarCommand = command;
	}

	@Override
	public String getTableName() {
		return "MainPageMenuBar";
	}

	@Override
	public void invoke(String command) {
		try {
			Method fun = IMainMenuBarCommand.class.getMethod(command);
			fun.invoke(mainMenuBarCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		// 不需要定制
	}

}
