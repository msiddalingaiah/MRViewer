
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

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.madhu.mr.KeyValuePair;

public class KeyValuePanel extends JPanel {
	private JTextField keyTextField;
	private JTextField valueTextField;

	/**
	 * Create the panel.
	 */
	public KeyValuePanel() {
		setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Key", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(inputPanel);
		inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		keyTextField = new JTextField();
		inputPanel.add(keyTextField);
		keyTextField.setColumns(10);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Value", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(outputPanel);
		outputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		valueTextField = new JTextField();
		outputPanel.add(valueTextField);
		valueTextField.setColumns(10);
	}

	public void setPair(KeyValuePair kv) {
		if (kv == null) {
			return;
		}
		keyTextField.setText(kv.getKey().toString());
		valueTextField.setText(kv.getValue().toString());
	}
}
