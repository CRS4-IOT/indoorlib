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
package com.crs4.roodin.modules.position;


import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;

import android.hardware.SensorManager;

import com.crs4.roodin.bayesian.Cell;
import com.crs4.roodin.bayesian.SessionsLogger;
import com.crs4.roodin.modules.rotation.Rotation;
import com.crs4.roodin.modules.rotation.RotationListener;
import com.crs4.roodin.modules.step.Step;
import com.crs4.roodin.modules.step.StepListener;
import com.crs4.roodin.moduletester.Block;
import com.crs4.roodin.moduletester.Box;
import com.crs4.utilities.Utilities;


/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Position implements StepListener, RotationListener{
	private String sessionId;
	private SessionsLogger sessionLogger;
//	private float cellDimension = 19; // da controllare bene su roodin app;
//	private PositionListener listener;
	private ArrayList<PositionListener> listeners = new ArrayList<PositionListener>();
	private Step step;
	private Rotation rotation;
	private boolean rotationFlag = false;
	private float currentRotation;
	private float[] positionCell = new float[]{0.0f,0.0f,0.0f};
	private float mRotationZ_deg;	
	private float initialRotation;
	private String sid;
	
	private Box box;

	private Boolean flagIntoTheBox = false;
	
	
	/**
	 * @param sensorManager
	 * @param positionListener
	 * @param block
	 */
	public Position(SensorManager sensorManager, PositionListener positionListener, Block block){
		sessionId = UUID.randomUUID().toString();;
		sessionLogger = new SessionsLogger();
		
//        block = this.getBlockFeatures();
		initialRotation = block.getInitialRotation();
        sid = initSession(block.getStartPos(), block.getShape(), block.getBarred(), 25);
        
        step = Step.getInstance();
        step.initialize(sensorManager);
        step.addListener(this);
        
        rotation = Rotation.getInstance();
        rotation.initialize(sensorManager);
        rotation.addListener(this);
        
        this.addListener(positionListener);
    }
	
	 /**
	 * @param listener
	 */
	public void addListener(PositionListener listener) {
		 this.listeners.add(listener);
	 }
	 
	 /**
	 * @param listener
	 */
	public void removeListener(PositionListener listener) {
		 this.listeners.remove(listener);
		 step.removeListener(this);
		 rotation.removeListener(this);
	 }
	 
	/**
	 * @param newPos
	 */
	private void notifyListeners(float[] newPos) {
		
		for(PositionListener list : listeners){
			list.onNewPositionEvent(newPos);
		}
	}

	
	/**
	 * @param startpos
	 * @param shape
	 * @param barred
	 * @param ppm
	 * @return
	 */
	public String initSession(JSONArray startpos, JSONArray shape, JSONArray barred, int ppm){
		try {
			sessionLogger.saveSession(sessionId, startpos, shape, barred, ppm);
			sessionLogger.setCurrentPos(sessionId, startpos.getInt(0), startpos.getInt(1));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sessionId;
	}
	
	/**
	 * @return
	 */
	public float getRotation(){
		return currentRotation;
		
	}

	/**
	 * 
	 */
	public void stopPositioning(){
        step.stop();
        rotation.stop();
	}
	
	/**
	 * @param sessId
	 * @param rotationDeg
	 * @return
	 */
	public float[] estimate(String sessId, float rotationDeg){
		
		// rotationDeg e' la somma della rotazione iniziale della mappa + la rotazione del giriscopio  o bussola 
		float piForServer = Utilities.calculatePiValueFromDegrees(-Math.round(rotationDeg));
		//try {
			Cell position = sessionLogger.estimatePos(sessId, piForServer, "");
			
			//Log.d("position from estimate: ", "" + position.getRow() + "-" + position.getCol());

			//JSONArray pos = new JSONArray();
			//pos.put(0, position.getRow());
			//pos.put(1, position.getCol());

			float[] pos = new float[3];
			pos[0] = position.getRow();
			pos[1] = position.getCol();
			
			//return convertCellPosToXY(pos);
			
			return pos;

//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}		
	}
	
	
	
//	public float[] convertCellPosToXY(JSONArray position) {
//		float xy[] = new float[3];
//
//		try {
//			//float cellDimension = currentBlock.getCellDimension();
//			xy[0] = (cellDimension  * position.getInt(1)) + (cellDimension / 2);
//			xy[1] = (cellDimension  * position.getInt(0)) + (cellDimension / 2);
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return xy;
//	}
	

	
	/**
	 * @return
	 */
	public String getSessionId(){
		return this.sessionId;
	}
	


	/* (non-Javadoc)
	 * @see com.crs4.roodin.modules.step.StepListener#onNewStepEvent(int)
	 */
	@Override
	public void onNewStepEvent(int count) {
		positionCell = estimate(sid, currentRotation);
		positionCell[2] = currentRotation;

		System.out.println("Position: "+positionCell[0]+"-"+positionCell[1]);
		
		notifyListeners(positionCell);
		
	}
	

	/* (non-Javadoc)
	 * @see com.crs4.roodin.modules.rotation.RotationListener#onNewRotationEvent(float)
	 */
	@Override
	public void onNewRotationEvent(float rotationValue) {
		currentRotation = initialRotation + rotationValue;
		positionCell[2] = currentRotation;

		notifyListeners(positionCell);
//		positionCell = estimate(sid, currentRotation);
//		positionCell[2] = currentRotation;
		
//		System.out.println("- "+rotationValue);
//		
//		if (Math.abs(rotationValue) > 20)
//			notifyListeners(positionCell);
	}
	

	
//	public void setBox(Box b){
//		box = b;
//	}
//	

//	private void evaluateBox(float[] pos) {
//		
//		//System.out.println("Valuto: "+pos[0]+ " in  "+box.getNorthWest()[0]+"-"+box.getSouthEast()[0]);
//		//System.out.println("Valuto: " + pos[1] + " in  "+ box.getNorthWest()[1] + "-" + box.getSouthEast()[1]);
//
//		if (pos[0] >= box.getNorthWest()[0] && pos[0] <= box.getSouthEast()[0]
//				&& pos[1] >= box.getNorthWest()[1]
//				&& pos[1] <= box.getSouthEast()[1]) {
//
//			if (flagIntoTheBox == false) {
//				for (PositionListener list : listeners) {
//					list.enteredBox();
//				}
//				flagIntoTheBox  = true;
//			}
//
//		} else {
//			flagIntoTheBox  = false;
//			
//		}
//		
//		
//	}





	
	
}
