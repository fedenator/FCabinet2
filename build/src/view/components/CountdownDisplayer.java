package view.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import flibs.graphics.Util;
import flibs.graphics.animation.CodedAnimation;

public class CountdownDisplayer extends JComponent{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage textImage;
	private String        text = "";
	private long          time = 0;
	private long          duration;
	
	public CountdownDisplayer(long duration, int x, int y, int w, int h) {
		this.duration = duration;
		setBounds(x, y, w, h);
		setOpaque(false);
		setVisible(false);
	}
	
	private void changeText(String text) {
		if ( !this.text.equals(text) && !text.isEmpty()) {
			textImage = Util.createTextImagen(text, new Font("Serif", Font.BOLD, 300), Color.WHITE);
			this.text = text;
			repaint();
		}
	}
	
	public void restart() {
		text = "";
		time = 0;
		setVisible(true);
	}
	
	public void fhide() {
		setVisible(false);
	}
	
	public synchronized void addTime(long mils) {
		time += mils;
		changeText("" + (int)( (duration-time)/CodedAnimation.SECOND + 1) );
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
//		if (isVisible()) if (g.getClipBounds().intersects(getBounds()) ||g.getClipBounds().contains(getBounds())) {
			Graphics2D g2d = (Graphics2D) g.create();
			
			g2d.setColor(Color.BLACK);
		
			g2d.setComposite( AlphaComposite.SrcOver.derive(0.8f) );
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.fillOval( 0, 0, getWidth(), getHeight() );
			
			//Pinta el texto
			if ( !text.isEmpty() ){
				g2d.setComposite( AlphaComposite.SrcOver.derive(0.5f) );
				
				int offSetX = (int)(getWidth()*0.3);
				int offSetY = (int)(getHeight()*0.1);
				
				g2d.drawImage(textImage, offSetX, offSetY, getWidth()-(offSetX*2), getHeight()-(offSetY*2), null);
			}
			
//		}
	}
	
}
