
public class Interval {
	
	private boolean isRunning;
	private long startTime;
	private long endTime;
	
	public Interval() {
		isRunning = false;
	}
	
	public void start() {
		startTime = System.nanoTime(); 
		isRunning = true; 
	}
	
	public void stop() {
		if (isRunning) {
			endTime = System.nanoTime();
		}
	}
	
	public long getMills() {
		if (isRunning) return System.nanoTime() - startTime;
		else return (endTime - startTime) * 1_000_000L;
	}
	
	public void reset() {
		startTime = 0;
		endTime = 0;
	}
	
}
