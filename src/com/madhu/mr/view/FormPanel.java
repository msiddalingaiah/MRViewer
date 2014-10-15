
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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormPanel<E> extends JPanel {
	private GridBagConstraints lastConstraints;
	private GridBagConstraints middleConstraints;
	private GridBagConstraints labelConstraints;
	private GridBagLayout layout;
	private E object;
	private ArrayList<Formatter> formatters;

	public FormPanel(E object) throws Exception {
		this(new Insets(5, 5, 1, 5), object);
	}

	public FormPanel(Insets insets, E object) throws Exception {
		this.object = object;
		layout = new GridBagLayout();
		setLayout(layout);
		lastConstraints = new GridBagConstraints();

		// Stretch components horizontally (but not vertically)
		lastConstraints.fill = GridBagConstraints.HORIZONTAL;

		// Components that are too short or narrow for their space
		// Should be pinned to the northwest (upper left) corner
		lastConstraints.anchor = GridBagConstraints.FIRST_LINE_START;

		// Give the "last" component as much space as possible
		lastConstraints.weightx = 1.0;

		// Give the "last" component the remainder of the row
		lastConstraints.gridwidth = GridBagConstraints.REMAINDER;

		// Add a little padding
		lastConstraints.insets = insets;

		// Now for the "middle" field components
		middleConstraints = (GridBagConstraints) lastConstraints.clone();

		// These still get as much space as possible, but do
		// not close out a row
		middleConstraints.gridwidth = GridBagConstraints.RELATIVE;

		// And finally the "label" constrains, typically to be
		// used for the first component on each row
		labelConstraints = (GridBagConstraints) lastConstraints.clone();

		// Give these as little space as necessary
		labelConstraints.weightx = 0.0;
		labelConstraints.gridwidth = 1;
		// Right justify
		labelConstraints.anchor = GridBagConstraints.LINE_END;
		labelConstraints.fill = GridBagConstraints.NONE;
		
		formatters = new ArrayList<Formatter>();
		
		Field[] fields = object.getClass().getFields();
		for (Field field : fields) {
			Formatter f = Formatter.getInstance(field, object);
			addRow(f.getLabel(), f.getComponent());
			f.fromObject();
			formatters.add(f);
		}
	}

	public E collectValues() throws Exception {
		for (Formatter f : formatters) {
			f.toObject();
		}
		return object;
	}

	public void addRow(String label, Component ... cs) {
		addRow(new JLabel(label), cs);
	}

	public void addRow(JLabel label, Component ... cs) {
		layout.setConstraints(label, labelConstraints);
		add(label);
		int n = cs.length - 1;
		if (n >= 0) {
			for (int i = 0; i < n; i++) {
				layout.setConstraints(cs[i], middleConstraints);
				add(cs[i]);
			}
			layout.setConstraints(cs[n], lastConstraints);
			add(cs[n]);
		}
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		final FormPanel form = new FormPanel(new Parameters());
//		form.addRow("Number of reducers", new JTextField(10));
//		form.addRow("Use combiner", new JCheckBox());
//		form.addRow("That, that", new JCheckBox(), new JCheckBox());
//		form.addRow("Other", new JTextField(), new JTextField());
		
		JFrame jf = new JFrame("Test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(form, BorderLayout.CENTER);
		JButton go = new JButton("Go!");
		JPanel south = new JPanel();
		south.add(go);
		jf.getContentPane().add(south, BorderLayout.SOUTH);
		go.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(form.collectValues());
				} catch (Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(null, exc.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		jf.pack();
		jf.setVisible(true);
	}
}

class Parameters {
	public String First_name;
	public String value;
	public int number;
	public boolean flag;
	
	public Parameters() {
		First_name = "this";
		value = "that";
		number = 951;
		flag = true;
	}

	public String toString() {
		return String.format("%s: %s %d %s", First_name, value, number, flag);
	}
}

abstract class Formatter {
	private Field field;
	private Object object;

	public Formatter(Field field, Object object) {
		this.field = field;
		this.object = object;
	}

	public void setValue(Object value) throws Exception {
		field.set(object, value);
	}
	
	public Object getValue() throws Exception {
		return field.get(object);
	}
	
	public String getLabel() {
		return field.getName().replace('_', ' ');
	}
	
	public static Formatter getInstance(Field field, Object object) {
		if (field.getType().isAssignableFrom(String.class)) {
			return new StringFormatter(field, object, 10);
		}
		if (field.getType().isAssignableFrom(int.class)) {
			return new IntFormatter(field, object, 10);
		}
		if (field.getType().isAssignableFrom(boolean.class)) {
			return new BooleanFormatter(field, object);
		}
		throw new IllegalArgumentException("Unexpected field: " + field.getName());
	}

	public abstract void toObject() throws Exception;
	public abstract void fromObject() throws Exception;
	public abstract JComponent getComponent();
}

class StringFormatter extends Formatter {
	private JTextField tf;

	public StringFormatter(Field field, Object object, int length) {
		super(field, object);
		this.tf = new JTextField(length);
	}

	@Override
	public void toObject() throws Exception {
		setValue(tf.getText());
	}

	@Override
	public void fromObject() throws Exception {
		tf.setText((String) getValue());
	}

	@Override
	public JComponent getComponent() {
		return tf;
	}
}

class IntFormatter extends Formatter {
	private JTextField tf;

	public IntFormatter(Field field, Object object, int length) {
		super(field, object);
		this.tf = new JTextField(length);
	}

	@Override
	public void toObject() throws Exception {
		setValue(Integer.parseInt(tf.getText()));
	}

	@Override
	public void fromObject() throws Exception {
		tf.setText(String.format("%d", getValue()));
	}

	@Override
	public JComponent getComponent() {
		return tf;
	}
}

class BooleanFormatter extends Formatter {
	private JCheckBox tf;

	public BooleanFormatter(Field field, Object object) {
		super(field, object);
		this.tf = new JCheckBox();
	}

	@Override
	public void toObject() throws Exception {
		setValue(tf.isSelected());
	}

	@Override
	public void fromObject() throws Exception {
		tf.setSelected((Boolean) getValue());
	}

	@Override
	public JComponent getComponent() {
		return tf;
	}
}
