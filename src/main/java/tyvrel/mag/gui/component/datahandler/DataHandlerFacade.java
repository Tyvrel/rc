package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.core.model.*;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.gui.component.ApplicationFrame;

import java.util.Arrays;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class DataHandlerFacade {
	private ApplicationFrame applicationFrame;

	public DataHandlerFacade(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	public ConcreteClassification[] getConcreteClassifications() throws DataHandlerException {
		return new ConcreteClassificationHandler(applicationFrame).get();
	}

	public ExposureClassification[] getExposureClassifications() throws DataHandlerException {
		return new ExposureClassificationsHandler(applicationFrame).get();
	}

	public Load[] getLoads() throws DataHandlerException {
		return new LoadsHandler(applicationFrame).get();
	}

	public Factors getPartialFactors() throws DataHandlerException {
		return new PartialFactorsHandler(applicationFrame).get();
	}

	public Range getHeightRange() throws DataHandlerException {
		return new ShapeHandler(applicationFrame).getHeightRange();
	}

	public Range getWidthRange() throws DataHandlerException {
		return new ShapeHandler(applicationFrame).getWidthRange();
	}

	public Steel getLongitudinalReinforcementSteel() throws DataHandlerException {
		return new LongitudinalReinforcementSteelHandler(applicationFrame).get();
	}

	public Steel getShearReinforcementSteel() throws DataHandlerException {
		return new ShearReinforcementSteelHandler(applicationFrame).get();
	}

	public double[] getFias() throws DataHandlerException {
		return Arrays.stream(new ReinforcementDiametersHandler(applicationFrame).getFias())
				.mapToDouble(value -> value)
				.toArray();
	}

	public double[] getFibs() throws DataHandlerException {
		return Arrays.stream(new ReinforcementDiametersHandler(applicationFrame).getFibs())
				.mapToDouble(value -> value)
				.toArray();
	}

	public double[] getFisws() throws DataHandlerException {
		return Arrays.stream(new ReinforcementDiametersHandler(applicationFrame).getFisws())
				.mapToDouble(value -> value)
				.toArray();
	}

	public String getLongitudinalReinforcementSteelType() throws DataHandlerException {
		return new LongitudinalReinforcementSteelTypeHandler(applicationFrame).get();
	}

	public String getShearReinforcementSteelType() throws DataHandlerException {
		return new ShearReinforcementSteelTypeHandler(applicationFrame).get();
	}

	public PriceList getPriceList() throws DataHandlerException {
		return new PriceListHandler(applicationFrame).get();
	}

	public boolean is100yWorkingLife() throws DataHandlerException {
		return new ConcreteCoverDataHandler(applicationFrame).is100yWorkingLife();
	}

	public boolean isQualityEnsured() throws DataHandlerException {
		return new ConcreteCoverDataHandler(applicationFrame).isQualityEnsured();
	}

	public boolean isHighAir() throws DataHandlerException {
		return new ConcreteCoverDataHandler(applicationFrame).isHighAir();
	}

	public double getDgNomMax() throws DataHandlerException {
		return new ConcreteCoverDataHandler(applicationFrame).getDgNomMax();
	}
}
