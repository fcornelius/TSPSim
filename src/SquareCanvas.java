import java.awt.BasicStroke;
import java.awt.Color;
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
	
	private BufferedImage bi;
	private BufferedImage bi_overlay;
	private BufferedImage biBack;
	private Graphics2D g2D;
	private Graphics2D g2D_cursor;
	private gWindow mainFrame;
	
	private int pointRadius;
	private boolean withNumbers;
	private int width;
	private int height;
	
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
		flushGraphics(false); 
	}
	
	public BufferedImage getImage() {
		return bi;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if (biBack != null)
			g.drawImage(biBack, 0, 0, null);
		g.drawImage(bi, 0, 0, this);
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
	
	public void redraw(int mode) {
		
		flushGraphics(false);
		
		switch (mode) {
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
	
	public void drawRoute(Instance inst, ArrayList<Integer> route) {
		
		drawEdge(inst.getKnot(0),inst.getKnot(route.get(0)));
		
		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) drawEdge(inst.getKnot(route.get(i)), inst.getKnot(route.get(i+1)));
			else drawEdge(inst.getKnot(route.get(i)), inst.getKnot(0));
		}
	}
	
	public void setBackground(BufferedImage back) {
		biBack = back;
	}
	
	
	public void flushGraphics(boolean clearBuffer) {
		
		if (g2D != null) g2D.dispose();
		if (bi != null) bi.flush();
		else bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);  
		
		g2D = bi.createGraphics();
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, width, height);
		
		if (mainFrame.getWithRulers()) drawCanvasBorder();
		
		g2D.setColor(Colors.colors[mainFrame.comboBox.getSelectedIndex()]); //TODO Getter
		
		if (clearBuffer) { knotBuffer.clear(); edgeBuffer.clear(); }
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
		g2D.setBackground(Color.white);
		g2D.setStroke(new BasicStroke(stroke));
		g2D.setColor(clr);

	}
	private void drawCanvasBorder() {
		int x1=0;
		
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
				g2D_cursor.setColor(Color.lightGray);
				g2D_cursor.drawImage(bi_overlay, 0, 0, null);
				g2D_cursor.drawLine(e.getX(), border+spacing, e.getX(), height);
				g2D_cursor.drawLine(border+spacing, e.getY(), width, e.getY());

				repaint();
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}
	
	class MouseEnter extends MouseAdapter {
		
		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			if (mainFrame.getWithCursor()) {
				bi_overlay = new BufferedImage(bi.getColorModel(),bi.copyData(null),false,null);
				g2D_cursor = bi.createGraphics();

				// Mouse-Cursor ausblenden:
				setCursor(getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(0,0),""));
			} else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			if (mainFrame.getWithCursor()) {
				g2D.drawImage(bi_overlay, 0, 0, null);
				repaint();
			}
			
		}
	}
	
	
	
}
