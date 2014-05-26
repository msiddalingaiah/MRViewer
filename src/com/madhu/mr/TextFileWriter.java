
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
import java.io.FileWriter;
import java.io.PrintWriter;

public class TextFileWriter extends KeyValueFileWriter {
	private PrintWriter out;
	
	public TextFileWriter(File file) throws Exception {
		setFile(file);
	}

	public TextFileWriter() {
	}

	@Override
	public void write(Comparable key, Comparable value) throws Exception {
		out.printf("%s\t%s\n", key, value);
	}

	@Override
	public void close() throws Exception {
		out.close();
	}

	@Override
	public void setFile(File file) throws Exception {
		out = new PrintWriter(new FileWriter(file));
	}
}
