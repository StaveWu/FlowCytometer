package projectTree;

import java.util.NoSuchElementException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import dao.beans.DirTreeBean;

public class ProjectTreeUtils {
	
	public static String getNextLid(DefaultMutableTreeNode parent) {
		try {
			DefaultMutableTreeNode lastChild
				= (DefaultMutableTreeNode) parent.getLastChild();
			return increaseLid(((DirTreeBean) lastChild.getUserObject()).getLid());
		}
		catch (NoSuchElementException e) {
			String parentLid =((DirTreeBean) parent.getUserObject()).getLid();
			if (parentLid == null) {
				return "0";
			}
			else {
				return parentLid + ".0";
			}
			
		}
	}
	
	private static String increaseLid(String lid) {
		String[] s = lid.split("\\.");
		String res = "";
		for (int i = 0; i < s.length; i++) {
			if (i == s.length - 1) {
				res += (Integer.valueOf(s[i]) + 1);
			}
			else {
				res += s[i] + ".";
			}
		}
		return res;
	}
	
	public static String getRelaPath(DefaultMutableTreeNode node) {
		// 获取node的绝对路径"C:/A/B"
		if (node == null) {
			return null;
		}
		TreeNode[] retNodes = node.getPath();
		if (retNodes == null || retNodes.length == 0) {
			return null;
		}
		String relaPath = "";
		final String FileSeparator = "/";
		for (int i = 1; i < retNodes.length; i++) {
			// 不包含root节点名称
			relaPath += FileSeparator;
			relaPath += retNodes[i].toString();
			
		}
		// 添加扩展名
		DirTreeBean info = (DirTreeBean) node.getUserObject();
		if (info.getNodeType() == NodeType.SETTINGS || info.getNodeType() == NodeType.WORKSHEET) {
			relaPath += ".mv.db";
		}
		else if (info.getNodeType() == NodeType.TUBE) {
			relaPath += ".out";
		}
		else {
			// duplicate
		}
		return relaPath;
	}
	
	public static String getOwnedProjectLid(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode projectNode = searchExperimentNode(node);
		if (projectNode == null) {
			return null;
		}
		return ((DirTreeBean)projectNode.getUserObject()).getLid();
	}
	
	public static String getOwnedProjectName(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode projectNode = searchExperimentNode(node);
		if (projectNode == null) {
			return null;
		}
		return ((DirTreeBean)projectNode.getUserObject()).getName();
	}
	
	private static DefaultMutableTreeNode searchExperimentNode(DefaultMutableTreeNode node) {
		// 递归查询Experiment节点
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		if (parent == null) {
			return null;
		}
		DirTreeBean nodeInfo = (DirTreeBean) parent.getUserObject();
		if (nodeInfo.getNodeType() == NodeType.EXPERIMENT_SOLUTION) {
			return parent;
		} else {
			return searchExperimentNode(parent);
		}
	}

}
