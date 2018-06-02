package test;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeTest {
	
	public static void main(String[] args) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("aaa");
		DefaultMutableTreeNode child = new DefaultMutableTreeNode("bbb");
		root.add(child);
		DefaultTreeModel model = new DefaultTreeModel(root);
		DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("ccc");
		root.add(child2);
		System.out.println(child.getParent());
		System.out.println(child2.getParent());
	}

}
