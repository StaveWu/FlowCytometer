package menu;

import javax.swing.tree.DefaultMutableTreeNode;

public interface IProjectTreeCommand {
	
	void createExperimentSolution(DefaultMutableTreeNode parent);
	
	void createSpecimen(DefaultMutableTreeNode parent);
	
	void createTube(DefaultMutableTreeNode parent);
	
	void delete(DefaultMutableTreeNode node);
	
	void rename(DefaultMutableTreeNode node);

}
