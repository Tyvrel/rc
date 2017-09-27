package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.stress.UncrackedStressFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.CrossSectionType;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.Stress;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.BaseJPanel;
import tyvrel.mag.gui.view.LoadPanel;
import tyvrel.mag.gui.view.ReinforcementDiameterPanel;
import tyvrel.mag.gui.view.ShapePanel;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class UncrackedStressCard extends AbstractCard {
	private ShapePanel shapePanel;
	private LoadPanel loadPanel;
	private ReinforcementDiameterPanel asaDiameterPanel;
	private ReinforcementDiameterPanel asbDiameterPanel;
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfCnom;


	public UncrackedStressCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		shapePanel = new ShapePanel();
		loadPanel = new LoadPanel();
		asaDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia górą", false);
		asbDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia dołem", false);
		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");

		JPanel pCnom = new BaseJPanel("Otulina");
		pCnom.add(ltfCnom);

		jPanel.add(shapePanel);
		jPanel.add(loadPanel);
		jPanel.add(asaDiameterPanel);
		jPanel.add(asbDiameterPanel);
		jPanel.add(asswDiameterPanel);
		jPanel.add(pCnom);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		Stress stress = calculateUncrackedStress();
		return "<html>Maksymalne naprężenia w betonie wynoszą: " + String.format("%.3f", stress.getSigmac() / 1000000)
				+ "MPa"
				+ "<br>Maksymalne naprężenia w stali wynoszą: " + String.format("%.3f", stress.getSigmas() / 1000000)
				+ "MPa</html>";
	}

	protected double getCnom() throws ImproperDataException, LSException {
		return pos(() -> ltfCnom.getDouble() / 1000);
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> shapePanel.getShape());
	}

	private double getPhia() throws ImproperDataException, LSException {
		return pos(() -> asaDiameterPanel.getDiameters()[0]);
	}

	private double getPhib() throws ImproperDataException, LSException {
		return pos(() -> asbDiameterPanel.getDiameters()[0]);
	}

	private double getPhisw() throws ImproperDataException, LSException {
		return pos(() -> asswDiameterPanel.getDiameters()[0]);
	}

	protected CrossSection getCrossSection() throws ImproperDataException, LSException {
		return new CrossSection(
				getShape(), null, null, null, CrossSectionType.BEAM,
				new LongitudinalReinforcement(
						new Reinforcement(0, getPhia(), 0, 0),
						new Reinforcement(0, getPhib(), 0, 0)
				),
				new ShearReinforcement(0, getPhisw(), 0, 0),
				getCnom()
		);
	}


	protected double getMa() throws ImproperDataException, LSException {
		return nonNeg(() -> loadPanel.getMa());
	}

	protected double getMb() throws ImproperDataException, LSException {
		return nonNeg(() -> loadPanel.getMb());
	}

	protected Stress calculateUncrackedStress() throws ImproperDataException, LSException {
		return new UncrackedStressFactory(getCrossSection(), getMa(), getMb()).build();
	}
}
