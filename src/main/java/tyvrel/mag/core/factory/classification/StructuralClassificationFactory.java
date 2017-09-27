package tyvrel.mag.core.factory.classification;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.classification.StructuralClassification;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class StructuralClassificationFactory extends AbstractClassificationFactory<StructuralClassification> {

	public static final StructuralClassification S1 = generateStructuralClassification("S1");
	public static final StructuralClassification S2 = generateStructuralClassification("S2");
	public static final StructuralClassification S3 = generateStructuralClassification("S3");
	public static final StructuralClassification S4 = generateStructuralClassification("S4");
	public static final StructuralClassification S5 = generateStructuralClassification("S5");
	public static final StructuralClassification S6 = generateStructuralClassification("S6");

	public StructuralClassificationFactory() {
		abstractClassificationMap.put("S1", S1);
		abstractClassificationMap.put("S2", S2);
		abstractClassificationMap.put("S3", S3);
		abstractClassificationMap.put("S4", S4);
		abstractClassificationMap.put("S5", S5);
		abstractClassificationMap.put("S6", S6);
	}

	public static StructuralClassification generateStructuralClassification(String symbol) {
		try {
			return new StructuralClassification(symbol);
		} catch (ImproperDataException e) {
			return null;
		}
	}
}
