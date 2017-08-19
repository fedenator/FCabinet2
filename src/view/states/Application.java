package view.states;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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
			changePanel      ( new PhotoSession() );
			window.repaint();
		});
	}
	
	/*------------------------------------- Funciones ------------------------------------------*/
	private void changePanel(JComponent panel) {
		if (currentPanel != null) window.getContentPane().remove(panel);
		window.getContentPane().add( panel, BorderLayout.CENTER );
		currentPanel = panel;
		window.repaint();
	}
	
	private void setFullScreen() {
		window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		window.setUndecorated(true);
		window.setVisible(true);
	}
	
	public void setGlassPane(JComponent comp) {
		window.setGlassPane(comp);
		comp.setVisible(true);
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
	/*---------------------- Singleton ------------------*/
	
}
