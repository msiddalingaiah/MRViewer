
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import com.madhu.mr.Reducer;
import javax.swing.border.EtchedBorder;

public class ReducerPanel extends JPanel implements ActionListener {
	private JSplitPane splitPane;
	private Reducer reducer;
	private JTable inputTable;
	private JTable outputTable;
	private DefaultListSelectionModel listModel;
	private JButton stepButton;
	private JButton runButton;

	/**
	 * Create the panel.
	 * @param reducer 
	 */
	public ReducerPanel(int index, File outputDir, Reducer reducer) {
		this.reducer = reducer;
		String name = String.format("Reducer %d %s", index, reducer.getFileName());
		setBorder(new TitledBorder(null, name, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel southPanel = new JPanel();
		add(southPanel, BorderLayout.SOUTH);
		
		stepButton = new JButton("Step");
		stepButton.setEnabled(false);
		southPanel.add(stepButton);
		
		runButton = new JButton("Run");
		runButton.setEnabled(false);
		southPanel.add(runButton);
		
		splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(null, "Input", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		inputPanel.setLayout(new BorderLayout(0, 0));
		splitPane.setLeftComponent(inputPanel);
		inputTable = new JTable();
		JScrollPane inputScrollPane = new JScrollPane(inputTable);
		inputPanel.add(inputScrollPane, BorderLayout.CENTER);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Step output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(outputPanel);
		outputPanel.setLayout(new BorderLayout(0, 0));
		
		stepButton.addActionListener(this);
		runButton.addActionListener(this);

		outputTable = new JTable();
		JScrollPane outputScrollPane = new JScrollPane(outputTable);
		outputPanel.add(outputScrollPane, BorderLayout.CENTER);

		inputTable.setPreferredScrollableViewportSize(new Dimension(250, 100));
		inputTable.setDefaultRenderer(Object.class, new ObjectCellRenderer());
		outputTable.setPreferredScrollableViewportSize(new Dimension(250, 100));
		outputTable.setDefaultRenderer(Object.class, new ObjectCellRenderer());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		try {
			if (source == stepButton) {
				doStep();
			} else if (source == runButton) {
				doRun();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			JOptionPane.showMessageDialog(this, exc.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateView() {
		stepButton.setEnabled(!reducer.isFinished());
		runButton.setEnabled(!reducer.isFinished());
		int readIndex1 = reducer.getReadIndex1();
		int readIndex2 = reducer.getReadIndex2();
		listModel.setSelectionInterval(readIndex1, readIndex2);
		outputTable.setModel(new KeyValueModel(reducer.getStepOutput()));
	}

	public void shuffle() {
		inputTable.setModel(new KeyValueModel(reducer.getInput()));
	}

	public void sort() {
		reducer.sort();
		inputTable.setModel(new KeyValueModel(reducer.getInput()));
		listModel = new DefaultListSelectionModel();
		inputTable.setSelectionModel(listModel);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
	}

	public void doStep() throws Exception {
		reducer.step();
		updateView();
	}

	public void doRun() throws Exception {
		stepButton.setEnabled(false);
		runButton.setEnabled(false);
		reducer.run();
	}

	public void writeOutput() throws Exception {
		reducer.writeOutput();
	}
}
