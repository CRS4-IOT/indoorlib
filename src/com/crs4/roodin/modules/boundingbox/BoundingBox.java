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
package com.crs4.roodin.modules.boundingbox;

import java.util.ArrayList;
import com.crs4.roodin.moduletester.Box;
import com.crs4.roodin.moduletester.Point;


/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class BoundingBox {
	
	private static BoundingBox instance;
	private ArrayList<BoundingBoxListener> listeners = new ArrayList<BoundingBoxListener>();

	/**
	 * @return
	 */
	public static synchronized BoundingBox getInstance() {
		if (instance == null) {
			instance = new BoundingBox();
		}
		return instance;
	}

	
	 /**
	 * @param listener
	 */
	public void addListener(BoundingBoxListener listener) {
		 this.listeners.add(listener);
	 }
	 
	 /**
	 * @param listener
	 */
	public void removeListener(BoundingBoxListener listener) {
		 this.listeners.remove(listener);
	 }
	 
	/**
	 * @param boxId
	 */
	private void notifyListeners(Box boxId) {
		
		for(BoundingBoxListener list : listeners){
			list.inBox(boxId);
		}
	}
	
	/**
	 * @param boxList
	 * @param pos
	 */
	public void evaluateBox(ArrayList<Box> boxList,  Point pos) {
	
		for (Box b : boxList){
//			System.out.println("-------------------------");
//			System.out.println(b.getNorthWest().getX() + " >= " + pos.x + " <= " + b.getSouthEast().getX() );
//			System.out.println(b.getNorthWest().getY() + " >= " + pos.y + " <= " + b.getSouthEast().getY() );
			
			if (pos.x >= b.getNorthWest().getX() && pos.x <= b.getSouthEast().getX()
					&& pos.y >= b.getNorthWest().getY()
					&& pos.y <= b.getSouthEast().getY()) {
				
				notifyListeners(b);
			}
		}
	}
	
}
