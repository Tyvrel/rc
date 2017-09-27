package tyvrel.mag.gui.component;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.Range;
import tyvrel.mag.gui.filter.DocumentCharacterFilter;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class RangeTextField extends JPanel {
	private JTextField jtfFrom;
	private JTextField jtfTo;
	private JTextField jtfIncrement;
	private JCheckBox jcbIsRange;

	public RangeTextField(String text) {
		this(text, "0", "0", "0");
	}

	public RangeTextField(String text, String from, String to, String increment) {
		super();
		add(new JLabel(text));
		jtfFrom = createJTextField(from);
		add(jtfFrom);
		add(new JLabel(" \u2014 "));
		jtfTo = createJTextField(to);
		add(jtfTo);
		add(new JLabel(" co "));
		jtfIncrement = createJTextField(increment);
		add(jtfIncrement);
		jcbIsRange = new JCheckBox("Przedział wartości", true);
		jcbIsRange.addActionListener(a -> {
			if (((JCheckBox) a.getSource()).isSelected()) {
				jtfTo.setEnabled(true);
				jtfIncrement.setEnabled(true);
			} else {
				jtfTo.setEnabled(false);
				jtfIncrement.setEnabled(false);
			}
		});
		add(jcbIsRange);
	}

	private JTextField createJTextField(String text) {
		JTextField jTextField = new JTextField(9);
		((AbstractDocument) jTextField.getDocument()).setDocumentFilter(new DocumentCharacterFilter());
		jTextField.setText(text);
		return jTextField;
	}

	public Range getRange() throws ImproperDataException {
		return (jcbIsRange.isSelected())
				? new Range(
				Double.parseDouble(jtfFrom.getText()
						.replace(",", ".")),
				Double.parseDouble(jtfTo.getText()
						.replace(",", ".")),
				Double.parseDouble(jtfIncrement.getText()
						.replace(",", ".")))
				: new Range(
				Double.parseDouble(jtfFrom.getText()
						.replace(",", ".")));
	}
}
