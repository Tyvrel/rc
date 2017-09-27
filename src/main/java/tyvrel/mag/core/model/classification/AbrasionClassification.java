package tyvrel.mag.core.model.classification;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Class that represents abrasion classification according to EN 1992-1-1
 */
public class AbrasionClassification extends AbstractClassification {
	private static final String FORMAT_REGEX = "^XM[0-9]+$";

	/**
	 * Creates abrasion classification denoted by <code>symbol</code>
	 *
	 * @param symbol abrasion class' symbol
	 * @throws ImproperDataException if data is improper
	 */
	public AbrasionClassification(String symbol) throws ImproperDataException {
		super(FORMAT_REGEX, symbol);
	}

	@Override
	public String toString() {
		return "AbrasionClassification{} " + super.toString();
	}
}
