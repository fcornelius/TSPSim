import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class ImageDialog extends JDialog {

	private JPanel contentPane;
	private JRadioButton rdbtnVoreinstellung;
	private JRadioButton rdbtnEigeneGrafik;
	private JRadioButton rdbtnZentriert;
	private JRadioButton rdbtnUrsprung;
	private JRadioButton rdbtnAnpassen;
	private JRadioButton rdbtnProzent;
	private JSpinner spnSkalieren;
	private JSpinner spnOffsetX;
	private JSpinner spnOffsetY;
	private JSpinner spnAlpha;
	private JCheckBox chckbxVorschau;


	private JTextField txtImagepath;
	private JComboBox<String> comboBox; 
	private ImagePanel thumbImage;
	private SquareCanvas canvas;
	private BufferedImage image;

	private File[] presets;
	private File presetDir;

	private float scale;
	private int offsetX;
	private int offsetY;
	private float alpha;

	private FileFilter imageFilter;

	/**
	 * Create the frame.
	 */
	public ImageDialog(SquareCanvas canvas) {

		this.canvas = canvas;
		
		imageFilter = new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".gif")) return true;
				else return false;
			}
		};
		
		initializeGUI();
		loadPresets();
		setVisible(true);
		
		
		
		image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_INDEXED);

	}

	private void loadPresets() {

		presetDir = new File("ressources/PresetImages").getAbsoluteFile();
		presets = presetDir.listFiles(imageFilter);

		for (File p : presets) {
			comboBox.addItem(p.getName().substring(0, p.getName().lastIndexOf(".")).replace("_", " "));
		}
	}

	private void updateBackground() {

		int id = comboBox.getSelectedIndex() - 1;
		int drawWidth = SquareCanvas.pixelWidth + 2*SquareCanvas.spacing;
		int drawHeight = SquareCanvas.pixelHeight + 2*SquareCanvas.spacing;
		if (id > -1) { 
			try {

				image = ImageIO.read(ImageDialog.class.getResource("/PresetImages/" + presets[id].getName()));

				if (rdbtnAnpassen.isSelected()) {

					if ((((float)drawWidth)/image.getWidth(null)) * image.getHeight(null) >  drawHeight) {
						scale = ((float)drawHeight)/image.getHeight(null);
					} else {
						scale = ((float)drawWidth)/image.getWidth(null);
					}
					spnSkalieren.setValue((int)(scale*100));
				} else {
					scale = ((int)spnSkalieren.getValue())/100.0f;
				}

				if(rdbtnZentriert.isSelected()) {

					offsetX = (int)(drawWidth/2.0 - ((image.getWidth(null) * scale)/2));
					offsetY = (int)(drawHeight/2.0 - ((image.getHeight(null) * scale)/2));
				} else {
					offsetX = 0;
					offsetY = 0;
				}

				offsetX += (int)spnOffsetX.getValue();
				offsetY += (int)spnOffsetY.getValue();

				alpha = ((int)spnAlpha.getValue())/100.0f;

				canvas.setBackground(image, scale, offsetX, offsetY,alpha);

			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}
		}
	}
	
	private void ResetUI() {
		
		comboBox.setSelectedIndex(0);
		rdbtnVoreinstellung.setSelected(true);
		rdbtnZentriert.setSelected(true);
		rdbtnAnpassen.setSelected(true);
		spnSkalieren.setValue(0);
		spnOffsetX.setValue(0);
		spnOffsetY.setValue(0);
		spnAlpha.setValue(50);
		
	}
	private void initializeGUI() {

		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 561, 318);
		setLocationRelativeTo(canvas);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Border titleBorder1 = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Grafik importieren");

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBounds(12, 20, 354, 88);
		panel.setBorder(titleBorder1);
		contentPane.add(panel);

		chckbxVorschau = new JCheckBox("Vorschau");
		chckbxVorschau.setSelected(true);
		chckbxVorschau.setBounds(412, 120, 113, 25);
		contentPane.add(chckbxVorschau);

		rdbtnVoreinstellung = new JRadioButton("Voreinstellung:",true);
		rdbtnVoreinstellung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtImagepath.setEnabled(false);
				comboBox.setEnabled(true);
			}
		});
		rdbtnVoreinstellung.setSelected(true);
		rdbtnVoreinstellung.setPreferredSize(new Dimension(120, 25));
		panel.add(rdbtnVoreinstellung);


		comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int id;

				if (presets != null) {
					id = comboBox.getSelectedIndex() - 1;
					if (id > -1) {
						thumbImage.setMode(ImagePanel.CENTER);
						thumbImage.setImage("thumbs/" + presets[id].getName() + ".png");
					} else {
						thumbImage.setMode(ImagePanel.CENTER);
						thumbImage.setImage("Icons/empty_64.png");
					}
					
					if (chckbxVorschau.isSelected())
						updateBackground();
				}


			}
		});
		comboBox.setPreferredSize(new Dimension(180, 23));
		comboBox.addItem("Grafik wählen...");
		panel.add(comboBox);



		rdbtnEigeneGrafik = new JRadioButton("Eigene Grafik:");
		rdbtnEigeneGrafik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtImagepath.setEnabled(true);
				comboBox.setEnabled(false);
			}
		});
		rdbtnEigeneGrafik.setPreferredSize(new Dimension(120, 25));
		panel.add(rdbtnEigeneGrafik);

		ButtonGroup importGroup = new ButtonGroup();
		importGroup.add(rdbtnEigeneGrafik);
		importGroup.add(rdbtnVoreinstellung);

		txtImagepath = new JTextField();
		txtImagepath.setEnabled(false);
		txtImagepath.setText("Durchsuchen...");
		txtImagepath.setPreferredSize(new Dimension(180, 23));
		panel.add(txtImagepath);

		Border titleBorder2 = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Grafik anpassen");

		//		SmartJPanel panel_1 = new SmartJPanel();
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(12, 116, 354, 144);
		panel_1.setBorder(titleBorder2);
		contentPane.add(panel_1);

		JLabel lblAusrichten = new JLabel("Ausrichten:");
		lblAusrichten.setPreferredSize(new Dimension(85, 16));
		panel_1.add(lblAusrichten);

		ButtonGroup ausrichtenButtons = new ButtonGroup();
		rdbtnZentriert = new JRadioButton("Zentriert");
		rdbtnZentriert.addChangeListener(new UpdateListener());
		rdbtnZentriert.setSelected(true);
		ausrichtenButtons.add(rdbtnZentriert);
		panel_1.add(rdbtnZentriert);

		rdbtnUrsprung = new JRadioButton("Ursprung");
		rdbtnUrsprung.addChangeListener(new UpdateListener());
		ausrichtenButtons.add(rdbtnUrsprung);
		panel_1.add(rdbtnUrsprung);

		JLabel lblSpace = new JLabel("");
		lblSpace.setPreferredSize(new Dimension(10, 16));
		panel_1.add(lblSpace);

		JLabel lblSkalieren = new JLabel("Skalieren:");
		lblSkalieren.setPreferredSize(new Dimension(85, 16));
		panel_1.add(lblSkalieren);

		ButtonGroup skalierenButtons = new ButtonGroup();
		rdbtnAnpassen = new JRadioButton("Anpassen");
		rdbtnAnpassen.addChangeListener(new UpdateListener());
		rdbtnAnpassen.setSelected(true);
		skalierenButtons.add(rdbtnAnpassen);
		panel_1.add(rdbtnAnpassen);

		rdbtnProzent = new JRadioButton("Prozent:");
		rdbtnProzent.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				spnSkalieren.setEnabled(rdbtnProzent.isSelected());
				
				if (chckbxVorschau.isSelected()) 
					updateBackground();
			}
		});


		skalierenButtons.add(rdbtnProzent);
		panel_1.add(rdbtnProzent);

		spnSkalieren = new JSpinner();
		spnSkalieren.addChangeListener(new UpdateListener());
		spnSkalieren.setEnabled(false);
		spnSkalieren.setPreferredSize(new Dimension(60, 22));
		panel_1.add(spnSkalieren);
		spnSkalieren.setModel(new SpinnerNumberModel(100, 1, 100, 1));

		JLabel lblverschieben = new JLabel("Verschiebung:");
		lblverschieben.setPreferredSize(new Dimension(90,16));
		panel_1.add(lblverschieben);

		spnOffsetX = new JSpinner();
		spnOffsetX.addChangeListener(new UpdateListener());
		spnOffsetX.setModel(new SpinnerNumberModel(0, -999, 999, 1));
		spnOffsetX.setPreferredSize(new Dimension(55, 22));
		panel_1.add(spnOffsetX);

		JLabel lblX = new JLabel("X Pixel");
		panel_1.add(lblX);

		spnOffsetY = new JSpinner();
		spnOffsetY.addChangeListener(new UpdateListener());
		spnOffsetY.setModel(new SpinnerNumberModel(0, -999, 999, 1));
		spnOffsetY.setPreferredSize(new Dimension(55, 22));
		panel_1.add(spnOffsetY);

		JLabel lblY = new JLabel("Y Pixel");
		panel_1.add(lblY);

		JLabel lblAlpha = new JLabel("Alpha:");
		lblAlpha.setPreferredSize(new Dimension(90,16));
		panel_1.add(lblAlpha);

		spnAlpha = new JSpinner();
		spnAlpha.addChangeListener(new UpdateListener());
		spnAlpha.setModel(new SpinnerNumberModel(new Integer(50), null, null, new Integer(1)));
		spnAlpha.setPreferredSize(new Dimension(60, 22));
		panel_1.add(spnAlpha);

		JLabel lblProzent = new JLabel("Prozent");
		panel_1.add(lblProzent);

		Border thumbBorder = BorderFactory.createLineBorder(Color.lightGray, 2);
		thumbImage = new ImagePanel("Icons/empty_64.png", ImagePanel.CENTER, Color.white);
		thumbImage.setBackground(Color.WHITE);
		thumbImage.setBorder(thumbBorder);
		FlowLayout fl_thumbImage = (FlowLayout) thumbImage.getLayout();
		fl_thumbImage.setVgap(1);
		fl_thumbImage.setHgap(1);
		thumbImage.setBounds(406, 150, 108, 108);
		contentPane.add(thumbImage);



		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setBackground(Color.WHITE);
		separator.setBounds(383, 13, 1, 250);
		contentPane.add(separator);

		JButton btnbernehmen = new JButton("\u00DCbernehmen");
		btnbernehmen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!chckbxVorschau.isSelected()) updateBackground();
				setVisible(false);
			}
		});
		btnbernehmen.setBounds(402, 26, 113, 25);
		contentPane.add(btnbernehmen);

		JButton btnZurcksetzen = new JButton("Zur\u00FCcksetzen");
		btnZurcksetzen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetUI();
				canvas.setBackground(image, 1, 0, 0, 0);
				
			}
		});
		btnZurcksetzen.setBounds(402, 53, 113, 25);
		contentPane.add(btnZurcksetzen);

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetUI();
				setVisible(false);
			}
		});
		btnAbbrechen.setBounds(402, 88, 113, 25);
		contentPane.add(btnAbbrechen);



		//		panel_1.setComponentsEnabled(false);
	}

	class UpdateListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			
			if (chckbxVorschau.isSelected())
				updateBackground();

		}

	}
	class SmartJPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public void setComponentsEnabled(boolean enable) {
			Component[] childs = this.getComponents();
			for (Component c : childs) c.setEnabled(enable);
		}
	}
}
