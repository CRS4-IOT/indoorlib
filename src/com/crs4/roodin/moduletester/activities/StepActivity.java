/*
 * 
 */
package com.crs4.roodin.moduletester.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;

import com.crs4.roodin.modules.step.Step;
import com.crs4.roodin.modules.step.StepListener;
import com.crs4.roodin.moduletester.R;

/* Extends activity and manage stepListeners*/
/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class StepActivity extends Activity /*implements StepListener */{

	private Step step;
	private Vibrator vibrator;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        
        //startListener();

		
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		step = Step.getInstance();
		step.initialize(sensorManager);
		step.addListener(new Stepper1());
		step.addListener(new Stepper2());
	
	}
	
	@Override
	public void onPause(){
		super.onPause();
        step.stop();
	}
	
	@Override
	public void onBackPressed() {
		
		//step.removeListener(this);
		
	    finish();
	    return;
	}   
	
	/**
	 * @param stepListener
	 */
	protected void detachListener(StepListener stepListener){
		step.removeListener(stepListener);
	}
	
	
	/**
	 * @author ICT/LBS Team - CRS4 Sardinia, Italy
	 *
	 */
	class Stepper1 implements StepListener{
				
		@Override
		public void onNewStepEvent(int stepCount) {
			System.out.println("-----------------");
			System.out.println("Stepper1: "+stepCount);		
		}


		
	}
	
	/**
	 * @author ICT/LBS Team - CRS4 Sardinia, Italy
	 *
	 */
	class Stepper2 implements StepListener{

		@Override
		public void onNewStepEvent(int stepCount) {
			System.out.println("-----------------");
			System.out.println("Stepper2: "+stepCount);		
			
			if (stepCount == 10)
				detachListener(this);    ////////////// for testing
		}


		
	}

}
