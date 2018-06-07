package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		
		executorService.execute(new All());
		executorService.execute(new Bll());
		
	}

}

class All implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("A");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

class Bll implements Runnable {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("B");
	}
	
}
