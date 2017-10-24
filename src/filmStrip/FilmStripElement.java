package filmStrip;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import flibs.util.FMath;

/**
 * Reprecenta un elemento de una tira de fotos
 */
public class FilmStripElement {

	//Tamaño que tiene que ocupar el elemento en la tira (en porcentage)
	private double percentX;
	private double percentY;
	private double percentW;
	private double percentH;

	//Imagen que reprecenta al elemento
	private BufferedImage image;

	//Crea un elemento del tamaño dado (tamaño relativo)
	public FilmStripElement(BufferedImage image, double percentX, double percentY,
			double percentW, double percentH) {

		this.image = image;

		this.percentX = percentX;
		this.percentY = percentY;
		this.percentW = percentW;
		this.percentH = percentH;
	}

	//Pinta la imagen en el graphis dado segun el tamaño de la hoja (en pixeles)
	public void render(Graphics g, int maxWidth, int maxHeight) {

		//Calcula el tamaño y la pocicion en pixeles en la hoja
		int x = (int) FMath.getNumberFromPercent(maxWidth, percentX);
		int y = (int) FMath.getNumberFromPercent(maxHeight, percentY);
		int width  = (int) FMath.getNumberFromPercent(maxWidth, percentW);
		int height = (int) FMath.getNumberFromPercent(maxHeight, percentH);

		//Pinta la imagen
		g.drawImage(image, x, y, width, height, null);
	}


	/*------------------------- Getters y Setters ----------------------*/
	public void setPercentX(double percentX) {
		this.percentX = percentX;
	}
	public double getPercentX() {
		return percentX;
	}

	public void setPercentY(double percentY) {
		this.percentY = percentY;
	}
	public double getPercentY() {
		return percentY;
	}

	public void setPercentW(double percentW) {
		this.percentW = percentW;
	}
	public double getPercentW() {
		return percentW;
	}

	public void setPercentH(double percentH) {
		this.percentH = percentH;
	}
	public double getPercentH() {
		return percentH;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public BufferedImage getImage() {
		return image;
	}
}
