import java.awt.Graphics2D;
import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTextPane;


public class Instance {
	
	private static final int floatingPointPrecision = 10;
	
	private ArrayList<Knot> knots;
	private ArrayList<Knot> neighbours;
	private ArrayList<Knot> spanningTreeKnots;
	private ArrayList<Edge> completeGraph;
	private ArrayList<Edge> edges;
	private ArrayList<Integer> minRoute;
	private ArrayList<ArrayList<Integer>> routes;
	
	private int startKnot;
	private double maxDist;
	private Knot[] furthestKnots;
	private double wayLength;
	private double calcTime;
	private double permutTime;
	
	public Interval stopWatch;
	
	/**
	 * Konstruktor
	 * Legt Knotenliste und Neighbourliste an
	 */
	public Instance() {
		knots = new ArrayList<Knot>();
		neighbours = new ArrayList<Knot>();
		furthestKnots = new Knot[2];
		spanningTreeKnots = new ArrayList<Knot>();
		completeGraph = new ArrayList<Edge>();
		edges = new ArrayList<Edge>();
		stopWatch = new Interval();
		maxDist = 0;
		wayLength = 0;
		permutTime = 0;
		calcTime = 0;
	}
	/**
	 * L�scht alle verbeibenden Neighbour und f�llt Neighbourliste neu mit Knoten aus knots
	 * f�r Wiederholung der Instanz.<br>
	 * Wegl�nge ({@link wayLength}) und gr��te Teilstrecke ({@link maxDist}) werden zur�ckgesetzt.
	 */
	public void resetInstance() {
		neighbours.clear();
		for (Knot k : knots) neighbours.add(k);
		maxDist = 0;
		wayLength = 0;
	}
	/**
	 * Zeichnet jeden Knoten in Knotenliste der Instanz ({@link knots}) neu.
	 * @param g Graphics-Objekt zum Zeichnen
	 * @param radius Radius der zu zeichnenden Punkte
	 * @param withNumber true/false ob Nummern zu Punkten gezeichnet werden
	 */
	public void redrawInstance(Graphics2D g, int radius, boolean withNumber) {
//		for (Knot k : knots) k.drawPoint(g, radius, withNumber);
	}
	/**
	 * F�gt einer Instanz einen bestehenden Knoten hinzu.<br>
	 * Knoten werden in Knotenliste ({@link knots}) und Neighbourliste ({@link neighbours}) abgelegt.
	 * @param k Knoten zum hinzuf�gen
	 */
	public void addKnot(Knot k) {
		knots.add(k);
		neighbours.add(k);
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
	/**
	 * Gibt einen Knoten aus der Knotenliste an Position {@link index} wieder
	 * @param index Position des Knotens ins Knotenliste (0-basiert)
	 * @return Knoten
	 */
	public Knot getKnot(int index) {
		return knots.get(index);
	}
	
	public int getCount() {
		return knots.size();
	}
	public double getWaylength() {
		return wayLength;
	}
	/**
	 * Gibt an ob die Instanz erstellt und noch nicht gel�st wurde.<br>
	 * Sobald �ber 'N�chster' ein Schritt des L�sens erfolgt ist, gibt die Instanz <b>false</b> zur�ck.
	 * @return <b>true</b> wenn Neighbourliste noch voll,<br> <b>false</b> wenn bereits Eintr�ge entfernt wurden
	 */
	public boolean isReady() {
		return (knots.size()==neighbours.size());
	}
	/**
	 * Gibt an ob die Instanz vollst�ndig gel�st wurde, also ob alle Neighbours entfernt wurden.
	 * @return <b>true</b> wenn Neighbourliste leer aber Knoten angelegt wurden,<br> <b>false</b> wenn noch Neighbours vorhanden sind
	 */
	public boolean isFinished() {
		return (neighbours.isEmpty() && !knots.isEmpty());
	}
	/**
	 * Setzt die Klassenvariable {@link startKnot} auf den angegebenen index,<br>
	 * erfolgt <b>nur</b> im Zusammenhang mit {@link isReady}
	 * @param index Der 0-basierte Index des Startknotens
	 */
	public void setStart(int index) {
		startKnot = index;
	}
	public Knot getStartKnot() {
		return knots.get(startKnot);
	}
	
	public ArrayList<Knot> transformCoordinates(ArrayList<ArrayList<Double>> coords) {
		
		int offset = 10; // Oberer linker Rand zum Ursprung	
		
		int width = SquareCanvas.pixelWidth;
		int height = SquareCanvas.pixelHeight;
		int x,y;
		int xCentre,yCentre;
		double maxX,maxY;
		double xFactor,yFactor;
		double xShift,yShift;
		
		ArrayList<Double> xList = coords.get(0);
		ArrayList<Double> yList = coords.get(1);
		ArrayList<Double> xsort = new ArrayList<Double>();
		ArrayList<Double> ysort = new ArrayList<Double>();
		xsort.addAll(xList); ysort.addAll(yList);
		
		Collections.sort(xsort);
		Collections.sort(ysort);
		
		maxX = xsort.get((xList.size()-1));
		maxY = ysort.get((yList.size()-1));
		xShift = xsort.get(0);
		yShift = ysort.get(0);
	
		xFactor = (width - 2*offset) / (maxX - xShift);
		yFactor = (height -2*offset) / (maxY - yShift);
		double factor = Math.min(xFactor, yFactor);
		
		xCentre = (int)((width - 2*offset - (maxX - xShift)*factor) / 2);
		yCentre = (int)((height - 2*offset - (maxY - yShift)*factor) / 2);
		
		for (int i=0;i<xList.size();i++) {
			x = (int) ((xList.get(i) - xShift) * factor) + offset + xCentre;
			y = (int) ((yList.get(i) - yShift) * factor) + offset + yCentre;
			
			addKnot(new Knot(i,x,y));
		}
		
		return knots;
	}
	
	
	public void bruteForceSolve(SquareCanvas canvas) {
		
		stopWatch.start();
		PermutationBuilder pb = new PermutationBuilder();
		routes = pb.BuildList(getCount()-1,true);
		minRoute = routes.get(0);							// Route: Der Pfad des Weges (die Permutation)
		double minTour = getTour(routes.get(0));      	    // Tour:  Die L�nge einer Route
		double thisTour;
		permutTime = stopWatch.getMills();
		
		for (ArrayList<Integer> thisRoute : routes) {
			
			thisTour = getTour(thisRoute);
			if (thisTour < minTour) { minTour = thisTour; minRoute = thisRoute; }
		}
		stopWatch.stop();
		calcTime = stopWatch.getMills();
		stopWatch.reset();
		
		wayLength = minTour;
		canvas.drawRoute(this, minRoute);
	}
	/**
	 * Gibt den Knoten zur�ck der am n�hesten zum �bergebenen {@link rootKnot} ist und zeichnet deren Verbindung.
	 * <li> Der �bergebene Knoten {@link rootKnot} wird aus der Liste m�glicher NN entfernt.
	 * <li> F�r jeden der verbliebenden Knoten wird die Entfernung zum {@link rootKnot} in {@link thisDist} zwischen gespeichert.
	 * <li> Der erste Knoten im Durchlauf wird als n�hester ({@link nearest}) festgelegt und seine Entfernung zum {@link rootKnot} als k�rzeste ({@link minDist})
	 * <li> Wenn ein nachfolgender Knoten eine k�rzere Entfernung zum {@link rootKnot} hat, gilt dieser als n�hester.
	 * <li> Bei geschlossendem Kreis wird f�r den letzten Knoten im Durchlauf der Startknoten als n�hester zur�ck gegeben, bei offenem Kreis wird null zur�ckgegeben.
	 * Die k�rzesten Entfernungen werden in {@link wayLength} aufakkumuliert.<br>
	 * Beim ersten Neighbour gilt maxDist=minDist, also die l�ngste Teilstrecke ist die Entfernung vom Ausgangsknoten zum ersten Neighbour.
	 * Danach werden rootKnot und nearest in {@link furthestKnots} abgelegt wenn ihre Entfernung gr��er ist als die bisher gr��te Teilstrecke ({@link maxDist})
	 * @param rootKnot Der Ausgangsknoten
	 * @param closed true/false ob geschlossener Kreis
	 * @param debug true/false ob Zwischenergebnisse geloggt werden
	 * @param g das zum zeichnen verwendete Graphics-Objekt
	 * @param debugPane Ausgabe f�r den Debug-Log
	 * @return n�hester Knoten
	 */
	public Knot nearestNeighbour(Knot rootKnot, boolean closed, boolean debug, boolean draw, SquareCanvas canvas, JTextPane debugPane) {
		int knotId = 0;
		double thisDist = 0;
		double minDist = 0;
		Knot nearest = null;
		neighbours.remove(rootKnot);

		if (neighbours.isEmpty()) {
			if (closed) {
				nearest = knots.get(startKnot);
				minDist = Point.distance(rootKnot.X(), rootKnot.Y(), nearest.X(), nearest.Y());
			} else return null;
		} else {
			for (Knot thisKnot : neighbours) {
				thisDist = Point.distance(thisKnot.X(), thisKnot.Y(), rootKnot.X(), rootKnot.Y());
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
				if (debug) debugPane.setText(debugPane.getText() +  String.format("knotId: %d rootKnot: %s thisKnot: %s thisDist: %f minDist: %f nearest: %s \n", 
							knotId, rootKnot, thisKnot, thisDist, minDist, nearest));
				knotId++;
			}
		}
		wayLength += minDist;
		if (minDist > maxDist) { maxDist = minDist; furthestKnots = new Knot[]{rootKnot, nearest}; }
		
		if (draw) canvas.drawEdge(rootKnot, nearest);
		return nearest;
	}
	
	public void makeMST(SquareCanvas canvas) {   //TODO MST Step-Verfahren wie bei NN implementieren (in selber Methode)

		int n = knots.size();
		stopWatch.start();
		
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < n-i; j++) {
				completeGraph.add(new Edge(getKnot(i), getKnot(i+j)));
			}
		}
		Collections.sort(completeGraph, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return (int)((e1.getCost() - e2.getCost()) * Math.pow(10, floatingPointPrecision)); //Ab ... NKS kein eindeutiger Vergleich mehr m�glich
			}
		});
		
		canvas.drawEdge(completeGraph.get(0));
		addEdgeToTree(completeGraph.get(0));
		
		while (spanningTreeKnots.size() < n) {

			for (Edge thisEdge : completeGraph) {
				if (spanningTreeKnots.contains(thisEdge.getStart()) ^ spanningTreeKnots.contains(thisEdge.getEnd())) {
					canvas.drawEdge(thisEdge);
					addEdgeToTree(thisEdge);
					break;
				} 
			}
		}
		stopWatch.stop();
		calcTime = stopWatch.getMills();
		stopWatch.reset();
	}
	
	public void addEdgeToTree(Edge e) {
		if (!spanningTreeKnots.contains(e.getStart())) spanningTreeKnots.add(e.getStart());
		if (!spanningTreeKnots.contains(e.getEnd())) spanningTreeKnots.add(e.getEnd());
		completeGraph.remove(e);
	}
	
	public double getTour(ArrayList<Integer> route) {
		
		double tourLength = getKnot(0).getDistance(getKnot(route.get(0)));
		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) tourLength += getKnot(route.get(i)).getDistance(getKnot(route.get(i+1)));
			else tourLength += getKnot(route.get(i)).getDistance(getKnot(0));
		} return tourLength;
	}
	
	public String getResult(int mode) {
		
		String result = String.format("[%s] Testinstanz mit %d Knoten wurde berechnet mittels ", LocalDateTime.now(), getCount());
		switch (mode) {
		case 0:
			result += String.format("Brute-Force Verfahren.\n" +
					"K�rzeste Tour ist %.5f PE lang (Route %d von m�glichen %d)\nRechenzeit: Permutationen %.2f ms, Routenl�nge %.2f ms, Gesamt %.2f ms\n" + 
					"Das entspricht einer durchschnittlichen Rechenzeit von %.2f �s pro Route", 
					wayLength, routes.indexOf(minRoute), routes.size(), permutTime, calcTime-permutTime, calcTime, ((calcTime)/(double)routes.size()) * 1000); 
			break;
		case 1: 
			result += String.format("Nearest-Neighbour Verfahren.\n" +
					"L�nge des Weges: %.5f Gew�hlter Startknoten: %s L�ngste Teilstrecke zwischen: %s und %s, %.5f\nRechenzeit: %.2f ms", 
					wayLength, knots.get(startKnot), furthestKnots[0], furthestKnots[1], maxDist,calcTime);
			break;
		case 2: 
			result += String.format("Best-Nearest-Neighbour Verfahren.\n" +
					"L�nge des Weges: %.5f Bester Startknoten: %s L�ngste Teilstrecke zwischen: %s und %s, %.5f\nRechenzeit: %.2f ms", 
					wayLength, knots.get(startKnot), furthestKnots[0], furthestKnots[1], maxDist,calcTime);
			break;
		case 3:
			result += String.format("Minimum-Spanning-Tree Verfahren.\n" +
					"Rechenzeit: %.2f ms", calcTime);
			break;
		}
		return result;
	}
}
