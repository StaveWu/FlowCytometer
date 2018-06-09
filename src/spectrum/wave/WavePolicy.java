package spectrum.wave;

public class WavePolicy {
	
	public static final IWavePolicy AREA = new AreaWavePolicy();
	public static final IWavePolicy HEIGHT = new HeightWavePolicy();
	public static final IWavePolicy WIDTH = new WidthWavePolicy();
	
	private static class AreaWavePolicy implements IWavePolicy {
		
	}
	
	private static class HeightWavePolicy implements IWavePolicy {
		
	}
	
	private static class WidthWavePolicy implements IWavePolicy {
		
	}

}
