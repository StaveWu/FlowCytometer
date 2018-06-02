package utils;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SwingUtils {
	
	/**
	 * Ϊ���������
	 * @param comp
	 * @param title
	 * @return
	 */
	public static JPanel createPanelForComponent(JComponent comp, String title) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(comp, BorderLayout.CENTER);
		if (title != null) {
			panel.setBorder(BorderFactory.createTitledBorder(title));
		}
		return panel;
	}
	
	public static JPanel createVerticalBoxPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return p;
    }
	
	public static void showErrorDialog(Component invoker, String message) {
		JOptionPane.showMessageDialog(invoker, 
				message, 
				"����", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	public static int showConfirmDialog(Component invoker, String message) {
		return JOptionPane.showConfirmDialog(invoker, 
				message, 
				"����", 
				JOptionPane.YES_NO_OPTION);
	}
	
	public static String showInputDialog(Component invoker, String message) {
		return JOptionPane.showInputDialog(
	             invoker,
	             message,
	             "����",
	             JOptionPane.PLAIN_MESSAGE);
	}

}
