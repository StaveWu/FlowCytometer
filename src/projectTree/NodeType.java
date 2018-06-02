package projectTree;

public enum NodeType {
	
	EXPERIMENT_SOLUTION("ExperimentSolution"),
	SPECIMEN("Specimen"),
	WORKSPACE("WorkSpace"),
	SETTINGS("Settings"),
	WORKSHEET("WorkSheet"),
	TUBE("Tube"),
	UNKNOWN("Unknown");
	
	private String filename;
	
	NodeType(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		return filename;
	}
}
