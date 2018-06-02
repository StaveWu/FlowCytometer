package unitTest;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JFrame;

import projectTree.IProjectTreeController;
import projectTree.IProjectTreeModel;
import projectTree.ProjectTreeController;
import projectTree.ProjectTreeModel;
import utils.SwingUtils;

public class ProjectTreeTest {
	
	public static void main(String[] args) throws SQLException {
		JFrame frame = new JFrame("");
		frame.setBounds(50, 50, 300, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		IProjectTreeModel model = new ProjectTreeModel();
		IProjectTreeController controller = 
				new ProjectTreeController(model);
		frame.getContentPane().add(
				SwingUtils.createPanelForComponent(controller.getView(), "项目列表"), BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
