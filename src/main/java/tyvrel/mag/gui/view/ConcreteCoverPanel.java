package tyvrel.mag.gui.view;

import tyvrel.mag.gui.component.LabeledComboBox;
import tyvrel.mag.gui.component.LabeledTextField;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ConcreteCoverPanel extends BaseJPanel {
	private JComboBox<String> jcbWorkingLife;
	private JCheckBox jckSpecialQualityAssurance;
	private JCheckBox jckHighAir;
	private JTextField jtfDgNomMax;

	public ConcreteCoverPanel() {
		super("Dodatkowe parametry");
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(50);
		setLayout(flowLayout);

		LabeledComboBox jlcbWorkingLife = new LabeledComboBox("Projektowy okres użytkowania:", new String[]{"50 " +
				"lat", "100 lat"});
		jckSpecialQualityAssurance = new JCheckBox("Zapewniona specjalna kontrola jakości betonu");
		jckHighAir = new JCheckBox("Stosuje się zawartość powietrza wyższą niż 4%");
		LabeledTextField jltfMaxAggregateDiameter = new LabeledTextField("Maksymalna średnica kruszywa [mm]:", "32");

		jtfDgNomMax = jltfMaxAggregateDiameter.getjTextField();
		jcbWorkingLife = jlcbWorkingLife.getjComboBox();

		add(jlcbWorkingLife);
		add(jckSpecialQualityAssurance);
		add(jckHighAir);
		add(jltfMaxAggregateDiameter);
	}

	public boolean is100yWorkingLife() {
		return jcbWorkingLife.getSelectedItem()
				.equals("100 lat");
	}

	public boolean isQualityEnsured() {
		return jckSpecialQualityAssurance.isSelected() && jckSpecialQualityAssurance.isEnabled();
	}

	public boolean isHighAir() {
		return jckHighAir.isSelected() && jckHighAir.isEnabled();
	}

	public double getDgNomMax() {
		return Double.parseDouble(jtfDgNomMax.getText()) / 1000;
	}
}
