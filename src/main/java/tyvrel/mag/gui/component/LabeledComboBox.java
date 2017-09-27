package tyvrel.mag.gui.component;

import javax.swing.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class LabeledComboBox extends JPanel {
	private JLabel jLabel;
	private JComboBox<String> jComboBox;

	public LabeledComboBox(String title, String[] items) {
		super();
		jLabel = new JLabel(title);
		add(jLabel);
		jComboBox = new JComboBox<>(items);
		add(jComboBox);
	}

	public JLabel getjLabel() {
		return jLabel;
	}

	public JComboBox<String> getjComboBox() {
		return jComboBox;
	}
}
