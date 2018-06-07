package paramSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import device.CommDataParser;
import device.SerialTool;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SpectrumAnalysis implements SerialPortEventListener, Runnable {
	
	private List<Spectrum> spectrums = new ArrayList<>();
	
	private Map<String, double[]> intensities;
	
	private List<IIntensityObserver> observers = new ArrayList<>();
	
	public void addSpectrum(Spectrum s) {
		spectrums.add(s);
	}
	
	public void removeSpectrum(int index) {
		spectrums.remove(index);
	}
	
	public Spectrum getSpectrumAt(int index) {
		return spectrums.get(index);
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Map<String, Double> received = decode(SerialTool.getInstance().read());
				for (String key : received.keySet()) {
					Spectrum sp = getSpectrumByName(key);
					sp.addData(received.get(key));
					notifyObservers();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void addObservers(IIntensityObserver o) {
		observers.add(o);
	}
	
	public void removeObservers(IIntensityObserver o) {
		observers.remove(o);
	}
	
	public void notifyObservers() {
		observers.stream().forEach(e -> e.intensitiesMayChanged(intensities));
	}
	
	private Spectrum getSpectrumByName(String name) {
		for (Spectrum s : spectrums) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		throw new NoSuchElementException();
	}
	
	private Map<String, Double> decode(byte[] data) {
		 return CommDataParser.PLAIN.decode(data);
	}

	@Override
	public void run() {
		while (true) {
			calculateIntensities();
		}
	}
	
	private void calculateIntensities() {
		Map<String, double[]> res = new HashMap<>();
		for (Spectrum s : spectrums) {
			res.put(s.getName(), s.getIntensities());
		}
		intensities = res;
	}

}
