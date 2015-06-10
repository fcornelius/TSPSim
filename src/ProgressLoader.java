import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class ProgressLoader extends JFrame {

	private JPanel contentPane;
	private JProgressBar progressBar;
	private Timer timer;
	private static int knotCount;
	
	private TSPLibIndex owner;

	

	/**
	 * Create the frame.
	 */
	public ProgressLoader(int knots, String inst, TSPLibIndex owner) {
		
		setTitle("Importiere " + inst + "...");
		this.owner = owner;
		knotCount = knots;
		initializeGUI();
		
		setVisible(true);
		loadProgress();
		
		
	}
	
	private void loadProgress() {
		
		timer = new Timer(1, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				progressBar.setValue(progressBar.getValue()+1);
				if (progressBar.getValue() == progressBar.getMaximum()) {
					timer.stop();
					setVisible(false);
				}
				
			}
		} );
		
		timer.start();
	}
	
	private void initializeGUI() {
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exp) { /* Fallback auf Metal LAF */ }
		
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 445, 135);
		setLocationRelativeTo(owner);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(28, 41, 358, 34);
		progressBar.setMinimum(0);
		progressBar.setMaximum(knotCount-1);
		contentPane.add(progressBar);
		
		JLabel lblLeseKoordinaten = new JLabel("Lese Koordinaten...");
		lblLeseKoordinaten.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeseKoordinaten.setBounds(28, 12, 358, 24);
		contentPane.add(lblLeseKoordinaten);
	}
}
