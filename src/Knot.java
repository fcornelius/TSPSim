import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;


public class Knot {
	
	public int x;
	public int y;
	private int id;

	
	public Knot(int id) {
		
		x = newCoordinate();
		y = newCoordinate();
		this.id = id;
	}
	
	private int newCoordinate() {
		return gWindow.borderPx + (int)(Math.random()*(gWindow.dimensionPx - 2 * gWindow.borderPx));
	}
	
	public void drawPoint(Graphics2D g, int radius, boolean withNumber) {
		
		g.fillOval(x-radius,y-radius,2*radius,2*radius);
		if (withNumber) {
			if (y > 580) g.drawString(String.valueOf(id+1), x-4, y-10);
			else g.drawString(String.valueOf(id+1), x-4, y+18);
		}
	}
	
	public void drawLine(Graphics2D g, Knot destKnot) {
		g.drawLine(x, y, destKnot.x, destKnot.y);
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
