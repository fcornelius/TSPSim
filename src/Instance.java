import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTextPane;


public class Instance {
	
	private static final int floatingPointPrecision = 10;
	
	private ArrayList<Knot> knots;
	private ArrayList<Knot> neighbours;
	private ArrayList<Knot> spanningTreeKnots;
	private ArrayList<Edge> edges;
	private ArrayList<Edge> spanningTreeEdges;
	
	private int startKnot;
	private double maxDist;
	private Knot[] furthestKnots;
	private double wayLenghth;
	
	/**
	 * Konstruktor
	 * Legt Knotenliste und Neighbourliste an
	 */
	public Instance() {
		knots = new ArrayList<Knot>();
		neighbours = new ArrayList<Knot>();
		spanningTreeKnots = new ArrayList<Knot>();
		edges = new ArrayList<Edge>();
		spanningTreeEdges = new ArrayList<Edge>();
		maxDist = 0;
		wayLenghth = 0;
	}
	/**
	 * Löscht alle verbeibenden Neighbour und füllt Neighbourliste neu mit Knoten aus knots
	 * für Wiederholung der Instanz.<br>
	 * Weglänge ({@link wayLenghth}) und größte Teilstrecke ({@link maxDist}) werden zurückgesetzt.
	 */
	public void resetInstance() {
		neighbours.clear();
		for (Knot k : knots) neighbours.add(k);
		maxDist = 0;
		wayLenghth = 0;
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
	/**
	 * Gibt einen Knoten aus der Knotenliste an Position {@link index} wieder
	 * @param index Position des Knotens ins Knotenliste (0-basiert)
	 * @return Knoten
	 */
	public Knot getKnotByIndex(int index) {
		return knots.get(index);
	}
	
	public int getCount() {
		return knots.size();
	}
	public double getWaylenghth() {
		return wayLenghth;
	}
	/**
	 * Gibt an ob die Instanz erstellt und noch nicht gelöst wurde.<br>
	 * Sobald über 'Nächster' ein Schritt des Lösens erfolgt ist, gibt die Instanz <b>false</b> zurück.
	 * @return <b>true</b> wenn Neighbourliste noch voll,<br> <b>false</b> wenn bereits Einträge entfernt wurden
	 */
	public boolean isReady() {
		return (knots.size()==neighbours.size());
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
	public void addEdgeToTree(Edge e) {
		if (!spanningTreeKnots.contains(e.getStart())) spanningTreeKnots.add(e.getStart());
		if (!spanningTreeKnots.contains(e.getEnd())) spanningTreeKnots.add(e.getEnd());
		edges.remove(e);
		spanningTreeEdges.add(e);
	}
	/**
	 * Nach Beendigung des Löseverfahrens ist der letzte Wert von {@link wayLenght} die Akkumulierte Weglänge
	 * und in {@link furthestKnots} stehen die beiden, am weitesten entfernten Knoten.
	 * @return ein String mit dem Ergebnis
	 */
	public String getResult() {
		//TODO Länge der längsten Entfernung ausgeben (maxDist)
		return String.format("Länge des Weges: %.2f<br>Längste Teilstrecke zwischen:<br>%s<br>und %s", wayLenghth, furthestKnots[0], furthestKnots[1]);
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
				if (debug) debugPane.setText(debugPane.getText() +  String.format("knotId: %d rootKnot: %s thisKnot: %s \nthisDist: %f minDist: %f nearest: %s \n-------------------------------------------------------------------------------------\n", 
							knotId, rootKnot, thisKnot, thisDist, minDist, nearest));
				knotId++;
			}
		}
		wayLenghth += minDist;
		if (minDist > maxDist) { maxDist = minDist; furthestKnots = new Knot[]{rootKnot, nearest}; }
		
		if (draw) canvas.drawEdge(rootKnot, nearest);
		return nearest;
	}
	
	public void makeMST(SquareCanvas canvas) {   //TODO MST Step-Verfahren wie bei NN implementieren (in selber Methode)

		int n = knots.size();

		for (int i = 0; i < n; i++) {
			for (int j = 1; j < n-i; j++) {
				edges.add(new Edge(getKnotByIndex(i), getKnotByIndex(i+j)));
			}
		}
		Collections.sort(edges, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return (int)((e1.getCost() - e2.getCost()) * Math.pow(10, floatingPointPrecision)); //Ab ... NKS kein eindeutiger Vergleich mehr möglich
			}
		});
		
		canvas.drawEdge(edges.get(0));
		addEdgeToTree(edges.get(0));
		
		while (spanningTreeKnots.size() < n) {

			for (Edge thisEdge : edges) {
				if (spanningTreeKnots.contains(thisEdge.getStart()) ^ spanningTreeKnots.contains(thisEdge.getEnd())) {
					canvas.drawEdge(thisEdge);
					addEdgeToTree(thisEdge);
					break;
				} 
			}
		}
	}
}
