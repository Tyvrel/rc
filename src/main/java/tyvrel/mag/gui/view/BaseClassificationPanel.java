package tyvrel.mag.gui.view;


import tyvrel.mag.gui.component.DataCheckBox;
import tyvrel.mag.core.model.classification.AbstractClassification;
import tyvrel.mag.core.factory.classification.AbstractClassificationFactory;

import javax.swing.*;
import java.util.*;
import java.util.function.IntFunction;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class BaseClassificationPanel<T extends AbstractClassification> extends BaseJPanel {
	private List<DataCheckBox> checkBoxList = new ArrayList<>();

	public BaseClassificationPanel(String title, AbstractClassificationFactory<T> abstractClassFactory) {
		this(title, abstractClassFactory, true);
	}

	public BaseClassificationPanel(String title, AbstractClassificationFactory<T> abstractClassFactory, boolean
			multipleChoice) {
		super(title);

		ButtonGroup buttonGroup = new ButtonGroup();
		ArrayList<T> abstractClasses = new ArrayList<>(abstractClassFactory.getAll());
		abstractClasses.sort(Comparator.comparing(AbstractClassification::getSymbol));
		for (T abstractClass : abstractClasses) {
			DataCheckBox<T> checkBox = new DataCheckBox<>(abstractClass.getSymbol(), abstractClass);
			checkBoxList.add(checkBox);
			add(checkBox);
			if (!multipleChoice) buttonGroup.add(checkBox);
		}
		checkBoxList.get(0)
				.setSelected(true);
	}

	@SuppressWarnings("unchecked")
	public T[] getClassifications(IntFunction<T[]> generator) {
		return checkBoxList
				.stream()
				.filter(checkBox -> checkBox.isEnabled() && checkBox.isSelected())
				.map(DataCheckBox::getT)
				.toArray(generator);
	}
}
