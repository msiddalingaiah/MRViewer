
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

import java.awt.Component;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ObjectCellRenderer extends DefaultTableCellRenderer {
	public ObjectCellRenderer() {
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		Object valueAt = table.getModel().getValueAt(row, column);
		label.setToolTipText(createTree(valueAt));

		return label;
	}

	private String createTree(Object value) {
		if (value == null) {
			return "null";
		}
		Class<? extends Object> clazz = value.getClass();
		if (clazz.isPrimitive() || clazz.getName().startsWith("java.lang.")) {
			return value.toString();
		}
		StringBuilder sb = new StringBuilder("<html><pre>");
		doCreateTree(sb, "", "", value);
		sb.append("</pre></html>");
		return sb.toString();
	}

	private void doCreateTree(StringBuilder sb, String tab, String name, Object value) {
		if (name.length() > 0) {
			sb.append(tab);
			sb.append(name);
			sb.append(": ");
			tab = tab + "    ";
		}
		if (value == null) {
			sb.append("null\n");
			return;
		}
		Class<? extends Object> clazz = value.getClass();
		if (clazz.isPrimitive() || clazz.getName().startsWith("java.lang.")) {
			sb.append(value);
			sb.append('\n');
			return;
		}
		if (clazz.isArray()) {
			String simpleName = clazz.getSimpleName();
			sb.append(simpleName.substring(0, simpleName.length()-1));
			sb.append(Array.getLength(value));
			sb.append("]\n");
			return;
		}
		ArrayList<Field> fields = new ArrayList<>();
		while (clazz != null) {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				fields.add(f);
			}
			clazz = clazz.getSuperclass();
		}
		sb.append('\n');
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				doCreateTree(sb, tab, field.getName(), field.get(value));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Message: " + e.getMessage());
			}
		}
	}
}
