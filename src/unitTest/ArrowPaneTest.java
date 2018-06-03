package unitTest;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import plot.ArrowPaneContainer;
import utils.SwingUtils;

public class ArrowPaneTest {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 700, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(SwingUtils.createPanelForComponent(new ArrowPaneContainer(), "ArrowPane"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
