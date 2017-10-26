package view.states;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Application {
	/*------------------------------------ Atributos -------------------------------------------*/
	//Ventana principal de la aplicacion
	private JFrame window;
	private JComponent currentPanel;

	//Instansia para el singleton
	private static Application instance = null;

	/*---------------------------------- Constructores -----------------------------------------*/
	private Application() {
		//Inicia la ventana y el estado inicial
		EventQueue.invokeLater( () -> {
			window = new JFrame("FCabinet 2.0");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setFullScreen();
			window.setVisible(true);
			window.setLayout ( new BorderLayout() );
			restart();
		});
	}

	/*------------------------------------- Funciones ------------------------------------------*/
	public void changePanel(JComponent panel) {
		if (currentPanel != null) window.getContentPane().remove(currentPanel);
		window.getContentPane().add( panel, BorderLayout.CENTER );
		currentPanel = panel;
		window.getContentPane().revalidate();
		window.repaint();
	}

	public void setFullScreen() {
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setVisible(true);
	}

	public void setGlassPane(JComponent comp) {
		window.setGlassPane(comp);
		comp.setVisible(true);
	}

	public void restart() {
		changePanel( new PhotoSession() );
	}

	public void makeFilmStrip(BufferedImage photo1, BufferedImage photo2, BufferedImage photo3) {
		changePanel( new FilmStripMaker(photo1, photo2, photo3) );
	}

	/*--------------------------------- Getters y Setters --------------------------------------*/
	public int getWidth() {
		return window.getWidth();
	}
	public int getHeight() {
		return window.getHeight();
	}

	/*----------------------- Singleton ------------------*/
	public static Application getInstance() {
		if (instance == null) instance = new Application();
		return instance;
	}

	public static void start() {
		getInstance();
	}
	/*---------------------- Singleton ------------------*/

}
