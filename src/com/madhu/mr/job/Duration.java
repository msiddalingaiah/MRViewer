
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

/**
 *  This class describes a duration of time.  It contains
 *  the hour, minutes and seconds.
 *
 *  <pre>
 *
 *    Usage Example:
 *
 *      Duration lunchDuration = new Duration(1, 0, 0); // 1 hr, 0 mins, 0 secs
 *      System.out.println(lunchDuration);
 *
 *      Duration breakDuration = new Duration(0, 15, 30); // 0 hrs, 15 mins, 30 secs
 *      System.out.println(breakDuration);
 *
 *      Duration labDuration = new Duration(2700); // 2700 seconds = 45 minutes
 *      System.out.println(labDuration);
 *
 *  </pre>
 *
 */
public class Duration implements Comparable, Serializable {

	/**
	 *  The number of hours
	 */
	private int hours;

	/**
	 *  The number of minutes
	 */
	private int minutes;


	/**
	 *  The number of seconds
	 */
	private int seconds;

	/**
	 *  Creates a Duration object with 0 hours, minutes and seconds.
	 */
	public Duration() {
		hours = 0;
		minutes = 0;
		seconds = 0;
	}

	/**
	 *  Creates a Duration object with given parameter values
	 */
	public Duration(int theHours, int theMinutes, int theSeconds) {
		hours = theHours;
		minutes = theMinutes;
		seconds = theSeconds;
	}

	/**
	 *  Creates a Duration object with given parameter values
	 */
	public Duration(int totalSeconds) {
		hours = totalSeconds / 3600;
		int rem = totalSeconds - (hours * 3600);
		minutes = rem / 60;
		seconds = rem % 60;
	}

	/**
	 *  Returns the hours portion of the duration
	 */
	public int getHours() {
		return hours;
	}

	/**
	 *  Returns the minutes portion of the duration
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 *  Returns the seconds portion of the duration.
	 *
	 *  <p/>
	 *  Note:  This is <b>NOT</b> the total seconds.
	 *
	 *  @see getTotalSeconds().
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 *  Returns the total seconds
	 */
	public int getTotalSeconds() {
		return seconds + (60 * (minutes + (60 * hours)));
	}

	/**
	 *  Returns a <b>new</b> duration object that is the sum of the
	 *  supplied Duration object and current object
	 */
	public Duration add(Duration someTime) {
		int total = getTotalSeconds() + someTime.getTotalSeconds();
		return new Duration(total);
	}

	/**
	 *  Returns a <b>new</b> duration object that is the difference of the
	 *  supplied Duration and current object.  The difference returned is
	 *  the absolute difference, so the duration will always be positive.
	 */
	public Duration subtract(Duration someTime) {
		int diff = getTotalSeconds() - someTime.getTotalSeconds();
		int total = Math.abs(diff);

		return new Duration(total);
	}


	/**
	 *  Returns a string representation of the Duration object: <br>
	 *
	 *	<pre>
	 *
	 *  Format
	 *    hh:mm:ss
	 *
	 *  </pre>
	 */
	public String toString() {
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);		
	}

	@Override
	public int compareTo(Object other) {
		Duration d = (Duration) other;
		return d.getTotalSeconds() - getTotalSeconds();
	}
}
