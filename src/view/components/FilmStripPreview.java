package view.components;

import java.awt.Graphics;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import filmStrip.FilmStrip;

import flibs.fson.FSON;

import java.io.IOException;

public class FilmStripPreview extends JComponent {

	private static final long serialVersionUID = 1L;

	private FilmStrip filmStrip;
	private BufferedImage image;
	private BufferedImage[] photos;

	public FilmStripPreview(FSON filmStripFile, int x, int y, int w, int h, BufferedImage... photos) throws IOException{
		this.photos = photos;
		filmStrip = new FilmStrip(filmStripFile, photos);
		setBounds(x, y, w, h);
		image = filmStrip.createImage( getWidth(), getHeight() );
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);

	}

	public FilmStrip getFilmStrip() {
		return filmStrip;
	}

	public BufferedImage[] getPhotos() {
		return photos;
	}
}
