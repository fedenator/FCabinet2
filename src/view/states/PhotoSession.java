package view.states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import camera.FCam;
import camera.FCamDebug;
import camera.FCamOpenCV;
import flibs.fson.FSON;
import flibs.fson.FsonFileManagement;
import flibs.graphics.animation.AnimationChain;
import flibs.graphics.animation.CodedAnimation;
import flibs.graphics.animation.ParallelAnimation;
import flibs.graphics.animation.ScriptAnimation;
import flibs.util.Loader;
import view.components.CameraPreview;
import view.components.CountdownDisplayer;
import view.components.FlashingGlassPane;
import view.components.PhotoDisplayer;

/**
 * Estado donde se sacan las fotos
 */
public class PhotoSession extends JLayeredPane{
	
	/*---------------------------- Constantes ----------------------------------------*/
	//Id pito corto
	private static final long serialVersionUID = -3298912502204354233L;
	
	/*----------------------------- Atributos ----------------------------------------*/
	//Displayer para las fotos que se sacan
	private PhotoDisplayer photoDisplayer1;
	private PhotoDisplayer photoDisplayer2;
	private PhotoDisplayer photoDisplayer3;
	
	private CameraPreview cameraPreview;           //El previsualizador de la camara
	
	private CountdownDisplayer countdownDisplayer; //Muestra la cuenta regresiva para las fotos
	
	private FlashingGlassPane fglassPane;          //Panel que cubre la ventana que hace la animacion de flasheo
	
	private AnimationChain animation;              //Animaciones de cuando sacas las fotos
	private CodedAnimation countdown;              //Animacion de cuenta regresiva
	
	private BufferedImage backgroundImage;
	
	private Application app;
	
	
	/*--------------------------- Constructores --------------------------------------*/
	public PhotoSession() {
		
		//Obtiene el singleton
		app = Application.getInstance();
		
		//Carga el archivo de configuracion
		FSON photoSessionConfig = FsonFileManagement.loadFsonFile("rsc\\PhotoSessionConfig.fson");
		FSON config             = FsonFileManagement.loadFsonFile("config\\Config.fson");
		
		//Imagen por defecto de los displayer para las fotos
		BufferedImage defaultPhotoDisplayerImg = Loader.loadBufferedImage( photoSessionConfig.getStringValue("PhotoDisplayerDefault") );
		
		backgroundImage = Loader.loadBufferedImage( photoSessionConfig.getStringValue("BackgroundImage") );
		
		//Carga la camara debug o la camara normal segun el archivo de configuracion
		FCam fcam = ( config.getBooleanValue("modoSinCamara") )? new FCamDebug("rsc\\Example.jpg"): new FCamOpenCV(0, "lib\\opencv_java320.dll"); 
		
		/*------------------------------ Crea y agrega el GUI --------------------------------*/
		fglassPane = new FlashingGlassPane();
		app.setGlassPane(fglassPane);
		
		this.setLayout(null);
		
		photoDisplayer1 = new PhotoDisplayer(this, defaultPhotoDisplayerImg, 10, 10, 442, 242);
		photoDisplayer2 = new PhotoDisplayer(this, defaultPhotoDisplayerImg, 10, 262, 442, 242);
		photoDisplayer3 = new PhotoDisplayer(this, defaultPhotoDisplayerImg, 10, 516, 442, 242);
		cameraPreview = new CameraPreview(this, fcam, 462, 10, 894, 748);
		countdownDisplayer = new CountdownDisplayer(100, 100, 500, 500);
		
		add(photoDisplayer1, photoDisplayer2, photoDisplayer3, cameraPreview);
		add(countdownDisplayer, POPUP_LAYER);
		
		countdown = new CodedAnimation(60, 10000);
		
		countdown.addSetUP( () -> countdownDisplayer.restart() );
		
		countdown.setLoop( () -> countdownDisplayer.addTime(countdown.getDelay()) );
		
		countdown.addFinisher( () -> countdownDisplayer.fhide() );
		
		//Crea la animacion de sacar fotos
		animation = new AnimationChain(
				countdown,
				new ParallelAnimation( 
						new ScriptAnimation( () -> photoDisplayer1.setImage(cameraPreview.getSnapShot()) ),
						photoDisplayer1.getAnimation(),
						fglassPane.getAnimation()
				),
				countdown,
				new ParallelAnimation( 
						new ScriptAnimation( () -> photoDisplayer2.setImage(cameraPreview.getSnapShot()) ),
						photoDisplayer2.getAnimation(),
						fglassPane.getAnimation()
				),
				countdown,
				new ParallelAnimation( 
						new ScriptAnimation( () -> photoDisplayer3.setImage(cameraPreview.getSnapShot()) ),
						photoDisplayer3.getAnimation(),
						fglassPane.getAnimation()
				)
				);
	}
	
	/*--------------------------------- Funciones ------------------------------------*/
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
	//Un atajo para añadir las cosas mas simple
	private void add(JComponent... components) {
		for (JComponent c : components) {
			add( c, new Integer(DEFAULT_LAYER) );
		}
	}
	
	/**
	 * Hace flashear el glassPane
	 */
	public void takePhotos() {
		animation.start();
	}
}
