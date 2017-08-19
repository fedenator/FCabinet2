package flibs.graphics.animation;

import java.awt.EventQueue;

import flibs.nodeList.NodeList;

/**
 * Representa una cadena de animaciones que se ejecutan en orden
 */
public class AnimationChain extends Animation{
	
	/*---------------------------- Propiedades -----------------------------------*/
	//Lista de animaciones a reproducir
	private NodeList<Animation> animations = new NodeList<Animation>();
	
	//Contador de en que animacion estamos
	private int counter = 0;
	
	/*--------------------------- Constructores ----------------------------------*/
	/**
	 * Una serie de animaciones que se ejecutan en orden
	 */
	public AnimationChain(Animation... animations) {
		for (Animation animation : animations) this.animations.add(animation);
	}
	
	/*------------------------------ Funciones -----------------------------------*/
	/**
	 * Inicia las animaciones
	 */
	public void start() {
		if (!isRunning()) {
			super.start();
			counter = 0;
			EventQueue.invokeLater( () -> next() );
		}
	}
	
	//Metodo de callback para notificar cuando termina una de las animaciones
	private void next() {
		if ( counter == animations.getSize() ) finish();
		else animations.find(counter).start( () -> next() );
		counter++;
	}
	
}
