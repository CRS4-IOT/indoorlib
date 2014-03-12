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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.crs4.dijkstra.DijkstraMain;
import com.crs4.roodin.moduletester.R;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
public class DijkstraActivity  extends Activity {
	
	DijkstraMain dijkstra;
	TextView textViewDijkstraPath;
	TextView textViewTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstra);

		try { 
			JSONObject jsonGraph = new JSONObject("{\"BC\": {\"DB203_BC\": 1}, \"DB203_BC\": {\"B203\": 1, \"BC\": 1},\"AB\": {\"DB203_AB\": 1}, \"DB203_AB\": {\"AB\": 1, \"B203\": 1}, \"DB229\": {\"B203\": 1, \"B229\": 1}, \"DB228\": {\"B203\": 1, \"B228\": 1}, \"DB223\": {\"B203\": 1, \"B223\": 1}, \"DB222\": {\"B203\": 2, \"B222\": 3}, \"DB221\": {\"B203\": 1, \"B221\": 1}, \"DB220\": {\"B203\": 1, \"B220\": 1}, \"DB227\": {\"B227\": 1, \"B203\": 1}, \"DB226\": {\"B226\": 1, \"B203\": 1}, \"DB225\": {\"B203\": 1, \"B225\": 1}, \"DB224\": {\"B224\": 1, \"B203\": 1}, \"B203\": {\"DB203_BC\": 1, \"DB203_AB\": 1, \"DB229\": 1, \"DB228\": 1, \"DB223\": 1, \"DB222\": 1, \"DB221\": 1, \"DB220\": 1, \"DB227\": 1, \"DB226\": 1, \"DB225\": 1, \"DB224\": 1}, \"B228\": {\"DB228\": 1}, \"B229\": {\"DB229\": 1}, \"B226\": {\"DB226\": 1}, \"B227\": {\"DB227\": 1}, \"B224\": {\"DB224\": 1}, \"B225\": {\"DB225\": 1}, \"B222\": {\"DB222\": 1}, \"B223\": {\"DB223\": 1}, \"B220\": {\"DB220\": 1}, \"B221\": {\"DB221\": 1} }");
			dijkstra = new DijkstraMain();
			ArrayList<String> res = dijkstra.getDijkstraPath(jsonGraph, "BC", "DB224");

			textViewTitle = (TextView) findViewById(R.id.textViewTitle);
			textViewTitle.setText("Example graph. Result expected: \nBC, DB203_BC, B203, DB224");
			
			textViewDijkstraPath = (TextView) findViewById(R.id.textViewDijkstraPath);
			textViewDijkstraPath.setText(res.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
