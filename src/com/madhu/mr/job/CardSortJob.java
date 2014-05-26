
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
import com.madhu.mr.Partitioner;
import com.madhu.mr.SortComparator;

public class CardSortJob extends MapReduceJob {
	private CardSortParameters params;

	public CardSortJob() {
		super("Sort a deck of cards");
		setMapperClass(CardMapper.class);
		params = new CardSortParameters();
	}

	@Override
	public String getDescription() {
		return "Sorts a deck of cards.\n" +
			"Mapper splits values into rank and suit.\n" +
			"If enabled, cards will be partioned by suit.\n" +
			"If enabled, cards will be sorted by rank.\n" +
			"Reducer outputs all values in order.";
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
					String suit = (String) value;
					suit = suit.toLowerCase();
					int hash = 0;
					if (suit.equals("clubs")) {
						hash = 0;
					} else if (suit.equals("diamonds")) {
						hash = 1;
					} else if (suit.equals("hearts")) {
						hash = 2;
					} else {
						hash = 3;
					}
					return hash % numReducers;
				}
			});
		} else {
			setPartitioner(new Partitioner());
		}
		if (params.Enable_sort_comparator) {
			setSortComparator(new CardSortComparator());
		} else {
			setSortComparator(new SortComparator());
		}
		super.init();
	}
}
