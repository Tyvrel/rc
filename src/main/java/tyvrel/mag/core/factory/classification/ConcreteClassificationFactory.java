package tyvrel.mag.core.factory.classification;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.classification.ConcreteClassification;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Builds ConcreteClassification instances defined by EN 206-1
 */
public class ConcreteClassificationFactory extends AbstractClassificationFactory<ConcreteClassification> {
	public static final ConcreteClassification C1215;
	public static final ConcreteClassification C1620;
	public static final ConcreteClassification C2025;
	public static final ConcreteClassification C2530;
	public static final ConcreteClassification C3037;
	public static final ConcreteClassification C3545;
	public static final ConcreteClassification C4050;
	public static final ConcreteClassification C4555;
	public static final ConcreteClassification C5060;
	public static final ConcreteClassification C5567;
	public static final ConcreteClassification C6075;
	public static final ConcreteClassification C7085;
	public static final ConcreteClassification C8095;
	public static final ConcreteClassification C90105;

	public ConcreteClassificationFactory() {
		abstractClassificationMap.put(C1215.getSymbol(), C1215);
		abstractClassificationMap.put(C1620.getSymbol(), C1620);
		abstractClassificationMap.put(C2025.getSymbol(), C2025);
		abstractClassificationMap.put(C2530.getSymbol(), C2530);
		abstractClassificationMap.put(C3037.getSymbol(), C3037);
		abstractClassificationMap.put(C3545.getSymbol(), C3545);
		abstractClassificationMap.put(C4050.getSymbol(), C4050);
		abstractClassificationMap.put(C4555.getSymbol(), C4555);
		abstractClassificationMap.put(C5060.getSymbol(), C5060);
		abstractClassificationMap.put(C5567.getSymbol(), C5567);
		abstractClassificationMap.put(C6075.getSymbol(), C6075);
		abstractClassificationMap.put(C7085.getSymbol(), C7085);
		abstractClassificationMap.put(C8095.getSymbol(), C8095);
		abstractClassificationMap.put(C90105.getSymbol(), C90105);
	}

	static {
		double[][] rawData = { //
				{12, 16, 20, 25, 30, 35, 40, 45, 50, 55, 60, 70, 80, 90}, //
				{15, 20, 25, 30, 37, 45, 50, 55, 60, 67, 75, 85, 95, 105}, //
				{20, 24, 28, 33, 38, 43, 48, 53, 58, 63, 68, 78, 88, 98}, //
				{1.6, 1.9, 2.2, 2.6, 2.9, 3.2, 3.5, 3.8, 4.1, 4.2, 4.4, 4.6, 4.8, 5.0}, //
				{1.1, 1.3, 1.5, 1.8, 2.0, 2.2, 2.5, 2.7, 2.9, 3.0, 3.1, 3.2, 3.4, 3.5}, //
				{2.0, 2.5, 2.9, 3.3, 3.8, 4.2, 4.6, 4.9, 5.3, 5.5, 5.7, 6.0, 6.3, 6.6}, //
				{27, 29, 30, 31, 32, 34, 35, 36, 37, 38, 39, 41, 42, 44}, //
				{1.8, 1.9, 2.0, 2.1, 2.2, 2.25, 2.3, 2.4, 2.45, 2.5, 2.6, 2.7, 2.8, 2.8}, //
				{3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.2, 3.0, 2.8, 2.8, 2.8}, //
				{2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.2, 2.3, 2.4, 2.5, 2.6}, //
				{3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.1, 2.9, 2.7, 2.6, 2.6}, //
				{2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 1.75, 1.6, 1.45, 1.4, 1.4}, //
				{1.75, 1.75, 1.75, 1.75, 1.75, 1.75, 1.75, 1.75, 1.75, 1.8, 1.9, 2.0, 2.2, 2.3}, //
				{3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 3.1, 2.9, 2.7, 2.6, 2.6} //
		};

		C1215 = generateConcreteClassification("C12/15", rawData, 0);
		C1620 = generateConcreteClassification("C16/20", rawData, 1);
		C2025 = generateConcreteClassification("C20/25", rawData, 2);
		C2530 = generateConcreteClassification("C25/30", rawData, 3);
		C3037 = generateConcreteClassification("C30/37", rawData, 4);
		C3545 = generateConcreteClassification("C35/45", rawData, 5);
		C4050 = generateConcreteClassification("C40/50", rawData, 6);
		C4555 = generateConcreteClassification("C45/55", rawData, 7);
		C5060 = generateConcreteClassification("C50/60", rawData, 8);
		C5567 = generateConcreteClassification("C55/67", rawData, 9);
		C6075 = generateConcreteClassification("C60/75", rawData, 10);
		C7085 = generateConcreteClassification("C70/85", rawData, 11);
		C8095 = generateConcreteClassification("C80/95", rawData, 12);
		C90105 = generateConcreteClassification("C90/105", rawData, 13);
	}

	private static ConcreteClassification generateConcreteClassification(String symbol, double[][] rawData,
	                                                                     int columnIndex) {
		ConcreteClassification concreteClassification = null;
		try {
			concreteClassification = new ConcreteClassification(
					symbol,
					rawData[0][columnIndex] * 1000000,
					rawData[1][columnIndex] * 1000000,
					rawData[2][columnIndex] * 1000000,
					rawData[3][columnIndex] * 1000000,
					rawData[4][columnIndex] * 1000000,
					rawData[5][columnIndex] * 1000000,
					rawData[6][columnIndex] * 1000000000,
					rawData[7][columnIndex] * 0.001,
					rawData[8][columnIndex] * 0.001,
					rawData[9][columnIndex] * 0.001,
					rawData[10][columnIndex] * 0.001,
					rawData[11][columnIndex] * 0.001,
					rawData[12][columnIndex] * 0.001,
					rawData[13][columnIndex] * 0.001);
		} catch (ImproperDataException e) {
		}
		return concreteClassification;
	}
}
