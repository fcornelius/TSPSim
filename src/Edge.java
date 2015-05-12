import java.awt.Graphics2D;


public class Edge {

	private Knot k1;
	private Knot k2;
	private double cost;
	
	public Edge(Knot start, Knot end) {
		k1 = start;
		k2 = end;
		cost = k1.getDistance(k2);
	}
	
	public double getCost() {
		return cost;
	}
	public Knot getStart() {
		return k1;
	}
	public Knot getEnd() {
		return k2;
	}
	
	public void drawLine(Graphics2D g) {
		k1.drawLine(g, k2);
	}
	
	public String toString() {
		return (k1 + "—" + k2);
	}
}
