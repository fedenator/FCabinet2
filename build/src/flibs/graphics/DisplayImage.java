package flibs.graphics;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DisplayImage {

    public static void displayAndWait(BufferedImage image) {
    	JLabel label = new JLabel(new ImageIcon(image));

        JPanel panel = new JPanel();
        panel.add(label);

        JScrollPane scrollPane = new JScrollPane(panel);
        JOptionPane.showMessageDialog(null, scrollPane);
    }
    
    public static void displayImage(Image image) {
        JLabel label = new JLabel(new ImageIcon(image));

        JPanel panel = new JPanel();
        panel.add(label);

        JScrollPane scrollPane = new JScrollPane(panel);
        JFrame jf = new JFrame();
        jf.setSize(600, 400);
        jf.setLayout(new BorderLayout());
        jf.add(scrollPane, BorderLayout.CENTER);
        jf.setVisible(true);
    }

}