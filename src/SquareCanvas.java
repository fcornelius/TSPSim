import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;


public class SquareCanvas extends JPanel {

	private static final long serialVersionUID = 1L;
	static int pixelWidth;
	static int pixelHeight;
	static int spacing;
	static int border;
	
	private BufferedImage bi;
	private Graphics2D g2D;
	private gWindow mainFrame;
	
	
	private int pointRadius;
	private boolean withNumbers;
	private int width;
	private int height;
	
	public SquareCanvas(int width, int height, int spacing, int border, gWindow owner) {
		
		mainFrame = owner;
		SquareCanvas.pixelWidth = width;
		SquareCanvas.pixelHeight = height;
		SquareCanvas.spacing = spacing;
		SquareCanvas.border = border;
		this.width = width + 2 * (spacing + border);
		this.height = height + 2 * (spacing + border);
		flushGraphics();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(bi, 0, 0, this);
	}
	
	public void drawKnot(Knot k) {
		
		g2D.fillOval(k.X()-pointRadius,k.Y()-pointRadius,2*pointRadius,2*pointRadius);
		if (withNumbers) {
			if (k.getY() > 580) g2D.drawString(String.valueOf(k.getId()+1), k.X()-4, k.Y()-10);
			else g2D.drawString(String.valueOf(k.getId()+1), k.X()-4, k.Y()+18);
		}
	}
	
	public void drawEdge(Edge e) {
		
		g2D.drawLine(e.getStart().X(), e.getStart().Y(), e.getEnd().X(), e.getEnd().Y());
	}
	
	public void drawEdge(Knot start, Knot end) {
		
		g2D.drawLine(start.X(), start.Y(), end.X(), end.Y());
	}
	
	public void redrawKnots(Instance inst) {
		
		for (int i=0; i<inst.getCount(); i++) { drawKnot(inst.getKnot(i)); }
	}
	
	public void drawRoute(Instance inst, ArrayList<Integer> route) {
		
		drawEdge(inst.getKnot(0),inst.getKnot(route.get(0)));
		
		for (int i=0; i<route.size(); i++) {
			if (i < route.size()-1) drawEdge(inst.getKnot(route.get(i)), inst.getKnot(route.get(i+1)));
			else drawEdge(inst.getKnot(route.get(i)), inst.getKnot(0));
		}
	}
	public void flushGraphics() {
		
		if (g2D != null) g2D.dispose();
		if (bi != null) bi.flush();
		else bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);  
		
		g2D = bi.createGraphics();
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, width, height);
		g2D.setColor(Colors.colors[mainFrame.comboBox.getSelectedIndex()]); //TODO Getter
		
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
	
	
	
}
