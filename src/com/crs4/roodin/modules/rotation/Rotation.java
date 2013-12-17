/*
 * 
 */
package com.crs4.roodin.modules.rotation;


import java.util.ArrayList;

import com.crs4.roodin.modules.step.Step;
import com.crs4.roodin.modules.step.StepListener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Rotation implements SensorEventListener {
	private SensorManager sensorManager;
	private float[] m_lastGyros;

	private int sample;
	private int COUNT;
//	private float[] outgyroZ = new float[COUNT];
	private float BIASZ;
	private long timestamp;
	private float mRotationZ;
	private float dT;
	private float lastGyrosZ;
	private float sumSamplesZ;
	private static final float NS2S = 1.0f / 1000000000.0f;
	
	private ArrayList<RotationListener> listeners = new ArrayList<RotationListener>();
	private float mRotationZ_deg;;

	
	private static Rotation instance;

	
	/**
	 * @return
	 */
	public static synchronized Rotation getInstance() {
		if (instance == null) {
			instance = new Rotation();
			System.out.println("rotation instance");
		}
		return instance;
	}
	
	/**
	 * @param sensorService
	 */
	public void initialize(SensorManager sensorService){
		this.sensorManager = sensorService;
	    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);
		
	    System.out.println("*** Rotation Started ***");
	    initVars();
	}
	
	/**
	 * 
	 */
	private void initVars(){
		BIASZ = 0.0f;
		sample = 0;
		COUNT = 50;
		sumSamplesZ = 0;
		
	}

	 /**
	 * @param listener
	 */
	public void addListener(RotationListener listener) {
		 this.listeners.add(listener);
	 }
	 
	 /**
	 * @param listener
	 */
	public void removeListener(RotationListener listener) {
		 this.listeners.remove(listener);
	 }
	 
	/**
	 * @param deg
	 */
	private void notifyListeners(float deg) {
		
		for(RotationListener list : listeners){
			list.onNewRotationEvent(deg);
		}
	}
	
	/**
	 * 
	 */
	private Rotation() {}

	
	/**
	 * 
	 */
	public void stop(){
		System.out.println("stop rotation");
		sensorManager.unregisterListener(this);
	}	
	
	
	
	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();

		switch (type) {
			case Sensor.TYPE_GYROSCOPE:		
				//System.out.println("TYPE_GYROSCOPE");

				m_lastGyros = event.values.clone();
			    lastGyrosZ = m_lastGyros[2];
				
			    
				if (BIASZ == 0.0f){
					if (sample < COUNT){
						sumSamplesZ += lastGyrosZ;
						sample++;
					}else{
						BIASZ = sumSamplesZ/sample;
					}
			    } else { 
			    	if (BIASZ > 0.0f){
			    		if (lastGyrosZ > BIASZ ){
			    			float BIASErrorMove = -0.24f; //% d'errore in movimento rilevata nei test di calibrazione 
			    			lastGyrosZ = lastGyrosZ-(lastGyrosZ * BIASErrorMove/100); // sottraggo la % d'errore (BIASErrorMove) sui tre assi
			    		}
			   
			    		if (timestamp != 0){
			    			dT = (event.timestamp - timestamp) * NS2S; 
			    			//velocita' angolare sull'asse z * tempo = angolo di rotazione in radianti
			    			mRotationZ += lastGyrosZ * dT; 
				    		//System.out.println("rotation integrato: " + mRotationZ);
		
			    		}
			    		timestamp = event.timestamp;
			    		
			    		mRotationZ_deg = (float) Math.toDegrees(mRotationZ);
			    		notifyListeners(mRotationZ_deg);
			    	}
			    	else{
					    System.out.println("ELSE ELSE -- BIASZ: " + BIASZ);	
					    initVars();  //reset again all the gyro params
			    	}
			    }
				break;
		}
	}
	

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}	



}
