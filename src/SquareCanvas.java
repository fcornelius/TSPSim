import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JPanel;


public class SquareCanvas extends JPanel {

	private static final long serialVersionUID = 1L;
	static int pixelWidth;
	static int pixelHeight;
	static int spacing;
	static int border;
	
	public static final int REDRAW_ALL = 0;
	public static final int REDRAW_KNOTS = 1;
	public static final int CLEAR_ALL = 0;
	public static final int CLEAR_EDGES = 1;
	public static final int KEEP_BUFFER = 2;
	
	private BufferedImage bi;
	private BufferedImage bi_overlay;
	private BufferedImage biBack;
	private Graphics2D g2D;
	private Graphics2D g2DBack;
	private Graphics2D g2D_cursor;
	private boolean drawCursor;
	private gWindow mainFrame;
	
	private int pointRadius;
	private boolean withNumbers;
	private int width;
	private int height;
	
	private boolean editMode;
	private boolean dragMode;
	private ArrayList<Knot> mouseKnots;
	
	private ArrayList<Knot> knotBuffer;
	private ArrayList<Edge> edgeBuffer;
	
	public SquareCanvas(int width, int height, int spacing, int border, gWindow owner) {
		
		mainFrame = owner;
		addMouseMotionListener(new MouseMotion());
		addMouseListener(new MouseEnter());
		
		SquareCanvas.pixelWidth = width;
		SquareCanvas.pixelHeight = height;
		SquareCanvas.spacing = spacing;
		SquareCanvas.border = border;
		this.width = width + 2 * (spacing + border);
		this.height = height + 2 * (spacing + border);
		
		knotBuffer = new ArrayList<Knot>();
		edgeBuffer = new ArrayList<Edge>();
		
		bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);  
		g2D = bi.createGraphics();
		g2D.setBackground(new Color(0,true));
		
