package tyvrel.mag.core.factory.classification;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.classification.ExposureClassification;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ExposureClassificationFactory extends AbstractClassificationFactory<ExposureClassification> {

	public static final ExposureClassification X0 = generateExposureClassification("X0");
	public static final ExposureClassification XC1 = generateExposureClassification("XC1");
	public static final ExposureClassification XC2 = generateExposureClassification("XC2");
	public static final ExposureClassification XC3 = generateExposureClassification("XC3");
	public static final ExposureClassification XC4 = generateExposureClassification("XC4");
	public static final ExposureClassification XS1 = generateExposureClassification("XS1");
	public static final ExposureClassification XS2 = generateExposureClassification("XS2");
	public static final ExposureClassification XS3 = generateExposureClassification("XS3");
	public static final ExposureClassification XD1 = generateExposureClassification("XD1");
	public static final ExposureClassification XD2 = generateExposureClassification("XD2");
	public static final ExposureClassification XD3 = generateExposureClassification("XD3");

	public ExposureClassificationFactory() {
		abstractClassificationMap.put("X0", X0);
		abstractClassificationMap.put("XC1", XC1);
		abstractClassificationMap.put("XC2", XC2);
		abstractClassificationMap.put("XC3", XC3);
		abstractClassificationMap.put("XC4", XC4);
		abstractClassificationMap.put("XS1", XS1);
		abstractClassificationMap.put("XS2", XS2);
		abstractClassificationMap.put("XS3", XS3);
		abstractClassificationMap.put("XD1", XD1);
		abstractClassificationMap.put("XD2", XD2);
		abstractClassificationMap.put("XD3", XD3);
	}

	public static ExposureClassification generateExposureClassification(String symbol) {
		try {
			return new ExposureClassification(symbol);
		} catch (ImproperDataException e) {
			return null;
		}
	}
}
