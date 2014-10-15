
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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.madhu.mr.KeyValueReader;
import com.madhu.mr.KeyValueWriter;
import com.madhu.mr.MapReduceJob;

public class MainFrame extends JFrame implements ActionListener {
	private JMenuItem inputMenuItem;
	private JMenuItem outputMenuItem;
	private JMenuItem startMenuItem;
	private JMenuItem tipsMenuItem;
	private JMenuItem aboutMenuItem;
	private File inputDirectory;
	private File outputDirectory;
	private JButton goButton;
	private JComboBox jobCombo;
	private JLabel lblMapreduceJob;
	private JLabel lblReducers;
	private JPanel centerPanel;
	private JPanel inputPanel;
	private JPanel outputPanel;
	private JLabel lblInputDirectory;
	private JTextField inputDirTF;
	private JButton inputDirButton;
	private JLabel lblOutputDirectory;
	private JTextField outputDirTF;
	private JButton outputDirButton;
	private JPanel jobPanel;
	private JTextArea jobTextArea;
	private JPanel northPanel;
	private JPanel filePanel;
	private JSplitPane splitPane;
	private FormPanel formPanel;
	private JComboBox<String> inputCombo;
	private JComboBox<String> outputCombo;
	private JMenuItem exitMenuItem;

	public MainFrame(String title, Properties props) throws Exception {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		northPanel = new JPanel();
		getContentPane().add(northPanel, BorderLayout.NORTH);
		
		lblMapreduceJob = new JLabel("MapReduce Job");
		northPanel.add(lblMapreduceJob);
		
		jobCombo = new JComboBox();
		northPanel.add(jobCombo);
		jobCombo.addActionListener(this);
		
		JPanel southPanel = new JPanel();
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		goButton = new JButton("Go!");
		goButton.setEnabled(false);
		southPanel.add(goButton);
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(inputMenuItem = new JMenuItem("Input directory..."));
		inputMenuItem.addActionListener(this);
		fileMenu.add(outputMenuItem = new JMenuItem("Output directory..."));
		outputMenuItem.addActionListener(this);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem = new JMenuItem("Exit"));
		exitMenuItem.addActionListener(this);

