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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.crs4.roodin.moduletester.Block;
import com.crs4.roodin.moduletester.R;
import com.crs4.roodin.pathFinder.PathFinder;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class PathActivity  extends Activity  {
	PathFinder path;
	TextView textViewPath;
	TextView textViewTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        
        //textViewTitle = (TextView) findViewById(R.id.textViewPathTitle);
		//textViewTitle.setText("Example graph. Result expected: \nBC, DB203_BC, B203, DB224");
		
		textViewPath = (TextView) findViewById(R.id.textViewPath);
				
		Block block = new Block(this.getApplicationContext());
      
        path = new PathFinder(block);
        
        String venueID = "460858b6bba1a82305000000";
        String start = "H204";
        String end = "C203";
        
		ArrayList<String> res = path.getPath(venueID, start, end);
		
		System.out.println(res);
		textViewPath.setText(res.toString());
		/*  */
	}
	
	
}
