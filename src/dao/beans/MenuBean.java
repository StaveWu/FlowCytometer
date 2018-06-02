package dao.beans;

public class MenuBean {
	
	private String lid;
	private String name;
	private String type;
	private int level;
	private String father;
	private String command;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public String toString() {
		return "lid = " + lid + ", "
				+ "name = " + name + ", "
				+ "type = " + type + ", "
				+ "level = " + level + ", "
				+ "father = " + father + ", "
				+ "command = " + command;	
	}

}
