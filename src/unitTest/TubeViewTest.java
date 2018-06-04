package unitTest;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import tube.ITubeController;
import tube.TubeController;
import tube.TubeModel;
import utils.SwingUtils;

public class TubeViewTest {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ITubeController controller = new TubeController(new TubeModel());
		frame.getContentPane().add(SwingUtils.createPanelForComponent(controller.getView(), "TubeView"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
