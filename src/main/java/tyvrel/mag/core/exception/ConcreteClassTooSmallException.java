package tyvrel.mag.core.exception;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ConcreteClassTooSmallException extends LSException {
	private static final long serialVersionUID = -4512168275506616393L;

	public ConcreteClassTooSmallException() {
		super();
	}

	public ConcreteClassTooSmallException(String message) {
		super(message);
	}

	public ConcreteClassTooSmallException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConcreteClassTooSmallException(Throwable cause) {
		super(cause);
	}
}
