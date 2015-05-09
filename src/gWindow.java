import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;

import java.awt.Canvas;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ScrollPaneConstants;


public class gWindow {
	
	public Instance instanz;
	public static final int dimensionPx = 600;
	
	
	private JFrame frame;
	private BufferedImage buffimg;
	private ImageIcon image;
	private Graphics2D g;
	private JLabel lblCanvas;
	private JPanel panel;
	private JButton btnPoints;
	private JTextField txtPoints;
	private JCheckBox chckbxAntia;
	private JButton btnReset;
	private JComboBox<Integer> comboBox_linie;
	private JComboBox<Integer> comboBox_punkt;
	private JComboBox<String> comboBox;
	private JLabel lblLinie;
	private JButton btnNext;
	private JTextPane txtDebug;
	private JCheckBox chckbxDebug;
	private JPanel panel_1;
	private JLabel lblPunkt;
	private JButton btnButton;
	private JCheckBox chckbxNummern;
	private JCheckBox chckbxGeschlossen;
	private JButton btnAuflsen;
	private JLabel lblResult;
	private JButton btnRepeat;
	private JPanel panelLogo;
	private JLabel lblLogo;
	private JLabel lblUnterlogo;
	private JLabel lblVersion;
	private JLabel lblFh;
	private JLabel lblFhKln;
	private JPanel panel_2;
	private JPanel panel_3;
	private JList<String> list;
	private JScrollPane sp_knots;
	private JComboBox<String> comboBoxMode;
	
	private int frameHeightPx;
	private int frameHeightDebugPx;
	private int sp_knotsHeightPx;
	private int sp_knotsHeightResultPx;
	private int sp_knotsYPx;
	private int sp_knotsYResultPx;

	/**
	 * Launch the application. // Generiert durch WindowManager
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gWindow window = new gWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame. //Generiert durch WindowManager
	 */
	private void initialize() {
		try
		{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exp) {}
		
		//Forms-Komponenten von Oben nach Unten
		
		//   Frame
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 843, 687);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frameHeightPx = frame.getHeight();
		frameHeightDebugPx = frame.getHeight() + 166;

		//   Top Panel
		panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		panel_1.setBounds(202, 0, 621, 35);
		frame.getContentPane().add(panel_1);
		
