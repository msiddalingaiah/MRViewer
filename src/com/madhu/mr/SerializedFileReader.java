
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
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SerializedFileReader extends KeyValueFileReader {
	private ObjectInputStream ois;
	private boolean eof;

	public SerializedFileReader() {
	}

	@Override
	public void setFile(File file) throws Exception {
		ois = new ObjectInputStream(new FileInputStream(file));
		eof = false;
	}

	@Override
	public KeyValuePair read() throws Exception {
		if (eof) {
			return null;
		}
		Object object = ois.readObject();
		if (object instanceof KeyValuePair) {
			return (KeyValuePair) object;
		}
		eof = true;
		return null;
	}

	@Override
	public void close() throws Exception {
		ois.close();
	}
}
