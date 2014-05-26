
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

public abstract class KeyValueWriter {
	public static final String[] TYPES = { "Text", "Serialized" };

	public static KeyValueWriter createWriter(String type) {
		if (type.equals("Text")) {
			return new TextFileWriter();
		}
		if (type.equals("Serialized")) {
			return new SerializedFileWriter();
		}
		throw new IllegalArgumentException("Unexpected type: " + type);
	}

	public static Class<? extends KeyValueWriter> getWriterClass(String type) {
		if (type.equals("Text")) {
			return TextFileWriter.class;
		}
		if (type.equals("Serialized")) {
			return SerializedFileWriter.class;
		}
		throw new IllegalArgumentException("Unexpected type: " + type);
	}

	public abstract void write(Comparable key, Comparable value) throws Exception;
	public abstract void close() throws Exception;
}
