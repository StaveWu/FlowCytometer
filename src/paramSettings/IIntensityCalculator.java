package paramSettings;

import java.util.List;

public interface IIntensityCalculator {
	
	double[] getIntensities(List<Double> data, int threshold, String ahw);

}
