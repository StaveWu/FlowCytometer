package dashBoard;

import java.util.Map;

public interface IDashBoardModelObserver {
	
	void statusChanged(boolean isOnSampling);
	
	void dataAvailable(Map<String, Double> data);

}
