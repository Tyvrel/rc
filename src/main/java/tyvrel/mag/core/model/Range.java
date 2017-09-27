package tyvrel.mag.core.model;

import tyvrel.mag.core.exception.ImproperDataException;

import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes range of doubles
 */
public class Range {
	private double from;
	private double to;
	private double increment;

	@Override
	public String toString() {
		return "Range{" +
				"from=" + from +
				", to=" + to +
				", increment=" + increment +
				", size=" + size() +
				'}';
	}

	/**
	 * Creates an instance of range
	 *
	 * @param from      starting point of the range (inclusive)
	 * @param to        ending point of the range (inclusive)
	 * @param increment increment of the range
	 * @throws ImproperDataException if data is improper
	 */
	public Range(double from, double to, double increment) throws ImproperDataException {
		pos(from);
		pos(to);
		nonNeg(increment);
		if (from > to) throw new ImproperDataException();
		if (to - from < increment) throw new ImproperDataException();
		if ((to - from) / increment > 1000000000) throw new ImproperDataException();

		this.from = from;
		this.to = to;
		this.increment = increment;
	}

	/**
	 * Creates instance of the one-value-range
	 *
	 * @param value value to be represented as range
	 * @throws ImproperDataException if data is improper
	 */
	public Range(double value) throws ImproperDataException {
		pos(value);
		this.from = value;
		this.to = value;
		this.increment = 0.000000001;
	}

	/**
	 * Returns size of the range
	 *
	 * @return size of the range
	 */
	public int size() {
		return (int) ((to - from) / increment);
	}

	/**
	 * Returns starting point of the range (inclusive)
	 *
	 * @return starting point of the range (inclusive)
	 */
	public double getFrom() {
		return from;
	}

	/**
	 * Returns ending point of the range (inclusive)
	 *
	 * @return ending point of the range (inclusive)
	 */
	public double getTo() {
		return to;
	}

	/**
	 * Returns increment of the range
	 *
	 * @return increment of the range
	 */
	public double getIncrement() {
		return increment;
	}
}
