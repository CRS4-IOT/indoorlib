/*
 * 
 */
package com.crs4.roodin.moduletester.activities;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.crs4.roodin.modules.rotation.Rotation;
import com.crs4.roodin.modules.rotation.RotationListener;
import com.crs4.roodin.modules.step.Step;
import com.crs4.roodin.modules.step.StepListener;
import com.crs4.roodin.moduletester.R;
import com.crs4.roodin.moduletester.activities.StepActivity.Stepper1;
import com.crs4.roodin.moduletester.activities.StepActivity.Stepper2;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class RotationActivity  extends Activity {

	private Rotation rotation;
	private float currentRotation;
	private TextView textViewRotationValue;
	
	private Rotater1 r1 = new Rotater1();
	private Rotater2 r2 = new Rotater2();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        
        textViewRotationValue = (TextView) findViewById(R.id.textViewRotationValue);
        startListeners();
	}
	
	
	/**
	 * 
	 */
	private void startListeners(){
        rotation = Rotation.getInstance();
        rotation.initialize((SensorManager) getSystemService(SENSOR_SERVICE));
        //rotation.addListener(this);
		System.out.println("rotation startListeners");
		
		// listeners list test
		rotation.addListener(r1); // listeners list test
		rotation.addListener(r2); // listeners list test
	}
		
	/**
	 * 
	 */
	private void stopListeners(){
		System.out.println("rotation stopListeners");
		
		rotation.removeListener(r1); // listeners list test
		rotation.removeListener(r2); // listeners list test
	}
	
	@Override
	public void onBackPressed() {
		
		//rotation.removeListener(this);
	    finish();
	    return;
	}   
	
	@Override
	public void onPause(){
		super.onPause();
		this.stopListeners();
		rotation.stop();
	}
	
	
	//****************** Listener list Test *********************//
	/**
	 * @author ICT/LBS Team - CRS4 Sardinia, Italy
	 *
	 */
	class Rotater1 implements RotationListener{

		@Override
		public void onNewRotationEvent(float rotation) {
			System.out.println("-----------------");
			System.out.println("Rotater1: "+rotation);	
			textViewRotationValue.setText(""+rotation);
		}
	}
	
	/**
	 * @author ICT/LBS Team - CRS4 Sardinia, Italy
	 *
	 */
	class Rotater2 implements RotationListener{

		@Override
		public void onNewRotationEvent(float rotation) {
			System.out.println("-----------------");
			System.out.println("Rotater2: "+rotation);					
		}
	}
}

