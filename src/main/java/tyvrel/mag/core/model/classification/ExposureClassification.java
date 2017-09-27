package tyvrel.mag.core.model.classification;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Class that represents exposure classification defined by EN 206
 */
public class ExposureClassification extends AbstractClassification {
	private static final String FORMAT_REGEX = "^X[a-zA-Z]*[0-9]+$";

	/**
	 * Creates exposure classification denoted by <code>symbol</code>
	 *
	 * @param symbol exposure class' symbol
	 * @throws ImproperDataException if data is improper
	 */
	public ExposureClassification(String symbol) throws ImproperDataException {
		super(FORMAT_REGEX, symbol);
	}

	@Override
	public String toString() {
		return "ExposureClassification{} " + super.toString();
	}
}
