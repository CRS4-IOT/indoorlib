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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The Class DijkstraAlgorithm.
 *
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * The dijkstra algorithm
 */
public class DijkstraAlgorithm {

	/** The nodes. */
	private final List<Vertex> nodes;
	
	/** The edges. */
	private final List<Edge> edges;
	
	/** The settled nodes. */
	private Set<Vertex> settledNodes;
	
	/** The un settled nodes. */
	private Set<Vertex> unSettledNodes;
	
	/** The predecessors. */
	private Map<Vertex, Vertex> predecessors;
	
	/** The distance. */
	private Map<Vertex, Integer> distance;

	
	/**
	 * Instantiates a new dijkstra algorithm.
	 *
	 * @param graph the graph
	 */
	public DijkstraAlgorithm(Graph graph) {
		// Create a copy of the array so that we can operate on this array
		this.nodes = new ArrayList<Vertex>(graph.getVertexes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	/**
	 * Execute.
	 *
	 * @param source the source
	 */
	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	/**
	 * Find the minimal distances.
	 *
	 * @param node is the vertex
	 */
	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target, getShortestDistance(node)
						+ getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	/**
	 * Gets the distance.
	 *
	 * @param node the node
	 * @param target the target
	 * @return distance between node and target
	 */
	private int getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	/**
	 * Gets the neighbors.
	 *
	 * @param node the node
	 * @return neighbors for node
	 */
	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	/**
	 * Gets the minimum.
	 *
	 * @param vertexes the vertexes
	 * @return the minimum distance from vertexes
	 */
	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	/**
	 * Checks if is settled.
	 *
	 * @param vertex the vertex
	 * @return true, if is settled
	 */
	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	/**
	 * Gets the shortest distance.
	 *
	 * @param destination the destination
	 * @return the shortest distance
	 */
	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	
	/**
	 * Returns the path from the source to the selected target and NULL if no path exists.
	 *
	 * @param target the target
	 * @return the shortest path
	 */
	public LinkedList<Vertex> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		// Check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

}
