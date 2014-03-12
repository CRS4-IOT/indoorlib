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

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class Point {
	public int x;
	public int y;
	
	public Point(){}
	
	/**
	 * @param x
	 * @param y
	 */
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
}
