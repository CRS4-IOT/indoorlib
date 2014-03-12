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
package com.crs4.roodin.bayesian;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * 
 * The cell of the map. A map is composed by a grid with colums and rows.
 * A cell has a row, col index and a deviance (standard deviance about the prob of being there)
 *
 */
public class Cell {
	int row;
	int col;
	double deviance; //TODO da convertire in intero poi

	
	/**
	 * @param row
	 * @param col
	 * @param stdev
	 */
	public Cell(int row, int col, double stdev) {
		super();
		this.row = row;
		this.col = col;
		this.deviance = stdev;
	}
	
	
	/**
	 * @return row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * @param row set the row
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	/**
	 * @return col
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * @param col set the col
	 */
	public void setCol(int col) {
		this.col = col;
	}
	
	/**
	 * @return deviance
	 */
	public double getDeviance() {
		return deviance;
	}
	
	/**
	 * @param deviance set the deviance
	 */
	public void setDeviance(float deviance) {
		this.deviance = deviance;
	}
	
	

}
