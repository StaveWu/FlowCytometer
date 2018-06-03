package test;

public class ReferenceTest {
	
	public static void main(String[] args) {
		String aString = null;
		String b = aString;
		aString = "aa";
		System.out.println(b);
	}

}

