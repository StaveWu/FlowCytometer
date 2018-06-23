package test;

import java.util.ArrayList;
import java.util.List;

import spectrum.Spectrum;
import spectrum.SpectrumIntensityHandler;

public class IntensityCalculateTest {
	
	public static void main(String[] args) {
		SpectrumIntensityHandler intensityHandler = new SpectrumIntensityHandler(null);
		Spectrum s1 = new Spectrum();
		s1.setThreshold(9);
		s1.setWavePolicy("A");
		s1.addData(8.0);
		s1.addData(12.0);
		s1.addData(8.0);
		s1.addData(12.0);
		s1.addData(15.0);
		s1.addData(8.0);
		
		Spectrum s2 = new Spectrum();
		s2.setThreshold(10);
		s2.setWavePolicy("W");
		s2.addData(8.0);
		s2.addData(13.0);
		s2.addData(13.0);
		s2.addData(13.0);
		s2.addData(13.0);
		s2.addData(13.0);
		s2.addData(13.0);
		s2.addData(8.0);
		s2.addData(9.0);
		s2.addData(13.0);
		s2.addData(8.0);
		
		List<Spectrum> ss = new ArrayList<>();
		ss.add(s1);
		ss.add(s2);
		
		System.out.println(intensityHandler.calculateIntensities(ss));
		
	}

}
