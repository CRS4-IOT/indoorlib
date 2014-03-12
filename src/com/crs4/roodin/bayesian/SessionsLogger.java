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
package com.crs4.roodin.bayesian;

// it's the old "server2.py" file for mobile. It contains the same functions.

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * 
 * Manage sessions and cell probs
 *
 */
public class SessionsLogger {

	//HashMap accetta chiavi nulle NON SINCRONIZZATA   (pi��� veloce dovrebbe andare bene nel nostro caso)
	//HashTable non accetta chiavi nulle SINCRONIZZATA pi��� sicura per multi-threading 

	Map<String, Map<String, Object>> sessions = new HashMap<String, Map<String, Object>>();

	
	
	/**
	 * @param input JSONArray
	 * @return double[] array from JSONArray
	 */
	private double[] convertToArray(JSONArray input) {
		double[] output = new double[input.length()];

		try {
			for (int i = 0; i < input.length(); i++) {
				Double e = (Double) input.get(i);
				output[i] = e.doubleValue();
			}

		} catch (JSONException e1) {
			Log.e("ERRORE", "in convertJSONArray "+e1.getMessage());
		}
		
		return output;
	}
	
	
	
	
	/**
	 * @param input
	 * @return int[][] matrix from JSONArray 
	 */
	private int[][] convertToMatrix(JSONArray input) {
		
		try {
			int rows = input.length();
			int cols = ((JSONArray)input.get(0)).length();
			
			Log.i("barred: ",""+rows+"-"+cols);

		
			int[][] output = new int[rows][cols];

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++){
					JSONArray e = (JSONArray) input.get(i);
					Integer value = e.getInt(j);
					output[i][j] = value.intValue();
				}
			}
			return output;

		} catch (JSONException e1) {
			Log.e("ERRORE", "in convertJSONArray "+e1.getMessage());
			return null;
		}
	}
	
	
	
	// save data and create probs
	/**
	 * Create and save a new session for the user inside the building
	 * 
	 * @param sessionid the id of this session
	 * @param startpos is the initial position
	 * @param shape is the entire map shape 
	 * @param barred the shape of barreds, usually a JSONArray of row,col as barred
	 * @param ppm pixel per meter
	 * @return the sessionid
	 * @throws JSONException
	 */
	public String saveSession(String sessionid, JSONArray startpos, JSONArray shape, JSONArray barred, int ppm) throws JSONException {

		Map<String, Object> session = new HashMap<String,Object>();
		Date date = new Date();
			    
		session.put("id", sessionid);
		session.put("date_created", date.getDate());   	//TODO la data ��� diversa da python
		session.put("probs", new double[shape.getInt(0)][shape.getInt(1)]);       //numpy.zeros(shape),
		session.put("barred", convertToMatrix(barred));
		session.put("ppm", ppm);

		sessions.put(sessionid, session);
		
		System.out.println("session put: "+sessionid);


		return sessionid;
	}
	
	/**
	 * Estimate the user position inside the shape
	 * 
	 * @param sessionid is the id of the session
	 * @param heading 
	 * @param radio is the radio string obtained from the wifi spots
	 * @return estimate cell position of the user
	 */ 
	public Cell estimatePos(String sessionid, double heading, String radio){ //TODO radio ��� string????
		
		Map<String, Object> session = sessions.get(sessionid);
				
		double[][] probs = (double[][]) session.get("probs");
		int[][] barred = (int[][]) session.get("barred");
		double[][] newprobs = Prob.evalProbs(probs, barred, heading);   //static?? 
		Cell cell = Prob.estimatePos(probs);   // row, col, deviance = prob.estimate_pos(probs)
		
		session.put("probs", newprobs);   // session['probs'] = newprobs
		return cell;
		
	}
	
	/**
	 * 
	 * 
	 * @param sessionid
	 * @return current probabilities as JSONArray probs
	 */
	public JSONArray getCurrentProbs(String sessionid){
		Map<String, Object> session = sessions.get(sessionid);

		try {

			double[][] probsDouble  =  (double[][]) session.get("probs");
			
			JSONArray probsJSONArray = new JSONArray();
			
			for (int i = 0; i < probsDouble.length; i++){
				JSONArray element = new JSONArray();
				for (int j=0; j < probsDouble[0].length; j++){
						element.put(j, probsDouble[i][j]);
				}
				probsJSONArray.put(i, element);
			}
			
			return probsJSONArray;
			
		} catch (JSONException e) {
			Log.e("ERRORE", "in getCurrentProbs "+e.getMessage());

			e.printStackTrace();
			return null;
		}
		
	}
	

		

	/**
	 * Sets the current probabilities
	 * 
	 * @param sessionid
	 * @param row
	 * @param col
	 */
	public void setCurrentPos(String sessionid, int row, int col) {
		System.out.println("in SessionLogger setCurrentPos");
		System.out.println("current pos: "+row+ " - "+col);
		
		Map<String, Object> session = sessions.get(sessionid);
		double[][] probs = (double[][]) session.get("probs");	
		
		System.out.println("probs: " + probs.length + " " + probs[0].length);

		
		double[][] newprobs = new double[probs.length][probs[0].length];
				
	    newprobs[row][col] = 1;
		
	    session.put("probs",newprobs);
		
		
	}

	
	
}


