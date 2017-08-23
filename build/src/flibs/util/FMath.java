package flibs.util;

/**
 * Coleccion de metods y constantes matematicas utiles
 */
public class FMath {
	
	/**
	 * Devuelve cuanto es el <percent> porciento de <unit> 
	 */
	public static double getNumberFromPercent(double unit, double percent) {
		return (unit/100) * percent;
	}
	/**
	 * Devuelve cuanto es porcentaje es <number> de <unit>  
	 */
	public static double getPercentFromNumber(double unit, double number) {
		return (number*100) / unit;
	}
	
	
	/*------------------- Funciones de impresion -------------------*/
	public static double fromPixelstoInch(double pixels, int dpi) {
		return pixels / dpi;
	}
	public static double fromInchToPixels(double inch, int dpi) {
		return inch * dpi;
	}
	
	public static double fromCMtoInch(double cm) {
		return cm / 0.3937007874;
	}
	public static double fromInchToCM(double inch) {
		return inch * 2.54;
	}
	
	public static double fromPixeltoCM(int pixels, int dpi) {
        return fromCMtoInch(fromPixelstoInch(pixels, dpi));
    }
    public static double fromCMtoPixels(double cm, int dpi) {
        return fromInchToPixels(fromCMtoInch(cm), dpi);
    }
}
