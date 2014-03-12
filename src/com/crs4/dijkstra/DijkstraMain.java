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
package com.crs4.dijkstra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * 
 * The main class for the dijkstra graph. It's an arraylist composed by jsonobject and start/stop.
 *
 */
public class DijkstraMain  {
	
    private List<Vertex> nodes;
	private List<Edge> edges;
	
	
	/**
	 * Return the shortest of the jsonGraph starting from the node "startName" and ending with "endName" node
	 * 
	 * @param jsonGraph the graph in json format.
	 * @param startName is the start node
	 * @param endName is the final node
	 * @return the shortest path in json format
	 */
	public ArrayList<String> getDijkstraPath(JSONObject jsonGraph, String startName, String endName){
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	    Vertex start = null;
	    Vertex end = null;
	    
	    ArrayList<String> resultPath = new ArrayList<String>();
	    
		try {
//			System.out.println("FILE IN STAMPA");
//			System.out.println(jsonGraph);
					
			Iterator iter = jsonGraph.keys();
		    while(iter.hasNext()){
		        String key = (String)iter.next();
		        Vertex location = new Vertex(key, key);
				nodes.add(location);
				
				Object values = jsonGraph.get(key);
				
				JSONObject valueObject = new JSONObject();
				JSONArray valueArray = new JSONArray();
				
				if (values instanceof JSONArray){
					valueArray = (JSONArray) values;
				}
				else{
					valueObject  = (JSONObject) values;
				}
					
		        Iterator iterValues = valueObject.keys();
		        while(iterValues.hasNext()){
		        	String keyValue = (String)iterValues.next();
		        	Vertex neightbour = new Vertex(keyValue, keyValue);
		        	int weight = valueObject.getInt(keyValue);
		        	System.out.println("key: "+key+" value: "+keyValue+ " weight: "+weight );
		        	Edge lane = new Edge("E"+location, location, neightbour, weight );
		        	edges.add(lane);
		        }
		        
		    }
		    
		    Graph graph = new Graph(nodes, edges);
			DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
			
			for (Vertex n : nodes){
				if (n.getName().compareTo(startName) == 0)
					start = n;
				else if (n.getName().compareTo(endName) == 0)
					end = n;
			}
			
			
			dijkstra.execute(start);
			LinkedList<Vertex> path = dijkstra.getPath(end);
			
			for (Vertex vertex : path) {
				System.out.println(vertex);
				resultPath.add(vertex.getName());
			}
		    
			return resultPath;
		    
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	
	/**
	 * 
	 */
	public void setStart(){
		
		
	}
	
	
	/**
	 * 
	 */
	public void setEnd(){
		
		
	}
	
    
	
	
}
