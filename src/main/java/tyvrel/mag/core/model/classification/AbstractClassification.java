package tyvrel.mag.core.model.classification;

import tyvrel.mag.core.exception.ImproperDataException;

import java.util.regex.PatternSyntaxException;

import static tyvrel.mag.core.exception.Precondition.notNull;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Base class that represents an classification
 */
public abstract class AbstractClassification {
	private String symbol;

	/**
	 * Creates base for the classification with naming policy described by <code>formatRegex</code>
	 *
	 * @param formatRegex naming policy regex, cannot be null
	 * @param symbol      symbol of the class
	 * @throws ImproperDataException if data is improper
	 */
	public AbstractClassification(String formatRegex, String symbol) throws ImproperDataException {
		try {
			notNull(symbol);
			if (symbol.matches(formatRegex)) {
				this.symbol = symbol;
			} else {
				throw new ImproperDataException();
			}
		} catch (PatternSyntaxException e) {
			throw new ImproperDataException();
		}
	}

	/**
	 * Returns classification's symbol.
	 *
	 * @return classification' symbol.
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractClassification other = (AbstractClassification) obj;
		if (symbol == null) {
			if (other.symbol != null) return false;
		} else if (!symbol.equals(other.symbol)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractClassification{" +
				"symbol='" + symbol + '\'' +
				'}';
	}
}
