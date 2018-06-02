package test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.SwingUtils;

public class AddOrderTest {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(50, 50, 500, 500);
		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		frame.add(panel);
		frame.setVisible(true);
		
		panel.add(new JLabel("aaa"));
		panel.add(new JLabel("bbb"));
		
		panel.add(SwingUtils.createPanelForComponent(new JPanel(), "sss"));
	}

}
