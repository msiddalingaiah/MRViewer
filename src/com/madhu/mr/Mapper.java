
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

public abstract class Mapper {
	private ArrayList<KeyValuePair> input;
	private ArrayList<KeyValuePair> output;
	private ArrayList<KeyValuePair> stepOutput;
	private int readIndex;
	private ArrayList<Reducer> reducers;
	private boolean finished;
	private Partitioner partitioner;
	private CounterMap counterMap;
	private File outputFile;
	
	public Mapper() {
		input = new ArrayList<KeyValuePair>();
		output = new ArrayList<KeyValuePair>();
		stepOutput = new ArrayList<KeyValuePair>();
		readIndex = -1;
		finished = false;
		counterMap = new CounterMap();
		counterMap.getCounter("Job Counters", "Launched map tasks").increment();
	}

	public void setReducers(ArrayList<Reducer> reducers) {
		this.reducers = reducers;
	}

	public void setPartitioner(Partitioner p) {
		partitioner = p;
	}

	public void setParameters(Object params) { }

	public abstract void map(Comparable key, Comparable value);
	
	public void appendInput(Comparable key, Comparable value) {
		input.add(new KeyValuePair(key, value));
	}

	public void write(Comparable key, Comparable value) {
		if (reducers.size() > 0) {
			int partition = partitioner.getPartition(key, value, reducers.size());
			stepOutput.add(new PartitionedKeyValuePair(key, value, partition));
			reducers.get(partition).appendInput(key, value);
		} else {
			stepOutput.add(new KeyValuePair(key, value));
			KeyValuePair kv = new KeyValuePair(key, value);
			output.add(kv);
		}
		counterMap.getCounter("MapReduce Framework", "Map output records").increment();
	}

	public ArrayList<KeyValuePair> getInput() {
		return input;
	}

	public ArrayList<KeyValuePair> getStepOutput() {
		return stepOutput;
	}

	public void step() {
		readIndex += 1;
		if (readIndex < input.size()) {
			stepOutput.clear();
			KeyValuePair inputPair = input.get(readIndex);
			counterMap.getCounter("MapReduce Framework", "Map input records").increment();
			map(inputPair.getKey(), inputPair.getValue());
		} else {
			finished = true;
		}
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setInputReader(KeyValueReader reader) throws Exception {
		readIndex = -1;
		KeyValuePair kv = reader.read();
		while (kv != null) {
			input.add(kv);
			kv = reader.read();
		}
		reader.close();
	}

	public void run() {
		while (!finished) {
			step();
		}
	}

	public int getReadIndex() {
		return readIndex;
	}
	
	public Counter getCounter(String group, String name) {
		return counterMap.getCounter(group, name);
	}
	
	public CounterMap getCounterMap() {
		return counterMap;
	}

	public void writeOutput() throws Exception {
		PrintWriter p = new PrintWriter(outputFile);
		for (KeyValuePair kv : output) {
			p.printf("%s\t%s\n", kv.getKey(), kv.getValue());
		}
		p.close();
	}

	public void setOutputFile(File file) {
		this.outputFile = file;
	}
}
