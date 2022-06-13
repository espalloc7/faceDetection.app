package com.faceDetection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import javax.imageio.ImageIO;

public class uploadToImgur {

    public static String upload(String clientID,String fileLoc) throws Exception {
    	BufferedImage image = null;
        
        image = ImageIO.read(new File(fileLoc));
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();
        String dataImage = encoder.encodeToString(byteImage);
    	
        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestMethod("POST");
    	conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
    	conn.connect();
    	
    	StringBuilder stb = new StringBuilder();
    	OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    	wr.write(data);
    	wr.flush();


    	BufferedReader rd = new BufferedReader(
        new InputStreamReader(conn.getInputStream()));
    	String line;
    	while ((line = rd.readLine()) != null) {
    		stb.append(line).append("\n");
    	}
    	wr.close();
    	rd.close();

    	return stb.toString();

    }
}
