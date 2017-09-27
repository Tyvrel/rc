package tyvrel.mag.gui.view;

import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.gui.component.LabeledComboBox;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class SteelPanel extends BaseJPanel {
	private JComboBox<String> jComboBox;
	private SteelFactory steelFactory;

	public SteelPanel(SteelFactory steelFactory) {
		this(steelFactory, "Stal zbrojenia podłużnego");
	}

	public SteelPanel(SteelFactory steelFactory, String title) {
		super(title);
		this.steelFactory = steelFactory;
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(50);
		setLayout(flowLayout);

		JLabel jLabel = new JLabel();
		LabeledComboBox labeledComboBox = new LabeledComboBox("Gatunek stali: ", steelFactory.getAll()
				.toArray(new String[0]));
		labeledComboBox.getjComboBox()
				.addActionListener(e -> setTextFromSelectedItem(jLabel, labeledComboBox.getjComboBox(), steelFactory));
		setTextFromSelectedItem(jLabel, labeledComboBox.getjComboBox(), steelFactory);
		add(labeledComboBox);
		add(jLabel);
		jComboBox = labeledComboBox.getjComboBox();
	}

	private void setTextFromSelectedItem(JLabel jLabel, JComboBox<String> jComboBox, SteelFactory steelFactory) {
		String name = (String) jComboBox.getSelectedItem();
		Steel steel = steelFactory.get(name);
		jLabel.setText("<html>Gatunek: " + name +
				"<br>E<sub>s</sub> = " + String.format("%.0f", steel.getEs() / 1000000000) + "GPa" +
				"<br>f<sub>y</sub> = " + String.format("%.0f", steel.getFy() / 1000000) + "MPa" +
				"<br>Klasa: " + steel.getClassification() + "</html>");
	}

	public Steel getSteel() {
		return steelFactory.get((String) jComboBox.getSelectedItem());
	}

	public String getSteelType() {
		return (String) jComboBox.getSelectedItem();
	}
}
