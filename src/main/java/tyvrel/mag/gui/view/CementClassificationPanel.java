package tyvrel.mag.gui.view;

import tyvrel.mag.core.model.classification.CementClassification;

import javax.swing.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class CementClassificationPanel extends BaseJPanel {
	private JCheckBox jcbCementS;
	private JCheckBox jcbCementN;
	private JCheckBox jcbCementR;

	public CementClassificationPanel() {
		super("Klasa cementu");
		jcbCementS = new JCheckBox("Cement S");
		jcbCementN = new JCheckBox("Cement N");
		jcbCementR = new JCheckBox("Cement R");

		ButtonGroup bgCement = new ButtonGroup();
		bgCement.add(jcbCementS);
		bgCement.add(jcbCementN);
		bgCement.add(jcbCementR);
		jcbCementN.setSelected(true);

		add(jcbCementS);
		add(jcbCementN);
		add(jcbCementR);
	}

	public int getCementClassification() {
		if (jcbCementS.isSelected()) {
			return CementClassification.CEMENT_S;
		} else if (jcbCementR.isSelected()) {
			return CementClassification.CEMENT_R;
		} else {
			return CementClassification.CEMENT_N;
		}
	}
}
