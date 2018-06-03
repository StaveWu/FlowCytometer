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
		// ��ȡnode�ľ���·��"C:/A/B"
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
			// ������root�ڵ�����
			relaPath += FileSeparator;
			relaPath += retNodes[i].toString();
			
		}
		// �����չ��
		DirTreeBean info = (DirTreeBean) node.getUserObject();
		if (info.getNodeType() == NodeType.SETTINGS || info.getNodeType() == NodeType.WORKSHEET) {
			// ������ӣ���Ϊ���ݿ�򿪻��Զ���Ӻ�׺
		}
		else if (info.getNodeType() == NodeType.TUBE) {
			relaPath += ".out";
		}
		else {
			// duplicate
		}
		return relaPath;
	}

}
