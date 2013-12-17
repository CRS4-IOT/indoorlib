/*
 * 
 */
package com.crs4.roodin.moduletester;

import java.util.UUID;


/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Box {
	private Point northWest;
	private Point southEast;
	private String id;
	private boolean cursorInBox = false;
	
	/**
	 * @param nw
	 * @param se
	 */
	public Box(Point nw, Point se){
		id = UUID.randomUUID().toString();
		
		this.northWest = nw;
		this.southEast = se;
	}
	
	
	
	
	/**
	 * @return
	 */
	public Point getNorthWest() {
		return northWest;
	}
	
	/**
	 * @param northWest
	 */
	public void setNorthWest(Point northWest) {
		this.northWest = northWest;
	}
	
	/**
	 * @return
	 */
	public Point getSouthEast() {
		return southEast;
	}
	
	/**
	 * @param southEast
	 */
	public void setSouthEast(Point southEast) {
		this.southEast = southEast;
	}


	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public boolean getCursorInBox() {
		return cursorInBox ;
	}

	/**
	 * @param c
	 */
	public void setCursorInBox(Boolean c) {
		cursorInBox = c;
	}

	

}
