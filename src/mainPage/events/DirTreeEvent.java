package mainPage.events;

public class DirTreeEvent extends ControllerEvent {
	
	public static final int NEW_PROJECT = 1;
	public static final int OPEN_SETTINGS = 2;
	public static final int OPEN_WORKSHEET = 4;
	public static final int OPEN_TUBE = 8;
	
	private String relaPath;
	
	public DirTreeEvent(Object source, int actionCommand, String relaPath) {
		super(source, actionCommand);
		this.relaPath = relaPath;
	}
	
	public String getRelaPath() {
		return relaPath;
	}
	
}
