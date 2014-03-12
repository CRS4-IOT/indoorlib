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
package com.crs4.roodin.moduletester.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crs4.roodin.moduletester.R;



/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class CheckpointActivity  extends Activity  {
	
	private String barcodeID =  null; //"4608494ebba1a81f05000001";  // C2 albe
	
	private Button cameraButton;
	private Button bluethootButton;


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);
        
        cameraButton = (Button) findViewById(R.id.buttonCamera);
        
        cameraButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scanBarcode();
				
			}
		});
        
        bluethootButton = (Button) findViewById(R.id.ButtonBluetooth);
        
        bluethootButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                startActivity(new Intent(CheckpointActivity.this, BluetoothActivity.class));
				
			}
		});

		/*  */
	}
	
	
	
	
	/// Camera part:
	
	/**
	 * 
	 */
	private void scanBarcode(){
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			showAlert("Please install 'Barcode Scanner' (Zxing) from Android Market","No Barcode Scanner found!!");
		}
	}
	
	

	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == 0 && resultCode == RESULT_OK) {
			String resulScan = intent.getStringExtra("SCAN_RESULT");
			showAlert(resulScan, "Result");
			//startComponents();

		} else if (resultCode == RESULT_CANCELED) {
			Log.w("RESULT_CANCELED","---------------------------RESULT_CANCELED-------------------------");
			showAlert("Barcode scanning canceled.", "Barcode Canceled");

		}
			
	}
	
	
	/**
	 * @param message
	 * @param title
	 */
	private void showAlert(String message, String title){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(true).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								//resumeSensors();
							}
						});
	
		AlertDialog alert = builder.create();
		alert.setTitle(title);
		alert.show();
	}	
}
