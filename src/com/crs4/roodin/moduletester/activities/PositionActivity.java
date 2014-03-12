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
package com.crs4.roodin.moduletester.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crs4.roodin.modules.boundingbox.BoundingBox;
import com.crs4.roodin.modules.boundingbox.BoundingBoxListener;
import com.crs4.roodin.modules.position.Position;
import com.crs4.roodin.modules.position.PositionListener;
import com.crs4.roodin.modules.rotation.Rotation;
import com.crs4.roodin.modules.rotation.RotationListener;
import com.crs4.roodin.modules.step.Step;
import com.crs4.roodin.modules.step.StepListener;
import com.crs4.roodin.moduletester.Block;
import com.crs4.roodin.moduletester.Box;
import com.crs4.roodin.moduletester.CustomView;
import com.crs4.roodin.moduletester.MapScrollView;
import com.crs4.roodin.moduletester.Point;
import com.crs4.roodin.moduletester.R;
import com.crs4.roodin.moduletester.R.id;
import com.crs4.roodin.moduletester.R.layout;
import com.crs4.roodin.moduletester.R.menu;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class PositionActivity extends Activity implements PositionListener, BoundingBoxListener {

	private CustomView customView;
	private MapScrollView mapScrollView;
	private Block block;
	private Position position;
	private Vibrator vibrator;
	private int initialRotation;
	private String sid;
	private Rotation rotation;
	private float cellDimension; // da controllare bene su roodin app;
	private float[] currentPosCell = new float[]{0.0f, 0.0f, 0.0f};
	
	private ArrayList<Box> boxList = new ArrayList<Box>();
	private Point currentPoint;
	private BoundingBox bb;
	private float currentRotation;
	
	private Point northWest = new Point(26,0);   // to draw the box
	private Point southEast = new Point(27,27);
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		block = this.getBlockFeatures();
		cellDimension = block.getCellDimension();

		position = new Position(sensorManager, this, block);
		position.addListener(this);
		
		//rotation = Rotation.getInstance();
		//rotation.initialize((SensorManager) getSystemService(SENSOR_SERVICE));
		//rotation.addListener(this);
        

		mapScrollView = (MapScrollView) findViewById(R.id.mapScrollView);

        customView = new CustomView(getApplicationContext());
        customView.setBlock(block);
        
        
        Box box = new Box(new Point(12,12), new Point(24,24)); //NE (row,col) alto-destra  ---   SW  (row, col) basso sinistra
        Box box2 = new Box(new Point(25,25), new Point(32,32)); //NW (row,col)   ---   SE  (row, col)
        boxList.add(box);
        boxList.add(box2);
        
        bb = new BoundingBox();
        bb.addListener(this);
       
        customView.setBoxList(boxList);            
        customView.setMapRotation(block.getInitialRotation());
        
        
        float[] pxInitialPosition = new float[2];
        pxInitialPosition[0] = 750;
        pxInitialPosition[1] = 464;
        
        
		mapScrollView.setOnDrawListener(customView, 1000, 1000);
		mapScrollView.centerDisplayTo(pxInitialPosition[0], pxInitialPosition[1]); //center display every step
		customView.setPosition(pxInitialPosition[0], pxInitialPosition[1]);

        initialRotation = block.getInitialRotation();

        sid = position.initSession(block.getStartPos(), block.getShape(), block.getBarred(), 25);
        
    }
	
	@Override 
	public void onPause(){
		super.onPause();
		position.stopPositioning();
	}
	
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	public void onNewPositionEvent(float[] newPosCell) {
		
		currentRotation = newPosCell[2];   // rotation is the third value
		customView.setMapRotation(currentRotation);

    	if(currentPosCell  != newPosCell){
			
    		currentPoint = new Point((int)newPosCell[1], (int)newPosCell[0]);
    		bb.evaluateBox(boxList, currentPoint);
    		
    		float[] posXY = convertCellPosToXY(newPosCell);
			customView.setPosition(posXY[0], posXY[1]);
			mapScrollView.centerDisplayTo(posXY[0], posXY[1]); //center display every step		
	    	vibrator.vibrate(50);
			currentPosCell = newPosCell;
		}
    	
    	mapScrollView.invalidate();	

    }

