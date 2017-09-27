package tyvrel.mag.core.model;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Represents shape of the cross section
 */
public class Shape {
	private double b;
	private double h;

	/**
	 * Creates an instance of shape
	 *
	 * @param b width of the web
	 * @param h height of the web
	 */
	public Shape(double b, double h) {
		this.b = b;
		this.h = h;
	}

	@Override
	public String toString() {
		return "Shape{" +
				"b=" + b +
				", h=" + h +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Shape shape = (Shape) o;

		if (Double.compare(shape.b, b) != 0) return false;
		return Double.compare(shape.h, h) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(b);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(h);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Returns area of the shape in m2
	 *
	 * @return area of the shape in m2
	 */
	public double getA() {
		return b * h;
	}

	/**
	 * Returns perimeter of the shape in m
	 *
	 * @return perimeter of the shape in m
	 */
	public double getU() {
		return 2 * b + 2 * h;
	}

	/**
	 * Returns moment of inertia in m4
	 *
	 * @return moment of inertia in m4
	 */
	public double getI() {
		return b * h * h * h / 12;
	}

	/**
	 * Returns distance to neutral axis from the top of the shape in m
	 *
	 * @return distance to neutral axis from the top of the shape in m
	 */
	public double getNeutralAxis() {
		return h / 2;
	}

	/**
	 * Returns height of the web in m
	 *
	 * @return height of the web in m
	 */
	public double getH() {
		return h;
	}

	/**
	 * Return width of the web in m
	 *
	 * @return width of the web in m
	 */
	public double getB() {
		return b;
	}
}
