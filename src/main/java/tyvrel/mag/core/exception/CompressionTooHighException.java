package tyvrel.mag.core.exception;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Thrown to indicate that compression in cross-section is too high. Either it's dimensions or strength should be
 * increased.
 */
public class CompressionTooHighException extends LSException {
	private static final long serialVersionUID = -5796207319427713852L;

	public CompressionTooHighException() {
		super();
	}

	public CompressionTooHighException(String message) {
		super(message);
	}

	public CompressionTooHighException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompressionTooHighException(Throwable cause) {
		super(cause);
	}


}
