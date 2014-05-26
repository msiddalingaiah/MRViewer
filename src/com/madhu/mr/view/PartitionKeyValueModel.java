
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

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.madhu.mr.PartitionedKeyValuePair;

public class PartitionKeyValueModel implements TableModel {
	private ArrayList<PartitionedKeyValuePair> list;
	private static final String[] COLUMNS = { "Key", "Value", "Partition" };

	public PartitionKeyValueModel(ArrayList<PartitionedKeyValuePair> list) {
		this.list = list;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PartitionedKeyValuePair kv = list.get(rowIndex);
		switch (columnIndex) {
		case 0: return kv.getKey();
		case 1: return kv.getValue();
		case 2: return Integer.toString(kv.getPartition());
		}
		return "";
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {}
}
