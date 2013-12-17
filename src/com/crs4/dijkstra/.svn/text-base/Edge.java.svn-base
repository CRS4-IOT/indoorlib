/*
 * 
 */
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