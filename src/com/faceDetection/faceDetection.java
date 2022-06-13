package com.faceDetection;
//AGALAR YAZILIM
//AGALAR YAZILIM
//AGALAR YAZILIM
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
public class faceDetection {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		JFrame frame = new JFrame("faceDetection.app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Dosya Konumu: ");
        JLabel imgLabel = new JLabel("",SwingConstants.CENTER);
        JTextField fileText = new JTextField(30);
        fileText.setEnabled(false);
        
        
        JButton choose = new JButton("Dosya Seç");
        JButton detect = new JButton("Tespit Et");
        JButton search = new JButton("Ara");
        panel.add(label);
        panel.add(fileText);
        panel.add(choose);
        panel.add(detect);
        panel.add(search);
        panel.add(imgLabel);
        
        
        new FileDrop(panel, new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   
        	for( int i = 0; i < files.length; i++ )
            {   try {
                fileText.setText(files[i].getCanonicalPath());
                imgLabel.setIcon(new ImageIcon(fileText.getText()));
                }
                catch( java.io.IOException e ) {}
            }
            }
        }); 

        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(frame, "İçinde yüz bulunan bir fotoğraf seçin.", FileDialog.LOAD);
                fd.setDirectory("C:\\");
                fd.setFile("*.jpg;*.jpeg;");
                fd.setVisible(true);
                String filename = fd.getDirectory() + fd.getFile();
                
                if(filename.equals("nullnull")) {
                	
                } else if (filename != null) {
                	fileText.setText(filename);
                	imgLabel.setIcon(new ImageIcon(fileText.getText()));
                } 
            }
        });
        
        detect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String imgFile = fileText.getText();
            	Mat src = Imgcodecs.imread(imgFile);
        		String xmlFile = "xml/haarcascade_frontalface_default.xml";
        		CascadeClassifier cc = new CascadeClassifier(xmlFile);
        		MatOfRect faceDetection = new MatOfRect();
        		
        		cc.detectMultiScale(src,faceDetection);
        		
        		for(Rect rect:faceDetection.toArray()) {
        			Imgproc.rectangle(src, new Point(rect.x, rect.y),new Point(rect.x + rect.width , rect.y + rect.height), new Scalar(0,0,255),3/2);
        		}
        		
        		String output = new String(imgFile.substring(0,imgFile.indexOf(".")) + " output.jpg");
        		Imgcodecs.imwrite(output, src);
        		imgLabel.setIcon(new ImageIcon(fileText.getText().substring(0,imgFile.indexOf(".")) + " output.jpg"));
        		JOptionPane.showMessageDialog(frame, String.format("Yüz tanıma işlemi başarıyla tamamlandı. Tespit edilen yüzler %d", faceDetection.toArray().length));
            }
        });
        
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	uploadToImgur faceDetection = new uploadToImgur();
        		try {
        			String imgurUrl = new String(faceDetection.upload("efce6070269a7f1",fileText.getText()));
        			String searchUrl = new String("https://www.google.com/searchbyimage?&image_url=" + imgurUrl.substring(imgurUrl.indexOf("http"),imgurUrl.indexOf("http") + 34).replace("\\", ""));
        			Desktop.getDesktop().browse(new URI(searchUrl));
        			
        		} catch (Exception e1) {
        			// TODO Auto-generated catch block
        			JOptionPane.showMessageDialog(frame, e1 +"Arama başarısız.");
        		}
            }
        });

        
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, imgLabel);
        frame.setVisible(true);
	}
}