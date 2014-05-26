
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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Reducer {
	private ArrayList<KeyValuePair> input;
	private ArrayList<KeyValuePair> output;
	private int readIndex;
	private boolean finished;
	private int readIndex1;
	private int readIndex2;
	private File outputFile;
	private SortComparator sortComparator;
	private GroupComparator groupComparator;
	private ArrayList<KeyValuePair> stepOutput;
	private CounterMap counterMap;
	private KeyValueWriter writer;

	public Reducer() {
		input = new ArrayList<KeyValuePair>();
		output = new ArrayList<KeyValuePair>();
		stepOutput = new ArrayList<KeyValuePair>();
		readIndex = 0;
		finished = false;
		counterMap = new CounterMap();
		counterMap.getCounter("Job Counters", "Launched reduce tasks").increment();
	}

	public void setParameters(Object params) { }

	public abstract void reduce(Comparable key, Iterable<Comparable> values);
	
	public void setOutputWriter(KeyValueWriter writer) {
		this.writer = writer;
	}

	public void write(Comparable key, Comparable value) {
		KeyValuePair kv = new KeyValuePair(key, value);
		stepOutput.add(kv);
		output.add(kv);
		counterMap.getCounter("MapReduce Framework", "Reduce output records").increment();
	}

	public void appendInput(Comparable key, Comparable value) {
		input.add(new KeyValuePair(key, value));
	}

	public void sort() {
		Collections.sort(input, sortComparator);
	}

	public ArrayList<KeyValuePair> getInput() {
		return input;
	}

	public ArrayList<KeyValuePair> getOutput() {
		return output;
	}

	public KeyValuePair next() {
		if (readIndex < input.size()) {
			KeyValuePair kv = input.get(readIndex);
			counterMap.getCounter("MapReduce Framework", "Reduce input records").increment();
			readIndex += 1;
			return kv;
		}
		finished = true;
		return null;
	}

	public void step() throws Exception {
		if (finished) {
			return;
		}
		readIndex1 = readIndex;
		KeyValuePair kv = next();
		if (kv != null) {
			KeyValuePair first = kv;
			ArrayList<Comparable> values = new ArrayList<Comparable>();
			while (kv != null && groupComparator.equals(first, kv)) {
				values.add(kv.getValue());
				kv = next();
			}
			if (!finished) {
				counterMap.getCounter("MapReduce Framework", "Reduce input records").increment(-1);
				readIndex -= 1;
			}
			readIndex2 = readIndex-1;
			stepOutput.clear();
			reduce(first.getKey(), values);
		}
	}
	
	public void writeOutput() throws Exception {
		for (KeyValuePair kv : output) {
			writer.write(kv.getKey(), kv.getValue());
		}
		writer.close();
	}

	public boolean isFinished() {
		return finished;
	}

	public void run() throws Exception {
		while (!finished) {
			step();
		}
	}

	public int getReadIndex1() {
		return readIndex1;
	}

	public int getReadIndex2() {
		return readIndex2;
	}

	public void setSortComparator(SortComparator s) {
		sortComparator = s;
	}

	public void setGroupComparator(GroupComparator g) {
		groupComparator = g;
	}

	public ArrayList<KeyValuePair> getStepOutput() {
		return stepOutput;
	}
	
	public Counter getCounter(String group, String name) {
		return counterMap.getCounter(group, name);
	}
	
	public CounterMap getCounterMap() {
		return counterMap;
	}

	public String getFileName() {
		return outputFile.toString();
	}

	public void setOutputFile(File file) {
		this.outputFile = file;
	}
}
