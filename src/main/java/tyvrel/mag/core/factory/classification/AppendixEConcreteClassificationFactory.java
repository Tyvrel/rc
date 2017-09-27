package tyvrel.mag.core.factory.classification;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.classification.AbstractClassification;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.WidthTooSmallException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class AppendixEConcreteClassificationFactory implements Factory<ConcreteClassification> {
	private ExposureClassification[] exposureClasses;

	public AppendixEConcreteClassificationFactory(ExposureClassification[] exposureClasses) {
		this.exposureClasses = exposureClasses;
	}

	@Override
	public ConcreteClassification build() throws ImproperDataException, WidthTooSmallException {
		ConcreteClassificationFactory concreteClassificationFactory = new ConcreteClassificationFactory();
		String concreteClassSymbol = Arrays.stream(exposureClasses)
				.map(AbstractClassification::getSymbol)
				.map(exposureClassSymbol -> map.get(exposureClassSymbol))
				.distinct()
				.max(Comparator.naturalOrder())
				.orElse("C12/15");
		return concreteClassificationFactory.get(concreteClassSymbol);
	}

	private static Map<String, String> map = new HashMap<>();

	static {
		map.put("XC1", "C20/25");
		map.put("XC2", "C25/30");
		map.put("XC3", "C30/37");
		map.put("XC4", "C30/37");

		map.put("XD1", "C30/37");
		map.put("XD2", "C30/37");
		map.put("XD3", "C35/45");

		map.put("XS1", "C30/37");
		map.put("XS2", "C35/45");
		map.put("XS3", "C35/45");

		map.put("X0", "C12/15");

		map.put("XF1", "C30/37");
		map.put("XF2", "C25/30");
		map.put("XF3", "C30/37");

		map.put("XA1", "C30/37");
		map.put("XA2", "C30/37");
		map.put("XA3", "C35/45");
	}
}
