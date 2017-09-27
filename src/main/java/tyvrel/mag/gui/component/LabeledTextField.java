package tyvrel.mag.gui.component;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.gui.filter.DocumentCharacterFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class LabeledTextField extends JPanel {
	private JLabel jLabel;
	private JTextField jTextField;

	public LabeledTextField(String title) {
		this(title, "0");
	}

	public LabeledTextField(String title, String text) {
		super();
		jLabel = new JLabel(title);
		add(jLabel);
		jTextField = new JTextField(9);
		((AbstractDocument) jTextField.getDocument()).setDocumentFilter(new DocumentCharacterFilter());
		jTextField.setText(text);
		add(jTextField);
	}

	public double getDouble() throws ImproperDataException {
		try {
			return Double.parseDouble(jTextField.getText()
					.replace(",", "."));
		} catch (NumberFormatException e) {
			throw new ImproperDataException(e);
		}
	}

	public JLabel getjLabel() {
		return jLabel;
	}

	public JTextField getjTextField() {
		return jTextField;
	}
}
