package flibs.graphics.animation;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.Timer;

/**
 * Implementacion basica de una animacion grafica (por codigo)
 */
public class CodedAnimation extends Animation{
	
	/*--------------------------- Constantes --------------------------*/
	/**
	 * Un segundo expresado en milisegundos
	 */
	public final static int SECOND = 1000;
	
	/*--------------------------- Atributos ---------------------------*/
	//Funciones que se ejecutan al iniciar
	private ArrayList<Runnable> setUps = new ArrayList<Runnable>();
	
	//Funcion que se ejecuta al finalizar
	private ArrayList<Runnable> finishers = new ArrayList<Runnable>();
	
	private Timer timer; //Temporizador del las repeticiones
	
	private int delay;         //Delay entre loop
	private int loopCount = 0; //Cuantos loop lleva
	
	private int loopCap = 0;   //Duracion de la animacion en loops
	
	/*-------------------------- Constructores ------------------------*/
	/**
	 * Animacion con los fps fijos
	 */
	public CodedAnimation(int fps) {
		delay = SECOND/fps;
	}
	
	/**
	 * Animacion con los fps fijos y duracion en milisegundos
	 */
	public CodedAnimation(int fps, long duration) {
		this(fps);
		this.loopCap = (int) (duration/delay);
	}
	
	/*----------------------------- Funciones -------------------------*/
	/**
	 * Inicia la animacion
	 */
	public void start() {
		if ( !isRunning() ) {
			super.start();
			loopCount = 0;
			
			EventQueue.invokeLater( ()-> {
				for(Runnable setUp : setUps) setUp.run();
				timer.start();
			});
		}
	}
	
	/**
	 * Finaliza la animacion
	 */
	public void finish() {
		timer.stop();
		for(Runnable finisher : finishers) finisher.run();
		super.finish();
	}
	
	/*----------------------------- Getters & Setters -----------------*/
	/**
	 * Funcion que se va a ejecutar al iniciar
	 * Puede ser null
	 */
	public void addSetUP(Runnable setUp) {
		this.setUps.add(setUp);
	}
	
	/**
	 * Funcion que se va a ejecutar cada repeticion
	 * No puede ser null
	 */
	public void setLoop(Runnable loop) {
		
		timer = new Timer(delay, e -> {
			loop.run();
			loopCount++;
			if (loopCap != 0) if (loopCount == loopCap) finish();
		});
	}
	
	/**
	 * Funcion que se va a ejecutar al finalizar
	 */
	public void addFinisher(Runnable finisher) {
		this.finishers.add(finisher);
	}
	
	/**
	 * Demora entre cada repeticion
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Cantidad de loops hasta que termine
	 */
	public int getLoopCap() {
		return loopCap;
	}
}
