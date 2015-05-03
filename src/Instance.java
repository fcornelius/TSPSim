import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;


public class Instance {
	
	private ArrayList<Knot> knots;
	private ArrayList<Knot> neighbours;
	private int startKnot;
	private double maxDist;
	private Knot[] furthestKnots;
	private double wayLenghth;
	
	public Instance() {
		knots = new ArrayList<Knot>();
		neighbours = new ArrayList<Knot>();
		maxDist = 0;
	}
	
	public void resetInstance() {
		neighbours.clear();
		for (Knot k : knots) neighbours.add(k);
		maxDist = 0;
		wayLenghth = 0;
	}
	
	public void redrawInstance(Graphics2D g, int radius, boolean withNumber) {
		for (Knot k : knots) k.drawPoint(g, radius, withNumber);
	}
	
	public void addKnot(Knot k) {
		knots.add(k);
		neighbours.add(k);
	}
	public Knot getKnotByIndex(int index) {
		return knots.get(index);
	}
	public boolean isReady() {
		return (knots.size()==neighbours.size());
	}
	public boolean isFinished() {
		return (neighbours.isEmpty());
	}
	public void setStart(int index) {
		startKnot = index;
	}
	public String getResult() {
		return "Länge des Weges: " + String.format("%.2f", wayLenghth) + "<br>Längste Teilstrecke zwischen:<br>" + furthestKnots[0] + "<br>und " + furthestKnots[1];
	}
	
	public Knot nearestNeighbour(Knot rootKnot, boolean closed, boolean debug, Graphics2D g, JTextPane debugPane) {
		int knotId = 0;
		double thisDist = 0;
		double minDist = 0;
		Knot nearest = null;
		neighbours.remove(rootKnot);

		if (neighbours.size() == 0) {
			neighbours.clear();
			if (closed) {
				nearest = knots.get(startKnot);
				minDist = Point.distance(rootKnot.x, rootKnot.y, nearest.x, nearest.y);
			}
			else return null;
		} else {
			for (Knot thisKnot : neighbours) {
				thisDist = Point.distance(thisKnot.x, thisKnot.y, rootKnot.x, rootKnot.y);
				if (knotId==0) {
					nearest = thisKnot;
					minDist = thisDist;
				}
				else {
					if (thisDist < minDist) {
						nearest = thisKnot;
						minDist = thisDist;
					}
				}
				if (debug) debugPane.setText(debugPane.getText() +  "knotId: " + knotId + " rootKnot: " + rootKnot + " thisKnot: " + thisKnot + "\nthisDist: " + thisDist + " minDist: " + minDist + " nearest: " + nearest + "\n" + 
						"-------------------------------------------------------------------------------------\n");
				knotId++;
			}
		}
		wayLenghth += minDist;
		if (minDist > maxDist) { maxDist = minDist; furthestKnots = new Knot[]{rootKnot, nearest}; }
		
		rootKnot.drawLine(g, nearest);
		return nearest;
	}

}
