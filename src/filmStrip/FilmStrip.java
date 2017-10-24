package filmStrip;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import flibs.fson.FSON;
import flibs.util.FMath;
import flibs.util.Loader;

/**
 * Representa una tira de fotos
 */
public class FilmStrip {
	/*---------------------------- Atributos ---------------------------------*/

	//El archivo donde esta el modelo de la tira
	private FSON model;

	//Lista de elemntos que conforman la tira
	private ArrayList<FilmStripElement> elements = new ArrayList<FilmStripElement>();

	//Tamaño en cm
	private double width, height;

	/*---------------------------- Constructores ------------------------------*/
	//Crea una tira de fotos con el modelo y las fotos dadas
	public FilmStrip(FSON model, BufferedImage[] photos) {
		this.model = model;
		this.loadElements(photos);
	}

	/*----------------------------- Funciones --------------------------------*/
	private void loadElements(BufferedImage[] photos) {

		this.width  = model.getDoubleValue("Width");
		this.height = model.getDoubleValue("Height");

		//Carga el fondo si hay
		if (model.hasKey("Background")) {
			BufferedImage img = Loader.loadBufferedImage( model.getStringValue("Background") );
			elements.add( new FilmStripElement(img, 0, 0, 100, 100) );
		}


		//Carga los elementos
		FSON components[] = model.getDirectSubElements();
		int photosUsed = 0; //Contador de fotos usadas
		for (FSON comp : components) {
			if(comp.getTag().equals("Image")) {
				double percentX = comp.getDoubleValue("x");
				double percentY = comp.getDoubleValue("y");
				double percentW = comp.getDoubleValue("width");
				double percentH = comp.getDoubleValue("height");

				//Si el elemento tiene el atributo src es que es una imagen predefinida
				BufferedImage img = null;
				if (comp.hasDirectKey("src")) {
					img = Loader.loadBufferedImage( comp.getStringValue("src") );
				} else { //Si no hay que usar una foto que se saco el usuario
					img = photos[photosUsed];
					photosUsed++;
				}

				elements.add(new FilmStripElement(img, percentX, percentY, percentW, percentH));
			}

		}
	}

	/**
	 * Crea una imagen de la tira de fotos en el tamaño dado (en pixeles)
	 */
	public BufferedImage createImage(int width, int height) {
		BufferedImage flag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = flag.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		for (FilmStripElement element : elements) {
			element.render(g, width, height);
		}

		g.dispose();
		return flag;
	}

	/**
	 * Crea una imagen del tamaño cargado del modelo con los dpi dados
	 */
	public BufferedImage createPrintableImage(int dpi) {
		int inchesW = (int) FMath.fromCMtoPixels(width, dpi);
		int inchesH = (int) FMath.fromCMtoPixels(height, dpi);
		return createImage(inchesW, inchesH);
	}


	/*------------------------- Getters y Setters ---------------------------------*/
	public ArrayList<FilmStripElement> getGobjects() {
		return elements;
	}
}
