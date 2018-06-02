package mainPage.events;

public class ControllerEvent {
	
	private Object source;
	
	private int actionCommand;
	
	public ControllerEvent(Object source, int actionCommand) {
		this.source = source;
		this.actionCommand = actionCommand;
	}

	public Object getSource() {
		return source;
	}

	public int getActionCommand() {
		return actionCommand;
	}

}
