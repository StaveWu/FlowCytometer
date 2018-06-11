package spectrum;

import java.util.List;
import java.util.Vector;

public interface SpectrumModelObserver {
	
	void newEventGenerated(List<Vector<Double>> data);

}
