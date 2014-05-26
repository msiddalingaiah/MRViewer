
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TextFileReader extends KeyValueFileReader {
	private BufferedReader br;
	private int lineNumber;

	public TextFileReader(File file) throws Exception {
		setFile(file);
	}

	public TextFileReader() {
	}

	public void setFile(File file) throws Exception {
		br = new BufferedReader(new FileReader(file));
		lineNumber = 0;
	}

	@Override
	public KeyValuePair read() throws Exception {
		String line = br.readLine();
		if (line == null) {
			return null;
		}
		KeyValuePair kv = new KeyValuePair(lineNumber, line);
		lineNumber += 1;
		return kv;
	}

	@Override
	public void close() throws Exception {
		br.close();
	}
	
	@Override
	public String toString() {
		return "Text";
	}
}
