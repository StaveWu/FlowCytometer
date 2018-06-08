package tube;

import java.util.Vector;

public interface ITubeController {
	
	void loadData(String pathname);
	
	void save();
	
	void setFields(Vector<String> fields);
	
	void clear();

}
