package mainPage.events;

import java.util.Vector;

public class ParamSettingsEvent extends ControllerEvent {
	
	public static final int UPDATE = 1;
	
	public static final int ADD = 2;
	
	public static final int REMOVE = 3;
	
	private Vector<String> dataNames;
	
	public ParamSettingsEvent(Object source, int actionCommand, Vector<String> dataNames) {
		super(source, actionCommand);
		this.dataNames = dataNames;
	}

	public Vector<String> getDataNames() {
		return dataNames;
	}

	public void setDataNames(Vector<String> dataNames) {
		this.dataNames = dataNames;
	}
	
	

}
