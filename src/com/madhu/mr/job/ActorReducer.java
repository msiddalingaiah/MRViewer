
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

import com.madhu.mr.Reducer;

public class ActorReducer extends Reducer {
	@Override
	public void reduce(Comparable key, Iterable<Comparable> values) {
		String compoundKey = (String) key;
		
		String[] fields = compoundKey.toString().split(",");
		String actor = fields[0];
		String first = null;
		String last = null;
		for (Comparable<VideoRecording> comp : values) {
			VideoRecording vr = (VideoRecording) comp;
			if (first == null) {
				first = String.format("%s %d", vr.getTitle(), vr.getYearReleased());
			}
			last = String.format("%s %d", vr.getTitle(), vr.getYearReleased());
		}
		if (first != null && !first.equals(last)) {
			write(actor, first);
			write(actor, last);
		}
	}
}
