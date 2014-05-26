
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

public class GroupComparator {
	public boolean equals(Comparable key1, Comparable key2) {
		return key1.equals(key2);
	}

	public boolean equals(KeyValuePair o1, KeyValuePair o2) {
		return equals(o1.getKey(), o2.getKey());
	}
}
