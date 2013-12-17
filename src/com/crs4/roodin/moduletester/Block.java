/*
 * 
 */
package com.crs4.roodin.moduletester;


import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crs4.roodin.dbManager.DataBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Block {
	
	private float cellDimension;
	private int initialRotation;
	private JSONArray barred;         //the JSONArray of the barred cells (walls, obstacles etc..)
	private JSONArray shape;
	private JSONArray startPos;
	
	private JSONObject superGraph;		//the entire graph of the venue
	private DataBaseHelper dbHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	
	
	/**
	 * @param context
	 */
	public Block(Context context){
		dbHelper = new DataBaseHelper(context);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHelper.openDataBase();
		db = dbHelper.getReadableDatabase();
		
	}
	
	
	/**
	 * @param venue_id
	 * @return
	 */
	public JSONObject getSuperGraph(String venue_id)  {
		
		
		String query = "SELECT supergraph FROM venues WHERE mongo_id=\"" + venue_id + "\";";
//		System.out.println("query: " + query.toString());
		cursor = db.rawQuery(query, new String [] {});
		cursor.moveToFirst();
		          		
		try {
			String result = new String();
			result = cursor.getString(cursor.getColumnIndex("supergraph"));
			
			result = result.replace("u'", "'");
			superGraph = new JSONObject(result);
			System.out.println(superGraph);

			
		} catch (JSONException e) {
			System.out.println("EXCEPTION IN block.getSuperGraph()... "+e);
			e.printStackTrace();
		}

		
		return superGraph;
	}
	
	/**
	 * @param superGraph
	 */
	public void setSuperGraph(JSONObject superGraph) {
		this.superGraph = superGraph;
	}
	
	
	/**
	 * @return
	 */
	public float getCellDimension() {
		return cellDimension;
	}
	
	/**
	 * @param cellDimension
	 */
	public void setCellDimension(float cellDimension) {
		this.cellDimension = cellDimension;
	}
	
	/**
	 * @return
	 */
	public JSONArray getBarred() {
		return barred;
	}
	
	/**
	 * @param barred
	 */
	public void setBarred(JSONArray barred) {
		this.barred = barred;
	}
	
	/**
	 * @return
	 */
	public JSONArray getShape() {
		return shape;
	}
	
	/**
	 * @param shape
	 */
	public void setShape(JSONArray shape) {
		this.shape = shape;
	}
	
	/**
	 * @return
	 */
	public JSONArray getStartPos() {
		return startPos;
	}
	
	/**
	 * @param startPos
	 */
	public void setStartPos(JSONArray startPos) {
		this.startPos = startPos;
	}
	
	/**
	 * @return
	 */
	public int getInitialRotation() {
		return initialRotation;
	}
	
	/**
	 * @param initialRotation
	 */
	public void setInitialRotation(int initialRotation) {
		this.initialRotation = initialRotation;
	}

}
