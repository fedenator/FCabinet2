package view.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import javax.print.attribute.standard.MediaSize;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import flibs.fson.FSON;
import flibs.fson.FsonFileManagement;
import flibs.graphics.Scaller;
import flibs.printer.FPrinter;
import flibs.util.Loader;
import view.components.FilmStripPreview;
import view.components.ImageDisplayer;

public class FilmStripMaker extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ImageDisplayer imageDisplayer1;
	private ImageDisplayer imageDisplayer2;
	private ImageDisplayer imageDisplayer3;
	
	private JButton btnPrint;
	private JButton btnCancel;
	
	private FilmStripPreview filmStripPreview;
	
	private BufferedImage background;
	
	private String saveFolder;
	
	private boolean save;
	private boolean print;
	
	public FilmStripMaker(BufferedImage... photos) {
		
		//Carga el archivo de configuracion
		FSON config         = FsonFileManagement.loadFsonFile("config\\Config.fson");
		FSON filmStripModel = FsonFileManagement.loadFsonFile("rsc\\filmStrip\\FilmStripModel.fson");
		
		saveFolder = config.getStringValue("carpetaDeGuardado");
		save       = config.getBooleanValue("guardar");
		print      = config.getBooleanValue("imprimir");
		
		background = Loader.loadBufferedImage( config.getStringValue("BackgroundImage") );
		
		BufferedImage buttonImage = Loader.loadBufferedImage( config.getStringValue("ButtonImage") );
		
		/*----------------- Create and show GUI -----------------*/
		setLayout(null);
		
		imageDisplayer1 = new ImageDisplayer(photos[0], 10, 10, 442, 242 );
		imageDisplayer2 = new ImageDisplayer(photos[1], 10, 262, 442, 242);
		imageDisplayer3 = new ImageDisplayer(photos[2], 10, 516, 442, 242);
		
		btnPrint  = new JButton    ("Imprimir");
		btnPrint.addActionListener ( a -> {
			if (save)  saveImages();
			if (print) print();
		});
		btnPrint.setBounds         (1206, 458, 150, 150);
		setUpButton                (btnPrint, buttonImage);
		
		btnCancel = new JButton     ("Cancelar");
		btnCancel.addActionListener ( a -> Application.getInstance().restart() );
		btnCancel.setBounds         (1206, 608, 150, 150);
		setUpButton                 (btnCancel, buttonImage);
		
		filmStripPreview = new FilmStripPreview(filmStripModel, 654, 10, 499, 748, photos);
		
		add(imageDisplayer1, imageDisplayer2, imageDisplayer3, filmStripPreview, btnPrint, btnCancel);
	}
	
	private void setUpButton(JButton jbutton, BufferedImage buttonImage) {
		jbutton.setContentAreaFilled (false);
		jbutton.setFocusPainted      (false);
		jbutton.setBorderPainted     (false);
		jbutton.setOpaque            (false);
		jbutton.setMargin            ( new Insets(0, 11, 0, 0) );
		jbutton.setIcon              ( new ImageIcon(Scaller.basicScale(buttonImage, jbutton.getWidth(), jbutton.getHeight())) );
		jbutton.setFont              ( new Font("Arial", Font.PLAIN, 30) );
		jbutton.setForeground        (Color.WHITE);
		jbutton.setHorizontalTextPosition (JButton.CENTER);
		jbutton.setVerticalTextPosition   (JButton.CENTER);
	}
	
	//Guarda las fotos en el disco duro
	private void saveImages() {
		for (int i = 0; i < filmStripPreview.getPhotos().length; i++) {
			//Genera un nombre que no tenga conflicto
			LocalDateTime date = LocalDateTime.now();			
			String name = "" + date.getDayOfMonth() + "-" +
			date.getMonth() + "-" + date.getYear() + "--" +
			date.getHour() + "-" + date.getMinute() + "-" + date.getSecond() + "-" + i + ".png";
			
			Loader.saveImage(filmStripPreview.getPhotos()[i], saveFolder+"\\"+name, "png");
			System.out.println(saveFolder+"\\"+name);
		}
	}
	
	//Imprime las fotos
	private void print() {
		FPrinter.print(filmStripPreview.getFilmStrip().createPrintableImage(FPrinter.DPI_STANDAR_PRINTER), 10, 15, MediaSize.ISO.A6);
	}
	
	private void add(JComponent... comps) {
		for (JComponent comp : comps) add(comp);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);
		
	}
}
