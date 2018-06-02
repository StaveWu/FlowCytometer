package projectTree;

public enum FileType {
	
	FILE("File"),
	FOLDER("Directory");
	
	private String s;
	
	FileType(String s) {
		this.s = s;
	}
	
	@Override
	public String toString() {
		return s;
	}

}
