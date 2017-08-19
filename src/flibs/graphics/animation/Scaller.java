package flibs.graphics.animation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Scaller {
	
	
	public static BufferedImage resize(BufferedImage img, int targetW, int targetH) {
		BufferedImage flag = null;
		
		if (targetW != 0 && targetH != 0) {
			
			if (img.getWidth() > targetW || img.getHeight() > targetH) {
				flag = progressibeBilinear(img, targetW, targetH);
			} else if (img.getWidth() == targetW && img.getHeight() == targetH) {
				flag = img;
			} else {
				flag = basicScale(img, targetW, targetH);
			}
		}
		return flag;
	}
	
	private static BufferedImage basicScale(BufferedImage img, int targetW, int targetH) {
		BufferedImage scalledImage = new BufferedImage( targetW, targetH, img.getType() );
		Graphics2D g2d = scalledImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(img, 0, 0, targetW, targetH, null);
		g2d.dispose();
		return scalledImage;
	}
	
	private static BufferedImage progressibeBilinear(BufferedImage img, int targetW, int targetH) {
		BufferedImage scalledImage = img; //Buffer donde se guarda el escalado cuando termina cada iteracion
		BufferedImage scratch = null; //Buffer donde se va haciendo el escalado durante cada iteracion
		Graphics2D scratchG = null;
		
		int width = scalledImage.getWidth();  //Ancho actual en el proceso de escalado
		int height = scalledImage.getHeight();//Alto actual en el proceso de escalado
		
		do {
			int nextWidth  = 0; //Siguiente ancho en la iteracion
			int nextHeight = 0; //Siguiente alto en la iteracion
			
			//Si todavia falta escalar ancho y no hay que escalar a menos de la mitad lo escala de una.
			//Si es mas de la mitad que falta escalar lo escala hasta la mitad y el resto lo hace en
			//la siguiente iteracion
			if (width > targetW) {
				nextWidth = width/2;
				if (nextWidth < targetW) nextWidth = targetW;
			}
			
			//Lo mismo con el alto
			if (height > targetH) {
				nextHeight = height/2;
				if (nextHeight < targetH) nextHeight = targetH;
			}
			
			/*-------- Apartir de aca empieza lo complicado --------*/
			//Java tiene problemas liberando la memoria, por lo tanto no es buena idea
			//crear una instancia por iteracion o proceso de escalado asi que se usa una sola instancia.
			//Eso significa que no se va a poder cambiar las medidas de la imagen aunque se valla escalando.
			//Por lo tanto hay que usar siempre la misma imagen e irle dibujando arriba del dibujo viejo pero hay que recordad de donde
			//a donde estamos pintando por que no va a coincidir con la medida de el buffer.
			
			if (scratch == null) {
				//scratch lo inicializo en el loop para iniciarlo con la medida del primer escalado asi nos ahorramos
				//tener una imagen mas grande al pedo.
				scratch = new BufferedImage( nextWidth, nextHeight, img.getType() );
				scratchG = scratch.createGraphics();
				scratchG.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			}
			
			//Pintamos el escalado
			scratchG.drawImage(scalledImage, 0, 0, nextWidth, nextHeight, 0, 0, width, height, null);
			
			//Guardamos el proceso
			scalledImage = scratch;
			width  = nextWidth;
			height = nextHeight;
			
			
		} while( scalledImage.getWidth() != width || scalledImage.getHeight() != height);
		
		//Ahora si recortamos la imagen.
		if (scalledImage.getWidth() != targetW || scalledImage.getHeight() != targetH) {
			scalledImage = new BufferedImage(targetW, targetH, img.getType());
			Graphics2D g2d = scalledImage.createGraphics();
			g2d.drawImage(scratch, 0, 0, null);
		}
		
		return scalledImage;
	}
}