		biBack = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB); 
		g2DBack = biBack.createGraphics();
		
		bi_overlay = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_ARGB);
		g2D_cursor = bi_overlay.createGraphics();
		g2D_cursor.setColor(Color.lightGray);
		g2D_cursor.setBackground(new Color(0,true));
		
		flushGraphics(KEEP_BUFFER,true); 
	}
	
	public void startEditMode() {
		editMode = true;
		mouseKnots = new ArrayList<Knot>();
		mainFrame.newList();
	}
	
	public void stopEditMode() {
		editMode = false;
	}
	
	public void setDragMode(boolean mode) {
		dragMode = mode;
	}
	public ArrayList<Knot> getMouseKnots() {
		return mouseKnots;
	}
	
	public BufferedImage getImage() {
		return bi;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.drawImage(biBack, 0, 0, null);
		g.drawImage(bi, 0, 0, this);
		g.drawImage(bi_overlay,0,0,null);
	}
	
	public void drawKnot(Knot k) {
		
		g2D.fillOval(k.X()-pointRadius,k.Y()-pointRadius,2*pointRadius,2*pointRadius);
		if (withNumbers) {
			if (k.getY() > 580) g2D.drawString(String.valueOf(k.getId()+1), k.X()-4, k.Y()-10);
			else g2D.drawString(String.valueOf(k.getId()+1), k.X()-4, k.Y()+18);
		}
		if (!knotBuffer.contains(k)) knotBuffer.add(k);
	}
	
	public void drawEdge(Edge e) {
		
		g2D.drawLine(e.getStart().X(), e.getStart().Y(), e.getEnd().X(), e.getEnd().Y());
		if (!edgeBuffer.contains(e)) edgeBuffer.add(e);
	}
	
	public void drawEdge(Knot start, Knot end) {
		
		drawEdge(new Edge(start, end));
	}
	
	public void redraw(int drawMode, int clearMode) {
		
		flushGraphics(clearMode,false);
		
		switch (drawMode) {
		case REDRAW_ALL:
			redrawEdges();
		case REDRAW_KNOTS:
			redrawKnots();
		}
		
		repaint();
	}
	

	
	public void redrawKnots() {
		
		for (Knot k : knotBuffer) drawKnot(k);
	}
	
	public void redrawEdges() {
		
		for (Edge e : edgeBuffer) drawEdge(e);
	}
	
	public void drawIndexedRoute(Instance inst, ArrayList<Integer> route) {
		
		drawEdge(inst.getKnot(0),inst.getKnot(route.get(0)));
		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) drawEdge(inst.getKnot(route.get(i)), inst.getKnot(route.get(i+1)));
			else drawEdge(inst.getKnot(route.get(i)), inst.getKnot(0));
		}
	}
	public void drawRoute(Instance inst, ArrayList<Knot> route) {

		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) drawEdge((route.get(i)), route.get(i+1));
			else drawEdge(route.get(i), route.get(0));
		}
	}
	
	
	
	public void setOverlayBack() {
		g2D.setStroke(new BasicStroke(4));
		g2D.setColor(Color.cyan);
	}
	public void setOverlayFront() {
		g2D.setStroke(new BasicStroke(1));
		g2D.setColor(Color.blue);
	}
	
	public void setBackground(BufferedImage back, float scale, int offsetX, int offsetY, float alpha) {

		Composite orig = g2DBack.getComposite();
		AlphaComposite ac1
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1-alpha);
		
		g2DBack.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2DBack.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2DBack.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2DBack.fillRect(0, 0, width, height);
		g2DBack.drawImage(back,  offsetX, offsetY, 
						 (int)(scale * back.getWidth()),
						 (int)(scale * back.getHeight()),null);
		g2DBack.setComposite(ac1);
		g2DBack.setColor(Color.white);
		g2DBack.fillRect(0,0, width, height);
		g2DBack.setComposite(orig);
		repaint();
	}
	
	public void resetBackground() {
		
	}
	
	public void flushGraphics(int clearMode, boolean clearBack) {
		
		
		g2D.clearRect(0, 0,width, height);
		
		if (clearBack) {
			g2DBack = biBack.createGraphics();
			g2DBack.setColor(Color.white);
			g2DBack.fillRect(0, 0, width, height);
		}
		
		if (mainFrame.getWithRulers()) drawCanvasBorder();
		
//		g2D.setColor(Colors.colors[mainFrame.comboBox.getSelectedIndex()]); //TODO Getter
		
		switch (clearMode) {
		case CLEAR_ALL:
			knotBuffer.clear();
		case CLEAR_EDGES:
			edgeBuffer.clear();
			break;
		}
		
		updateGraphics();
		repaint();
	}
	
	public void updateGraphics() {
		
		int stroke = (int) mainFrame.comboBox_linie.getSelectedItem(); //TODO Getters in gWindow wären besser...
		Color clr = Colors.colors[mainFrame.comboBox.getSelectedIndex()];
		pointRadius = (int) mainFrame.comboBox_punkt.getSelectedItem();
		withNumbers = mainFrame.chckbxNummern.isSelected();
		
		if (mainFrame.chckbxAntia.isSelected()) g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2D.setFont(new Font("Arial",Font.BOLD,12));
		g2D.setStroke(new BasicStroke(stroke));
		g2D.setColor(clr);

	}
	private void drawCanvasBorder() {
		int x1=0;
		
		g2D.setStroke(new BasicStroke(1));
		g2D.setColor(Color.lightGray);
		
		g2D.drawLine(0, border+spacing, width, border+spacing);
		g2D.drawLine(border+spacing+pixelWidth, 0, border+spacing+pixelWidth, height);
		g2D.drawLine(width,border+spacing+pixelHeight,0,border+spacing+pixelHeight);
		g2D.drawLine(border+spacing, height, border+spacing, 0);
		
		g2D.setColor(Color.lightGray);
		for (int i=2; i<pixelWidth; i+=2) { 
			if (i%50==0) { x1=5; g2D.setColor(Color.darkGray); }
			else if (i%10==0) { x1=3; g2D.setColor(Color.gray); }
			else { x1=0; g2D.setColor(Color.lightGray); }
			
			if (i<pixelHeight) g2D.drawLine(border+spacing+pixelWidth-x1, border+spacing+i, width, border+spacing+i);
			g2D.drawLine(border+spacing+i, border+spacing+pixelHeight-x1, border+spacing+i, height);
		}
		
	}
	
	class MouseMotion implements MouseMotionListener {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			mainFrame.lblMouseX.setText(e.getX()-(border+spacing) + " :");
			mainFrame.lblMouseY.setText(e.getY()-(border+spacing)+"");
			
			if (mainFrame.getWithCursor()) {
				
				g2D_cursor.clearRect(0, 0, width, height);
				

				g2D_cursor.drawLine(e.getX(), border+spacing, e.getX(), height);
				g2D_cursor.drawLine(border+spacing, e.getY(), width, e.getY());

				repaint();
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
			
			if (editMode && dragMode) newMouseKnot(e.getX(),e.getY());
		}
	}
	
	class MouseEnter extends MouseAdapter {
		
		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			if (mainFrame.getWithCursor()) {
				
				setCursor(getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(0,0),""));
			} else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			if (mainFrame.getWithCursor()) {
				g2D_cursor.clearRect(0, 0, width, height);
				repaint();
			}
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			if (editMode) newMouseKnot(e.getX(),e.getY());
		}

	}
	
	private void newMouseKnot(int x, int y) {
		
		Knot mouseAdd = new Knot(mouseKnots.size(), x - spacing - border, y - spacing - border);
		drawKnot(mouseAdd);
		mouseKnots.add(mouseAdd);
		mainFrame.addToList(mouseAdd.toString());
		repaint();
	}
	
	
	
}
