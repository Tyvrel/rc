package tyvrel.mag.gui.view;

import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.classification.ConcreteClassification;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class MaterialsPanel extends BaseJPanel {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel;
	private SteelPanel longitudinalReinforcementSteelPanel;
	private SteelPanel shearReinforcementSteelPanel;
	private ReinforcementDiameterPanel asbReinforcementDiameterPanel;
	private ReinforcementDiameterPanel asaReinforcementDiameterPanel;
	private ReinforcementDiameterPanel aswReinforcementDiameterPanel;

	public MaterialsPanel() {
		super("Materiały");
		setLayout(new GridLayout(0, 1));

		concreteClassificationPanel =
				new BaseClassificationPanel<>("Klasy betonu", new ConcreteClassificationFactory());
		JPanel steelPanel = new BaseJPanel("Stal");
		steelPanel.setLayout(new GridLayout(1, 2));
		longitudinalReinforcementSteelPanel = new SteelPanel(new SteelFactory(), "Stal zbrojenia na ścinanie");
		shearReinforcementSteelPanel = new SteelPanel(new SteelFactory());
		steelPanel.add(longitudinalReinforcementSteelPanel);
		steelPanel.add(shearReinforcementSteelPanel);
		asbReinforcementDiameterPanel =
				new ReinforcementDiameterPanel("<html>Średnica zbrojenia podłużnego dołem (\u00D8<sub>sB</sub>)" +
						"</html>");
		asaReinforcementDiameterPanel =
				new ReinforcementDiameterPanel("<html>Średnica zbrojenia podłużnego górą (\u00D8<sub>sA</sub>)" +
						"</html>");
		aswReinforcementDiameterPanel =
				new ReinforcementDiameterPanel("<html>Średnica zbrojenia na ścinanie (\u00D8<sub>sw</sub>)</html>");

		add(concreteClassificationPanel);
		add(steelPanel);
		add(asbReinforcementDiameterPanel);
		add(asaReinforcementDiameterPanel);
		add(aswReinforcementDiameterPanel);
	}

	public ConcreteClassification[] getConcreteClassifications() {
		return concreteClassificationPanel.getClassifications(ConcreteClassification[]::new);
	}

	public Steel getLongitudinalReinforcementSteel() {
		return longitudinalReinforcementSteelPanel.getSteel();
	}

	public String getLongitudinalReinforcementSteelType() {
		return longitudinalReinforcementSteelPanel.getSteelType();
	}

	public Steel getShearReinforcementSteel() {
		return shearReinforcementSteelPanel.getSteel();
	}

	public String getShearReinforcementSteelType() {
		return shearReinforcementSteelPanel.getSteelType();
	}

	public Double[] getAsbDiameters() {
		return asbReinforcementDiameterPanel.getDiameters();
	}

	public Double[] getAsaDiameters() {
		return asaReinforcementDiameterPanel.getDiameters();
	}

	public Double[] getAswDiameters() {
		return aswReinforcementDiameterPanel.getDiameters();
	}


}
