
/*
 * Copyright 2014 Madhu Siddalingaiah
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.madhu.mr.job;

import com.madhu.mr.MapReduceJob;

public class LineCountJob extends MapReduceJob {
	private ReduceParameter params;

	public LineCountJob() {
		super("Line count");
		setMapperClass(LCMapper.class);
		setReducerClass(IntSumReducer.class);
		params = new ReduceParameter();
	}

	@Override
	public String getDescription() {
		return "Counts the number of equal lines.\n" +
			"Mapper outputs the input line as the key, and the number 1 as the value.\n" +
			"Reducer ouputs the key the and sum of all values.";
	}

	@Override
	public Object getParameters() {
		return params;
	}
	
	@Override
	public void init() throws Exception {
		setNumReducers(params.Number_of_reducers);
		super.init();
	}
}
