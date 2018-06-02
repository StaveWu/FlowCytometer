package test;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ActionTest {
	
	public static void main(String[] args) {
		JFrame f = new JFrame("");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(300, 300, 800, 500);
		f.getContentPane().setLayout(null);
		f.getContentPane().setBackground(Color.red);
		
		JPanel p = new MyPanel();
		p.setName("aaa");
		p.setBounds(10, 10, 200, 200);
		f.add(p);
		
		JPanel p2 = new MyPanel();
		p2.setName("bbb");
		p2.setBounds(500, 10, 200, 200);
		f.add(p2);
		
		f.setVisible(true);
	}

}

class MyPanel extends JPanel {
	
	public MyPanel() {
		TestListener l = new TestListener();
		addMouseListener(l);
		addMouseMotionListener(l);
	}
	
	class TestListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("clicked" + getName());
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("dragged" + getName());
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("released" + getName());
		}
	}
	
}
