package tyvrel.mag.gui.view;

import tyvrel.mag.gui.component.DataCheckBox;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ReinforcementDiameterPanel extends BaseJPanel {
	private List<DataCheckBox> checkBoxList = new ArrayList<>();

	public ReinforcementDiameterPanel(String title) {
		this(title, true);
	}

	public ReinforcementDiameterPanel(String title, boolean multipleChoice) {
		super(title);
		ButtonGroup buttonGroup = new ButtonGroup();
		int[] diameters = {6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 28, 32};
		for (int diameter : diameters) {
			String symbol = "\u00D8" + diameter;
			double value = (double) diameter / 1000;
			DataCheckBox<Double> checkBox = new DataCheckBox<>(symbol, value);
			checkBoxList.add(checkBox);
			add(checkBox);
			if (!multipleChoice) buttonGroup.add(checkBox);
		}
		checkBoxList.get(5).setSelected(true);
	}

	public Double[] getDiameters() {
		return checkBoxList.stream()
				.filter(checkBox -> checkBox.isEnabled() && checkBox.isSelected())
				.map(DataCheckBox::getT)
				.toArray(Double[]::new);
	}
}
