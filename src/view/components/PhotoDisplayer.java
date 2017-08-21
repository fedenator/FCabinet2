package view.components;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import flibs.graphics.Scaller;
import flibs.graphics.animation.CodedAnimation;
import view.states.PhotoSession;

/**
 * Es un componente que muestra una imagen escalada al size del componente
 */
public class PhotoDisplayer extends JComponent{
	/*---------------------------- Constantes ------------------------------------*/
	//Id pitocorto
	private static final long serialVersionUID = 3850221175976555346L;
		
	//Duracion de la animacion en milisegundos
	private static final int ANIMATION_TIME = 500;
	
	//Actualizaciones por segundo
	private static final int FPS = 60;
	
	/*----------------------------- Atributos ------------------------------------*/
	
	//Imagen original(sin escalar)
	private BufferedImage originalImage;
	
	//Imagen a mostrar
	private BufferedImage image;
	
	//Animacion de agrandarce y achicarse
	private CodedAnimation animation = new CodedAnimation(FPS, ANIMATION_TIME);
	
	//Posicion normal
	private Rectangle normalPos;
	
	//Cambio por frame a la posicion
	private double modX, modY, modW, modH;
	
	//Siguiente posicion que tiene que ir
	private double nextX, nextY, nextW, nextH;
	
	
	/*---------------------------- Constructores ---------------------------------*/
	public PhotoDisplayer(PhotoSession photoSession, BufferedImage image, int x, int y, int width, int height) {
		normalPos = new Rectangle(x, y, width, height);
		setBounds(normalPos);
		setImage(image);
		
		//Se pone arriba del todo y ocupando toda la ventana
		animation.addSetUP( () -> {
			nextX = 0;
			nextY = 0;
			nextW = photoSession.getWidth();
			nextH = photoSession.getHeight();
			
			modX = (normalPos.x - nextX) / animation.getLoopCap();
			modY = (normalPos.y - nextY) / animation.getLoopCap();
			modW = (normalPos.width  - nextW) / animation.getLoopCap();
			modH = (normalPos.height - nextH) / animation.getLoopCap();
			photoSession.moveToFront(this);
		});
		
		//Cambia el alpha
		animation.setLoop( () -> {
			nextX += modX;
			nextY += modY;
			nextW += modW;
			nextH += modH;
			
			if (nextX >= normalPos.x || nextY >= normalPos.y ||
					nextW <= normalPos.width || nextH <= normalPos.height) {
				EventQueue.invokeLater( () -> setBounds(normalPos) );
			} else {
				EventQueue.invokeLater( () -> setBounds( (int)nextX, (int)nextY, (int)nextW, (int)nextH ) );
			}
			
			repaint();
		});
	}
	
	/*----------------------------- Funciones ------------------------------------*/
	protected void paintComponent(Graphics g) {
		if (image != null) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
		}
	}
	
	public void doAnimation() {
		animation.start();
	}
	
	/*--------------------------------- Getters y Setters -------------------------*/
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		originalImage = image;
		this.image = Scaller.basicScale( originalImage, getWidth(), getHeight() );
	}
	public CodedAnimation getAnimation() {
		return animation;
	}
	
}
