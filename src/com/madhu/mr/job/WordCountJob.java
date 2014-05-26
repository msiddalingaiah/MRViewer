
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

public class WordCountJob extends MapReduceJob {
	private ReduceParameter params;

	public WordCountJob() {
		super("Word count");
		setMapperClass(WCMapper.class);
		setReducerClass(IntSumReducer.class);
		params = new ReduceParameter();
	}

	@Override
	public String getDescription() {
		return "Counts the number of equal words.\n" +
			"This is the classic word count job.\n" +
			"Mapper splits lines along whitespace, outputs words as keys and 1 as the value.\n" +
			"Reducer outputs each word and the sum of values.";
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
