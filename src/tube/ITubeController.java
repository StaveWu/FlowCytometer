package tube;

import java.util.List;
import java.util.Vector;

public interface ITubeController {
	
	void loadData(String pathname);
	
	void save();
	
	void setFields(Vector<String> fields);
	
	void addEvents(List<Vector<Double>> es);
	
	/**
	 * �滻���е�events
	 * @param es
	 */
	void updateEvents(List<Vector<Double>> es);
	
	void clear();

}
