package tyvrel.mag.core.exception;

import java.util.Collection;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class Precondition {
	public static <T> T notNull(T t) throws ImproperDataException {
		if (t == null) throw new ImproperDataException("object has to be not-null");
		return t;
	}

	public static <T> Collection<T> notEmpty(Collection<T> t) throws ImproperDataException {
		if (notNull(t).isEmpty()) throw new ImproperDataException("collection has to be not-empty");
		return t;
	}

	public static <T> T[] notEmpty(T[] t) throws ImproperDataException {
		if (notNull(t).length == 0) throw new ImproperDataException("array has to be not-empty");
		return t;
	}

	public static double[] notEmpty(double[] t) throws ImproperDataException {
		if (notNull(t).length == 0) throw new ImproperDataException("array has to be not-empty");
		return t;
	}

	public static double[] ofSize(int size, double[] t) throws ImproperDataException {
		if (notNull(t).length != 2) throw new ImproperDataException("array has to have " + size + " elements");
		return t;
	}

	public static double pos(double d) throws ImproperDataException {
		real(d);
		if (d <= 0) throw new ImproperDataException("value has to be pos, but is: " + d);
		return d;
	}

	public static double neg(double d) throws ImproperDataException {
		real(d);
		if (d >= 0) throw new ImproperDataException("value has to be negative, but is: " + d);
		return d;
	}

	public static double nonNeg(double d) throws ImproperDataException {
		real(d);
		if (d < 0) throw new ImproperDataException("value has to be not-negative, but is: " + d);
		return d;
	}

	public static double nonPos(double d) throws ImproperDataException {
		real(d);
		if (d > 0) throw new ImproperDataException("value has to be not-pos, but is: " + d);
		return d;
	}

	public static double real(double d) throws ImproperDataException {
		if (d == Double.MAX_VALUE || d == Double.NEGATIVE_INFINITY || d == Double.POSITIVE_INFINITY || d == Double.NaN)
			throw new ImproperDataException("value have to be real, but is: " + d);
		return d;
	}

	public static double pos(ThrowableConsumer<Double> function) throws ImproperDataException, LSException {
		try {
			double d = function.accept();
			real(d);
			if (d <= 0) throw new ImproperDataException("value has to be pos, but is: " + d);
			return d;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static double real(ThrowableConsumer<Double> function) throws ImproperDataException, LSException {
		try {
			double d = function.accept();
			if (d == Double.MAX_VALUE || d == Double.NEGATIVE_INFINITY || d == Double.POSITIVE_INFINITY || d == Double
					.NaN)
				throw new ImproperDataException("value have to be real, but is: " + d);
			return d;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static double nonNeg(ThrowableConsumer<Double> function) throws ImproperDataException, LSException {
		try {
			double d = function.accept();
			real(d);
			if (d < 0) throw new ImproperDataException("value has to be not-negative, but is: " + d);
			return d;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static <T> T notNull(ThrowableConsumer<T> function) throws ImproperDataException, LSException {
		try {
			T t = function.accept();
			if (t == null) throw new ImproperDataException("object has to be not-null");
			return t;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static <T> Collection<T> notEmptyCollection(ThrowableConsumer<Collection<T>> function) throws
			ImproperDataException, LSException {
		try {
			Collection<T> t = function.accept();
			if (t.isEmpty()) throw new ImproperDataException("collection has to be not-empty");
			return t;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static <T> T[] notEmptyArray(ThrowableConsumer<T[]> function) throws ImproperDataException, LSException {
		try {
			T[] t = function.accept();
			if (t.length == 0) throw new ImproperDataException("array has to be not-empty");
			return t;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static double[] notEmptyDoubles(ThrowableConsumer<double[]> function) throws ImproperDataException,
			LSException {
		try {
			double[] t = function.accept();
			if (t.length == 0) throw new ImproperDataException("array has to be not-empty");
			return t;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}

	public static double[] ofSize(int size, ThrowableConsumer<double[]> function) throws ImproperDataException,
			LSException {
		try {
			double[] t = function.accept();
			if (t.length != 2) throw new ImproperDataException("array has to have " + size + " elements");
			return t;
		} catch (LSException e) {
			throw new LSException(e);
		} catch (Exception e) {
			throw new ImproperDataException(e);
		}
	}


	public interface ThrowableConsumer<R> {
		R accept() throws Exception;
	}
}
