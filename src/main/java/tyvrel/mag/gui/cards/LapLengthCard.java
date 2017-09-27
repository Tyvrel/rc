package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.factory.longitudinalreinforcement.LapLengthFactory;
import tyvrel.mag.core.factory.longitudinalreinforcement.LongitudinalReinforcementRequiredAnchorageLengthFactory;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.gui.component.LabeledComboBox;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.BaseClassificationPanel;
import tyvrel.mag.gui.view.BaseJPanel;
import tyvrel.mag.gui.view.ReinforcementDiameterPanel;
import tyvrel.mag.gui.view.SteelPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class LapLengthCard extends BaseJPanel {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel = new BaseClassificationPanel
			<>("Beton", new ConcreteClassificationFactory(), false);
	private LabeledTextField ltfGammaC = new LabeledTextField("<html>\u03B3<sub>C</sub> [-]:</html>", "1.4");
	private LabeledTextField ltfGammaS = new LabeledTextField("<html>\u03B3<sub>S</sub> [-]:</html>", "1.15");
	private SteelPanel steelPanel = new SteelPanel(new SteelFactory());
	private ReinforcementDiameterPanel reinforcementDiameterPanel = new ReinforcementDiameterPanel("Średnica " +
			"zbrojenia", false);
	private LabeledTextField ltfP1 = new LabeledTextField("Udział prętów połączonych na zakład w przekroju: [-]",
			"0.4");
	private LabeledComboBox lcbBondConditionsType;

	public LapLengthCard(String name) {
		super(name);

		setLayout(new GridLayout(0, 1));
		lcbBondConditionsType = new LabeledComboBox("Warunki przyczepności:", new String[]{"Dobre", "Złe"});
		add(concreteClassificationPanel);
		JPanel partialFactorsPanel = new BaseJPanel("Inne:");
		partialFactorsPanel.add(ltfGammaC);
		partialFactorsPanel.add(ltfGammaS);
		partialFactorsPanel.add(ltfP1);
		partialFactorsPanel.add(lcbBondConditionsType);
		add(partialFactorsPanel);
		add(steelPanel);
		add(reinforcementDiameterPanel);

		JPanel jPanel = new JPanel();
		JLabel jLabel = new JLabel();
		JButton jButton = new JButton("Oblicz");
		jButton.addActionListener(e -> {
			try {
				jLabel.setText(
						"Obliczona długość zakotwienia wynosi: " + String.format("%.0f", calculateLapLength() * 1000)
								+ "mm");
			} catch (ImproperDataException e1) {
				jLabel.setText("");
				JOptionPane.showMessageDialog(getTopLevelAncestor(), "Algorym zakończył się niepowodzeniem", "Błąd",
						JOptionPane.WARNING_MESSAGE);
			} catch (LSException e1) {
				jLabel.setText("");
				JOptionPane.showMessageDialog(getTopLevelAncestor(), "Przekroczony stan graniczny", "Błąd",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		jPanel.add(jButton);
		jPanel.add(jLabel);
		add(jPanel);
	}

	public double calculateLapLength() throws ImproperDataException, LSException {
		double lbrqd = new LongitudinalReinforcementRequiredAnchorageLengthFactory(
				concreteClassificationPanel.getClassifications(ConcreteClassification[]::new)[0],
				steelPanel.getSteel(),
				new Factors(ltfGammaS.getDouble(), ltfGammaC.getDouble()),
				reinforcementDiameterPanel.getDiameters()[0],
				getBondConditionsType())
				.build();
		return new LapLengthFactory(
				reinforcementDiameterPanel.getDiameters()[0],
				ltfP1.getDouble(),
				lbrqd
		).build();
	}

	public int getBondConditionsType() throws ImproperDataException {
		switch ((String) lcbBondConditionsType.getjComboBox().getSelectedItem()) {
			case "Dobre":
				return LongitudinalReinforcementRequiredAnchorageLengthFactory.GOOD_BOND_CONDITIONS;
			case "Złe":
				return LongitudinalReinforcementRequiredAnchorageLengthFactory.BAD_BOND_CONDITIONS;
			default:
				throw new ImproperDataException();
		}
	}

}
