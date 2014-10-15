
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

package com.madhu.mr.view;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JTable;

import com.madhu.mr.KeyValuePair;
import com.madhu.mr.Mapper;

public class MapOnlyPanel extends MapperPanel {
	public MapOnlyPanel(int index, File file, Mapper mapper) {
		super(index, file, mapper);
	}

	@Override
	public void updateTable(JTable table, ArrayList<KeyValuePair> stepOutput) {
		table.setModel(new KeyValueModel(stepOutput));
	}
}
