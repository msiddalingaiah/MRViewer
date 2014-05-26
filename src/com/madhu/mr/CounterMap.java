
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

package com.madhu.mr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class CounterMap {
	private HashMap<String, HashMap<String, Counter>> counters;
	
	public CounterMap() {
		counters = new HashMap<String, HashMap<String,Counter>>();
	}
	
	public Counter getCounter(String group, String name) {
		if (!counters.containsKey(group)) {
			counters.put(group, new HashMap<String, Counter>());
		}
		HashMap<String, Counter> map = counters.get(group);
		if (!map.containsKey(name)) {
			map.put(name, new Counter());
		}
		return map.get(name);
	}

	public Set<String> getGroups() {
		return counters.keySet();
	}

	public Set<String> getNames(String group) {
		return counters.get(group).keySet();
	}

	public void addAll(CounterMap other) {
		Set<String> groups = other.getGroups();
		for (String group : groups) {
			Set<String> names = other.getNames(group);
			for (String name : names) {
				getCounter(group, name).increment(other.getCounter(group, name).getValue());
			}
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> groups = new ArrayList<String>(getGroups());
		Collections.sort(groups);
		for (String group : groups) {
			sb.append(group);
			sb.append('\n');
			ArrayList<String> names = new ArrayList<String>(getNames(group));
			Collections.sort(names);
			for (String name : names) {
				sb.append(String.format("    %s: %d\n", name, getCounter(group, name).getValue()));
			}
		}
		return sb.toString();
	}
}
