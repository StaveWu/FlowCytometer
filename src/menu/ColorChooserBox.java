package menu;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import plot.Plot;

public class ColorChooserBox {

	private JFrame frame;
	private JColorChooser colorChooser;
	
	private Plot pane;
	private Point location;

	/**
	 * Create the application.
	 */
	public ColorChooserBox(Plot pane, Point location) {
		this.pane = pane;
		this.location = location;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		frame.setBounds(location.x, location.y, 485, 350);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		colorChooser = new JColorChooser();
		colorChooser.setBounds(10, 10, 450, 250);
		frame.getContentPane().add(colorChooser);
		
		JButton btnNewButton = new JButton("È·¶¨");
		btnNewButton.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton.setBounds(310, 280, 60, 23);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pane.setGateColor(colorChooser.getColor());
				pane.repaint();
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("¹Ø±Õ");
		btnNewButton_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 12));
		btnNewButton_1.setBounds(380, 280, 60, 23);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnNewButton_1);
	}
}
