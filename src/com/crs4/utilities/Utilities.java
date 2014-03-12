/*******************************************************************************
 * Copyright 2013 CRS4
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
