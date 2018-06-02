package mainPage.interfaces;

import mainPage.events.WorkSheetEvent;

public interface WorkSheetObserver {
	
	public void workSheetUpdated(WorkSheetEvent event);

}
