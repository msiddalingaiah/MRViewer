
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
 *  This class represents a Recording.  It describes the recording
 *  title, price, category, imageName and duration.  <p>
 *
 *  Subclasses must implement the method <code>getDuration()</code> to
 *  return the total duration for the recording.
 *
 */
public abstract class Recording implements Comparable, Serializable {

	//
	//  DATA MEMBERS
	//

	/**
	 *  The recording title
	 */
	private String title;

	/**
	 *  The recording price
	 */
	private double price;

	/**
	 *  The recording category
	 */
	private String category;

	/**
	 *  The recording image name
	 */
	private String imageName;


	//
	//  CONSTRUCTORS
	//

	/**
	 *  Basic default constructor
	 */
	public Recording() {
		// default constructor
	}



	/**
	 *  Constructs a recording w/ given parameter values.
	 */
	public Recording(String theTitle, double thePrice,
					 String theCategory, String theImageName) {

		title = theTitle;
		price = thePrice;
		category = theCategory;
		imageName = theImageName;
	}


	//
	//  GETTER / SETTER METHODS
	//

	/**
	 *  Returns the recording title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 *  Sets the recording title
	 */
	public void setTitle(String theTitle) {
		title = theTitle;
	}


	/**
	 *  Returns the recording price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 *  Sets the recording price
	 */
	public void setPrice(double thePrice) {
		price = thePrice;
	}


	/**
	 *  Returns the recording category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 *  Sets the recording category
	 */
	public void setCategory(String theCategory) {
		category = theCategory;
	}


	/**
	 *  Returns the recording image name
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 *  Sets the recording image name
	 */
	public void setImageName(String theImageName) {
		imageName = theImageName;
	}


	//
	//  ABSTRACT METHOD(S)
	//

	/**
	 *  Returns the recording duration.  Subclasses must
	 *  override this method to return the total duration.
	 */
	public abstract Duration getDuration();
}
