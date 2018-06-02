package test;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import plot.ArrowPaneContainer;

public class ArrowPaneContainerTest {
	
	public static void main(String[] args) {
		JFrame f = new JFrame("");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(10, 10, 1300, 800);
		
		ArrowPaneContainer container = new ArrowPaneContainer();
		container.setBackground(Color.orange);
		container.setLayout(null);
		f.add(container, BorderLayout.CENTER);
		
		f.setVisible(true);
	}

}
