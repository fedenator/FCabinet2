package flibs.printer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.PrintQuality;

public class FPrinter {
    
	/**
	 * Resolucion de una impresora estandar de chorro de tinta
	 */
	public static int DPI_STANDAR_PRINTER = 72;
	
    public static void print(BufferedImage image, int w, int h, MediaSize mediaSize) {

    	PrinterJob pj = PrinterJob.getPrinterJob();
//    	if (pj.printDialog()) {
    	PageFormat pf = pj.defaultPage();
    	Paper paper = pf.getPaper();
    	PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
    	printAttributes.add(PrintQuality.HIGH);
    	printAttributes.add(new MediaPrintableArea(0, 0, mediaSize.getX(MediaSize.INCH), mediaSize.getY(MediaSize.INCH), MediaSize.INCH));
    	
    	
    	
    	double width = fromCMToPPI(w);
    	double height = fromCMToPPI(h);
    	paper.setSize(width, height);
    	paper.setImageableArea(
    			fromCMToPPI(0.1),
    			fromCMToPPI(0.1),
    			width - fromCMToPPI(0.1),
    			height - fromCMToPPI(0.1));
    	pf.setOrientation(PageFormat.PORTRAIT);
    	pf.setPaper(paper);
    	PageFormat validatePage = pj.validatePage(pf);
    	
    	pj.setPrintable(new MyPrintable(image), validatePage);
    	try {
//    		new DisplayImage();
    		pj.print(printAttributes);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
//    	}
	}

//    public static void main(String[] args) {
//    	try {
//    		FPrinter.print(ImageIO.read(new File("C:\\Users\\Fede\\Desktop\\SaveDirectorie\\11-MARCH-2017--16-36-35-2.png")),
//    				10, 15, MediaSize.ISO.A6);
//    	} catch (IOException exp) {
//    		exp.printStackTrace();
//    	}
//    }

    protected static double fromPPItoCM(double dpi) {
        return dpi / 72 / 0.393700787;
    }

    protected static double fromCMToPPI(double cm) {
        return toPPI(cm * 0.393700787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    protected static String dump(Paper paper) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
                        .append("/").append(paper.getImageableX()).append("x").
                        append(paper.getImageableY()).append(" - ").append(paper
                        .getImageableWidth()).append("x").append(paper.getImageableHeight());
        return sb.toString();
    }

    protected static String dump(PageFormat pf) {
        Paper paper = pf.getPaper();
        return dump(paper);
    }

    private static class MyPrintable implements Printable {

    	private BufferedImage image;
    	
    	public MyPrintable(BufferedImage image) {
			this.image = image;
		}
    	
        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                                         int pageIndex) throws PrinterException {
            System.out.println(pageIndex);
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;
                System.out.println("[Print] " + dump(pageFormat));
                double width = pageFormat.getImageableWidth();
                double height = pageFormat.getImageableHeight();

                System.out.println("Print Size = " + fromPPItoCM(width) + "x" + fromPPItoCM(height));
                g2d.translate((int) pageFormat.getImageableX(),
                                (int) pageFormat.getImageableY());
                Image scaled = null;
                if (width > height) {
                    scaled = image.getScaledInstance((int)Math.round(width), (int)Math.round(height), Image.SCALE_SMOOTH);
                } else {
                    scaled = image.getScaledInstance((int)Math.round(width), (int)Math.round(height), Image.SCALE_SMOOTH);
                }
                g2d.drawImage(scaled, 0, 0, null);
                g2d.dispose();
                result = PAGE_EXISTS;
            }
            return result;
        }

    }

}