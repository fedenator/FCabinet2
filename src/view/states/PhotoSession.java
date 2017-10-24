package view.states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;

import camera.FCam;
import camera.FCamDebug;
import camera.FCamOpenCV;

import flibs.fson.FSON;
import flibs.fson.FsonFileManagement;

import flibs.graphics.animation.AnimationChain;
import flibs.graphics.animation.CodedAnimation;
import flibs.graphics.animation.ParallelAnimation;
import flibs.graphics.animation.ScriptAnimation;

import flibs.util.ActionFactory;
import flibs.util.Loader;
import flibs.util.Sys;

import view.components.CameraPreview;
import view.components.CountdownDisplayer;
import view.components.FlashingGlassPane;
import view.components.PhotoDisplayer;

/**
 * Estado donde se sacan las fotos
 */
public class PhotoSession extends JLayeredPane {

	/*---------------------------- Constantes ----------------------------------------*/
	//Id pito corto
	private static final long serialVersionUID = -3298912502204354233L;

	/*----------------------------- Atributos ----------------------------------------*/
	private long delay;

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
		FSON config = FsonFileManagement.loadFsonFile("config/Config.fson");

		delay = (int)config.getDoubleValue("delay") * CodedAnimation.SECOND;

		String takePhotosKey = config.getStringValue("teclaSacarFotos").toUpperCase();

		//Imagen por defecto de los displayer para las fotos
		BufferedImage defaultPhotoDisplayerImg = Loader.loadBufferedImage( config.getStringValue("PhotoDisplayerDefault") );

		backgroundImage = Loader.loadBufferedImage( config.getStringValue("BackgroundImage") );

		//Carga la camara debug o la camara normal segun el archivo de configuracion
		FCam fcam = loadCamera( config.getBooleanValue("modoSinCamara") );

		/*------------------------------ Crea y agrega el GUI --------------------------------*/
		fglassPane = new FlashingGlassPane();
		app.setGlassPane(fglassPane);

		this.setLayout(null);

		ActionFactory.addActionToKeyStroke(this, "takePhotos", takePhotosKey, () -> takePhotos() );

		this.getInputMap().put(KeyStroke.getKeyStroke(takePhotosKey), "takePhotos");
		this.getActionMap().put("takePhotos", ActionFactory.basic( () -> takePhotos() ));

		photoDisplayer1 = new PhotoDisplayer(this, defaultPhotoDisplayerImg, 10, 10, 442, 242);
		photoDisplayer2 = new PhotoDisplayer(this, defaultPhotoDisplayerImg, 10, 262, 442, 242);
		photoDisplayer3 = new PhotoDisplayer(this, defaultPhotoDisplayerImg, 10, 516, 442, 242);
		cameraPreview = new CameraPreview(this, fcam, 462, 10, 894, 748);
		countdownDisplayer = new CountdownDisplayer(delay, (int)(app.getWidth()/2 - 200/2), (int)(app.getHeight()/2 - 200/2), 200, 200);

		add(photoDisplayer1, photoDisplayer2, photoDisplayer3, cameraPreview);
		add(countdownDisplayer, POPUP_LAYER);

		countdown = new CodedAnimation(60, delay);

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
				),
				new ScriptAnimation( () -> app.makeFilmStrip(photoDisplayer1.getImage(), photoDisplayer2.getImage(), photoDisplayer3.getImage()) )
				);

	}

	/*--------------------------------- Funciones ------------------------------------*/

	private FCam loadCamera(boolean cameraDebug) {
		FCam flag = null;

		int osBits = Sys.geyOSBytes();
		Sys.OS os = Sys.getOS();

		if (cameraDebug) {
			flag = new FCamDebug("rsc/Example.jpg");
		} else {
			if (os == Sys.OS.LINUX)
				flag = new FCamOpenCV(0, "lib/libopencv_java330.so");
			else
				flag = new FCamOpenCV(0, "lib/opencv_java320x"+osBits+".dll");
		}

		return flag;
	}

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
