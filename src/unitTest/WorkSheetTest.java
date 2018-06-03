package unitTest;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import utils.SwingUtils;
import worksheet.WorkSheetController;
import worksheet.WorkSheetModel;
import worksheet.interfaces.IWorkSheetController;

public class WorkSheetTest {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 700, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		IWorkSheetController controller = new WorkSheetController(new WorkSheetModel());
		frame.getContentPane().add(SwingUtils.createPanelForComponent(controller.getView(), "WorkSheet"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
