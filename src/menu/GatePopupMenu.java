package menu;

import java.awt.Point;
import java.lang.reflect.Method;

import plot.Plot;

public class GatePopupMenu extends PopupMenuX {
	
	private Plot plot;
	private Point location;
	
	public GatePopupMenu(Plot plot, Point location) {
		this.plot = plot;
		this.location = location;
	}

	@Override
	public String getTableName() {
		return "GateMenu";
	}

	@Override
	public void invoke(String command) {
		try {
			Method fun = Plot.class.getMethod(command, Point.class);
			fun.invoke(plot, location);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		// 这边菜单不需要改变
	}

}
