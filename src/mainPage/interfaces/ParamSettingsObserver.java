package mainPage.interfaces;

import mainPage.events.ParamSettingsEvent;

public interface ParamSettingsObserver {
	
	public void paramSettingsUpdated(ParamSettingsEvent ev);
}
