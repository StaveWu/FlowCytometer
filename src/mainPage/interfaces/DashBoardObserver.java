package mainPage.interfaces;

import mainPage.events.DashBoardEvent;

public interface DashBoardObserver {
	
	public void dashBoardUpdated(DashBoardEvent ev);

}
