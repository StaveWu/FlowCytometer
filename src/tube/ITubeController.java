package tube;

import java.util.Vector;

import javax.swing.JPanel;

public interface ITubeController {
	
	void loadData(String pathname);
	
	void save();
	
	void setFields(Vector<String> fields);
	
	JPanel getView();
	
	ITubeModel getData();

}
