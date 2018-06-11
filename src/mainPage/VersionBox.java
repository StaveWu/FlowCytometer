package mainPage;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class VersionBox extends JFrame {
	
	public VersionBox() {
		this.setBounds(500, 400, 260, 230);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		JLabel label = new JLabel("V 1.0.0", JLabel.CENTER);
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 36));
		this.getContentPane().add(label, BorderLayout.CENTER);
		
		this.setVisible(true);
	}

}
