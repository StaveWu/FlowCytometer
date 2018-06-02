package test;

public class EnumTest {
	
	public enum Type {
		A("file"),
		B("directory");
		
		private String s;
		
		Type(String s) {
			this.s = s;
		}
		
		@Override
		public String toString() {
			return s;
		}
		
	}
	
	public static void main(String[] args) {
		Type t = Type.A;
		System.out.println(t);
		
	}
	
	

}
