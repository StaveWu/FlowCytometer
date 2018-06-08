package menu;

import java.awt.Point;

public interface IContainerPopupMenuCommand {
	
	void save(Point location);
	
	void createDotPlot(Point location);
	
	void createHistogram(Point location);
	
	void createDensityPlot(Point location);

}
