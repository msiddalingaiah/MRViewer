
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.madhu.mr.SerializedFileWriter;

/**
 * This class implements a trivial database for video recordings. Methods are
 * available to get a list of the video categories and a list of recordings.
 * <p>
 * 
 * Usage Example:
 * 
 * <pre>
 * 
 * 
 *     // create and load the data accessor
 *     VideoDataAccessor myDataAccessor = new VideoDataAccessor();
 * 
 *     // get a list of available categories;
 *     ArrayList&lt;String&gt; cats = myDataAccessor.getCategories();
 *     ...
 * 
 *     // get a list of action videos
 *     ArrayList&lt;VideoRecording&gt; recordingList = myDataAccessor.getRecordings(&quot;Action&quot;);
 *     ...
 * 
 *   
 * </pre>
 * 
 * @author 471 Development Team
 */
public class VideoDataAccessor {

	// ////////////////////////////////////////////////////
	//
	// DATA FILE FORMAT:
	//
	// The data file has the following format:
	//
	// producer, video title, category, image name, rating, number minutes, year
	// released, number of actors/actresses
	// actor/actress #1
	// actor/actress #2
	// ----------------------------
	//
	//
	// Here is a sample file:
	//
	// Mel Gibson, Braveheart, Action & Adventure, braveheart.gif, R, 177, 2
	// Mel Gibson
	// Patrick McGoohan
	// -----------------
	//
	//
	// Note that each recording is ended w/ a line separator. The contents
	// of the separator is ignored during the read. The reader simply consumes
	// an extra line feed after reading a recording.
	//

	//

	/**
	 * Marker used to separate the video recordings in the data file.
	 */
	protected static final String RECORD_SEPARATOR = "----------";

	/**
	 * A HashMap/hashtable of the recordings.
	 * 
	 * The key is the "category".
	 * 
	 * The data stored for each key is an ArrayList which is the collection of
	 * music recordings.
	 */
	protected HashMap<String, ArrayList<VideoRecording>> dataTable;

	/**
	 * This holds a list of the recent recordings that have been added using the
	 * addRecording() method.
	 * 
	 */
	protected ArrayList<VideoRecording> recentRecordingList;

	/**
	 * The list of categories
	 */
	protected ArrayList<String> categories;

	/**
	 * Internal helper
	 */
	protected HashSet<String> categorySet;

	public VideoDataAccessor() {
	}

	/**
	 * Constructs the data accessor and calls the load() method to load data.
	 * 
	 */
	public VideoDataAccessor(String fileName) {
		load(fileName);
	}

	String debug;

	/**
	 * Loads the data from a storage device.
	 */
	public void load(String fileName) {

		recentRecordingList = new ArrayList<VideoRecording>();
		categorySet = new HashSet<String>();

		dataTable = new HashMap<String, ArrayList<VideoRecording>>();

		ArrayList<VideoRecording> videoArrayList = null;
		StringTokenizer st = null;

		VideoRecording myRecording;
		String line = "";

		String producer, title;
		String category, imageName;
		String rating;
		int numberOfMinutes;
		int yearReleased;

		int numberOfActors;
		int basePrice;
		double price; // all of the recordings are the same LOW price :-)

		String[] actorList;

		try {
			// use the getResource() method for reading locally or over a
			// network
			BufferedReader inputFromFile = new BufferedReader(new FileReader(fileName));

			// read until end-of-file
			while ((line = inputFromFile.readLine()) != null) {

				// create a tokenizer for a comma delimited line
				st = new StringTokenizer(line, ",");

				// Parse the info line to read following items formatted as
				// - the artist, title, category, imageName, rating, number of
				// minutes, yearReleased, number of actors
				//
				producer = st.nextToken().trim();
				title = st.nextToken().trim();
				debug = title;

				category = st.nextToken().trim();
				categorySet.add(category);

				imageName = st.nextToken().trim();
				rating = st.nextToken().trim();
				numberOfMinutes = Integer.parseInt(st.nextToken().trim());
				yearReleased = Integer.parseInt(st.nextToken().trim());
				numberOfActors = Integer.parseInt(st.nextToken().trim());

				// read all of the actors in
				actorList = readActors(inputFromFile, numberOfActors);

				// create the video recording
				// select a random price between 9.99 and 24.99
				basePrice = 9 + (int) (Math.random() * 16);
				price = basePrice + .99;

				Duration duration = new Duration(numberOfMinutes * 60);
				myRecording = new VideoRecording(producer, actorList, rating,
						yearReleased, title, price, category, imageName,
						duration);

				// check to see if we have information on this category
				if (dataTable.containsKey(category)) {

					// get the list of recordings for this category
					videoArrayList = dataTable.get(category);
				} else {

					// this is a new category. simply add the category
					// to our dataTable
					videoArrayList = new ArrayList<VideoRecording>();
					dataTable.put(category, videoArrayList);
				}

				// add the recording
				videoArrayList.add(myRecording);

				// move ahead and consume the line separator
				line = inputFromFile.readLine();
			}

			inputFromFile.close();

			// update the category list
			categories = new ArrayList<String>(categorySet);
			Collections.sort(categories);
		} catch (Exception exc) {
			System.out.println(debug);
			exc.printStackTrace();
		}

	}

