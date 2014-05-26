
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

import java.util.HashMap;

import com.madhu.mr.SortComparator;

public class CardSortComparator extends SortComparator {
	private HashMap<String, Integer> cardMap;

	public CardSortComparator() {
		cardMap = new HashMap<String, Integer>();
		for (int i = 2; i <= 10; i++) {
			cardMap.put(Integer.toString(i), i);
		}
		cardMap.put("Jack", 11);
		cardMap.put("Queen", 12);
		cardMap.put("King", 13);
		cardMap.put("Ace", 14);
	}

	@Override
	public int compare(Comparable key1, Comparable key2) {
		String skey1 = (String) key1;
		String skey2 = (String) key2;
		int n1 = parseInt(skey1);
		int n2 = parseInt(skey2);
		return n1 - n2;
	}

	private int parseInt(String key) {
		Integer value = cardMap.get(key);
		if (value == null) {
			return 0;
		}
		return value;
	}
}
