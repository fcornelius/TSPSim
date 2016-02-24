import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


public class InstAssist extends JFrame {

	private JPanel contentPane;
	private JTextField txtKnotcount;
	private JTable table;
	private JTabbedPane tabbedPane;
	private JPanel pnlTable;
	private gWindow owner;
	private SquareCanvas canvas;

	

	/**
	 * Create the frame.
	 */
	public InstAssist(gWindow owner, SquareCanvas canvas) {
		
		this.owner = owner;
		this.canvas = canvas;
		
		initializeGUI();
		setVisible(true);
	}

	private void initializeGUI() {
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exp) { /* Fallback auf Metal LAF */ }
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 300, 260);
		setLocationRelativeTo(owner.getFrame());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNeueInstanzErstellen = new JLabel("Neue Instanz erstellen");
		lblNeueInstanzErstellen.setBounds(82, 13, 181, 16);
		contentPane.add(lblNeueInstanzErstellen);
		
		ButtonGroup optButtons = new ButtonGroup();
		JRadioButton rdbtnRandom = new JRadioButton("Zuf\u00E4llige Punkte: ");
		rdbtnRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setVisible(false);
				setSize(new Dimension(400,260));
			}
		});
		rdbtnRandom.setSelected(true);
		rdbtnRandom.setBounds(52, 57, 137, 25);
		optButtons.add(rdbtnRandom);
		contentPane.add(rdbtnRandom);
		
		txtKnotcount = new JTextField();
		txtKnotcount.setText("100");
		txtKnotcount.setBounds(192, 58, 51, 22);
		contentPane.add(txtKnotcount);
		txtKnotcount.setColumns(10);
		txtKnotcount.setBackground(Color.white);
		
		JRadioButton rdbtnMouse = new JRadioButton("Mit Maus eingeben");
		rdbtnMouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setVisible(false);
				//setSize(new Dimension(400,260));
			}
		});
		rdbtnMouse.setBounds(52, 87, 181, 25);
		optButtons.add(rdbtnMouse);
		contentPane.add(rdbtnMouse);
		
		JRadioButton rdbtnMatrix = new JRadioButton("Aus Adjazenzmatrix berechnen");
		rdbtnMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setVisible(true);
				setSize(new Dimension(636,474));
			}
		});
		rdbtnMatrix.setBounds(52, 117, 225, 25);
		optButtons.add(rdbtnMatrix);
		contentPane.add(rdbtnMatrix);
		
		pnlTable = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pnlTable.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(10);
		flowLayout.setHgap(0);
		pnlTable.setBounds(72, 151, 520, 281);
		contentPane.add(pnlTable);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(515, 220));
		pnlTable.add(tabbedPane);
		tabbedPane.setVisible(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		tabbedPane.addTab("Tabellarisch", null, panel, null);
		
		JLabel lblSpalten = new JLabel("Rang:");
		panel.add(lblSpalten);
		
		table = new JTable(10,10) {
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				if (column > row) return false;
//				else return true;
//			}
		};
		TableColumn col = table.getColumnModel().getColumn(1);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(10, 4, 20, 1));
		panel.add(spinner);
		
		
		table.setPreferredSize(new Dimension(512, 190));
		
		panel.add(table);
		
		
		
		
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Text", null, panel_1, null);
		
		JButton btnErstellen = new JButton("Erstellen");
		pnlTable.add(btnErstellen);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		pnlTable.add(btnAbbrechen);
		
		
		btnErstellen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (rdbtnRandom.isSelected())
					owner.randInstance(Integer.parseInt(txtKnotcount.getText()));
				else if (rdbtnMouse.isSelected()) {
					owner.Reset();
					owner.mouseMode(true);
					canvas.startEditMode();
					
				}
				setVisible(false);
			}
		});
		
		JTextField textbox = new JTextField();
		col.setCellEditor(new DefaultCellEditor(textbox));
		
	}
}
