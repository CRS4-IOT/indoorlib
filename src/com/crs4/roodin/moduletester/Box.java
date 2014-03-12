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
