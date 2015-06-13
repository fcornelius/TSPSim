import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ClassLoader cl;
	private InputStream in;
	private Image backimg;
	private Color back;
	private int mode;
	public static final int FIT_CENTER = 0;
	public static final int CENTER = 1;
	public static final int AS_IS = 2;
	private double scale;
	private int centerX;
	private int centerY;
	
	private File imagefile;
	
	public ImagePanel(String img) {
		this(img,ImagePanel.AS_IS,Color.white);
	}
	public ImagePanel(String img, int mode, Color back) {
		
		setImage(img);
		
		this.mode = mode;
		this.back = back;
		scale = 1;
		centerX = 0;
		centerY = 0;
		
	}
	
	public void setImage(String img) {
		
		try {
			backimg =  ImageIO.read(ImagePanel.class.getResource("/" + img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		repaint();
	}
	
	
	public void setMode(int mode) {
		this.mode = mode;
		scale = 1;
		centerX = 0;
		centerY = 0;
	}
	
	private void fitSize() {
		
		if (backimg.getWidth(null) >  backimg.getHeight(null)) {
			scale = ((double)getWidth())/backimg.getWidth(null);
		} else {
			scale = ((double)getHeight())/backimg.getHeight(null);
		}
	}
	
	private void centerImage() {
		centerX = (int)(getWidth()/2.0 - ((backimg.getWidth(null) * scale)/2));
		centerY = (int)(getHeight()/2.0 - ((backimg.getHeight(null) * scale)/2));
	}
	@Override
	protected void paintComponent(Graphics g) {
		
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		
	    
////		  g2d.setComposite(AlphaComposite.Src);
//		  g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		  g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//		  g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		    
		switch (mode) {
		case ImagePanel.FIT_CENTER:
			fitSize();
		case ImagePanel.CENTER:
			centerImage();
		case ImagePanel.AS_IS:
			g.setColor(back);
			g.fillRect(0, 0, getWidth(), getHeight());

			if (mode==ImagePanel.FIT_CENTER) 
				backimg = backimg.getScaledInstance(
						(int)(scale * backimg.getWidth(null)), 
						(int)(scale * backimg.getHeight(null)), 
						Image.SCALE_SMOOTH); 
				
			
			g.drawImage(backimg,centerX, centerY,null);
			
		}
		
	}
}
