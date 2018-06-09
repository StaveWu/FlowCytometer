package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {
	
	public static void main(String[] args) {
		String s = "CH12";
		Matcher m = Pattern.compile("\\d+").matcher(s);
		while (m.find()) {
			System.out.println(s.substring(m.start(), m.end()));
		}
	}

}
