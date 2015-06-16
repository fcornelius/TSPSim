import java.awt.Graphics2D;
import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTextPane;


public class Instance {
	
	private static final int floatingPointPrecision = 10;
	
	public static final int MODE_BRUTEFORCE = 0;
	public static final int MODE_DYNPROG = 1;
	public static final int MODE_NN = 2;
	public static final int MODE_BESTNN = 3;
	public static final int MODE_MSTBUILD = 4;
	public static final int MODE_MSTTRANSFORM = 5;
	public static final int MODE_BESTMST = 6;
	
	
	private ArrayList<Knot> knots;
	private ArrayList<Knot> neighbours;
	private ArrayList<Knot> spanningTreeKnots;
	private ArrayList<Edge> spanningTreeEdges;
	public ArrayList<Knot> mstRoute;
	private ArrayList<Edge> completeGraph;
	private ArrayList<Edge> edges;
	private ArrayList<Integer> minRoute;
	private ArrayList<ArrayList<Integer>> routes;
	
	private int startKnot;
	private double maxDist;
	private Knot[] furthestKnots;
	public Knot MSTbestStart;
	public double wayLength;
	private double calcTime;
	private double permutTime;
	private double[][] cache;
	private ArrayList<ArrayList<Integer>> powerSets;
	
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
		spanningTreeEdges = new ArrayList<Edge>();
		mstRoute = new ArrayList<Knot>();
		completeGraph = new ArrayList<Edge>();
		edges = new ArrayList<Edge>();
		stopWatch = new Interval();
		maxDist = 0;
		wayLength = 0;
		permutTime = 0;
		calcTime = 0;
	}
	/**
	 * Löscht alle verbeibenden Neighbour und füllt Neighbourliste neu mit Knoten aus knots
	 * für Wiederholung der Instanz.<br>
	 * Weglänge ({@link wayLength}) und größte Teilstrecke ({@link maxDist}) werden zurückgesetzt.
	 */
	public void resetInstance() {
		
		spanningTreeEdges.clear();
		spanningTreeKnots.clear();
		mstRoute.clear();
		edges.clear();
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
	 * Fügt einer Instanz einen bestehenden Knoten hinzu.<br>
	 * Knoten werden in Knotenliste ({@link knots}) und Neighbourliste ({@link neighbours}) abgelegt.
	 * @param k Knoten zum hinzufügen
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
	 * Gibt an ob die Instanz erstellt und noch nicht gelöst wurde.<br>
	 * Sobald über 'Nächster' ein Schritt des Lösens erfolgt ist, gibt die Instanz <b>false</b> zurück.
	 * @return <b>true</b> wenn Neighbourliste noch voll,<br> <b>false</b> wenn bereits Einträge entfernt wurden
	 */
	public boolean isReady() {
		return (knots.size()==neighbours.size());
	}
	
	public boolean hasMST() {
		return (spanningTreeKnots.size() > 0);
	}
	/**
	 * Gibt an ob die Instanz vollständig gelöst wurde, also ob alle Neighbours entfernt wurden.
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
	
	public void mirrorKnots(int mode) {
		
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
		double minTour = getIndexedTour(routes.get(0));      	    // Tour:  Die Länge einer Route
		double thisTour;
		permutTime = stopWatch.getMills();
		
		for (ArrayList<Integer> thisRoute : routes) {
			
			thisTour = getIndexedTour(thisRoute);
			if (thisTour < minTour) { minTour = thisTour; minRoute = thisRoute; }
		}
		stopWatch.stop();
		calcTime = stopWatch.getMills();
		stopWatch.reset();
		
		wayLength = minTour;
		canvas.drawIndexedRoute(this, minRoute);
	}
	
	public void dynProgrammingSolve(SquareCanvas canvas) {
		
		stopWatch.start();
		cache = new double[1 << (this.knots.size()-1)][this.knots.size()];
		PowerSetBuilder psb = new PowerSetBuilder(this.knots.size());
//		powerSets = PowerSetBuilder.buildPowerSet(psb.getSet());
		
		for(int i = 0; i < this.knots.size(); i++) {
			cache[0][i] = this.knots.get(0).getDistance(this.knots.get(i));
		}	
		rek(1, powerSets);
				
		stopWatch.stop();
		calcTime = stopWatch.getMills();
		stopWatch.reset();
		
//		canvas.drawRoute(this, minRoute);
	}
	
	private double rek(int iStartKnot, ArrayList<ArrayList<Integer>> set) {
		
		if(set.size() == 1 && set.get(0).isEmpty()) {
			return cache[0][iStartKnot-1];
		}
		
		for(int i = 1; i <= (this.knots.size()-1); i++) {
			for(ArrayList<Integer> subSet : PowerSetBuilder.getSubSets(set, i)) {
				for(int j = 0; j <= (this.knots.size()-1); j++) {
					for(Integer element : subSet) {
						if(!subSet.contains(j+1)){
							iStartKnot = Integer.valueOf(element);
							ArrayList<Integer> tmpList = new ArrayList<Integer>(subSet.subList(0, subSet.indexOf(element)));
							tmpList.addAll(subSet.subList(subSet.indexOf(element)+1, subSet.size()));							//neue Liste, in der element nicht mehr vorkommt
							if(cache[i][j] == 0.0) { cache[i][j] = Double.MAX_VALUE; }
							cache[i][j] = Math.min(cache[i][j], (this.knots.get(j).getDistance(this.knots.get(element-1)) + rek(iStartKnot, PowerSetBuilder.buildPowerSet(tmpList))));	//Array wird noch nicht korrekt durchlaufen
						}
					}
				}
			}
		}
		
		return 0.0;	
	}
	/**
	 * Gibt den Knoten zurück der am nähesten zum übergebenen {@link rootKnot} ist und zeichnet deren Verbindung.
	 * <li> Der übergebene Knoten {@link rootKnot} wird aus der Liste möglicher NN entfernt.
	 * <li> Für jeden der verbliebenden Knoten wird die Entfernung zum {@link rootKnot} in {@link thisDist} zwischen gespeichert.
	 * <li> Der erste Knoten im Durchlauf wird als nähester ({@link nearest}) festgelegt und seine Entfernung zum {@link rootKnot} als kürzeste ({@link minDist})
	 * <li> Wenn ein nachfolgender Knoten eine kürzere Entfernung zum {@link rootKnot} hat, gilt dieser als nähester.
	 * <li> Bei geschlossendem Kreis wird für den letzten Knoten im Durchlauf der Startknoten als nähester zurück gegeben, bei offenem Kreis wird null zurückgegeben.
	 * Die kürzesten Entfernungen werden in {@link wayLength} aufakkumuliert.<br>
	 * Beim ersten Neighbour gilt maxDist=minDist, also die längste Teilstrecke ist die Entfernung vom Ausgangsknoten zum ersten Neighbour.
	 * Danach werden rootKnot und nearest in {@link furthestKnots} abgelegt wenn ihre Entfernung größer ist als die bisher größte Teilstrecke ({@link maxDist})
	 * @param rootKnot Der Ausgangsknoten
	 * @param closed true/false ob geschlossener Kreis
	 * @param debug true/false ob Zwischenergebnisse geloggt werden
	 * @param g das zum zeichnen verwendete Graphics-Objekt
	 * @param debugPane Ausgabe für den Debug-Log
	 * @return nähester Knoten
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
				return (int)((e1.getCost() - e2.getCost()) * Math.pow(10, floatingPointPrecision)); //Ab ... NKS kein eindeutiger Vergleich mehr möglich
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
		spanningTreeEdges.add(e);
		
		e.getStart().addAjacentKnot(e.getEnd());
		e.getEnd().addAjacentKnot(e.getStart());
	}
	
	public void nextKnotfromTree(Knot thisKnot) {
		
		if (mstRoute.contains(thisKnot)) return;
		
		mstRoute.add(thisKnot);
		for (Knot ak : thisKnot.getAdjacentKnots()) nextKnotfromTree(ak);
	}
	
	public void showMSTwithTSP(SquareCanvas canvas) {
		
		canvas.redraw(SquareCanvas.REDRAW_KNOTS, SquareCanvas.CLEAR_EDGES);
		canvas.setOverlayBack();
		for (Edge e : spanningTreeEdges) canvas.drawEdge(e);
		canvas.setOverlayFront();
		canvas.drawRoute(this, mstRoute);
		
		
	}
	
	public double getIndexedTour(ArrayList<Integer> route) {
		
		double tourLength = getKnot(0).getDistance(getKnot(route.get(0)));
		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) tourLength += getKnot(route.get(i)).getDistance(getKnot(route.get(i+1)));
			else tourLength += getKnot(route.get(i)).getDistance(getKnot(0));
		} return tourLength;
	}
	
	public double getTour(ArrayList<Knot> route) {
		double tourLength = 0;
		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) tourLength += route.get(i).getDistance(route.get(i+1));
			else tourLength += route.get(i).getDistance(route.get(0));
		} return tourLength;
	}
	
	
	public String getResult(int mode) {
		
		String result = String.format("[%s] Testinstanz mit %d Knoten wurde berechnet mittels ", LocalDateTime.now(), getCount());
		switch (mode) {
		case 0:
			result += String.format("Brute-Force Verfahren.\n" +
					"Kürzeste Tour ist %.5f PE lang (Route %d von möglichen %d)\nRechenzeit: Permutationen %.2f ms, Routenlänge %.2f ms, Gesamt %.2f ms\n" + 
					"Das entspricht einer durchschnittlichen Rechenzeit von %.2f µs pro Route", 
					wayLength, routes.indexOf(minRoute), routes.size(), permutTime, calcTime-permutTime, calcTime, ((calcTime)/(double)routes.size()) * 1000); 
			break;
		case 1:
			result += String.format("Dynamischer Programierung.\n" +
					"Kürzeste Tour ist %.5f PE lang\nRechenzeit: %.2f ms\n",
					wayLength, calcTime); 
			break;
		case 2: 
			result += String.format("Nearest-Neighbour Verfahren.\n" +
					"Länge des Weges: %.5f Gewählter Startknoten: %s Längste Teilstrecke zwischen: %s und %s, %.5f\nRechenzeit: %.2f ms", 
					wayLength, knots.get(startKnot), furthestKnots[0], furthestKnots[1], maxDist,stopWatch.getMills());
			break;
		case 3: 
			result += String.format("Best-Nearest-Neighbour Verfahren.\n" +
					"Länge des Weges: %.5f Bester Startknoten: %s Längste Teilstrecke zwischen: %s und %s, %.5f\nRechenzeit: %.2f ms", 
					wayLength, knots.get(startKnot), furthestKnots[0], furthestKnots[1], maxDist,stopWatch.getMills());
			break;
		case 4:
			result += String.format("Minimum-Spanning-Tree Verfahren. (Berechnung des Spanning-Trees)\n" +
					"Rechenzeit: %.2f ms Kanten im Spanning-Tree: %d", calcTime,spanningTreeEdges.size());
			break;
		case 5:
			result += String.format("Minimum-Spanning-Tree Transformation\n" +
					"Länge des Weges: %.5f Rechenzeit: %.2f ms", getTour(mstRoute), stopWatch.getMills());
			break;
		case 6:
			result += String.format("Best Minimum-Spanning-Tree Transformation\n" +
					"Länge des Weges: %.5f Bester Startknoten: %s Rechenzeit: %.2f ms", getTour(mstRoute), MSTbestStart, stopWatch.getMills());
			break;
		}
		
		stopWatch.reset();
		return result;
	}
}
