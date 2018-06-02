package menu;

import java.lang.reflect.Method;

import javax.swing.tree.DefaultMutableTreeNode;

import dao.beans.DirTreeBean;
import projectTree.IProjectTreeController;

public class ProjectTreeMenu extends PopupMenuX {

	public static final String EXPERIMENT_SOLUTION = "实验方案";
	public static final String TUBE = "试管";
	public static final String SPECIMEN = "样品";
	public static final String DELETE = "删除";
	public static final String RENAME = "重命名";
	
	private IProjectTreeController controller;

	public ProjectTreeMenu(IProjectTreeController controller) {
		this.controller = controller;
	}

	@Override
	public String getTableName() {
		return "DirTreeMenu";
	}

	@Override
	public void invoke(String command) {
		//反射调用控制器方法
		try {
			Method fun = IProjectTreeController.class.getDeclaredMethod(command, DefaultMutableTreeNode.class);
			fun.invoke(controller, (DefaultMutableTreeNode) source);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customizeMenuItem(Object source) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) source;
		DirTreeBean message = (DirTreeBean) node.getUserObject();
		switch (message.getNodeType()) {
		case EXPERIMENT_SOLUTION:
			enableMenuItem(SPECIMEN);
			disableMenuItem(EXPERIMENT_SOLUTION);
			disableMenuItem(TUBE);
			enableMenuItem(DELETE);
			enableMenuItem(RENAME);
			break;
		case SPECIMEN:
			enableMenuItem(TUBE);
			disableMenuItem(EXPERIMENT_SOLUTION);
			disableMenuItem(SPECIMEN);
			enableMenuItem(DELETE);
			enableMenuItem(RENAME);
			break;
		case WORKSPACE:
			enableMenuItem(EXPERIMENT_SOLUTION);
			disableMenuItem(TUBE);
			disableMenuItem(SPECIMEN);
			disableMenuItem(DELETE);
			disableMenuItem(RENAME);
			break;
		case TUBE:
			disableMenuItem(EXPERIMENT_SOLUTION);
			disableMenuItem(SPECIMEN);
			disableMenuItem(TUBE);
			enableMenuItem(DELETE);
			enableMenuItem(RENAME);
			break;
		default:
			disableMenuItem(EXPERIMENT_SOLUTION);
			disableMenuItem(SPECIMEN);
			disableMenuItem(TUBE);
			disableMenuItem(DELETE);
			disableMenuItem(RENAME);
			break;
		}
	}


}
