package worksheet.interfaces;

import javax.swing.JPanel;

import mainPage.events.WorkSheetEvent;
import mainPage.interfaces.WorkSheetObserver;
import menu.IContainerPopupMenuCommand;
import tube.ITubeModel;

public interface IWorkSheetController extends IContainerPopupMenuCommand {
	
	void loadWorkSheet(String pathname);
	
	void addDataSource(ITubeModel data);
	
	JPanel getView();
	
	void addObserver(WorkSheetObserver observer);
	
	void removeObserver(WorkSheetObserver observer);
	
	void notifyObservers(WorkSheetEvent event);

}
