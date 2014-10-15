
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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import com.madhu.mr.MapReduceJob;
import com.madhu.mr.Mapper;
import com.madhu.mr.Reducer;

public class MRPanel extends JPanel implements ActionListener {
	private JPanel mapContent;
	private JPanel reduceContent;
	private MapReduceJob job;
	private JButton shuffleButton;
	private JButton sortButton;
	private JButton mapStepAll;
	private JButton mapRunAll;
	private JButton reduceStepAll;
	private JButton reduceRunAll;
	private ArrayList<MapperPanel> mapperPanels;
	private ArrayList<ReducerPanel> reducerPanels;
	private File outputDirectory;
	private JButton writeOutputButton;

	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public MRPanel(MapReduceJob job) throws Exception {
		this.job = job;
		setLayout(new BorderLayout(0, 0));

		JPanel southPanel = new JPanel();
		add(southPanel, BorderLayout.SOUTH);
		
		if (job.isMapOnly()) {
			setBorder(new TitledBorder(null, "Map only job", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			layoutMapOnly();
		} else {
			setBorder(new TitledBorder(null, "MapReduce job", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			layoutMapReduce();
			
			shuffleButton = new JButton("Shuffle");
			southPanel.add(shuffleButton);
			
			sortButton = new JButton("Sort");
			sortButton.setEnabled(false);
			southPanel.add(sortButton);
			
			shuffleButton.addActionListener(this);
			sortButton.addActionListener(this);
			reduceStepAll.addActionListener(this);
			reduceRunAll.addActionListener(this);
		}
		
		writeOutputButton = new JButton("Write Output");
		southPanel.add(writeOutputButton);
		writeOutputButton.addActionListener(this);

		mapStepAll.addActionListener(this);
		mapRunAll.addActionListener(this);
		
		setInputDirectory(job.getInputDirectory());
		setOutputDirectory(job.getOutputDirectory());
	}

	private void layoutMapOnly() {
		JPanel mapPanel = createMapPanel();
		add(mapPanel, BorderLayout.CENTER);
	}

	private void layoutMapReduce() {
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(0.5);
		add(splitPane, BorderLayout.CENTER);
		
		JPanel mapPanel = createMapPanel();

		splitPane.setLeftComponent(mapPanel);

		JPanel reducePanel = new JPanel();
		splitPane.setRightComponent(reducePanel);
		reducePanel.setBorder(new TitledBorder(null, "Reduce Tasks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		reducePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel reduceNorth = new JPanel();
		reducePanel.add(reduceNorth, BorderLayout.NORTH);
		
		JPanel reduceSouth = new JPanel();
		reducePanel.add(reduceSouth, BorderLayout.SOUTH);
		
		reduceStepAll = new JButton("Step All");
		reduceStepAll.setEnabled(false);
		reduceSouth.add(reduceStepAll);
		
		reduceRunAll = new JButton("Run All");
		reduceRunAll.setEnabled(false);
		reduceSouth.add(reduceRunAll);
		
		reduceContent = new JPanel();
		reducePanel.add(new JScrollPane(reduceContent), BorderLayout.CENTER);
	}

	private JPanel createMapPanel() {
		JPanel mapPanel = new JPanel();
		mapPanel.setBorder(new TitledBorder(null, "Map Tasks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mapPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel mapNorth = new JPanel();
		mapPanel.add(mapNorth, BorderLayout.NORTH);
		
		JPanel mapSouth = new JPanel();
		mapPanel.add(mapSouth, BorderLayout.SOUTH);
		
		mapStepAll = new JButton("Step All");
		mapSouth.add(mapStepAll);
		
		mapRunAll = new JButton("Run All");
		mapSouth.add(mapRunAll);
		
		mapContent = new JPanel();
		mapPanel.add(new JScrollPane(mapContent), BorderLayout.CENTER);
		return mapPanel;
	}

	public void setInputDirectory(File dir) {
		mapContent.removeAll();
		File[] files = dir.listFiles();
		ArrayList<File> validFiles = new ArrayList<File>();
		for (File file : files) {
			if (file.getName().charAt(0) != '_') {
				validFiles.add(file);
			}
		}
		mapContent.setLayout(new GridLayout(validFiles.size(), 1, 5, 5));
		int index = 1;
		try {
			mapperPanels = new ArrayList<MapperPanel>();
			for (File file : validFiles) {
				Mapper mapper = job.createMapper(file);
				mapper.setReducers(job.getReducers());
				MapperPanel mp;
				if (job.isMapOnly()) {
					mp = new MapOnlyPanel(index, file, mapper);
				} else {
					mp = new MapperPanel(index, file, mapper);
				}
				mapContent.add(mp);
				mapperPanels.add(mp);
				index += 1;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void setOutputDirectory(File dir) {
		this.outputDirectory = dir;
		if (!job.isMapOnly()) {
			ArrayList<Reducer> reducers = job.getReducers();
			int n = reducers.size();
			reduceContent.removeAll();
			reduceContent.setLayout(new GridLayout(n, 1, 5, 5));
			reducerPanels = new ArrayList<ReducerPanel>();
			for (int i = 0; i < n; i++) {
				ReducerPanel rp = new ReducerPanel(i, dir, reducers.get(i));
				reduceContent.add(rp);
				reducerPanels.add(rp);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		try {
			if (source == shuffleButton) {
				doShuffle();
			} else if (source == sortButton) {
				doSort();
			} else if (source == mapStepAll) {
				for (MapperPanel mp : mapperPanels) {
					mp.doStep();
				}
				mapStepAll.setEnabled(!isMapFinished());
				mapRunAll.setEnabled(!isMapFinished());
			} else if (source == mapRunAll) {
				for (MapperPanel mp : mapperPanels) {
					mp.doRun();
				}
				mapStepAll.setEnabled(false);
				mapRunAll.setEnabled(false);
			} else if (source == reduceStepAll) {
				for (ReducerPanel rp : reducerPanels) {
					rp.doStep();
				}
				checkReduceFinished();
			} else if (source == reduceRunAll) {
				for (ReducerPanel rp : reducerPanels) {
					rp.doRun();
				}
				checkReduceFinished();
			} else if (source == writeOutputButton) {
				doWriteOutput();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			JOptionPane.showMessageDialog(this, exc.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void checkReduceFinished() {
		if (isReduceFinished()) {
			reduceStepAll.setEnabled(false);
			reduceRunAll.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	private void doShuffle() {
		if (!isMapFinished()) {
			JOptionPane.showMessageDialog(this, "Map phase is not finished", "Not Ready", JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (ReducerPanel rp : reducerPanels) {
			rp.shuffle();
		}
		mapStepAll.setEnabled(false);
		mapRunAll.setEnabled(false);
		sortButton.setEnabled(true);
		shuffleButton.setEnabled(false);
	}

	/**
	 * 
	 */
	private void doSort() {
		for (ReducerPanel rp : reducerPanels) {
			rp.sort();
		}
		sortButton.setEnabled(false);
		reduceStepAll.setEnabled(true);
		reduceRunAll.setEnabled(true);
	}

	private void doWriteOutput() throws Exception {
		if (job.isMapOnly()) {
			if (!isMapFinished()) {
				JOptionPane.showMessageDialog(this, "Map phase is not finished", "Not Ready", JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (MapperPanel mp : mapperPanels) {
				mp.writeOutput();
			}
		} else {
			if (!isReduceFinished()) {
				JOptionPane.showMessageDialog(this, "Reduce phase is not finished", "Not Ready", JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (ReducerPanel rp : reducerPanels) {
				rp.writeOutput();
			}
		}
		writeOutputButton.setEnabled(false);
//		JOptionPane.showMessageDialog(this, "Job completed\n" +
//				"Check " + outputDirectory.getAbsolutePath(),  "All done!", JOptionPane.INFORMATION_MESSAGE);
		JFrame cf = new JFrame(job.getName() + " counters");
		cf.getContentPane().add(new CounterPanel(job));
		cf.pack();
		cf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cf.setVisible(true);
	}

	private boolean isMapFinished() {
		for (MapperPanel mp : mapperPanels) {
			if (!mp.getMapper().isFinished()) {
				return false;
			}
		}
		return true;
	}

	private boolean isReduceFinished() {
		ArrayList<Reducer> reducers = job.getReducers();
		for (Reducer reducer : reducers ) {
			if (!reducer.isFinished()) {
				return false;
			}
		}
		return true;
	}
}
