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

import java.util.List;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * 
 * A group of nodes connected by edges. 
 *
 */
public class Graph {
	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	/**
	 * @param vertexes
	 * @param edges
	 */
	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	/**
	 * @return the list of the vertexes
	 */
	public List<Vertex> getVertexes() {
		return vertexes;
	}

	/**
	 * @return the list of the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}
	
	
	
}

