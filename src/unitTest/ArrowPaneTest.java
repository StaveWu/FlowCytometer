package unitTest;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import plot.ArrowPane;
import plot.ArrowPaneContainer;
import plot.Histogram;
import utils.SwingUtils;

public class ArrowPaneTest {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 700, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ArrowPaneContainer c = new ArrowPaneContainer();
		ArrowPane p1 = new Histogram();
		ArrowPane p2 = new Histogram();
		ArrowPane p3 = new Histogram();
		
		p1.setBounds(10, 10, 250, 250);
		p2.setBounds(300, 10, 250, 250);
		p3.setBounds(10, 300, 250, 250);
		c.add(p1);
		c.add(p2);
		c.add(p3);
		
		frame.getContentPane().add(SwingUtils.createPanelForComponent(c, "ArrowPane"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
