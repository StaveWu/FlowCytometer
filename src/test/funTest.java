package test;

public class funTest {

	public static void main(String[] args) {
		String str = "0.0.0";
		System.out.println(str);
		System.out.println(increaseLid(str));
		
		System.out.println(str.substring(0, str.length() - 2));
	}
	
	private static String increaseLid(String lid) {
		String[] s = lid.split("\\.");
		String res = "";
		for (int i = 0; i < s.length; i++) {
			if (i == s.length - 1) {
				res += (Integer.valueOf(s[i]) + 1);
			}
			else {
				res += s[i] + ".";
			}
		}
		return res;
	}
}
