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

import com.crs4.roodin.moduletester.activities.PositionActivity;
import com.crs4.roodin.moduletester.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class MainActivity extends Activity {
	
	Button testPositionButton;
	Button testStepButton;
	Button testRotationButton;
	Button testDijkstraButton;
	Button testPathButton;
	Button testCheckpointButton;

	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		testPositionButton = (Button) findViewById(R.id.buttonTestPosition);
		testStepButton = (Button) findViewById(R.id.buttonTestStep);
		testDijkstraButton = (Button) findViewById(R.id.buttonTestDijkstra);
		testRotationButton = (Button) findViewById(R.id.buttonTestRotation);
		testPathButton = (Button) findViewById(R.id.buttonTestPath);
		testCheckpointButton = (Button) findViewById(R.id.ButtonTestCheckpoint);

		// Click listeners
		testPositionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, PositionActivity.class));
			}
		});
		
		testStepButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, StepActivity.class));
			}
		});
		
		testDijkstraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, DijkstraActivity.class));
			}
		});
		
		testRotationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, RotationActivity.class));
			}
		});	
		
		
		testPathButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, PathActivity.class));
			}
		});
		
		testCheckpointButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, CheckpointActivity.class));

				
			}
		});
	
	
	}
	
}
