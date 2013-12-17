/*
 * 
 */
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

