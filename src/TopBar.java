import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class TopBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private ClassLoader cl;
	private InputStream in;
	private Image backimg;
	
	public TopBar(String img) {
		
		cl = Thread.currentThread().getContextClassLoader();
		in = cl.getResourceAsStream(img);
		try {
			backimg =  ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(backimg, 0, 0, null);
	}
}
