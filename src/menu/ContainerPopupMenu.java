package menu;

import java.awt.Point;
import java.lang.reflect.Method;

@SuppressWarnings("serial")
public class ContainerPopupMenu extends PopupMenuX {
	
	private IContainerPopupMenuCommand containerPopupMenuCommand;
	private Point location;

	public ContainerPopupMenu(IContainerPopupMenuCommand controller, Point location) {
		this.containerPopupMenuCommand = controller;
		this.location = location;
	}

	@Override
	public String getTableName() {
		return "ArrowPaneContainer";
	}

	@Override
	public void invoke(String command) {
		try {
			Method fun = IContainerPopupMenuCommand.class.getMethod(command, Point.class);
			fun.invoke(containerPopupMenuCommand, location);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		// 不用定制
	}

}
