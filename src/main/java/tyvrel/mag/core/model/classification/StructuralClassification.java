package tyvrel.mag.core.model.classification;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Class that represents structural classification defined by PN-EN 1990
 */
public class StructuralClassification extends AbstractClassification implements Comparable<StructuralClassification> {
	private static final String FORMAT_REGEX = "^S-*[0-9]+$";

	/**
	 * Creates structural classification denoted by <code>symbol</code>
	 *
	 * @param symbol structural class' symbol, can be null
	 * @throws ImproperDataException if data is improper
	 */
	public StructuralClassification(String symbol) throws ImproperDataException {
		super(FORMAT_REGEX, symbol);
	}

	@Override
	public int compareTo(StructuralClassification arg0) {
		if (arg0 == null) throw new NullPointerException("Object cannot be null");
		String otherSymbol = arg0.getSymbol();
		if (otherSymbol == null && getSymbol() == null) return 0;
		if (getSymbol().equals(otherSymbol)) return 0;
		if (getSymbol() == null || otherSymbol == null) throw new NullPointerException("Cannot compare to null");
		Integer thisStrength = Integer.parseInt(getSymbol().replace("S", ""));
		Integer otherStrength = Integer.parseInt(otherSymbol.replace("S", ""));
		return thisStrength.compareTo(otherStrength);
	}

	@Override
	public String toString() {
		return "StructuralClassification{} " + super.toString();
	}
}
