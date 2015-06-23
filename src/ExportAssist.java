import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ExportAssist extends JFrame {

	private Instance inst;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtType;
	private JTextPane txtComment;
	private JTextField txtDim;
	private JTextField txtPath;
	private JButton btnSpeichern;
	
	private gWindow owner;
	private File export;


	public ExportAssist(Instance inst, String tspName, String tspComment, String tspType, int dim, gWindow owner) {

		this.inst = inst;
		this.owner = owner;
		
		initializeGUI();
		
		txtName.setText(tspName);
		txtType.setText(tspType);
		txtComment.setText(tspComment);
		txtDim.setText(dim + "");
		
		setVisible(true);
	}
	
	private void ExportTSP(File tspFile) {

		try {
			File export = tspFile;
			FileWriter fw = new FileWriter(export.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("NAME : " + txtName.getText());
			bw.newLine();
			bw.write("COMMENT : " + txtComment.getText());
			bw.newLine();
			bw.write("TYPE : " + txtType.getText());
			bw.newLine();
			bw.write("DIMENSION : " + txtDim.getText());
			bw.newLine();
			bw.write("NODE_COORD_SECTION");
			bw.newLine();

			for (int i=0; i<inst.getCount(); i++) {
				bw.write((i+1) + " " + inst.getKnot(i).getX() + " " + inst.getKnot(i).getY());
				bw.newLine();
			}

			bw.write("EOF");
			bw.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initializeGUI() {
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exp) { /* Fallback auf Metal LAF */ }
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 355);
		setLocationRelativeTo(owner.getFrame());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblInstanzAlsTsp = new JLabel("Instanz als TSP Datei exportieren...");
		lblInstanzAlsTsp.setBounds(103, 10, 249, 50);
		contentPane.add(lblInstanzAlsTsp);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 73, 45, 22);
		contentPane.add(lblName);
		
		txtName = new JTextField();
		txtName.setBounds(61, 73, 85, 22);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtType = new JTextField();
		txtType.setColumns(10);
		txtType.setBounds(195, 73, 53, 22);
		contentPane.add(txtType);
		
		JLabel lblTyp = new JLabel("Typ:");
		lblTyp.setBounds(159, 73, 45, 22);
		contentPane.add(lblTyp);
		
		txtDim = new JTextField();
		txtDim.setEnabled(false);
		txtDim.setColumns(10);
		txtDim.setBounds(314, 73, 77, 22);
		contentPane.add(txtDim);
		
		JLabel lblKnoten = new JLabel("Knoten:");
		lblKnoten.setBounds(257, 73, 45, 22);
		contentPane.add(lblKnoten);
		
		JLabel lblKommentar = new JLabel("Kommentar:");
		lblKommentar.setBounds(12, 109, 85, 22);
		contentPane.add(lblKommentar);
		
		btnSpeichern = new JButton("Speichern");
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportTSP(export);
			}
		});
		btnSpeichern.setEnabled(false);
		btnSpeichern.setBounds(103, 270, 97, 25);
		contentPane.add(btnSpeichern);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportAssist.this.setVisible(false);
			}
		});
		btnAbbrechen.setBounds(205, 270, 97, 25);
		contentPane.add(btnAbbrechen);
		
		JButton btnBrowse = new JButton("...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				JFileChooser dirDialog = new JFileChooser();
				dirDialog.setAcceptAllFileFilterUsed(false);
				dirDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
				dirDialog.addChoosableFileFilter(new FileNameExtensionFilter("TSP-Datei", "TSP"));
				dirDialog.setDialogTitle("TSP Instanz speichern...");
				dirDialog.setSelectedFile(new File(txtName.getText()));
				
				if (dirDialog.showSaveDialog(btnBrowse) == JFileChooser.APPROVE_OPTION) {
					
					export = dirDialog.getSelectedFile();
					if (!export.getAbsolutePath().endsWith(".tsp"))
						export = new File(export.getAbsolutePath() + ".tsp");
					
					txtPath.setText(export.getAbsolutePath());
					btnSpeichern.setEnabled(true);
				}
			}
		});
		btnBrowse.setBounds(346, 195, 45, 26);
		contentPane.add(btnBrowse);
		
		txtPath = new JTextField();
		txtPath.setEnabled(false);
		txtPath.setText("Speicherort...");
		txtPath.setBounds(12, 196, 335, 24);
		contentPane.add(txtPath);
		txtPath.setColumns(10);
		
		txtComment = new JTextPane();
		txtComment.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		txtComment.setBounds(12, 133, 379, 50);
		contentPane.add(txtComment);
	}
}
