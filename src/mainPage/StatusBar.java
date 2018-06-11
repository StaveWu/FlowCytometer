package mainPage;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StatusBar extends JPanel {
	
	public StatusBar(Component[] components) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(10));
		for (int i = 0; i < components.length; i++) {
			this.add(components[i]);
			if (i != components.length - 1) {
				this.add(Box.createHorizontalGlue());
			}
		}
		this.add(Box.createHorizontalStrut(10));
	}
	
	

}
