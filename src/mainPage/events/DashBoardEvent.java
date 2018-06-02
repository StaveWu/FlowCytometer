package mainPage.events;

public class DashBoardEvent extends ControllerEvent {
	
	public static final int START_SAMPLING = 1;
	
	public static final int STOP_SAMPLING = 2;

	public DashBoardEvent(Object source, int actionCommand) {
		super(source, actionCommand);
		// TODO Auto-generated constructor stub
	}

}
