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


public class gWindow {
	
	public Instance instanz;
	public static final int dimensionPx = 500;
	public static final int frameHeightPx = 578;
	public static final int frameHeightDebugPx = 744;
	
	public JFrame frame;
	public JLabel imagelabel;
	private BufferedImage buffimg;
	private ImageIcon image;
	private Graphics2D g;
	private JTextField txtCountinput;
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
	private JList<String> list;
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
	private JScrollPane sp_knots;
	private JPanel panelLogo;
	private JLabel lblLogo;
	private JLabel lblUnterlogo;
	private JLabel lblVersion;
	private JLabel lblFh;
	private JLabel lblFhKln;
	private JPanel panel_2;
	private JPanel panel_3;

	/**
	 * Launch the application.
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try
		{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exp) {}
		
		//   Frame
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 713, 578);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//   Top Panel
		panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		panel_1.setBounds(179, 0, 514, 33);
		frame.getContentPane().add(panel_1);
		
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
		
		//   Linie ComboBox
		comboBox_linie = new JComboBox<Integer>();
		comboBox_linie.setPreferredSize(new Dimension(37,21));
		for (int i = 1; i <= 4; i++) comboBox_linie.addItem(i);
		panel_1.add(comboBox_linie);
		
		//   Punkt Label
		lblPunkt = new JLabel("Punkt");
		panel_1.add(lblPunkt);
		
		//   Punkt ComboBox
		comboBox_punkt = new JComboBox<Integer>();
		comboBox_punkt.setPreferredSize(new Dimension(37,21));
		for (int i = 0; i <= 10; i++) comboBox_punkt.addItem(i);
		comboBox_punkt.setSelectedItem(comboBox_punkt.getItemAt(4));
		panel_1.add(comboBox_punkt);
		
		//   Color ComboBox
		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(80,21));
		for (Colors.ColorNames c : Colors.ColorNames.values()) comboBox.addItem(c.toString());
		comboBox.setSelectedItem(comboBox.getItemAt(12));
		panel_1.add(comboBox);
		
		//   Reset Button
		btnReset = new JButton("Reset");
		btnReset.setPreferredSize(new Dimension(65,23));
		panel_1.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reset();
			}
		});
		
		//   Punkt Liste
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(10, 187, 140, 302);

		//   Punkte Scrollbar
		sp_knots = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp_knots.setPreferredSize(new Dimension(259, 100));
		sp_knots.setMinimumSize(new Dimension(50, 50));
		sp_knots.setOpaque(false);
		sp_knots.setBounds(10, 215, 175, 324);
		frame.getContentPane().add(sp_knots);
		
		//   Result Label
		lblResult = new JLabel("");
	    lblResult.setVerticalAlignment(SwingConstants.TOP);
	    lblResult.setBounds(10, 215, 182, 70);
	    frame.getContentPane().add(lblResult);
	    
		//   Wiederholen Button
		btnRepeat = new JButton("Instanz wiederholen");
		btnRepeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repeatInstance(instanz);
			}
		});
		btnRepeat.setBounds(15, 285, 149, 23);
		frame.getContentPane().add(btnRepeat);
		
		//   Canvas Panel
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(2);
		flowLayout.setHgap(2);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(189, 35, dimensionPx+4, dimensionPx+4);
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
	    sp.setPreferredSize(new Dimension(250, 100));
	    sp.setMinimumSize(new Dimension(50, 50));
	    sp.setOpaque(false);
	    sp.setBounds(10, 550, 687, 155);
	    frame.getContentPane().add(sp);
	    
	    panelLogo = new JPanel();
	    FlowLayout flowLayout_2 = (FlowLayout) panelLogo.getLayout();
	    flowLayout_2.setAlignOnBaseline(true);
	    flowLayout_2.setVgap(3);
	    flowLayout_2.setAlignment(FlowLayout.LEFT);
	    panelLogo.setBounds(10, 0, 169, 81);
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
	    
	    lblFhKln = new JLabel("FH K\u00F6ln");
	    lblFhKln.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panelLogo.add(lblFhKln);
	    
	    panel_2 = new JPanel();
	    FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
	    flowLayout_3.setHgap(3);
	    flowLayout_3.setVgap(3);
	    flowLayout_3.setAlignment(FlowLayout.LEFT);
	    panel_2.setBounds(10, 87, 175, 117);
	    frame.getContentPane().add(panel_2);
	    
	    //   Punkte Button
	    btnPoints = new JButton("Neue Instanz");
	    btnPoints.setPreferredSize(new Dimension(120, 30));
	    panel_2.add(btnPoints);
	    
	    btnPoints.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		instanz = createInstance();
	    	}
	    });
	    
	    //   Punkte Anzahl
	    txtPoints = new JTextField("10");
	    txtPoints.setHorizontalAlignment(SwingConstants.RIGHT);
	    txtPoints.setPreferredSize(new Dimension(35, 25));
	    panel_2.add(txtPoints);
	    
	    //   Zuf�llig Button
	    btnButton = new JButton("Zuf\u00E4llige Route");
	    btnButton.setPreferredSize(new Dimension(120, 23));
	    panel_2.add(btnButton);
	    
	   //   Zuf�llig Anzahl
	   txtCountinput = new JTextField("10");
	   txtCountinput.setHorizontalAlignment(SwingConstants.RIGHT);
	   txtCountinput.setPreferredSize(new Dimension(35, 20));
	   panel_2.add(txtCountinput);
	   
	   panel_3 = new JPanel();
	   FlowLayout flowLayout_4 = (FlowLayout) panel_3.getLayout();
	   flowLayout_4.setHgap(80);
	   flowLayout_4.setVgap(2);
	   panel_2.add(panel_3);
	   
	   //   N�chster NN Button
	   btnNext = new JButton("N\u00E4chster");
	   btnNext.setPreferredSize(new Dimension(85, 23));
	   panel_2.add(btnNext);
	   btnNext.setEnabled(false);
	   
	   //   Aufl�sen Button
	    btnAuflsen = new JButton("L\u00F6sen");
	    btnAuflsen.setPreferredSize(new Dimension(70, 23));
	    panel_2.add(btnAuflsen);
	    btnAuflsen.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		NN_Solve(instanz);
	    	}
	    });
	    btnAuflsen.setEnabled(false);
	    
	    //   geschlossen Checkbox
	     chckbxGeschlossen = new JCheckBox("geschlossen");
	     panel_2.add(chckbxGeschlossen);
	     chckbxGeschlossen.setSelected(true);
	     
		//   Debug Checkbox
		chckbxDebug = new JCheckBox("Debug");
		panel_2.add(chckbxDebug);
		chckbxDebug.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxDebug.isSelected()) frame.setBounds(frame.getX(), frame.getY(), 711, frameHeightDebugPx);
				else frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(), frameHeightPx);
			}
		});
	   btnNext.addActionListener(new ActionListener() {
	   	public void actionPerformed(ActionEvent e) {
	   		NN_Next(instanz);
	   	}
	   });
	    btnButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		button_Clicked();
	    	}
	    });
	    
	    
	    
	   
	    
	   
	    
	   
	    

	    //   Canvas Image
		Reset();
		
		
		btnPoints.grabFocus();
		
	}
	
	private void Reset() {
		
		DefaultListModel<String> blank = new DefaultListModel<String>();
		blank.addElement("�ber 'Neue Instanz'");
		blank.addElement("Knoten hinzuf�gen.");
		list.setModel(blank);
		txtDebug.setText("");
		
		flushCanvas();
		
		sp_knots.setBounds(10, 215, 159, 324);
		btnNext.setEnabled(false);
		btnAuflsen.setEnabled(false);
		btnRepeat.setVisible(false);
		lblResult.setVisible(false);
	}
	
	private void button_Clicked() {
		
		int count = Integer.parseInt(txtCountinput.getText());
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

	private void NN_Next(Instance inst) {
		
		if (list.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Zuerst einen Startknoten in der Liste ausw�hlen");
		else {
			boolean closed = chckbxGeschlossen.isSelected();
			if (inst.isReady()) inst.setStart(list.getSelectedIndex());
			g = makeGraphics();
			
			Knot nearest = inst.nearestNeighbour(inst.getKnotByIndex(list.getSelectedIndex()), closed, true, g, txtDebug);
			if (nearest != null) list.setSelectedIndex((nearest.getId()));
			
			showResult(inst);
			drawToCanvas(buffimg);
			g.dispose();
		}
	}
	
	private void NN_Solve(Instance inst) {
		
		if (list.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Zuerst einen Startknoten in der Liste ausw�hlen");
		else {
			boolean closed = chckbxGeschlossen.isSelected();
			if (inst.isReady()) inst.setStart(list.getSelectedIndex());
			g = makeGraphics();
			
			Knot knot = inst.getKnotByIndex(list.getSelectedIndex());
			while (!inst.isFinished()) knot = inst.nearestNeighbour(knot, closed, false, g, txtDebug);

			showResult(inst);
			drawToCanvas(buffimg);
			g.dispose();
		}
	
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
			sp_knots.setBounds(10, 318, 159, 221);
			btnRepeat.setVisible(true);
			lblResult.setVisible(true);
			
			btnNext.setEnabled(false);
			btnAuflsen.setEnabled(false);
			lblResult.setText("<html>" + inst.getResult() + "</html>");
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
