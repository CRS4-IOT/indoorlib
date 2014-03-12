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

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * 
 * The connector for nodes.
 * 
 */
public class Edge  {
	private final String id; 
	private final Vertex source;
	private final Vertex destination;
	private final int weight; 
	
	/**
	 * @param id
	 * @param source
	 * @param destination
	 * @param weight 
	 */
	public Edge(String id, Vertex source, Vertex destination, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}
	
	/**
	 * @return the edge id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return the destination of the edge
	 */
	public Vertex getDestination() {
		return destination;
	}

	/**
	 * @return the source of the edge
	 */
	public Vertex getSource() {
		return source;
	}
	
	/**
	 * @return the weight of the edge
	 */
	public int getWeight() {
		return weight;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return source + " " + destination;
	}
	
	
}
