package projectTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * ��ģ�ͣ��ṩһЩ�����Բ������漰���ļ�ϵͳ�����ݿ⡢JTree�����TreeModel
 * @author wteng
 *
 */
public interface IProjectTreeModel {
	
	void insert(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) throws Exception;
	
	void delete(DefaultMutableTreeNode node) throws Exception;
	
	void rename(DefaultMutableTreeNode node, String newName) throws Exception;
	
	TreeModel getDelegate();

}
