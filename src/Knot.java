import java.awt.Graphics2D;


public class Knot {
	
	public int x;
	public int y;
	private int id;
	
	public Knot(int id) {
		
		x = (int)(Math.random()*gWindow.dimensionPx);
		y = (int)(Math.random()*gWindow.dimensionPx);
		this.id = id;
	}
	
	public void drawPoint(Graphics2D g, int radius, boolean withNumber) {
		
		g.fillOval(x-radius,y-radius,2*radius,2*radius);
		if (withNumber) g.drawString(String.valueOf(id+1), x-4, y+18);
	}
	
	public void drawLine(Graphics2D g, Knot destKnot) {
		g.drawLine(x, y, destKnot.x, destKnot.y);
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return "Knoten " + (id + 1) + ": (" + x + ";" + y + ")";
	}

}
