/*
 * 
 */
package com.crs4.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Utilities {
	
	public static String makeBase64(String message) {
		if (message != null){
			return Base64.encodeToString(message.getBytes(), Base64.URL_SAFE);
		}
		else 
			return null;
	}
	
    // method used for calculate the correct PI value for the bayesian server alghorithm
	/**
	 * @param deg
	 * @return
	 */
	public static float calculatePiValueFromDegrees(float deg){
		return roundDecimals( ((-(Math.PI/180) * deg + Math.PI/2)/Math.PI) , 3);
	}
	
	
	/**
	 * @param number
	 * @param decimals
	 * @return
	 */
	public static float roundDecimals(double number, int decimals) {
		double rounder = Math.pow(10, decimals);
	
		int ix = (int)(number * rounder); 
		return (float)(ix/rounder);
	}
	
	
	/**
	 * @param url
	 * @return
	 */
	public static Bitmap getBitMapFromURL(URL url) {
		Bitmap mBitmap = null;
		
		try {			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			conn.getContentLength();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			mBitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (Exception e) {
			Log.e("Exception in MapScrollActivity.getBitmapFromURL",""+e.getMessage());
			e.printStackTrace();
		}
		return mBitmap;
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static Bitmap getBitMapFromFile(String path) {
		File imgFile = new  File(path);
		Bitmap myBitmap = null;
		if(imgFile.exists()){

		     myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

		}

		return myBitmap;
	}
}
