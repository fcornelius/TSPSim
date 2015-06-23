

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
	
	public void swap() {
		Knot tmp = k1;
		k1 = k2;
		k2 = tmp;
	}
	
	public void setStart(Knot s) {
		k1 = s;
	}
	
	public void setEnd(Knot e) {
		k2 = e;
	}
	
	public String toString() {
		return (k1 + "—" + k2);
	}
}
