package mainPage.interfaces;

import mainPage.events.DirTreeEvent;

public interface DirTreeObserver {
	
	public void dirTreeUpdated(DirTreeEvent event);

}