		JMenu editMenu = new JMenu("Edit");

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(startMenuItem = new JMenuItem("Getting started"));
		startMenuItem.addActionListener(this);
		helpMenu.add(tipsMenuItem = new JMenuItem("Tips and tricks..."));
		tipsMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem = new JMenuItem("About"));
		aboutMenuItem.addActionListener(this);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		centerPanel.add(splitPane, BorderLayout.CENTER);
		jobTextArea = new JTextArea(3, 20);
		jobTextArea.setWrapStyleWord(true);
		splitPane.setTopComponent(jobTextArea);
		jobPanel = new JPanel();
		splitPane.setBottomComponent(jobPanel);
		
		filePanel = new JPanel();
		centerPanel.add(filePanel, BorderLayout.SOUTH);
		filePanel.setLayout(new BorderLayout(0, 0));
		
		inputPanel = new JPanel();
		filePanel.add(inputPanel, BorderLayout.NORTH);
		FlowLayout flowLayout = (FlowLayout) inputPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		
		lblInputDirectory = new JLabel("Input directory");
		inputPanel.add(lblInputDirectory);
		
		inputDirTF = new JTextField();
		inputPanel.add(inputDirTF);
		inputDirTF.setColumns(40);
		inputDirTF.setEditable(false);
		
		inputDirButton = new JButton("...");
		inputPanel.add(inputDirButton);
		inputCombo = new JComboBox<String>(KeyValueReader.TYPES);
		inputPanel.add(inputCombo);
		
		outputPanel = new JPanel();
		filePanel.add(outputPanel, BorderLayout.SOUTH);
		FlowLayout flowLayout_1 = (FlowLayout) outputPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		
		lblOutputDirectory = new JLabel("Output directory");
		outputPanel.add(lblOutputDirectory);
		
		outputDirTF = new JTextField();
		outputPanel.add(outputDirTF);
		outputDirTF.setColumns(40);
		outputDirTF.setEditable(false);
		
		outputDirButton = new JButton("...");
		outputPanel.add(outputDirButton);
		outputCombo = new JComboBox<String>(KeyValueWriter.TYPES);
		outputPanel.add(outputCombo);

		outputDirButton.addActionListener(this);
		inputDirButton.addActionListener(this);

		int index = 1;
		String clazz;
		while ((clazz = props.getProperty(String.format("job.class.%d", index))) != null) {
			jobCombo.addItem(Class.forName(clazz).newInstance());
			index += 1;
		}

		goButton.addActionListener(this);
		doJobSelect();
		pack();
	}

	// FIXME: this doesn't seem to work with jar files		
	public ArrayList<Class> getSubclasses(Class superClass) throws Exception {
		ArrayList<Class> list = new ArrayList<Class>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> res = cl.getResources("");
		System.out.println(res);
		while (res.hasMoreElements()) {
			URL url = res.nextElement();
			String dir = url.getFile();
			findClasses(dir, "", list, superClass);
		}
		return list;
	}

	private void findClasses(String root, String path, ArrayList<Class> list, Class superClass) throws Exception {
		File file = new File(root, path);
		if (file.isFile() && path.endsWith(".class")) {
			path = path.substring(1, path.length() - 6);
			Class<?> clazz = Class.forName(path.replace('/', '.'));
			if (superClass.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
				list.add(clazz);
			}
		} else if (file.isDirectory()) {
			String[] pathList = file.list();
			for (String p : pathList) {
				findClasses(root, path + '/' + p, list, superClass);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == inputMenuItem || source == inputDirButton) {
			doChooseInput();
		} else if (source == outputMenuItem || source == outputDirButton) {
			doChooseOutput();
		} else if (source == startMenuItem) {
			JOptionPane.showMessageDialog(null,
					"1. Select input and output directories from the File menu\n" +
					"2. Choose a MapReduce job\n" +
					"3. Select the number of reducers\n" +
					"4. Press the Go! button\n" +
					"5. Step or run the Map tasks\n" +
					"6. Press Shuffle, then Sort\n" +
					"7. Step or run the Reduce tasks\n" +
					"8. Press the Write output button", "Getting started", JOptionPane.INFORMATION_MESSAGE);
		} else if (source == tipsMenuItem) {
			JOptionPane.showMessageDialog(null, "The number of mappers is equal to the number of input files.\n" +
					"After each mapper step, the destination reducer is displayed for each mapper.\n" +
					"MapReduce jobs can implement custom partitioners, sort and group comparators.\n" +
					"Hover the mouse pointer over keys or values to view details.",
					"Tips and tricks...", JOptionPane.INFORMATION_MESSAGE);
		} else if (source == aboutMenuItem) {
			JOptionPane.showMessageDialog(null, "MapReduce Viewer 1.0a\n" +
					"Copyright Madhu Siddalingaiah, 2014\n" +
					"madhu@madhu.com", "About", JOptionPane.INFORMATION_MESSAGE);
		} else if (source == jobCombo) {
			doJobSelect();
		} else if (source == goButton) {
			try {
				doGoButton();
			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null, exc.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == exitMenuItem) {
			System.exit(0);
		}
	}

	private void doChooseInput() {
		File dir = new File(inputDirTF.getText());
		if (!dir.exists()) {
			dir = new File(System.getProperty("user.dir"));
		}
		JFileChooser chooser = new JFileChooser(dir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			inputDirectory = chooser.getSelectedFile();
			inputDirTF.setText(inputDirectory.getAbsolutePath());
			goButton.setEnabled(outputDirectory != null);
		}
	}

	private void doChooseOutput() {
		File dir = new File(outputDirTF.getText());
		if (!dir.exists()) {
			dir = new File(System.getProperty("user.dir"));
		}
		JFileChooser chooser = new JFileChooser(dir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			outputDirectory = chooser.getSelectedFile();
			outputDirTF.setText(outputDirectory.getAbsolutePath());
			goButton.setEnabled(inputDirectory != null);
		}
	}

	private void doGoButton() throws Exception {
		inputDirectory = new File(inputDirTF.getText());
		if (!inputDirectory.exists()) {
			String msg = String.format("Input directory %s does not exist", inputDirectory);
			JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		outputDirectory = new File(outputDirTF.getText());
		if (outputDirectory.exists() && outputDirectory.list().length > 0) {
			String message = String.format("Output directory\n%s\nis not empty, " +
					"files may be overwritten", outputDirectory.getAbsolutePath());
			JOptionPane.showMessageDialog(null, message, "About", JOptionPane.WARNING_MESSAGE);
		}
		MapReduceJob job = (MapReduceJob) jobCombo.getSelectedItem();
		if (formPanel != null) {
			formPanel.collectValues();
		}
		job.setInputDirectory(inputDirectory);
		job.setInputReaderClass(KeyValueReader.getReaderClass((String) inputCombo.getSelectedItem()));
		job.setOutputDirectory(outputDirectory);
		job.setOutputWriterClass(KeyValueWriter.getWriterClass((String) outputCombo.getSelectedItem()));
		job.init();
		MRPanel p = new MRPanel(job);
		JFrame f = new JFrame(job.getName());
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		f.getContentPane().add(p);
		f.pack();
		f.setVisible(true);
	}

	private void doJobSelect() {
		MapReduceJob job = (MapReduceJob) jobCombo.getSelectedItem();
		jobTextArea.setText(job.getDescription());
		Object params = job.getParameters();
		if (params == null) {
			splitPane.setBottomComponent(new JLabel("No configurable parameters"));
			formPanel = null;
		} else {
			try {
				formPanel = new FormPanel(params);
				splitPane.setBottomComponent(formPanel);
				pack();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String propName = "props.txt";
		if (args.length > 0) {
			propName = args[0];
		}
		File file = new File(propName);
		if (!file.exists()) {
			System.out.println("Unable to find properties file " + propName);
			System.exit(1);
		}
		Properties props = new Properties();
		props.load(new FileReader(file));
		MainFrame m = new MainFrame("MapReduce Viewer", props);
		m.setVisible(true);
	}
}
