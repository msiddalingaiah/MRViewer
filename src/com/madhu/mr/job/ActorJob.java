
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

import com.madhu.mr.GroupComparator;
import com.madhu.mr.MapReduceJob;
import com.madhu.mr.Partitioner;
import com.madhu.mr.SortComparator;

public class ActorJob extends MapReduceJob {
	private ActorParameters params;

	public ActorJob() {
		super("Actor job");
		params = new ActorParameters();
		setMapperClass(ActorMapper.class);
		setReducerClass(ActorReducer.class);
	}

	@Override
	public String getDescription() {
		return "Finds the first and last movie each actor has appeard in.\n" +
			"Use serialized seq_input for this Job.\n" +
			"Mapper output compound key as <actor,year released>.\n" +
			"If enabled, rows will be partioned by actor.\n" +
			"If enabled, rows will be sorted in descending order.\n" +
			"If enabled, rows will be grouped by actor.\n" +
			"Reducer select first and last movie for each actor.\n" +
			"Actors with only one movie are excluded.";
	}

	@Override
	public Object getParameters() {
		return params;
	}
	
	@Override
	public void init() throws Exception {
		setNumReducers(params.Number_of_reducers);
		if (params.Enable_partitioner) {
			setPartitioner(new Partitioner() {
				@Override
				public int getPartition(Comparable key, Comparable value, int numReducers) {
					String skey = (String) key;
					String[] fields = skey.split(",");
					int hash = fields[0].hashCode();
					if (hash < 0) {
						hash = -hash;
					}
					return hash % numReducers;
				}
			});
		} else {
			setPartitioner(new Partitioner());
		}
		if (params.Sort_descending) {
			setSortComparator(new SortComparator() {
				@Override
				public int compare(Comparable key1, Comparable key2) {
					return -super.compare(key1, key2);
				}
			});
		} else {
			setSortComparator(new SortComparator());
		}
		if (params.Enable_grouping_comparator) {
			setGroupComparator(new GroupComparator() {
				@Override
				public boolean equals(Comparable key1, Comparable key2) {
					String skey1 = (String) key1;
					String[] fields1 = skey1.split(",");
					String skey2 = (String) key2;
					String[] fields2 = skey2.split(",");
					return fields1[0].equals(fields2[0]);
				}
			});
		} else {
			setGroupComparator(new GroupComparator());
		}
		super.init();
	}
}
