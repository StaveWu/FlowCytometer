package paramSettings.interfaces;

import java.awt.Component;
import java.util.Map;

public interface IParamController {
	
	void loadSettings();
	
	void saveSettings();
	
	void addRow();
	
	void removeRow(int row);
	
	void clear();
	
	void modifySelection(int row, int column, Map<Integer, Component> checkBoxsMap);

}
