package menu;

import java.awt.Point;
import java.lang.reflect.Method;

import worksheet.interfaces.IWorkSheetController;

public class ContainerPopupMenu extends PopupMenuX {
	
	private IWorkSheetController controller;
	private Point location;

	public ContainerPopupMenu(IWorkSheetController controller, Point location) {
		this.controller = controller;
		this.location = location;
	}

	@Override
	public String getTableName() {
		return "ArrowPaneContainer";
	}

	@Override
	public void invoke(String command) {
		try {
			Method fun = IWorkSheetController.class.getMethod(command, Point.class);
			fun.invoke(controller, location);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		// 不用定制
	}

}
