
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
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import com.madhu.mr.KeyValuePair;
import com.madhu.mr.Mapper;
import com.madhu.mr.PartitionedKeyValuePair;

public class MapperPanel extends JPanel implements ActionListener {
	private JSplitPane splitPane;
	private JButton stepButton;
	private JButton runButton;
	private Mapper mapper;
	private JTable inputTable;
	private KeyValueModel inputModel;
	private DefaultListSelectionModel inputListModel;
	private JPanel inputPanel;
	private JTable outputTable;

	/**
	 * Create the panel.
	 * @param file 
	 * @throws Exception 
	 * @throws InstantiationException 
	 */
	public MapperPanel(int index, File file, Mapper mapper) {
		this.mapper = mapper;
		String name = String.format("Mapper %d %s", index, file.getName());
		setBorder(new TitledBorder(null, name, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel southPanel = new JPanel();
		add(southPanel, BorderLayout.SOUTH);
		
		stepButton = new JButton("Step");
		southPanel.add(stepButton);
		
		runButton = new JButton("Run");
		southPanel.add(runButton);
		
		splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(null, "Input", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(inputPanel);
		inputPanel.setLayout(new BorderLayout(0, 0));
		inputTable = new JTable();
		JScrollPane inputScrollPane = new JScrollPane(inputTable);
		inputPanel.add(inputScrollPane, BorderLayout.CENTER);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(null, "Step output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(outputPanel);
		outputPanel.setLayout(new BorderLayout(0, 0));
		outputTable = new JTable();
		JScrollPane outputScrollPane = new JScrollPane(outputTable);
		outputPanel.add(outputScrollPane, BorderLayout.CENTER);

		stepButton.addActionListener(this);
		runButton.addActionListener(this);

		inputModel = new KeyValueModel(mapper.getInput());
		inputListModel = new DefaultListSelectionModel();
		inputTable.setModel(inputModel);
		inputTable.setSelectionModel(inputListModel);
		inputTable.setPreferredScrollableViewportSize(new Dimension(250, 100));
		inputTable.setDefaultRenderer(Object.class, new ObjectCellRenderer());
		
		outputTable.setPreferredScrollableViewportSize(new Dimension(250, 100));
		outputTable.setDefaultRenderer(Object.class, new ObjectCellRenderer());

		int readIndex = mapper.getReadIndex();
		inputListModel.setSelectionInterval(readIndex , readIndex);
	}

	public void adjustUI() {
		splitPane.setDividerLocation(0.5);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Object source = e.getSource();
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

	public void doRun() {
		stepButton.setEnabled(false);
		runButton.setEnabled(false);
		mapper.run();
	}

	private void updateView() {
		stepButton.setEnabled(!mapper.isFinished());
		runButton.setEnabled(!mapper.isFinished());
		int readIndex = mapper.getReadIndex();
		inputListModel.setSelectionInterval(readIndex, readIndex);
		updateTable(outputTable, mapper.getStepOutput());
	}

	public void updateTable(JTable table, ArrayList<KeyValuePair> stepOutput) {
		// Unfortunate type erasure
		ArrayList<PartitionedKeyValuePair> pkv = (ArrayList) stepOutput;
		table.setModel(new PartitionKeyValueModel(pkv));
	}

	public Mapper getMapper() {
		return mapper;
	}

	public void doStep() {
		mapper.step();
		updateView();
	}

	public void writeOutput() throws Exception {
		mapper.writeOutput();
	}
}
