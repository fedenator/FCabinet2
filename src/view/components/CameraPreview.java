package view.components;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import camera.FCam;
import view.states.PhotoSession;

/**
 * Componente que muestra lo que esta viendo la camara
 */
public class CameraPreview extends JComponent implements Runnable, MouseListener{
	
	/*--------------------------- Constantes ------------------------------*/
	//Id pija corta
	private static final long serialVersionUID = 6777059407593823792L;
	
	/*---------------------------- Atributos ------------------------------*/
	private FCam fcam;                 //Objeto que representa la camara
	private BufferedImage img;         //Ultima imagen que se saco
	private PhotoSession photoSession;
	
	/*--------------------------- Constructores ---------------------------*/
	public CameraPreview(PhotoSession photoSession, FCam fcam, int x, int y, int width, int height) {
		
		this.photoSession = photoSession;
		this.fcam = fcam;
		this.img = fcam.getSnapShot();
		
		EventQueue.invokeLater( () -> {
			this.setBounds(x, y, width, height);
		});
		
		new Thread(this).start();
		this.addMouseListener(this);
	}
	
	/*--------------------------- Funciones -------------------------------*/
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}
	
	//Controla el tiempo de las animaciones
	public void run() {
		
		while (true) {
			EventQueue.invokeLater( () -> {
				if (getWidth() > 0 && getHeight() > 0) {
					BufferedImage snapshot = fcam.getSnapShot();
					if (snapshot != null) {
						 img = snapshot;
						repaint();
					}
				}
			});
			try                 { Thread.sleep(30); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	/*----------------------------- Listeners -----------------------------*/
	public void mouseClicked(MouseEvent e) {
		if ( !e.isConsumed() && e.getSource() == this) {
			photoSession.takePhotos();
			e.consume();
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
	
	/*-------------------------- Getters & Setters --------------------------*/
	public BufferedImage getSnapShot() {
		return img;
	}
}
