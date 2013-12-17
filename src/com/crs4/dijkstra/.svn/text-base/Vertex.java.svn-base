/*
 * 
 */
package com.crs4.dijkstra;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 * It's a vertex of the dijkstra algorithm. The vertex is the node of the graph.
 * 
 */
public class Vertex {
	final private String id;
	final private String name;
	
	
	/**
	 * @param id 
	 * @param name
	 */
	public Vertex(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * @return the current vertex id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the current vertex name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
}