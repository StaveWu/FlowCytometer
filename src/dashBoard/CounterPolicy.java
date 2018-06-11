package dashBoard;

public class CounterPolicy implements ITickPolicy {
	
	private IDataCounter counter;
	private int limit;
	private boolean constaint;
	
	private boolean stop = false;

	public CounterPolicy(IDataCounter counter, int limit, boolean constaint) {
		this.counter = counter;
		this.limit = limit;
		this.constaint = constaint;
	}

	@Override
	public void tick(IHandlerTask task) {
		this.stop = false;
		new Thread(new CountHandler(task)).start();
	}
	
	public void stop() {
		this.stop = true;
	}
	
	
	private class CountHandler implements Runnable {
		
		private IHandlerTask task;
		
		public CountHandler(IHandlerTask task) {
			this.task = task;
		}

		@Override
		public void run() {
			int i;
			while ((i = counter.getDataCount(constaint)) < limit && !stop) {
				// duplicate
			}
			System.out.println(i);
			task.execute();
		}

	}


}
