package flibs.graphics.animation;

import java.awt.EventQueue;

/**
 * Es una animacion para poder introducir una sequencia de codigo en medio de una animacion mas compleja
 */
public class ScriptAnimation extends Animation {

	/*-------------------------- Atributos --------------------------*/
	private Runnable script; //Script a ejecutar
	
	/*-------------------------- Constructores ----------------------*/
	/**
	 * Animacion simple con un script
	 */
	public ScriptAnimation(Runnable script) {
		this.script = script;
	}

	/*-------------------------- Funciones --------------------------*/
	/**
	 * Inicia el script
	 */
	public void start() {
		if (!isRunning()) {
			super.start();
			
			EventQueue.invokeLater( () -> {
				script.run();
				finish();
			});
		}
	}

}
