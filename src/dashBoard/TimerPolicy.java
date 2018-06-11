package dashBoard;

import java.util.concurrent.TimeUnit;

public class TimerPolicy implements ITickPolicy {
	
	private int seconds;
	private boolean stop = false;
	
	public TimerPolicy(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public void tick(IHandlerTask task) {
		this.stop = false;
		new Thread(new TimeHandler(task)).start();
	}

	@Override
	public void stop() {
		this.stop = true;
	}
	
	
	private class TimeHandler implements Runnable {
		
		/**
		 * º¯Êý¶ÔÏó
		 */
		private IHandlerTask task;
		
		public TimeHandler(IHandlerTask task) {
			this.task = task;
		}

		@Override
		public void run() {
			while (seconds > 0 && !stop) {
				try {
					TimeUnit.SECONDS.sleep(1);
					seconds --;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			task.execute();
		}

	}
	
}