//	@Override
//	public void onNewRotationEvent(float rotation) {
//		customView.setMapRotation(initialRotation + rotation);
//		mapScrollView.invalidate();	
//	}
	
	
	/**
	 * @param position
	 * @return
	 */
	public float[] convertCellPosToXY(float[] position) {
		float xy[] = new float[2];

		// float cellDimension = currentBlock.getCellDimension();
		xy[0] = (cellDimension * position[1]) + (cellDimension / 2);
		xy[1] = (cellDimension * position[0]) + (cellDimension / 2);

		return xy;
	}
	
    /**
     * @return
     */
    public Block getBlockFeatures() {
    	Block b = new Block(this.getApplicationContext());
    	try {
    		
    		//************** da sostituire con i getBarred ecc
    		
    		b.setCellDimension(17.0f);
    		b.setBarred(new JSONArray("[[0, 43], [0, 44], [0, 45], [0, 49], [0, 50], [1, 43], [1, 44], [1, 45], [1, 49], [1, 50], [2, 42], [2, 43], [2, 44], [2, 45], [2, 49], [2, 50], [3, 10], [3, 11], [3, 12], [3, 13], [3, 14], [3, 15], [3, 16], [3, 17], [3, 18], [3, 19], [3, 20], [3, 21], [3, 22], [3, 23], [3, 24], [3, 25], [3, 26], [3, 27], [3, 28], [3, 29], [3, 30], [3, 31], [3, 32], [3, 33], [3, 34], [3, 35], [3, 36], [3, 37], [3, 38], [3, 39], [3, 40], [3, 41], [3, 42], [3, 43], [3, 44], [3, 45], [3, 49], [3, 50], [4, 10], [4, 11], [4, 12], [4, 13], [4, 14], [4, 15], [4, 16], [4, 17], [4, 18], [4, 19], [4, 20], [4, 21], [4, 22], [4, 23], [4, 24], [4, 25], [4, 26], [4, 27], [4, 28], [4, 29], [4, 30], [4, 31], [4, 32], [4, 33], [4, 34], [4, 35], [4, 36], [4, 37], [4, 38], [4, 39], [4, 40], [4, 41], [4, 42], [4, 43], [4, 44], [4, 45], [4, 46], [4, 49], [4, 50], [5, 10], [5, 11], [5, 12], [5, 13], [5, 14], [5, 15], [5, 16], [5, 17], [5, 18], [5, 19], [5, 20], [5, 21], [5, 22], [5, 23], [5, 24], [5, 25], [5, 26], [5, 27], [5, 28], [5, 29], [5, 30], [5, 31], [5, 32], [5, 33], [5, 34], [5, 35], [5, 36], [5, 37], [5, 38], [5, 39], [5, 40], [5, 41], [5, 42], [5, 43], [5, 44], [5, 45], [5, 46], [5, 49], [5, 50], [5, 51], [5, 52], [6, 10], [6, 11], [6, 12], [6, 13], [6, 14], [6, 15], [6, 16], [6, 17], [6, 18], [6, 19], [6, 20], [6, 21], [6, 22], [6, 23], [6, 24], [6, 25], [6, 26], [6, 27], [6, 28], [6, 29], [6, 30], [6, 31], [6, 32], [6, 33], [6, 34], [6, 35], [6, 36], [6, 37], [6, 38], [6, 39], [6, 40], [6, 41], [6, 42], [6, 43], [6, 50], [6, 51], [6, 52], [7, 10], [7, 11], [7, 12], [7, 13], [7, 14], [7, 15], [7, 16], [7, 17], [7, 18], [7, 19], [7, 20], [7, 21], [7, 22], [7, 23], [7, 24], [7, 25], [7, 26], [7, 27], [7, 28], [7, 29], [7, 30], [7, 31], [7, 32], [7, 33], [7, 34], [7, 35], [7, 36], [7, 37], [7, 38], [7, 39], [7, 40], [7, 41], [7, 42], [7, 43], [7, 50], [7, 51], [7, 52], [8, 10], [8, 11], [8, 12], [8, 13], [8, 14], [8, 15], [8, 16], [8, 17], [8, 18], [8, 19], [8, 20], [8, 21], [8, 22], [8, 23], [8, 24], [8, 25], [8, 26], [8, 27], [8, 28], [8, 29], [8, 30], [8, 31], [8, 32], [8, 33], [8, 34], [8, 35], [8, 36], [8, 37], [8, 38], [8, 39], [8, 40], [8, 41], [8, 42], [8, 43], [8, 45], [8, 50], [8, 51], [8, 52], [9, 10], [9, 11], [9, 12], [9, 13], [9, 14], [9, 15], [9, 16], [9, 17], [9, 18], [9, 19], [9, 20], [9, 21], [9, 22], [9, 23], [9, 24], [9, 25], [9, 26], [9, 27], [9, 28], [9, 29], [9, 30], [9, 31], [9, 32], [9, 33], [9, 34], [9, 35], [9, 36], [9, 37], [9, 38], [9, 39], [9, 40], [9, 41], [9, 42], [9, 43], [9, 45], [9, 50], [9, 51], [9, 52], [10, 10], [10, 11], [10, 12], [10, 13], [10, 14], [10, 15], [10, 16], [10, 17], [10, 18], [10, 19], [10, 20], [10, 21], [10, 22], [10, 23], [10, 24], [10, 25], [10, 26], [10, 27], [10, 28], [10, 29], [10, 30], [10, 31], [10, 32], [10, 33], [10, 34], [10, 35], [10, 36], [10, 37], [10, 38], [10, 39], [10, 40], [10, 41], [10, 42], [10, 43], [10, 45], [10, 50], [10, 51], [10, 52], [11, 10], [11, 11], [11, 12], [11, 13], [11, 14], [11, 15], [11, 16], [11, 17], [11, 18], [11, 19], [11, 20], [11, 21], [11, 22], [11, 23], [11, 24], [11, 25], [11, 26], [11, 27], [11, 28], [11, 29], [11, 30], [11, 31], [11, 32], [11, 33], [11, 34], [11, 35], [11, 36], [11, 37], [11, 38], [11, 39], [11, 40], [11, 41], [11, 42], [11, 43], [11, 45], [11, 50], [11, 51], [11, 52], [12, 10], [12, 11], [12, 12], [12, 13], [12, 14], [12, 15], [12, 16], [12, 17], [12, 18], [12, 19], [12, 20], [12, 21], [12, 22], [12, 23], [12, 24], [12, 25], [12, 26], [12, 27], [12, 28], [12, 29], [12, 30], [12, 31], [12, 32], [12, 33], [12, 34], [12, 35], [12, 36], [12, 37], [12, 38], [12, 39], [12, 40], [12, 41], [12, 42], [12, 43], [12, 45], [12, 50], [12, 51], [12, 52], [13, 10], [13, 11], [13, 12], [13, 13], [13, 14], [13, 15], [13, 16], [13, 17], [13, 18], [13, 19], [13, 20], [13, 21], [13, 22], [13, 23], [13, 24], [13, 25], [13, 26], [13, 27], [13, 28], [13, 29], [13, 30], [13, 31], [13, 32], [13, 33], [13, 34], [13, 35], [13, 36], [13, 37], [13, 38], [13, 39], [13, 40], [13, 41], [13, 42], [13, 43], [13, 45], [13, 50], [13, 51], [13, 52], [14, 10], [14, 11], [14, 12], [14, 13], [14, 14], [14, 15], [14, 16], [14, 17], [14, 18], [14, 19], [14, 20], [14, 21], [14, 22], [14, 23], [14, 24], [14, 25], [14, 26], [14, 27], [14, 28], [14, 29], [14, 30], [14, 31], [14, 32], [14, 33], [14, 34], [14, 35], [14, 36], [14, 37], [14, 38], [14, 39], [14, 40], [14, 41], [14, 42], [14, 43], [14, 45], [14, 50], [14, 51], [14, 52], [15, 10], [15, 11], [15, 12], [15, 13], [15, 14], [15, 15], [15, 16], [15, 17], [15, 18], [15, 19], [15, 20], [15, 21], [15, 22], [15, 23], [15, 24], [15, 25], [15, 26], [15, 27], [15, 28], [15, 29], [15, 30], [15, 31], [15, 32], [15, 33], [15, 34], [15, 35], [15, 36], [15, 37], [15, 38], [15, 39], [15, 40], [15, 41], [15, 42], [15, 43], [15, 45], [15, 50], [15, 51], [15, 52], [16, 10], [16, 11], [16, 12], [16, 13], [16, 14], [16, 15], [16, 16], [16, 17], [16, 18], [16, 19], [16, 20], [16, 21], [16, 22], [16, 23], [16, 24], [16, 25], [16, 26], [16, 27], [16, 28], [16, 29], [16, 30], [16, 31], [16, 32], [16, 33], [16, 34], [16, 35], [16, 36], [16, 37], [16, 38], [16, 39], [16, 40], [16, 41], [16, 42], [16, 43], [16, 45], [16, 50], [16, 51], [16, 52], [17, 10], [17, 11], [17, 12], [17, 13], [17, 14], [17, 15], [17, 16], [17, 17], [17, 18], [17, 19], [17, 20], [17, 21], [17, 22], [17, 23], [17, 24], [17, 25], [17, 26], [17, 27], [17, 28], [17, 29], [17, 30], [17, 31], [17, 32], [17, 33], [17, 34], [17, 35], [17, 36], [17, 37], [17, 38], [17, 39], [17, 40], [17, 41], [17, 42], [17, 43], [17, 45], [17, 50], [17, 51], [17, 52], [18, 10], [18, 11], [18, 12], [18, 13], [18, 14], [18, 15], [18, 16], [18, 17], [18, 18], [18, 19], [18, 20], [18, 21], [18, 22], [18, 23], [18, 24], [18, 25], [18, 26], [18, 27], [18, 28], [18, 29], [18, 30], [18, 31], [18, 32], [18, 33], [18, 34], [18, 35], [18, 36], [18, 37], [18, 38], [18, 39], [18, 40], [18, 41], [18, 42], [18, 43], [18, 45], [18, 50], [18, 51], [18, 52], [19, 10], [19, 11], [19, 12], [19, 13], [19, 14], [19, 15], [19, 16], [19, 17], [19, 18], [19, 19], [19, 20], [19, 21], [19, 22], [19, 23], [19, 24], [19, 25], [19, 26], [19, 27], [19, 28], [19, 29], [19, 30], [19, 31], [19, 32], [19, 33], [19, 34], [19, 35], [19, 36], [19, 37], [19, 38], [19, 39], [19, 40], [19, 41], [19, 42], [19, 43], [19, 45], [19, 50], [19, 51], [19, 52], [20, 6], [20, 7], [20, 8], [20, 9], [20, 10], [20, 11], [20, 12], [20, 13], [20, 14], [20, 16], [20, 17], [20, 18], [20, 19], [20, 20], [20, 21], [20, 22], [20, 23], [20, 24], [20, 25], [20, 26], [20, 27], [20, 28], [20, 29], [20, 30], [20, 31], [20, 32], [20, 33], [20, 34], [20, 35], [20, 36], [20, 37], [20, 38], [20, 39], [20, 40], [20, 41], [20, 42], [20, 43], [20, 44], [20, 45], [20, 50], [20, 51], [20, 52], [21, 6], [21, 7], [21, 8], [21, 9], [21, 10], [21, 11], [21, 12], [21, 13], [21, 14], [21, 50], [21, 51], [21, 52], [22, 1], [22, 2], [22, 3], [22, 4], [22, 5], [22, 6], [22, 7], [22, 8], [22, 9], [22, 10], [22, 11], [22, 12], [22, 13], [22, 14], [22, 50], [22, 51], [22, 52], [23, 7], [23, 8], [23, 9], [23, 10], [23, 11], [23, 12], [23, 13], [23, 14], [23, 18], [23, 19], [23, 20], [23, 21], [23, 22], [23, 23], [23, 24], [23, 25], [23, 26], [23, 27], [23, 28], [23, 29], [23, 32], [23, 33], [23, 34], [23, 35], [23, 36], [23, 37], [23, 38], [23, 39], [23, 40], [23, 41], [23, 42], [23, 43], [23, 50], [23, 51], [23, 52], [24, 7], [24, 8], [24, 9], [24, 10], [24, 11], [24, 12], [24, 13], [24, 14], [24, 18], [24, 19], [24, 20], [24, 21], [24, 22], [24, 23], [24, 24], [24, 25], [24, 26], [24, 27], [24, 28], [24, 29], [24, 32], [24, 33], [24, 34], [24, 35], [24, 36], [24, 37], [24, 38], [24, 39], [24, 40], [24, 41], [24, 42], [24, 43], [24, 50], [24, 51], [24, 52], [25, 3], [25, 4], [25, 5], [25, 6], [25, 7], [25, 8], [25, 9], [25, 10], [25, 11], [25, 12], [25, 13], [25, 14], [25, 18], [25, 19], [25, 20], [25, 21], [25, 22], [25, 23], [25, 24], [25, 25], [25, 26], [25, 27], [25, 28], [25, 29], [25, 32], [25, 33], [25, 34], [25, 35], [25, 36], [25, 37], [25, 38], [25, 39], [25, 40], [25, 41], [25, 42], [25, 43], [25, 50], [25, 51], [25, 52], [26, 6], [26, 7], [26, 8], [26, 9], [26, 10], [26, 11], [26, 12], [26, 13], [26, 14], [26, 50], [26, 51], [26, 52], [27, 6], [27, 7], [27, 8], [27, 9], [27, 10], [27, 11], [27, 12], [27, 13], [27, 14], [27, 50], [27, 51], [27, 52], [28, 6], [28, 7], [28, 8], [28, 9], [28, 10], [28, 11], [28, 12], [28, 13], [28, 14], [28, 15], [28, 16], [28, 17], [28, 18], [28, 19], [28, 20], [28, 21], [28, 22], [28, 23], [28, 24], [28, 25], [28, 26], [28, 27], [28, 28], [28, 29], [28, 30], [28, 31], [28, 32], [28, 33], [28, 34], [28, 35], [28, 36], [28, 37], [28, 38], [28, 39], [28, 40], [28, 41], [28, 42], [28, 43], [28, 44], [28, 45], [28, 50], [28, 51], [28, 52], [29, 10], [29, 11], [29, 12], [29, 13], [29, 14], [29, 15], [29, 16], [29, 17], [29, 18], [29, 19], [29, 20], [29, 21], [29, 22], [29, 23], [29, 24], [29, 25], [29, 26], [29, 27], [29, 28], [29, 29], [29, 30], [29, 31], [29, 32], [29, 33], [29, 34], [29, 35], [29, 36], [29, 37], [29, 38], [29, 39], [29, 40], [29, 41], [29, 42], [29, 43], [29, 44], [29, 45], [29, 50], [29, 51], [29, 52], [30, 10], [30, 11], [30, 12], [30, 13], [30, 14], [30, 15], [30, 16], [30, 17], [30, 18], [30, 19], [30, 20], [30, 21], [30, 22], [30, 23], [30, 24], [30, 25], [30, 26], [30, 27], [30, 28], [30, 29], [30, 30], [30, 31], [30, 32], [30, 33], [30, 34], [30, 35], [30, 36], [30, 37], [30, 38], [30, 39], [30, 40], [30, 41], [30, 42], [30, 43], [30, 44], [30, 45], [30, 50], [30, 51], [30, 52], [31, 10], [31, 11], [31, 12], [31, 13], [31, 14], [31, 15], [31, 16], [31, 17], [31, 18], [31, 19], [31, 20], [31, 21], [31, 22], [31, 23], [31, 24], [31, 25], [31, 26], [31, 27], [31, 28], [31, 29], [31, 30], [31, 31], [31, 32], [31, 33], [31, 34], [31, 35], [31, 36], [31, 37], [31, 38], [31, 39], [31, 40], [31, 41], [31, 42], [31, 43], [31, 44], [31, 45], [31, 50], [31, 51], [31, 52], [32, 10], [32, 11], [32, 12], [32, 13], [32, 14], [32, 15], [32, 16], [32, 17], [32, 18], [32, 19], [32, 20], [32, 21], [32, 22], [32, 23], [32, 24], [32, 25], [32, 26], [32, 27], [32, 28], [32, 29], [32, 30], [32, 31], [32, 32], [32, 33], [32, 34], [32, 35], [32, 36], [32, 37], [32, 38], [32, 39], [32, 40], [32, 41], [32, 42], [32, 43], [32, 44], [32, 45], [32, 50], [32, 51], [32, 52], [33, 10], [33, 11], [33, 12], [33, 13], [33, 14], [33, 15], [33, 16], [33, 17], [33, 18], [33, 19], [33, 20], [33, 21], [33, 22], [33, 23], [33, 24], [33, 25], [33, 26], [33, 27], [33, 28], [33, 29], [33, 30], [33, 31], [33, 32], [33, 33], [33, 34], [33, 35], [33, 36], [33, 37], [33, 38], [33, 39], [33, 40], [33, 41], [33, 42], [33, 43], [33, 44], [33, 45], [33, 50], [33, 51], [33, 52], [34, 10], [34, 11], [34, 12], [34, 13], [34, 14], [34, 15], [34, 16], [34, 17], [34, 18], [34, 19], [34, 20], [34, 21], [34, 22], [34, 23], [34, 24], [34, 25], [34, 26], [34, 27], [34, 28], [34, 29], [34, 30], [34, 31], [34, 32], [34, 33], [34, 34], [34, 35], [34, 36], [34, 37], [34, 38], [34, 39], [34, 40], [34, 41], [34, 42], [34, 43], [34, 44], [34, 45], [34, 50], [34, 51], [34, 52], [35, 10], [35, 11], [35, 12], [35, 13], [35, 14], [35, 15], [35, 16], [35, 17], [35, 18], [35, 19], [35, 20], [35, 21], [35, 22], [35, 23], [35, 24], [35, 25], [35, 26], [35, 27], [35, 28], [35, 29], [35, 30], [35, 31], [35, 32], [35, 33], [35, 34], [35, 35], [35, 36], [35, 37], [35, 38], [35, 39], [35, 40], [35, 41], [35, 42], [35, 43], [35, 44], [35, 45], [35, 50], [35, 51], [35, 52], [36, 10], [36, 11], [36, 12], [36, 13], [36, 14], [36, 15], [36, 16], [36, 17], [36, 18], [36, 19], [36, 20], [36, 21], [36, 22], [36, 23], [36, 24], [36, 25], [36, 26], [36, 27], [36, 28], [36, 29], [36, 30], [36, 31], [36, 32], [36, 33], [36, 34], [36, 35], [36, 36], [36, 37], [36, 38], [36, 39], [36, 40], [36, 41], [36, 42], [36, 43], [36, 44], [36, 45], [36, 50], [36, 51], [36, 52], [37, 10], [37, 11], [37, 12], [37, 13], [37, 14], [37, 15], [37, 16], [37, 17], [37, 18], [37, 19], [37, 20], [37, 21], [37, 22], [37, 23], [37, 24], [37, 25], [37, 26], [37, 27], [37, 28], [37, 29], [37, 30], [37, 31], [37, 32], [37, 33], [37, 34], [37, 35], [37, 36], [37, 37], [37, 38], [37, 39], [37, 40], [37, 41], [37, 42], [37, 43], [37, 44], [37, 45], [37, 50], [37, 51], [37, 52], [38, 10], [38, 11], [38, 12], [38, 13], [38, 14], [38, 15], [38, 16], [38, 17], [38, 18], [38, 19], [38, 20], [38, 21], [38, 22], [38, 23], [38, 24], [38, 25], [38, 26], [38, 27], [38, 28], [38, 29], [38, 30], [38, 31], [38, 32], [38, 33], [38, 34], [38, 35], [38, 36], [38, 37], [38, 38], [38, 39], [38, 40], [38, 41], [38, 42], [38, 43], [38, 44], [38, 45], [38, 50], [38, 51], [38, 52], [39, 10], [39, 11], [39, 12], [39, 13], [39, 14], [39, 15], [39, 16], [39, 17], [39, 18], [39, 19], [39, 20], [39, 21], [39, 22], [39, 23], [39, 24], [39, 25], [39, 26], [39, 27], [39, 28], [39, 29], [39, 30], [39, 31], [39, 32], [39, 33], [39, 34], [39, 35], [39, 36], [39, 37], [39, 38], [39, 39], [39, 40], [39, 41], [39, 42], [39, 43], [39, 44], [39, 45], [39, 50], [39, 51], [39, 52], [40, 10], [40, 11], [40, 12], [40, 13], [40, 14], [40, 15], [40, 16], [40, 17], [40, 18], [40, 19], [40, 20], [40, 21], [40, 22], [40, 23], [40, 24], [40, 25], [40, 26], [40, 27], [40, 28], [40, 29], [40, 30], [40, 31], [40, 32], [40, 33], [40, 34], [40, 35], [40, 36], [40, 37], [40, 38], [40, 39], [40, 40], [40, 41], [40, 42], [40, 43], [40, 44], [40, 45], [40, 50], [40, 51], [40, 52], [41, 10], [41, 11], [41, 12], [41, 13], [41, 14], [41, 15], [41, 16], [41, 17], [41, 18], [41, 19], [41, 20], [41, 21], [41, 22], [41, 23], [41, 24], [41, 25], [41, 26], [41, 27], [41, 28], [41, 29], [41, 30], [41, 31], [41, 32], [41, 33], [41, 34], [41, 35], [41, 36], [41, 37], [41, 38], [41, 39], [41, 40], [41, 41], [41, 42], [41, 43], [41, 44], [41, 45], [41, 50], [41, 51], [41, 52], [42, 10], [42, 11], [42, 12], [42, 13], [42, 14], [42, 15], [42, 16], [42, 17], [42, 18], [42, 19], [42, 20], [42, 21], [42, 22], [42, 23], [42, 24], [42, 25], [42, 26], [42, 27], [42, 28], [42, 29], [42, 30], [42, 31], [42, 32], [42, 33], [42, 34], [42, 35], [42, 36], [42, 37], [42, 38], [42, 39], [42, 40], [42, 41], [42, 42], [42, 43], [42, 44], [42, 45], [42, 50], [42, 51], [42, 52], [43, 10], [43, 11], [43, 12], [43, 13], [43, 14], [43, 15], [43, 16], [43, 17], [43, 18], [43, 19], [43, 20], [43, 21], [43, 22], [43, 23], [43, 24], [43, 25], [43, 26], [43, 27], [43, 28], [43, 29], [43, 30], [43, 31], [43, 32], [43, 33], [43, 34], [43, 35], [43, 36], [43, 37], [43, 38], [43, 39], [43, 40], [43, 41], [43, 42], [43, 43], [43, 44], [43, 45], [43, 46], [43, 49], [43, 50], [43, 51], [43, 52], [44, 11], [44, 12], [44, 13], [44, 14], [44, 15], [44, 16], [44, 17], [44, 18], [44, 19], [44, 20], [44, 21], [44, 22], [44, 23], [44, 24], [44, 25], [44, 26], [44, 27], [44, 28], [44, 29], [44, 30], [44, 31], [44, 32], [44, 33], [44, 34], [44, 35], [44, 36], [44, 37], [44, 38], [44, 39], [44, 40], [44, 41], [44, 42], [44, 43], [44, 44], [44, 45], [44, 46], [44, 49], [44, 50], [44, 51], [44, 52], [45, 11], [45, 12], [45, 13], [45, 14], [45, 15], [45, 16], [45, 17], [45, 18], [45, 19], [45, 20], [45, 21], [45, 22], [45, 23], [45, 24], [45, 25], [45, 26], [45, 27], [45, 28], [45, 29], [45, 30], [45, 31], [45, 32], [45, 33], [45, 34], [45, 35], [45, 36], [45, 37], [45, 38], [45, 39], [45, 40], [45, 41], [45, 42], [45, 43], [45, 44], [45, 45], [45, 46], [45, 49], [45, 50], [45, 51], [45, 52], [20, 15]]"));
    		b.setShape(new JSONArray("[46,53]"));
    		b.setInitialRotation(90);
    		
    		
    		JSONArray coords = new JSONArray();
			
			int x = 755;
			int y = 464;
			int ppm = 25;
			
			int col = (int) java.lang.Math.round((x/ppm) / 0.7);
		    int row = (int) java.lang.Math.round((y/ppm) / 0.7);
			
			coords.put(row);
			coords.put(col);
			
 
    		b.setStartPos(coords);
    		    	
    	} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	
		return b;
	}

	@Override
	public void enteredBox() {
		
		//Toast.makeText(this, "ENTRATO", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void exitBox() {
		Toast.makeText(this, "USCITO", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void inBox(Box box) {
		if(boxList.contains(box)){
			if(box.getCursorInBox() == false){
				// do something
				Toast.makeText(this, "ENTRATO", Toast.LENGTH_SHORT).show();
				box.setCursorInBox(true);
			}
			
		} else {
			Toast.makeText(this, "USCITO", Toast.LENGTH_SHORT).show();
			box.setCursorInBox(false);

		}
	}	
}















