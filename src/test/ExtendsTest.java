package test;

public class ExtendsTest {
	
	public static void main(String[] args) {
		A a = new B();
		a.sss();
	}

}

abstract class A {
	Quack quack;
	
	public void sss() {
		quack.quack();
	}
}

class B extends A {
	public B() {
//		quack = new AQuack();
		quack = new BQuack();
	}
}

interface Quack {
	void quack();
}

class AQuack implements Quack {
	@Override
	public void quack() {
		// TODO Auto-generated method stub
		System.out.println("sss");
	}
}
class BQuack implements Quack {
	@Override
	public void quack() {
		// TODO Auto-generated method stub
		System.out.println("bbb");
	}
}


