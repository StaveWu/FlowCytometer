package unitTest;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import paramSettings.ParamController;
import paramSettings.ParamModel;
import utils.SwingUtils;

public class ParamSettingsTest {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ParamController controller = new ParamController(new ParamModel());
		frame.getContentPane().add(SwingUtils.createPanelForComponent(controller.getView(), "Paramters"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
