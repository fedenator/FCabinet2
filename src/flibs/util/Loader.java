package flibs.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Cargador y guardador de recursos.
 */
public class Loader {

	/**
	 * Carga una imagen compatible(Optimisada para la pantalla), devuelve null si no puede cargar la imagen
	 */
	public static BufferedImage loadBufferedImage(String path) throws IOException {
	    BufferedImage image = ImageIO.read(new File(path));
	    BufferedImage convertedImage = convertToCompatibleImage(image);

		return convertedImage;
	}

	/**
	 * Guarda una imagen dada en el path dado, el path debe contener el nombre (y extencion) del archivo
	 * y el format es un string que indica el formato real (png, jpg, etc)
	 */
	public static void saveImage(BufferedImage image, String path, String format) throws IOException {
		File file = new File(path);
		file.createNewFile();
		ImageIO.write(image, format, file);
	}

	/**
	 * Carga una imagen pero no la hace compatible con la pantalla
	 */
	public static BufferedImage loadOriginalBufferImage(String path) throws IOException {
		return ImageIO.read(new File(path));
	}

	/**
	 * Devuelve una version compatible con la pantalla de la imagen dada
	 */
	public static BufferedImage convertToCompatibleImage(BufferedImage image) {
		BufferedImage convertedImage = null;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
	    GraphicsDevice gd = ge.getDefaultScreenDevice ();
	    GraphicsConfiguration gc = gd.getDefaultConfiguration ();
	    convertedImage = gc.createCompatibleImage (image.getWidth (),
	                                               image.getHeight (),
	                                               image.getTransparency () );
	    Graphics2D g2d = convertedImage.createGraphics ();
	    g2d.drawImage ( image, 0, 0, image.getWidth (), image.getHeight (), null );
	    g2d.dispose();

	    return convertedImage;
	}
}
