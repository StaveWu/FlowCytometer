package menu;

import java.lang.reflect.Method;

import plot.Plot;

public class DataRegionPopupMenu extends PopupMenuX {
	
	private Plot plot;
	
	public DataRegionPopupMenu(Plot plot) {
		this.plot = plot;
	}
	
	@Override
	public String getTableName() {
		return "DataRegionMenu";
	}

	@Override
	public void invoke(String command) {
		try {
			Method fun = Plot.class.getMethod(command);
			fun.invoke(plot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		// 这里不需要定制菜单
	}

}
