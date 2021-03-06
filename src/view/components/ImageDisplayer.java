package view.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ImageDisplayer extends JComponent{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	public ImageDisplayer(BufferedImage image, int x, int y, int w, int h) {
		this.image = image;
		setBounds(x, y, w, h);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
	}
}
