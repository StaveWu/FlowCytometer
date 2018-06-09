package test;

public class ReferenceTest {
	
	public static void main(String[] args) {
		At t = new At();
		Bt bt = new Bt(t);
		bt.say();
		t = new At();
		t.s = "b";
		bt.say();
		
	}

}

class At {
	String s = "a";
	void say() {
		System.out.println(s);
	}
}

class Bt {
	private At t;
	Bt(At t) {
		this.t = t;
	}
	
	void say() {
		System.out.println(t.s);
	}
	
	void change() {
//		t = new At();
		t.s = "b";
	}
}

