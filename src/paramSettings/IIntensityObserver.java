package paramSettings;

import java.util.Map;

public interface IIntensityObserver {
	
	public void intensitiesMayChanged(Map<String, double[]> intensities);

}
