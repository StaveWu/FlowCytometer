package dashBoard;

public interface ITickPolicy {
	
	void tick(IHandlerTask task);
	
	void stop();

}
