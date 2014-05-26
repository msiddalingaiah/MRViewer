
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.madhu.mr.CounterMap;
import com.madhu.mr.MapReduceJob;

public class CounterPanel extends JPanel {
	/**
	 * Create the panel.
	 */
	public CounterPanel(MapReduceJob job) {
		setBorder(new TitledBorder(null, "Counters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JTable table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		CounterMap cm = job.getCounterMap();

		ArrayList<String[]> data = new ArrayList<String[]>();
		ArrayList<String> groups = new ArrayList<String>(cm.getGroups());
		Collections.sort(groups);
		for (String group : groups) {
			ArrayList<String> names = new ArrayList<String>(cm.getNames(group));
			Collections.sort(names);
			String gt = group;
			for (String name : names) {
				String value = Long.toString(cm.getCounter(group, name).getValue());
				String[] row = { gt, name, value };
				data.add(row);
				gt = "";
			}
		}

		Object[] columnNames = new String[] { "Group", "Name", "Value" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		int rowCount = data.size();
		for (int i = 0; i < rowCount; i++) {
			model.addRow(data.get(i));
		}
		
		table.setPreferredScrollableViewportSize(new Dimension(425, 100));
		table.setModel(model);
	}
}
