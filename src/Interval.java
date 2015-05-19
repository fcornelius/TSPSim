
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
	
	public double getMills() {
		if (isRunning) return (System.nanoTime() - startTime) / 1000000.0;
		else return (endTime - startTime) / 1000000.0;
	}
	
	public void reset() {
		startTime = 0;
		endTime = 0;
	}
	
}
