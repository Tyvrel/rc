package tyvrel.mag.gui.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * An filter, that filters out any string that doesn't fit pattern 1234.56789 or 1234,56789 (maximum of 5 digits
 * before and after dot).
 */
public class DocumentCharacterFilter extends DocumentFilter {
	public static final String regex = "^(([1-9]\\d{0,4})|0)(([.,])|(([.,])(\\d{1,5})))?$";

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws
			BadLocationException {
		String currentText = fb.getDocument()
				.getText(0, fb.getDocument()
						.getLength());
		String futureText = currentText + string;
		if ((futureText).matches(regex) || futureText.isEmpty()) {
			super.insertString(fb, offset, string, attr);
		} else {
			Toolkit.getDefaultToolkit()
					.beep();
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws
			BadLocationException {
		String currentText = fb.getDocument()
				.getText(0, fb.getDocument()
						.getLength());
		String futureText = new StringBuilder(currentText).replace(offset, offset + length, text)
				.toString();

		if ((futureText).matches(regex) || futureText.isEmpty()) {
			super.replace(fb, offset, length, text, attrs);
		} else {
			Toolkit.getDefaultToolkit()
					.beep();
		}
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		String currentText = fb.getDocument()
				.getText(0, fb.getDocument()
						.getLength());
		String futureText = new StringBuilder(currentText).replace(offset, offset + length, "")
				.toString();
		if ((futureText).matches(regex) || futureText.isEmpty()) {
			super.remove(fb, offset, length);
		} else {
			Toolkit.getDefaultToolkit()
					.beep();
		}
	}
}
