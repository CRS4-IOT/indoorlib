/*
 * 
 */
package com.crs4.roodin.modules.position;


/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public interface PositionListener {
	
	public void onNewPositionEvent(float[] newPos);
	
	public void enteredBox();

	public void exitBox();

}

