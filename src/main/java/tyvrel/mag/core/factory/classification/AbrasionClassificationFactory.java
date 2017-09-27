package tyvrel.mag.core.factory.classification;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.classification.AbrasionClassification;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class AbrasionClassificationFactory extends AbstractClassificationFactory<AbrasionClassification> {
	public static final AbrasionClassification XM0 = generateAbrasionClassification("XM0");
	public static final AbrasionClassification XM1 = generateAbrasionClassification("XM1");
	public static final AbrasionClassification XM2 = generateAbrasionClassification("XM2");
	public static final AbrasionClassification XM3 = generateAbrasionClassification("XM3");

	public AbrasionClassificationFactory() {
		abstractClassificationMap.put("XM0", XM0);
		abstractClassificationMap.put("XM1", XM1);
		abstractClassificationMap.put("XM2", XM2);
		abstractClassificationMap.put("XM3", XM3);
	}

	public static AbrasionClassification generateAbrasionClassification(String symbol) {
		try {
			return new AbrasionClassification(symbol);
		} catch (ImproperDataException e) {
			return null;
		}
	}
}
