
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

import java.io.Serializable;

public class VideoRecording extends Recording implements Serializable {
	/**
	 *  Data members
	 */
	private String producer;
	private String[] actors;
	private String rating;
	private int yearReleased;
	private Duration myDuration;

	public VideoRecording() {
	}

	/**
	 *  Creates a VideoRecording object with given parameter values
	 */
	public VideoRecording(String theProducer, String[] theActors, String theRating, int theYearReleased,
						  String theTitle, double thePrice,
						  String theCategory, String theImageName,
						  Duration theDuration) {

		super(theTitle, thePrice, theCategory, theImageName);

		producer = theProducer;
		actors = theActors;
		rating = theRating;
		yearReleased = theYearReleased;
		myDuration = theDuration;
	}

	public int getYearReleased() {
		return yearReleased;
	}

	public String getRating() {
		return rating;
	}
	public String getProducer() {
		return producer;
	}

	public String[] getActors() {
		return actors;
	}

	public Duration getDuration() {

		return myDuration;
	}

	/**
	 *  Returns the title of recording
	 */
	public String toString() {
		return String.format("%s - %s", getTitle(), myDuration);
	}


	@Override
	public int compareTo(Object object) {
		VideoRecording recording = (VideoRecording) object;
		String targetTitle = recording.getTitle();

		return getTitle().compareTo(targetTitle);
	}
}
