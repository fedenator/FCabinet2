package flibs.graphics.animation;

import java.util.ArrayList;

/**
 * Serie de animaciones en paralelo (al mismo tiempo)
 */
public class ParallelAnimation extends Animation{
	
	/*-------------------------- Propiedades -----------------------------*/
	//Animaciones a reproducir
	private ArrayList<Animation> animations = new ArrayList<Animation>();
	
	//Contador de las animaciones finalizadas
	private int animationsFinished = 0;
	
	/*-------------------------- Constructores ---------------------------*/
	/**
	 * Serie de animaciones en paralelo (al mismo tiempo)
	 */
	public ParallelAnimation(Animation... animations) {
		for (Animation animation :  animations)	this.animations.add(animation);
	}
	
	/*----------------------------- Funciones ----------------------------*/
	/**
	 * Inicia la animacion
	 */
	public void start() {
		if (!isRunning()) {
			super.start();
			animationsFinished = 0;
			for (Animation animation : animations) animation.start( () -> done() );
		}
	}
	
	//Metodo de callback para ir controlando cuando terminan las animaciones
	private void done() {
		animationsFinished++;
		if ( animationsFinished == animations.size() ) finish();
	}
}
