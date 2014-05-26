
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

public class SearchReplaceJob extends MapReduceJob {
	private SearchReplaceParameters params;

	public SearchReplaceJob() {
		super("Search and replace");
		setMapperClass(SearchReplaceMapper.class);
		setNumReducers(0);
		params = new SearchReplaceParameters();
	}

	@Override
	public String getDescription() {
		return "Replace specified search strings. This is a map-only job.\n" +
			"Mapper outputs the replaced line as the value.";
	}

	@Override
	public Object getParameters() {
		return params;
	}
	
	@Override
	public void init() throws Exception {
		super.init();
	}
}
