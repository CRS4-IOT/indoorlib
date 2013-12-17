/*
 * 
 */
package com.crs4.roodin.pathFinder;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.crs4.dijkstra.DijkstraMain;
import com.crs4.roodin.moduletester.Block;

/*
 * Get path from dijkstra. Graph will be gathered from mongo, sql, text file or other DB sources.
 * This class call DijkstraMain(json_graph, start, end) to get the shortest path.
 * 
 * */
/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class PathFinder {
	JSONObject jsonGraph;
	DijkstraMain dijkstra = new DijkstraMain();
	private Block block;
	
	/**
	 * @param b
	 */
	public PathFinder(Block b){
		this.block = b;
		
		
	}
	
	
	/**
	 * @param venueID
	 * @param start
	 * @param end
	 * @return
	 */
	public ArrayList<String> getPath(String venueID, String start, String end){
		
		jsonGraph = block.getSuperGraph(venueID);
		return dijkstra.getDijkstraPath(jsonGraph, start, end);
		
	}
	

}








/*try {
			jsonGraph = new JSONObject("{\"BC\": {\"DB203_BC\": 1}, \"DB203_BC\": {\"B203\": 1, \"BC\": 1},\"AB\": {\"DB203_AB\": 1}, \"DB203_AB\": {\"AB\": 1, \"B203\": 1}, \"DB229\": {\"B203\": 1, \"B229\": 1}, \"DB228\": {\"B203\": 1, \"B228\": 1}, \"DB223\": {\"B203\": 1, \"B223\": 1}, \"DB222\": {\"B203\": 2, \"B222\": 3}, \"DB221\": {\"B203\": 1, \"B221\": 1}, \"DB220\": {\"B203\": 1, \"B220\": 1}, \"DB227\": {\"B227\": 1, \"B203\": 1}, \"DB226\": {\"B226\": 1, \"B203\": 1}, \"DB225\": {\"B203\": 1, \"B225\": 1}, \"DB224\": {\"B224\": 1, \"B203\": 1}, \"B203\": {\"DB203_BC\": 1, \"DB203_AB\": 1, \"DB229\": 1, \"DB228\": 1, \"DB223\": 1, \"DB222\": 1, \"DB221\": 1, \"DB220\": 1, \"DB227\": 1, \"DB226\": 1, \"DB225\": 1, \"DB224\": 1}, \"B228\": {\"DB228\": 1}, \"B229\": {\"DB229\": 1}, \"B226\": {\"DB226\": 1}, \"B227\": {\"DB227\": 1}, \"B224\": {\"DB224\": 1}, \"B225\": {\"DB225\": 1}, \"B222\": {\"DB222\": 1}, \"B223\": {\"DB223\": 1}, \"B220\": {\"DB220\": 1}, \"B221\": {\"DB221\": 1} }");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		*/