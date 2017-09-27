package tyvrel.mag.gui.component;


import javax.swing.*;

import java.awt.*;

import static java.util.Optional.ofNullable;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public abstract class MenuBarFactory {
	public JMenuBar build() {
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu = new JMenu("Plik");
		jMenuBar.add(jMenu);

		JMenuItem jmiMenu = new JMenuItem("Menu");
		jmiMenu.addActionListener(e -> menuClickPerformed());
		jMenu.add(jmiMenu);

		JMenuItem jmiExit = new JMenuItem("Wyjście");
		jmiExit.addActionListener(
				e -> ofNullable((JFrame) jMenuBar.getTopLevelAncestor()).ifPresent(Window::dispose));
		jMenu.add(jmiExit);
		return jMenuBar;
	}

	public abstract void menuClickPerformed();
}