		//   Zufällig Button
		btnButton = new JButton("Route");
		panel_1.add(btnButton);
		btnButton.setPreferredSize(new Dimension(70, 25));
		btnButton.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		randRoute();
		 	}
		 });
		
		//   AntiAliasing Checkbox
		chckbxAntia = new JCheckBox("AntiAliasing");
		panel_1.add(chckbxAntia);
		chckbxAntia.setSelected(true);
		
		chckbxNummern = new JCheckBox("Nummern");
		chckbxNummern.setSelected(true);
		panel_1.add(chckbxNummern);
		
		//   Linie Label
		lblLinie = new JLabel("Linie");
		panel_1.add(lblLinie);
		
		//   Linie ComboBox mit Items 1 bis 4
		comboBox_linie = new JComboBox<Integer>();
		comboBox_linie.setPreferredSize(new Dimension(40, 25));
		for (int i = 1; i <= 4; i++) comboBox_linie.addItem(i);
		panel_1.add(comboBox_linie);
		
		//   Punkt Label
		lblPunkt = new JLabel("Punkt");
		panel_1.add(lblPunkt);
		
		//   Punkt ComboBox
		comboBox_punkt = new JComboBox<Integer>();
		comboBox_punkt.setPreferredSize(new Dimension(40, 25));
		for (int i = 0; i <= 10; i++) comboBox_punkt.addItem(i);
		comboBox_punkt.setSelectedItem(comboBox_punkt.getItemAt(4));
		panel_1.add(comboBox_punkt);
		
		//   Color ComboBox mit Items aus Colors enum
		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(80, 25));
		for (Colors.ColorNames c : Colors.ColorNames.values()) comboBox.addItem(c.toString());
		comboBox.setSelectedItem(comboBox.getItemAt(12));
		panel_1.add(comboBox);
		
		//   Reset Button
		btnReset = new JButton("Reset");
		btnReset.setPreferredSize(new Dimension(70, 27));
		panel_1.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reset();
			}
		});
		
		//   Result Label
		lblResult = new JLabel("");
	    lblResult.setVerticalAlignment(SwingConstants.TOP);
	    lblResult.setBounds(10, 262, 201, 97);
	    frame.getContentPane().add(lblResult);
	    
		//   Wiederholen Button
		btnRepeat = new JButton("Instanz wiederholen");
		btnRepeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repeatInstance(instanz);
			}
		});
		btnRepeat.setBounds(10, 362, 182, 23);
		frame.getContentPane().add(btnRepeat);
		
		//   Canvas Panel
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(2);
		flowLayout.setHgap(2);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(223, 38, 604, 604);
		frame.getContentPane().add(panel);
		
		//   Canvas Label
		lblCanvas = new JLabel(image);
		lblCanvas.setForeground(Color.WHITE);
		lblCanvas.setBackground(Color.WHITE);
		lblCanvas.setBounds(10, 10, dimensionPx, dimensionPx);
		panel.add(lblCanvas);
	
		//   Debug Textpane
		txtDebug = new JTextPane();
		txtDebug.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtDebug.setBounds(168, 500, 258, 72);

		//   Debug Scrollbar
		JScrollPane sp = new JScrollPane(txtDebug,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    sp.setBounds(26, 677, 707, 155);
	    frame.getContentPane().add(sp);
	    
	    panelLogo = new JPanel();
	    FlowLayout flowLayout_2 = (FlowLayout) panelLogo.getLayout();
	    flowLayout_2.setAlignOnBaseline(true);
	    flowLayout_2.setVgap(3);
	    flowLayout_2.setAlignment(FlowLayout.LEFT);
	    panelLogo.setBounds(21, 13, 169, 81);
	    frame.getContentPane().add(panelLogo);
	    
	    lblLogo = new JLabel("TSPSim ");
	    lblLogo.setForeground(new Color(128, 0, 0));
	    lblLogo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
	    panelLogo.add(lblLogo);
	    
	    lblVersion = new JLabel("v0.2");
	    lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 10));
	    lblVersion.setVerticalAlignment(SwingConstants.BOTTOM);
	    panelLogo.add(lblVersion);
	    
	    lblUnterlogo = new JLabel("(Nearest-Neighbour-Verfahren)");
	    lblUnterlogo.setFont(new Font("Tahoma", Font.PLAIN, 10));
	    panelLogo.add(lblUnterlogo);
	    
	    lblFh = new JLabel("MA2 Projekt SoSe '15    ");
	    lblFh.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panelLogo.add(lblFh);
	    
	    lblFhKln = new JLabel("FH Köln");
	    lblFhKln.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panelLogo.add(lblFhKln);
	    
	    panel_2 = new JPanel();
	    FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
	    flowLayout_3.setHgap(3);
	    flowLayout_3.setVgap(3);
	    panel_2.setBounds(0, 121, 201, 139);
	    frame.getContentPane().add(panel_2);
	    
	    comboBoxMode = new JComboBox<String>();
	    comboBoxMode.setPreferredSize(new Dimension(156, 25));
	    comboBoxMode.addItem("BruteForce");
	    comboBoxMode.addItem("NearestNeighbour");
	    comboBoxMode.addItem("Best NearestNeighbour");
	    comboBoxMode.setSelectedIndex(1);
	    panel_2.add(comboBoxMode);
	    
	    panel_3 = new JPanel();
	    FlowLayout flowLayout_4 = (FlowLayout) panel_3.getLayout();
	    flowLayout_4.setHgap(80);
	    panel_2.add(panel_3);
	    
	    //   Punkte Button
	    btnPoints = new JButton("Neue Instanz");
	    btnPoints.setPreferredSize(new Dimension(120, 35));
	    panel_2.add(btnPoints);
	    
	    btnPoints.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		instanz = createInstance();
	    	}
	    });
	    
	    //   Punkte Anzahl
	    txtPoints = new JTextField("10");
	    txtPoints.setHorizontalAlignment(SwingConstants.RIGHT);
	    txtPoints.setPreferredSize(new Dimension(35, 30));
	    panel_2.add(txtPoints);
	   
	   //   Nächster NN Button
	   btnNext = new JButton("N\u00E4chster");
	   btnNext.setPreferredSize(new Dimension(85, 23));
	   panel_2.add(btnNext);
	   btnNext.setEnabled(false);
	   
	   //   Auflösen Button
	    btnAuflsen = new JButton("L\u00F6sen");
	    btnAuflsen.setPreferredSize(new Dimension(70, 23));
	    panel_2.add(btnAuflsen);
	    btnAuflsen.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		switch (comboBoxMode.getSelectedIndex()) {
	    		case 0:
	    			//bruteForce(instanz)
	    			break;
	    		case 1:
	    			NN_Solve(instanz);
	    			break;
	    		case 2:
	    			bestNN(instanz);
	    		}
	    		
	    	}
	    });
	    btnAuflsen.setEnabled(false);
	    
	    //   geschlossen Checkbox
	     chckbxGeschlossen = new JCheckBox("geschlossen");
	     panel_2.add(chckbxGeschlossen);
	     chckbxGeschlossen.setSelected(true);
	     
		//   Debug Checkbox mit Changehandler, ändert Framehöhe
		chckbxDebug = new JCheckBox("Debug");
		panel_2.add(chckbxDebug);
		
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setBackground(Color.WHITE);
		list.setBounds(243, 240, 59, 81);
		
		
		sp_knots = new JScrollPane(list);
		sp_knots.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp_knots.setBounds(10, 303, 182, 335);
		frame.getContentPane().add(sp_knots);
		sp_knotsHeightPx = sp_knots.getHeight();
		sp_knotsHeightResultPx = sp_knots.getY()-89;
		sp_knotsYPx = sp_knots.getY();
		sp_knotsYResultPx = sp_knots.getY() + 89;
		
		JLabel lblVerfahren = new JLabel("Verfahren:");
		lblVerfahren.setBounds(22, 102, 83, 23);
		frame.getContentPane().add(lblVerfahren);
		
		chckbxDebug.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxDebug.isSelected()) frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(), frameHeightDebugPx);
				else frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(), frameHeightPx);
			}
		});
	   btnNext.addActionListener(new ActionListener() {
	   	public void actionPerformed(ActionEvent e) {
	   		NN_Next(instanz);
	   	}
	   });
	    
	    //   Canvas Image
		Reset();

	}
	
	/**
	 * Setzt sowohl die Instanz als auch das Canvas zurück:
	 * Knotenliste wird überschrieben, Canvas wird mit weißem Rechteck übermalt (flushCanvas()),
	 * Steuerelemente werden zurückgesetzt: Knotenliste wieder in voller Höhe, Lösen-Buttons deaktiviert,
	 * Weiderholen-Button und Ergebnis-Label als unsichtbar gesetzt
	 */
	private void Reset() {
		
		DefaultListModel<String> blank = new DefaultListModel<String>();
		blank.addElement("Über 'Neue Instanz'");
		blank.addElement("Knoten hinzufügen.");
		list.setModel(blank);
		txtDebug.setText("");
		
		flushCanvas();
		sp_knots.setBounds(sp_knots.getX(), sp_knotsYPx, sp_knots.getWidth(), sp_knotsHeightPx);;
		btnNext.setEnabled(false);
		btnAuflsen.setEnabled(false);
		btnRepeat.setVisible(false);
		lblResult.setVisible(false);
	}
	
	/**
	 * Erzeugt einen Startknoten {@link startKnot} und je nach Eingabe weitere Knoten {@link nextKnot}<br>
	 * Iteriert wird über {@link thisKnot}, welcher am Anfang auf {@link startKnot} zeigt.<br>
	 * Für jeden erzeugten Knoten wird eine Linie zum jweils nächsten gezeichnet, es sei denn, es ist der 
	 * letzte Knoten, dann verbindet die Linie diesem mit dem Startknoten
	 */
	private void randRoute() {
		
		int count = Integer.parseInt(txtPoints.getText());
		int radius = (int) comboBox_punkt.getSelectedItem();
		boolean withNumbers = chckbxNummern.isSelected();

		g = makeGraphics();

		Knot startKnot = new Knot(0);
		Knot thisKnot = startKnot;
		Knot nextKnot;
		
		for (int i = 1; i<=count; i++) {
			nextKnot = new Knot(i);
			
			thisKnot.drawPoint(g, radius, withNumbers);
			if (nextKnot.getId() == count) thisKnot.drawLine(g, startKnot);
			else thisKnot.drawLine(g, nextKnot);
			
			thisKnot = nextKnot;
		}
		drawToCanvas(buffimg);
		g.dispose();
	}

	/**
	 * Erzeugt ein neues Objekt der Klasse Instance und fügt diesem dei festgelegte Anzahl der Knoten hinzu.<br>
	 * Jeder Knoten wird über das DefaultListModel in die JList aufgenommen und mit {@link  drawPoint} auf das 
	 * BufferedImage, das zu g gehört gezeichnet. Das BufferedImage wird anschließend dem CanvasLabel zugeordnet.
	 * 
	 * @return den Verweis auf die erstellte Instanz
	 */
	private Instance createInstance() {
		
		Reset();
		Instance inst = new Instance();

		int count = Integer.parseInt(txtPoints.getText());
		int radius = (int) comboBox_punkt.getSelectedItem();
		boolean withNumbers = chckbxNummern.isSelected();
		
		g = makeGraphics();
		
		DefaultListModel<String> knotenlist = new DefaultListModel<String>();

		for (int i = 0; i<count; i++) {
			Knot n = new Knot(i);
			inst.addKnot(n);
			knotenlist.addElement(n.toString());
			
			n.drawPoint(g, radius, withNumbers);
		}
		list.setModel(knotenlist);

		drawToCanvas(buffimg);
		g.dispose();
		btnNext.setEnabled(true);
		btnAuflsen.setEnabled(true);
		
		return inst;
	}

	/**
	 * Zeigt den nächstgelenen Knoten zum ausgewählten in der JList an.
	 * Über nearestNeighbour wird dieser auf das Graphics-Objekt g gezeichnet, 
	 * welches anschließend durch drawToCanvas auf die Zeichenfläche angewandt wird
	 * <br>Bedingunen:<br>
	 * Ein Knoten ist ausgewählt und die Instanz ist bereit, also es wurde noch kein Neighbour der Liste entfernt.
	 * @see isReady
	 * @see nearestNeighbour
	 * 
	 * @param inst der Verweis auf die zu lösende Instanz
	 */
	private void NN_Next(Instance inst) {
		
		if (list.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Zuerst einen Startknoten in der Liste auswï¿½hlen");
		else {
			boolean closed = chckbxGeschlossen.isSelected();
			if (inst.isReady()) inst.setStart(list.getSelectedIndex());
			g = makeGraphics();
			
			Knot nearest = inst.nearestNeighbour(inst.getKnotByIndex(list.getSelectedIndex()), closed, true, true, g, txtDebug);
			if (nearest != null) list.setSelectedIndex((nearest.getId()));
			
			showResult(inst);
			drawToCanvas(buffimg);
			g.dispose();
		}
	}
	
	private void NN_Solve(Instance inst) {
		
		if (list.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Zuerst einen Startknoten in der Liste auswï¿½hlen");
		else {
			boolean closed = chckbxGeschlossen.isSelected();
			if (inst.isReady()) inst.setStart(list.getSelectedIndex());
			g = makeGraphics();
			
			Knot knot = inst.getKnotByIndex(list.getSelectedIndex());
			while (!inst.isFinished()) knot = inst.nearestNeighbour(knot, closed, false, true, g, txtDebug);

			showResult(inst);
			drawToCanvas(buffimg);
			g.dispose();
		}
	
	}
	
	private void bestNN(Instance inst) {
		
		Knot thisknot;
		double thisLenghth = 0;
		Knot bestStart = null;
		boolean closed = chckbxGeschlossen.isSelected();
		
		
		for (int i = 0; i<inst.getCount();i++) {
			if (inst.isReady()) inst.setStart(i);
			thisknot = inst.getKnotByIndex(i);
			
			while (!inst.isFinished()) thisknot = inst.nearestNeighbour(thisknot, closed, false, false, g, txtDebug);
			if ((i==0) || (inst.getWaylenghth() < thisLenghth)) {
				thisLenghth = inst.getWaylenghth();
				bestStart = thisknot;
			}
			inst.resetInstance();
		}
		
		g = makeGraphics();
		if (inst.isReady()) inst.setStart(bestStart.getId());
		list.setSelectedIndex(bestStart.getId());
		while (!inst.isFinished()) bestStart = inst.nearestNeighbour(bestStart, closed, false, true, g, txtDebug);
		showResult(inst);
		drawToCanvas(buffimg);
		g.dispose();
		
	}
	
	private void repeatInstance(Instance inst) {
		
		int radius = (int) comboBox_punkt.getSelectedItem();
		boolean withNumbers = chckbxNummern.isSelected();
		
		flushCanvas();
		g = makeGraphics();
		inst.resetInstance();
		inst.redrawInstance(g, radius, withNumbers);
		drawToCanvas(buffimg);
		g.dispose();
		
		btnNext.setEnabled(true);
		btnAuflsen.setEnabled(true);
	}
	
	private Graphics2D makeGraphics() {
		
		Graphics2D graphics = buffimg.createGraphics();
		int stroke = (int) comboBox_linie.getSelectedItem();
		Color clr = Colors.colors[comboBox.getSelectedIndex()];
		
		if (chckbxAntia.isSelected()) 
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setFont(new Font("Arial",Font.BOLD,12));
		graphics.setBackground(Color.white);
		graphics.setStroke(new BasicStroke(stroke));
		graphics.setColor(clr);
		
		return graphics;

	}
	
	private void showResult(Instance inst) {
		
		if (inst.isFinished()) {
			
			sp_knots.setBounds(sp_knots.getX(), sp_knotsYResultPx, sp_knots.getWidth(), sp_knotsHeightResultPx);
			btnRepeat.setVisible(true);
			lblResult.setVisible(true);
			
			btnNext.setEnabled(false);
			btnAuflsen.setEnabled(false);
			String result = inst.getResult();
			if (comboBoxMode.getSelectedIndex() == 2) result += ("<br>Bester Start: " + inst.getStartKnot());
			lblResult.setText("<html>" + result + "</html>");
		}
	}
	private void flushCanvas() {
		
		buffimg = new BufferedImage(dimensionPx,dimensionPx,BufferedImage.TYPE_BYTE_INDEXED);
		g = buffimg.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, dimensionPx, dimensionPx);
		g.dispose();
		drawToCanvas(buffimg);
	}
	private void drawToCanvas(BufferedImage bimg) {
		lblCanvas.setIcon(new ImageIcon(bimg));
	}
	
	public void logLines(String lines) {
		txtDebug.setText(txtDebug.getText() + lines);
	}
}
