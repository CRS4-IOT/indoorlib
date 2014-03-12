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
package com.crs4.roodin.modules.step;


import java.util.ArrayList;

import com.crs4.roodin.modules.position.PositionListener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Step implements SensorEventListener {
	private SensorManager sensorManager;
	private float thHigh = 12f;
	private float thLow = 9.5f;
	private int count = 0;
	public boolean newStep = false;	
	private float[] m_lastAccels;
	StepListener listener;
	private boolean stepFlag = true;
	
	private static Step instance;
	
	private ArrayList<StepListener> listeners = new ArrayList<StepListener>();;

	
	/**
	 * @return
	 */
	public static synchronized Step getInstance() {
		if (instance == null) {
			instance = new Step();
		}
		return instance;
	}
	
//	private Step() {}

	
	/**
	 * @param sensorService
	 */
	public void initialize(SensorManager sensorService){
		this.sensorManager = sensorService;
		System.out.println("thHigh "+thHigh);
		System.out.println("*** Step Started ***");
	    sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);

	}
	
	// add a new listener to the class
	 /**
	 * @param listener
	 */
	public void addListener(StepListener listener) {
		 this.listeners.add(listener);
	 }
	 
	 // remove a listener from the class list
	 /**
	 * @param listener
	 */
	public void removeListener(StepListener listener) {
		 this.listeners.remove(listener);
	 }
	 
	/**
	 * 
	 */
	private void notifyListeners() {
		listeners.size();
		for(StepListener list : listeners){
			list.onNewStepEvent(count);
		}
	}
	 

	/**
	 * 
	 */

	public void stop() {
	    sensorManager.unregisterListener(this);
	}
	
	 
	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {		
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();

		switch (type) {
			case Sensor.TYPE_ACCELEROMETER:
				m_lastAccels = event.values.clone();
				estimateStepEvent(m_lastAccels);
				break;
		}
	}

//	private void estimateStepEvent(float[] m_lastAccels) {
//    	float accX = (float) (Math.round(m_lastAccels[0]*100.0)/100.0);
//    	float accY = (float) (Math.round(m_lastAccels[1]*100.0)/100.0);
//    	float accZ = (float) (Math.round(m_lastAccels[2]*100.0)/100.0);
//    	                	    	
//    	double modAcc = Math.sqrt((accX*accX)+(accY*accY)+(accZ*accZ));  //modulo acc
//    	double modAccRounded = (Math.round(modAcc*100.0)/100.0); //modulo acc arrotondato
//    	
//    	stepFlag = true; //TEMP
//    	
//    	if (stepFlag  && (Math.round(modAccRounded) > thHigh) ){
//    		count++;
//
//    		notifyListeners();
//
//    		//stepFlag = false;
//    	}
//
////    	if ( !stepFlag && modAccRounded < thLow){
////			stepFlag = true;
////		}    	
    	
    	
	
	/**
	 * @param m_lastAccels
	 */
	private void estimateStepEvent(float[] m_lastAccels){
    	float accX = (float) (Math.round(m_lastAccels[0]*100.0)/100.0);
    	float accY = (float) (Math.round(m_lastAccels[1]*100.0)/100.0);
    	float accZ = (float) (Math.round(m_lastAccels[2]*100.0)/100.0);
    	                	    	
    	double modAcc = Math.sqrt((accX*accX)+(accY*accY)+(accZ*accZ));  //modulo acc
    	double modAccRounded = (Math.round(modAcc*100.0)/100.0); //modulo acc arrotondato		
    	
    	if (stepFlag && (Math.round(modAccRounded) > thHigh) ){
    		count++;
    		notifyListeners();
			stepFlag = false;
    	}

    	if ( !stepFlag && modAccRounded < thLow){
			stepFlag = true;
		}    	
	}
	


}
