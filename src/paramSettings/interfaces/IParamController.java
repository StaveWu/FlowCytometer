package paramSettings.interfaces;

import java.awt.Component;
import java.util.Map;

import javax.swing.JPanel;

import mainPage.events.ParamSettingsEvent;
import mainPage.interfaces.ParamSettingsObserver;

public interface IParamController {
	
	void loadSettings(String pathname);
	
	void addRow();
	
	void removeRow(int row);
	
	void modifySelection(int row, int column, Map<Integer, Component> checkBoxsMap);
	
	void disableTableEdit();
	
	void enableTableEdit();
	
	JPanel getView();
	
	void addObserver(ParamSettingsObserver observer);
	
	void removeObserver(ParamSettingsObserver observer);
	
	void notifyObservers(ParamSettingsEvent event);

}
