package dao;

public class GroupCondition {
	
	/**
	 * 参数名
	 */
	private String name;
	
	/**
	 * 关系
	 */
	private String rela;
	
	/**
	 * 值
	 */
	private String value;
	
	public GroupCondition() {}
	
	public GroupCondition(String name, String rela, String value) {
		this.name = name;
		this.rela = rela;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRela() {
		return rela;
	}

	public void setRela(String rela) {
		this.rela = rela;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
