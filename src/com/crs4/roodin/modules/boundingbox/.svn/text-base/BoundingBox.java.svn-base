/*
 * 
 */
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
