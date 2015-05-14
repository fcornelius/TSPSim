import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class gWindow {
	
	public Instance instanz;
	public static final int dimension = 600;
	public static final int canvasSpacing = 6;
	public static final int canvasBorder = 3;
	
	
	private JFrame frame;

	private JButton btnPoints;
	private JTextField txtPoints;
	public JCheckBox chckbxAntia;
	private JButton btnReset;
	public JComboBox<Integer> comboBox_linie;
	public JComboBox<Integer> comboBox_punkt;
	public JComboBox<String> comboBox;
	private JLabel lblLinie;
	private JButton btnNext;
	private JTextPane txtDebug;
	private JCheckBox chckbxDebug;
	private JPanel panel_1;
	private JLabel lblPunkt;
	public JCheckBox chckbxNummern;
	private JCheckBox chckbxGeschlossen;
	private JButton btnAuflsen;
	private JLabel lblResult;
	private JButton btnRepeat;
	private JPanel panelLogo;
	private JLabel lblLogo;
	private JLabel lblVersion;
	private JLabel lblFh;
	private JLabel lblFhKln;
	private JPanel panel_2;
	private JPanel panel_3;
	private JList<String> list;
	private JScrollPane sp_knots;
	private JComboBox<String> comboBoxMode;
	private SquareCanvas canvas;
	private DefaultListModel<String> blank;
	
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
		} catch (Exception exp) { /* Fallback auf Metal LAF */ }
		
		//Forms-Komponenten von Oben nach Unten
		
		//   Frame
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 853, 694);
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
		
		
		//   AntiAliasing Checkbox
		chckbxAntia = new JCheckBox("AntiAliasing");
		panel_1.add(chckbxAntia);
		chckbxAntia.setSelected(true);
		chckbxAntia.addChangeListener(new OptionListener());
		
		chckbxNummern = new JCheckBox("Nummern");
		chckbxNummern.setSelected(true);
		chckbxNummern.addChangeListener(new OptionListener());
		panel_1.add(chckbxNummern);
		
		//   Linie Label
		lblLinie = new JLabel("Linie");
		panel_1.add(lblLinie);
		
		//   Linie ComboBox mit Items 1 bis 4
		comboBox_linie = new JComboBox<Integer>();
		comboBox_linie.setPreferredSize(new Dimension(40, 25));
		for (int i = 1; i <= 4; i++) comboBox_linie.addItem(i);
		comboBox_linie.addItemListener(new OptionListener());
		panel_1.add(comboBox_linie);
		
		//   Punkt Label
		lblPunkt = new JLabel("Punkt");
		panel_1.add(lblPunkt);
		
		//   Punkt ComboBox
		comboBox_punkt = new JComboBox<Integer>();
		comboBox_punkt.setPreferredSize(new Dimension(40, 25));
		for (int i = 0; i <= 10; i++) comboBox_punkt.addItem(i);
		comboBox_punkt.setSelectedItem(comboBox_punkt.getItemAt(4));
		comboBox_punkt.addItemListener(new OptionListener());
		panel_1.add(comboBox_punkt);
		
		//   Color ComboBox mit Items aus Colors enum
		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(80, 25));
		for (Colors.ColorNames c : Colors.ColorNames.values()) comboBox.addItem(c.toString());
		comboBox.setSelectedItem(comboBox.getItemAt(12));
		comboBox.addItemListener(new OptionListener());
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
	    lblResult.setVisible(false);
	    frame.getContentPane().add(lblResult);
	    
		//   Wiederholen Button
		btnRepeat = new JButton("Instanz wiederholen");
		btnRepeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repeatInstance(instanz);
			}
		});
		btnRepeat.setBounds(10, 362, 182, 23);
		btnRepeat.setVisible(false);
		frame.getContentPane().add(btnRepeat);
	
		//   Debug Textpane
		txtDebug = new JTextPane();
		txtDebug.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtDebug.setBounds(168, 500, 258, 72);

		//   Debug Scrollbar
		JScrollPane sp = new JScrollPane(txtDebug,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    sp.setBounds(12, 663, 811, 142);
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
	    comboBoxMode.addItem("MST-Transform");
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
	   btnNext.addActionListener(new ActionListener() {
		   	public void actionPerformed(ActionEvent e) {
		   		switch (comboBoxMode.getSelectedIndex()) {
	    		case 1:
	    			NN_Next(instanz);
	    			break;
	    		case 3: 
	    			MST(instanz);
	    			break;
		   		}
		   	}
		   });
	   
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
	    		case 3:
	    			
	    		}
	    		
	    	}
	    });
	    btnAuflsen.setEnabled(false);
	    
	    // Verfahren ComboBox Listeners
	    comboBoxMode.addItemListener(new ItemListener() {
	    	public void itemStateChanged(ItemEvent e) {
	    		switch (comboBoxMode.getSelectedIndex()) {
	    		case 0: 
	    			btnNext.setEnabled(false);
	    			btnNext.setText("Nächster");
	    			break;
	    		case 1: 
	    			btnNext.setEnabled(true);
	    			btnNext.setText("Nächster");
	    			break;
	    		case 2:
	    			btnNext.setEnabled(false);
	    			btnNext.setText("Nächster");
	    			break;
	    		case 3:
	    			btnNext.setText("MST");
	    			btnNext.setEnabled(true);
	    			btnAuflsen.setEnabled(false);
	    		
	    		}
	    	}
	    });
	    
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
		
		blank = new DefaultListModel<String>();
		blank.addElement("Über 'Neue Instanz'");
		blank.addElement("Knoten hinzufügen.");
		list.setModel(blank);
		
		sp_knots = new JScrollPane(list);
		sp_knots.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp_knots.setBounds(20, 303, 172, 335);
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
	 
		canvas = new SquareCanvas(dimension, canvasSpacing, canvasBorder, this);
		canvas.setBounds(223, 38, canvas.getSideLength(), canvas.getSideLength());
		canvas.setBorder(BorderFactory.createLineBorder(Color.gray, canvasBorder));
		frame.getContentPane().add(canvas);

	}
	
	class OptionListener implements ItemListener, ChangeListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			canvas.updateGraphics();
		}
		@Override
		public void stateChanged(ChangeEvent e) {
			canvas.updateGraphics();
		}
	}
	
	/**
	 * Setzt sowohl die Instanz als auch das Canvas zurück:
	 * Knotenliste wird überschrieben, Canvas wird mit weißem Rechteck übermalt (flushCanvas()),
	 * Steuerelemente werden zurückgesetzt: Knotenliste wieder in voller Höhe, Lösen-Buttons deaktiviert,
	 * Weiderholen-Button und Ergebnis-Label als unsichtbar gesetzt
	 */
	private void Reset() {
		
		list.setModel(blank);
		txtDebug.setText("");
		
		canvas.flushGraphics();
		sp_knots.setBounds(sp_knots.getX(), sp_knotsYPx, sp_knots.getWidth(), sp_knotsHeightPx);;
		btnNext.setEnabled(false);
		btnAuflsen.setEnabled(false);
		btnRepeat.setVisible(false);
		lblResult.setVisible(false);
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
		DefaultListModel<String> knotenlist = new DefaultListModel<String>();

		for (int i = 0; i<count; i++) {
			Knot n = new Knot(i);
			inst.addKnot(n);
			knotenlist.addElement(n.toString()); //TODO Setter von Instance-Klasse aus wäre schöner, dann erstellen der Instanz über new Instance(count)
			canvas.drawKnot(n);
		}
		
		canvas.repaint();
		list.setModel(knotenlist);
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
	private void NN_Next(Instance inst) {     //TODO NN Next und Solve zusammenführen, über Parameter step steuern
		
		if (list.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Zuerst einen Startknoten in der Liste auswählen");
		else {
			boolean closed = chckbxGeschlossen.isSelected();
			if (inst.isReady()) inst.setStart(list.getSelectedIndex());

			Knot nearest = inst.nearestNeighbour(inst.getKnotByIndex(list.getSelectedIndex()), closed, true, true, canvas, txtDebug);
			if (nearest != null) list.setSelectedIndex((nearest.getId()));
			
			showResult(inst);
			canvas.repaint();
		}
	}
	
	private void NN_Solve(Instance inst) {
		
		if (list.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Zuerst einen Startknoten in der Liste auswählen");
		else {
			boolean closed = chckbxGeschlossen.isSelected();
			if (inst.isReady()) inst.setStart(list.getSelectedIndex());
			
			Knot knot = inst.getKnotByIndex(list.getSelectedIndex());
			while (!inst.isFinished()) knot = inst.nearestNeighbour(knot, closed, false, true, canvas, null);

			showResult(inst);
			canvas.repaint();
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
			
			while (!inst.isFinished()) thisknot = inst.nearestNeighbour(thisknot, closed, false, false, null, null);
			if ((i==0) || (inst.getWaylenghth() < thisLenghth)) {
				thisLenghth = inst.getWaylenghth();
				bestStart = thisknot;
			}
			inst.resetInstance();
		}
		
		if (inst.isReady()) inst.setStart(bestStart.getId());
		list.setSelectedIndex(bestStart.getId());
		while (!inst.isFinished()) bestStart = inst.nearestNeighbour(bestStart, closed, false, true, canvas, null);
		
		showResult(inst);
		canvas.repaint();
	}
	
	private void MST(Instance inst) {
		
		inst.makeMST(canvas);
		canvas.repaint();
	}
	
	private void repeatInstance(Instance inst) {
		
		canvas.flushGraphics();
		inst.resetInstance();
		
		canvas.redrawKnots(inst);
		canvas.repaint();
		
		btnNext.setEnabled(true);
		btnAuflsen.setEnabled(true);
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
	
	public void logLines(String lines) {
		txtDebug.setText(txtDebug.getText() + lines);
	}
	
}