	/**
	 * Helper method for reading a given number of actors from a Reader.
	 * 
	 * @param inputFromFile
	 *            the reader for the file we are reading
	 * @param numberOfStrings
	 *            the number of actors to read
	 * 
	 * @return an array of Actorss
	 */
	protected String[] readActors(BufferedReader inputFromFile,
			int numberOfActors) throws IOException {
		String[] actorList = new String[numberOfActors];

		StringTokenizer st;
		String actorLine;
		String actorName;

		for (int i = 0; i < numberOfActors; i++) {
			actorLine = inputFromFile.readLine();
			st = new StringTokenizer(actorLine, ",");
			actorName = st.nextToken().trim();
			actorList[i] = actorName;
		}

		return actorList;
	}

	/**
	 * Returns a sorted list of recordings that match a given category
	 * 
	 * @param category
	 *            the category for requested recordings.
	 * @return collection of <code>MusicRecording</code> objects
	 */
	public ArrayList<VideoRecording> getRecordings(String category) {

		ArrayList<VideoRecording> recordingList = new ArrayList<VideoRecording>();

		// get a list of all recordings for this category
		if (dataTable.containsKey(category)) {
			recordingList = dataTable.get(category);
		} else {
			System.out.println("===>   NO RECORDINGS FOUND FOR: " + category);
		}
		return recordingList;
	}

	/**
	 * @return Returns the categories.
	 */
	public ArrayList<String> getCategories() {
		return categories;
	}
	
	public ArrayList<VideoRecording> getRecordings() {
		ArrayList<VideoRecording> result = new ArrayList<>();
		ArrayList<String> categories = getCategories();
		for (String category : categories) {
			ArrayList<VideoRecording> recordings = getRecordings(category);
			for (VideoRecording recording : recordings) {
				result.add(recording);
			}
		}
		return result;
	}

	private static String createTree(Object value) {
		StringBuilder sb = new StringBuilder();
		doCreateTree(sb, "", "", value);
		return sb.toString();
	}

	private static void doCreateTree(StringBuilder sb, String tab, String name, Object value) {
		if (value == null) {
			sb.append(tab);
			sb.append(name);
			sb.append(": null\n");
			return;
		}
		Class<? extends Object> clazz = value.getClass();
		if (clazz.isPrimitive() || clazz.getName().startsWith("java.lang.")) {
			sb.append(tab);
			sb.append(name);
			sb.append(": ");
			sb.append(value);
			sb.append('\n');
			return;
		}
		if (clazz.isArray()) {
			sb.append(tab);
			sb.append(name);
			sb.append(": array\n");
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
		if (name.length() > 0) {
			sb.append(tab);
			sb.append(name);
			sb.append(":\n");
			tab = tab + "\t";
		}
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				doCreateTree(sb, tab, field.getName(), field.get(value));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Message: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		writeSerialized();
	}

	private static void writeSerialized() throws Exception {
		int nFiles = 3;
		VideoDataAccessor vda = new VideoDataAccessor("video.db");
		ArrayList<VideoRecording> recordings = vda.getRecordings();
		Iterator<VideoRecording> it = recordings.iterator();
		int fraction = recordings.size() / nFiles;
		int nWritten = 0;
		SerializedFileWriter sfw = new SerializedFileWriter();
		for (int i = 0; i < nFiles; i++) {
			String fname = String.format("seq_input/input%d.ser", i);
			sfw.setFile(new File(fname));
			if (i == nFiles - 1) {
				fraction = recordings.size() - nWritten;
			}
			for (int j = 0; j < fraction; j++) {
				VideoRecording vr = it.next();
				sfw.write(vr.getTitle(), vr);
				nWritten += 1;
			}
			sfw.close();
			System.out.printf("%s: %d\n", fname, fraction);
		}
	}
}
