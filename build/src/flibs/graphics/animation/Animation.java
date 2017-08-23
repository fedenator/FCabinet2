package flibs.graphics.animation;

import java.awt.EventQueue;

/**
 * Repesenta una animacion grafica
 */
public abstract class Animation {
	
	/*-------------------------- Propiedades ----------------------------*/
	//Runnable para que se pueda seguir track cuande termine esta animacion
	private Runnable callback;
	
	//Estado de la animacion
	private boolean running = false;
	
	/*--------------------------- Funciones -----------------------------*/
	/**
	 * Se inicia la animacion
	 */
	public void start() {
		running = true;
	};
	
	/**
	 * Inicia la animacion y cuando termina ejecuta el callback
	 */
	public void start(Runnable callback) {
		this.callback = callback;
		this.start();
	}
	
	/**
	 * Termina la animacion
	 */
	public void finish() {
		if (callback != null) {
			EventQueue.invokeLater(callback);
			callback = null;
		}
		
		running = false;
	}
	
	/*-------------------------- Getters y Setters --------------------*/
	/**
	 * Devuelve el estado de la animacion
	 */
	public boolean isRunning() {
		return running;
	}
}
