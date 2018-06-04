package worksheet.interfaces;

import java.awt.Point;

import javax.swing.JPanel;

import mainPage.events.WorkSheetEvent;
import mainPage.interfaces.WorkSheetObserver;
import tube.ITubeModel;

public interface IWorkSheetController {
	
	void loadWorkSheet(String pathname);
	
	void save(Point location);
	
	void createDotPlot(Point location);
	
	void createHistogram(Point location);
	
	void createDensityPlot(Point location);
	
	void addDataSource(ITubeModel data);
	
	JPanel getView();
	
	void addObserver(WorkSheetObserver observer);
	
	void removeObserver(WorkSheetObserver observer);
	
	void notifyObservers(WorkSheetEvent event);

}
