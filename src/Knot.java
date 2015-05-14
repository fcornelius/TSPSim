import java.awt.Point;


public class Knot {
	
	private int x;
	private int y;
	private int id;

	
	public Knot(int id) {
		
		x = newCoordinate();
		y = newCoordinate();
		this.id = id;
	}
	
	private int newCoordinate() {
		return (int)(Math.random()*(SquareCanvas.dimension));
	}
	
	public int X() {
		return x + SquareCanvas.border + SquareCanvas.spacing;   // Angepasste Koordinaten auf Canvas Rahmen und Freiraum
	}
	
	public int Y() {
		return y + SquareCanvas.border + SquareCanvas.spacing;
	}
	public int getX() {											//  Echte Koordinaten
		return x;
	}
	public int getY() {
		return y;
	}
	public double getDistance(Knot k) {
		return Point.distance(x, y, k.x, k.y);
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return "Knoten " + (id + 1) + ": (" + x + ";" + y + ")";
	}
	
	

}
