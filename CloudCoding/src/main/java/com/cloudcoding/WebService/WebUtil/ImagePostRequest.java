package com.cloudcoding.WebService.WebUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ==================================
 * Created by michael.carr on 11/07/2014.
 * ==================================
 */
public class ImagePostRequest {

    public static boolean SendHttpImage(String urlServer, byte[] bytes) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        int serverResponseCode = 0;

        try
        {

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");

            outputStream = new DataOutputStream( connection.getOutputStream() );

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(bytes);
            byteArrayOutputStream.writeTo(outputStream);

            outputStream.flush();
            outputStream.close();

            connection.connect();

            // Responses from the server (code and message)
            serverResponseCode = connection.getResponseCode();
        }
        catch (Exception ex)
        {
            //Exception handling
        }

        if(serverResponseCode == 200){
            return true;
        }else{
            return false;
        }
    }

    public static boolean SendHttpImage(String urlServer, String pathToOurFile) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(pathToOurFile);
        preProcessBitmap(bitmap,byteArrayOutputStream);
        return SendHttpImage(urlServer,byteArrayOutputStream.toByteArray());
    }

    public static void preProcessBitmap(Bitmap bitmap,ByteArrayOutputStream byteArrayOutputStream) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
    }

}
