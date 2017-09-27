package tyvrel.mag.gui.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class BaseJPanel extends JPanel {
	public BaseJPanel(String title) {
		super();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
		titledBorder.setBorder(new LineBorder(Color.BLACK));
		setBorder(titledBorder);

		setName(title);
	}
}
