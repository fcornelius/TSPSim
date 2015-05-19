

public class Edge {

	private Knot k1;
	private Knot k2;
	
	public Edge(Knot start, Knot end) {
		k1 = start;
		k2 = end;
	}
	
	public double getCost() {
		return k1.getDistance(k2);
	}
	public Knot getStart() {
		return k1;
	}
	public Knot getEnd() {
		return k2;
	}
	
	public String toString() {
		return (k1 + "—" + k2);
	}
}
