package dao.beans;

import projectTree.FileType;
import projectTree.NodeType;

public class DirTreeBean implements Comparable<DirTreeBean> {
	
	private String lid;
	
	private String name;
	
	private FileType fileType;
	
	private int level;
	
	private String father;
	
	private NodeType nodeType;
	
	public DirTreeBean(String lid, String name, NodeType nodeType, FileType fileType) {
		this.lid = lid;
		this.name = name;
		this.nodeType = nodeType;
		this.fileType = fileType;
	}
	
	public DirTreeBean() {}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(String type) {
		this.fileType = type.equals("File") ? FileType.FILE : FileType.FOLDER;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		if (nodeType.equals("ExperimentSolution")) {
			this.nodeType = NodeType.EXPERIMENT_SOLUTION;
		}
		else if (nodeType.equals("Settings")) {
			this.nodeType = NodeType.SETTINGS;
		}
		else if (nodeType.equals("WorkSheet")) {
			this.nodeType = NodeType.WORKSHEET;
		}
		else if (nodeType.equals("Specimen")) {
			this.nodeType = NodeType.SPECIMEN;
		}
		else if (nodeType.equals("Tube")) {
			this.nodeType = NodeType.TUBE;
		}
		else {
			this.nodeType = NodeType.UNKNOWN;
		}
	}

	@Override
	public int compareTo(DirTreeBean bean) {
		String[] s1 = this.lid.split("\\.");
		String[] s2 = bean.getLid().split("\\.");
		int id1 = Integer.parseInt(s1[s1.length - 1]);
		int id2 = Integer.parseInt(s2[s2.length - 1]);
		return s2.length - s1.length != 0 ? s2.length - s1.length : (id1 - id2);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String Message() {
		return "[" + lid + ", "
				+ name + ", " 
				+ fileType + ", "
				 + nodeType + ", "
				+ level + ", "
				+ father + "]";
	}

}
