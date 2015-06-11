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

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;


public class ImageDialog extends JDialog {

	private JPanel contentPane;
	private JTextField txtImagepath;
	private JComboBox<String> comboBox; 
	private ImagePanel thumbImage;
	private SquareCanvas canvas;
	private BufferedImage image;

	private File[] presets;
	private File presetDir;

	/**
	 * Create the frame.
	 */
	public ImageDialog(SquareCanvas canvas) {

		this.canvas = canvas;
		
		initializeGUI();
		loadPresets();
		setVisible(true);
		
	}

	private void loadPresets() {

		presetDir = new File("ressources/PresetImages").getAbsoluteFile();
		presets = presetDir.listFiles();

		for (File p : presets) {
			comboBox.addItem(p.getName().substring(0, p.getName().lastIndexOf(".")).replace("_", " "));
		}
	}
	private void initializeGUI() {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 561, 318);
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

		JRadioButton rdbtnVoreinstellung = new JRadioButton("Voreinstellung:",true);
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
						thumbImage.setMode(ImagePanel.FIT_CENTER);
						thumbImage.setImage("PresetImages/" + presets[id].getName());
					} else {
						thumbImage.setMode(ImagePanel.CENTER);
						thumbImage.setImage("Icons/empty_64.png");
					}
				}


		}
	});
		comboBox.setPreferredSize(new Dimension(180, 23));
		comboBox.addItem("Grafik wählen...");
		panel.add(comboBox);



		JRadioButton rdbtnEigeneGrafik = new JRadioButton("Eigene Grafik:");
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

		JRadioButton rdbtnZentriert = new JRadioButton("Zentriert");
		rdbtnZentriert.setSelected(true);
		panel_1.add(rdbtnZentriert);

		JRadioButton rdbtnUrsprung = new JRadioButton("Ursprung");
		panel_1.add(rdbtnUrsprung);

		JLabel lblSpace = new JLabel("");
		lblSpace.setPreferredSize(new Dimension(10, 16));
		panel_1.add(lblSpace);

		JLabel lblSkalieren = new JLabel("Skalieren:");
		lblSkalieren.setPreferredSize(new Dimension(85, 16));
		panel_1.add(lblSkalieren);

		JRadioButton rdbtnAnpassen = new JRadioButton("Anpassen");
		rdbtnAnpassen.setSelected(true);
		panel_1.add(rdbtnAnpassen);

		JRadioButton rdbtnProzent = new JRadioButton("Prozent:");
		panel_1.add(rdbtnProzent);

		JSpinner spinner = new JSpinner();
		spinner.setEnabled(false);
		spinner.setPreferredSize(new Dimension(43, 22));
		panel_1.add(spinner);
		spinner.setModel(new SpinnerNumberModel(100, 1, 100, 1));

		JLabel lblverschieben = new JLabel("Verschiebung:");
		lblverschieben.setPreferredSize(new Dimension(90,16));
		panel_1.add(lblverschieben);

		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(0, 0, 999, 1));
		spinner_1.setPreferredSize(new Dimension(43, 22));
		panel_1.add(spinner_1);

		JLabel lblX = new JLabel("X Pixel");
		panel_1.add(lblX);

		JSpinner spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(0, 0, 999, 1));
		spinner_2.setPreferredSize(new Dimension(43, 22));
		panel_1.add(spinner_2);

		JLabel lblY = new JLabel("Y Pixel");
		panel_1.add(lblY);

		JLabel lblAlpha = new JLabel("Alpha:");
		lblAlpha.setPreferredSize(new Dimension(90,16));
		panel_1.add(lblAlpha);

		JSpinner spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(new Integer(50), null, null, new Integer(1)));
		spinner_3.setPreferredSize(new Dimension(43, 22));
		panel_1.add(spinner_3);

		JLabel lblProzent = new JLabel("Prozent");
		panel_1.add(lblProzent);

		thumbImage = new ImagePanel("Icons/empty_64.png", ImagePanel.CENTER, Color.white);
		thumbImage.setBackground(Color.WHITE);
		FlowLayout fl_thumbImage = (FlowLayout) thumbImage.getLayout();
		fl_thumbImage.setVgap(1);
		fl_thumbImage.setHgap(1);
		thumbImage.setBounds(407, 150, 108, 108);
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
				int id = comboBox.getSelectedIndex() - 1;
				
				try {
					image = ImageIO.read(ImageDialog.class.getResource("/PresetImages/" + presets[id].getName()));
					canvas.setBackground(image);
					canvas.flushGraphics(false);
				} catch (Exception ex) {}
			}
		});
		btnbernehmen.setBounds(402, 26, 113, 25);
		contentPane.add(btnbernehmen);

		JButton btnZurcksetzen = new JButton("Zur\u00FCcksetzen");
		btnZurcksetzen.setBounds(402, 53, 113, 25);
		contentPane.add(btnZurcksetzen);

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setBounds(402, 88, 113, 25);
		contentPane.add(btnAbbrechen);

		JCheckBox chckbxVorschau = new JCheckBox("Vorschau");
		chckbxVorschau.setSelected(true);
		chckbxVorschau.setBounds(412, 120, 113, 25);
		contentPane.add(chckbxVorschau);

		//		panel_1.setComponentsEnabled(false);
}

class SmartJPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public void setComponentsEnabled(boolean enable) {
		Component[] childs = this.getComponents();
		for (Component c : childs) c.setEnabled(enable);
	}
}
}
