package tyvrel.mag.gui.component;

import javax.swing.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class DataCheckBox<T> extends JCheckBox {

	public DataCheckBox(String text) {
		super(text);
	}

	public DataCheckBox(String text, T t) {
		super(text);
		this.t = t;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	private T t;

}
