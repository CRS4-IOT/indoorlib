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
