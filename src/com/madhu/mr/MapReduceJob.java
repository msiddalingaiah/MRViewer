
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
import java.util.ArrayList;
import java.util.Set;

public abstract class MapReduceJob {
	private String name;
	private Class<? extends Mapper> mapperClass;
	private Class<? extends Reducer> reducerClass;
	private Partitioner partitioner;
	private SortComparator sortComparator;
	private GroupComparator groupComparator;
	private ArrayList<Mapper> mappers;
	private ArrayList<Reducer> reducers;
	private File inputDirectory;
	private File outputDirectory;
	private CounterMap counterMap;
	private int numReducers;
	private Class<? extends KeyValueReader> readerClass;
	private Class<? extends KeyValueWriter> writerClass;
	
	public MapReduceJob(String name) {
		this.name = name;
		partitioner = new Partitioner();
		sortComparator = new SortComparator();
		groupComparator = new GroupComparator();
		mapperClass = IdentityMapper.class;
		reducerClass = IdentityReducer.class;
		mappers = new ArrayList<Mapper>();
		reducers = new ArrayList<Reducer>();
	}

	public abstract String getDescription();
	public abstract Object getParameters();
	
	/**
	 * @param mapperClass the mapperClass to set
	 */
	public void setMapperClass(Class<? extends Mapper> mapperClass) {
		this.mapperClass = mapperClass;
	}

	/**
	 * @param reducerClass the reducerClass to set
	 */
	public void setReducerClass(Class<? extends Reducer> reducerClass) {
		this.reducerClass = reducerClass;
	}

	public void setPartitioner(Partitioner p) {
		this.partitioner = p;
	}

	public void setSortComparator(SortComparator s) {
		sortComparator = s;
	}

	public void setGroupComparator(GroupComparator g) {
		groupComparator = g;
	}

	public String toString() {
		return name;
	}

	public Mapper createMapper(File file) throws Exception {
		Mapper m = mapperClass.newInstance();
		KeyValueFileReader reader = (KeyValueFileReader) readerClass.newInstance();
		reader.setFile(file);
		m.setInputReader(reader);
		m.setPartitioner(partitioner);
		m.setParameters(getParameters());
		String fileName = String.format("%s/part-m-%05d", outputDirectory.getAbsolutePath(), mappers.size());
		m.setOutputFile(new File(fileName));
		mappers.add(m);
		return m;
	}

	public Reducer createReducer() throws Exception {
		Reducer r = reducerClass.newInstance();
		r.setSortComparator(sortComparator);
		r.setGroupComparator(groupComparator);
		r.setParameters(getParameters());
		String fileName = String.format("%s/part-r-%05d", outputDirectory.getAbsolutePath(), reducers.size());
		File file = new File(fileName);
		r.setOutputFile(file);
		KeyValueFileWriter writer = (KeyValueFileWriter) writerClass.newInstance();
		writer.setFile(file);
		r.setOutputWriter(writer);
		reducers.add(r);
		return r;
	}

	/**
	 * @return the mappers
	 */
	public ArrayList<Mapper> getMappers() {
		return mappers;
	}

	/**
	 * @return the reducers
	 */
	public ArrayList<Reducer> getReducers() {
		return reducers;
	}
	
	public CounterMap getCounterMap() {
		if (counterMap == null) {
			counterMap = new CounterMap();
			for (Mapper m : mappers) {
				counterMap.addAll(m.getCounterMap());
			}
			for (Reducer r : reducers) {
				counterMap.addAll(r.getCounterMap());
			}
		}
		return counterMap;
	}

	public Set<String> getGroups() {
		return getCounterMap().getGroups();
	}

	public Set<String> getNames(String group) {
		return getCounterMap().getNames(group);
	}
	
	public Counter getCounter(String group, String name) {
		return getCounterMap().getCounter(group, name);
	}

	public String getName() {
		return name;
	}

	public void init() throws Exception {
		mappers = new ArrayList<Mapper>();
		reducers = new ArrayList<Reducer>();
		for (int i = 0; i < numReducers; i++) {
			createReducer();
		}
	}

	public void setNumReducers(int nr) {
		numReducers = nr;
	}

	public int getNumReducers() {
		return numReducers;
	}
	
	public boolean isMapOnly() {
		return numReducers == 0;
	}

	public File getInputDirectory() {
		return inputDirectory;
	}

	public void setInputDirectory(File inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setInputReaderClass(Class<? extends KeyValueReader> readerClass) {
		this.readerClass = readerClass;
	}

	public void setOutputWriterClass(Class<? extends KeyValueWriter> writerClass) {
		this.writerClass = writerClass;
	}
}
