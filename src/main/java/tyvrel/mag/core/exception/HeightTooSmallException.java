package tyvrel.mag.core.exception;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Thrown to indicate that height of the cross-section too small.
 */
public class HeightTooSmallException extends LSException {
	private static final long serialVersionUID = -1584178898505351838L;

	public HeightTooSmallException() {
		super();
	}

	public HeightTooSmallException(String message) {
		super(message);
	}

	public HeightTooSmallException(String message, Throwable cause) {
		super(message, cause);
	}

	public HeightTooSmallException(Throwable cause) {
		super(cause);
	}
}
