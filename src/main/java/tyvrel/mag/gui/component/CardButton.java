package tyvrel.mag.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class CardButton extends JButton {
	public CardButton(String text, JPanel cardPanel) {
		super(text);
		addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, text));
	}
}
