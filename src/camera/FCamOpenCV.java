package camera;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import flibs.util.Loader;

/**
 * Capturador de camara que usa OpenCV y trabaja con concurrencia
 * Captura todo el tiempo la camara y va dejando la ultima captura procesada guardada para devolver
 */
public class FCamOpenCV implements Runnable, FCam {
	
	/*--------------------------- Propiedades --------------------------------------*/
	private VideoCapture camera; //Reprecenta la camara que se esta usando
	private Mat frame;           //Matriza que representa una imagen
	
	//Hilo de procesamiento en donde se va caputrando la camara
	private Thread thread = new Thread(this);
	
	//Ultima captura procesada
	private volatile BufferedImage image;
	
	/*-------------------------------- Constructores ---------------------------------*/
	/**
	 * Crea una capturador para la camara dada,
	 */
	public FCamOpenCV(int cammera, String pathLib) {
		try {
			//Obtiene la direccion del jar
			File jarDir = new File(FCamOpenCV.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
			String path = jarDir.getAbsolutePath();
			
			//Carga la libreria de OpenCV
			path += "\\" + pathLib;
			System.out.println(path);
			System.load(path);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Abre la camara
		camera = new VideoCapture(0);
		
		thread.start();
	}

	/*--------------------------------------- Funciones -----------------------------*/
	/**
	 * Devuelva la ultima captura de la camaraprocesada
	 */
	public BufferedImage getSnapShot() {
		return this.image;
	}
	
	//Devuelve una captura de la camara en tiempo real
	private BufferedImage takeSnapShot() {		
		BufferedImage flag = null;

		//Guarda una representacion de lo que ve la camara en la matriz frame
		frame = new Mat();
        camera.read(frame);

        //Imprime error si la camara no esta abierta
        if(!camera.isOpened()){
            System.out.println("Error");
        }
        //Si no esta el programa 
        else {                  
            while(true){        
                if (camera.read(frame)){
                    flag = MatToBufferedImage(frame);
                    break;
                }
            }   
        }
        
        frame.release();
        
        return flag;
	}
	
	//Crea un BufferedImage compatible con la pantalla con la imagen de la camara
    private BufferedImage MatToBufferedImage(Mat frame) {
    	//Guarda el tipo de la imagen
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        //Crea un buffered imagen del mismo tipo
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        
        //Guarda el contenido del Mat en el raster del BufferedImage
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

    //Toma una captura cada 30 milisegundos
	public void run() {
		while (true) {
			BufferedImage buffer = Loader.convertToCompatibleImage(this.takeSnapShot());
			image = buffer;
			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
