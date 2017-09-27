package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.shearreinforcement.ShearReinforcementAnchorageLengthFactory;
import tyvrel.mag.gui.component.datahandler.DataHandlerException;
import tyvrel.mag.gui.view.ReinforcementDiameterPanel;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class ShearReinforcementAnchorageLengthCard extends AbstractCard {
	private ReinforcementDiameterPanel reinforcementDiameterPanel;

	public ShearReinforcementAnchorageLengthCard(String name) {
		super(name);
	}

	protected void modifyDataPanel(JPanel jPanel) {
		reinforcementDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia", false);
		jPanel.add(reinforcementDiameterPanel);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		return "Obliczona długość zakotwienia wynosi: "
				+ String.format("%.0f", calculateAnchorageLength() * 1000) + "mm";
	}

	protected double getPhi() throws DataHandlerException {
		try {
			return pos(() -> reinforcementDiameterPanel.getDiameters()[0]);
		} catch (ImproperDataException | LSException e) {
			throw new DataHandlerException();
		}
	}

	protected double calculateAnchorageLength() throws ImproperDataException, LSException, DataHandlerException {
		return new ShearReinforcementAnchorageLengthFactory(getPhi()).build();
	}
}
