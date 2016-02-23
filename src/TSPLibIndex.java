import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;



public class TSPLibIndex extends JDialog {

	private static final long serialVersionUID = 1L;

	private SAXBuilder builder;
	private File tspIndex;
	private Document doc;
	private Element root;
	private List<Element> instIndex;
	private Element selectedInst;

	private DefaultListModel<String> listm;
	private JList<String> list;
	private JLabel lblInstInfo;
	private JButton btnImportieren;

	private gWindow owner;

	public TSPLibIndex(gWindow owner) {


		this.owner = owner;

		initializeGUI();
		readIndexToList();
		
		list.setSelectedIndex(0);
		setVisible(true);

	}

	private void readIndexToList() {

		listm = new DefaultListModel<String>();

		try {
			builder = new SAXBuilder();
			tspIndex = new File("TSPLibIndex.xml");

			doc = builder.build(tspIndex);
			root = doc.getRootElement();
			instIndex = root.getChildren("TSPInstance");

			for (Element inst : instIndex) 
				listm.addElement(inst.getAttributeValue("name"));

			list.setModel(listm);

		} catch (Exception e) { 
			e.printStackTrace(); 
			owner.logLine("TSPLibIndex.xml konnte nicht eingelesen werden. " + e.getMessage()); }
	}

	private void getTSPInfo(int index) {

		selectedInst = instIndex.get(index);

		lblInstInfo.setText(String.format("<html>"
				+ "<b>Name: </b> <span>%s</span><br><br>"
				+ "<b>Beschreibung: </b>%s<br><br>"
				+ "<b>Knoten: </b>%s<br><br>"
				+ "<b>Typ: </b>%s<br><br>"
				+ "<b>Quelle: </b>%s<br><br>"
				+ "<b>Größe (Byte): </b>%s<br><br>",
				selectedInst.getAttributeValue("name") + ".tsp",
				selectedInst.getChildText("description"),
				selectedInst.getChildText("knots"),
				selectedInst.getChildText("type"),
				selectedInst.getChildText("source"),
				selectedInst.getChildText("size")
				));
	}
	private void  loadInstance(int index) {

		try {
			URL src = new URL(selectedInst.getChildText("url"));
			owner.loadTSPFromLib(src);

		} catch (Exception e) { 
			e.printStackTrace(); 
			 }

		
	}

	private void initializeGUI() {

		setTitle("Importieren...");
		setBounds(100, 100, 571, 404);
//		setAlwaysOnTop(true);
		setLocationRelativeTo(owner.getFrame());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblImportierenAusTsplib = new JLabel("Importieren aus TSPLib");
		lblImportierenAusTsplib.setBounds(12, 13, 219, 28);
		getContentPane().add(lblImportierenAusTsplib);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBackground(Color.WHITE);
		panel.setBounds(199, 43, 342, 225);
		getContentPane().add(panel);

		lblInstInfo = new JLabel("New label");
		panel.add(lblInstInfo);
		lblInstInfo.setBackground(Color.WHITE);
		lblInstInfo.setForeground(Color.BLACK);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane);
		scrollPane.setBounds(12, 47, 175, 221);

		list = new JList<String>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) btnImportieren.doClick();
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				getTSPInfo(((JList)e.getSource()).getSelectedIndex()); 
			}
		});
		scrollPane.setViewportView(list);



		btnImportieren = new JButton("Importieren");
		btnImportieren.setBounds(105, 307, 153, 37);
		getContentPane().add(btnImportieren);
		btnImportieren.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				new AnswerWorker().execute();
				
				int knots = Integer.parseInt(selectedInst.getChildText("knots"));
				String name = selectedInst.getAttributeValue("name");
				btnImportieren.setText("Lade..");
				btnImportieren.setEnabled(false);
				
				ProgressLoader pr;
				if (knots > 400) 
					pr = new ProgressLoader((int)(knots/70.0), name, TSPLibIndex.this);
				
				
			}
		});

		JButton btnInTsplibAnzeigen = new JButton("in TSPLib anzeigen");
		btnInTsplibAnzeigen.setBounds(261, 307, 153, 37);
		getContentPane().add(btnInTsplibAnzeigen);
	}
	
	class AnswerWorker extends SwingWorker<Integer,Integer> {

		@Override
		protected Integer doInBackground() throws Exception {
			loadInstance(list.getSelectedIndex());
			return null;
		}
		@Override
		protected void done() {
			btnImportieren.setText("Importieren");
			btnImportieren.setEnabled(true);
			TSPLibIndex.this.setVisible(false);
			super.done();
		}
		
	}
	
	
}
