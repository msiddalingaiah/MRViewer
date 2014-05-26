
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

import com.madhu.mr.Mapper;

public class SearchReplaceMapper extends Mapper {
	private String regex;
	private String replacement;

	@Override
	public void setParameters(Object params) {
		SearchReplaceParameters sp = (SearchReplaceParameters) params;
		regex = sp.Regular_expression_search;
		replacement = sp.Replace_with;
	}

	@Override
	public void map(Comparable key, Comparable value) {
		String svalue = (String) value;
		String result = svalue.replaceAll(regex, replacement);
		write(key, result);
	}
}
