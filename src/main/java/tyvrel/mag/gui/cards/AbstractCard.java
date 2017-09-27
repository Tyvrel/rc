package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.gui.component.datahandler.DataHandlerException;
import tyvrel.mag.gui.view.BaseJPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractCard extends BaseJPanel {
	protected AbstractCard(String name) {
		super(name);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setName(name);

		modifyDataPanel(this);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setMaximumSize(new Dimension(10000, 10000));

		add(createButtonPanel());
	}

	protected abstract void modifyDataPanel(JPanel jPanel);

	protected abstract String getSuccessMessage() throws Exception;

	protected JPanel createButtonPanel() {
		JPanel jPanel = new BaseJPanel("Wynik");
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel jlResult = new JLabel(" ");
		jlResult.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton jbCalculate = new JButton("Oblicz");
		jbCalculate.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbCalculate.addActionListener(e -> {
			try {
				jlResult.setText(getSuccessMessage());
			} catch (DataHandlerException e1) {
				showDialog("Dane są wprowadzone nieprawidłowo", jlResult);
			} catch (LSException e1) {
				showDialog("Przekroczony stan graniczny", jlResult);
			} catch (Exception e1) {
				showDialog("Algorym zakończył się niepowodzeniem", jlResult);
			}
		});

		jPanel.add(jbCalculate, Component.CENTER_ALIGNMENT);
		jPanel.add(new JLabel(" "), Component.CENTER_ALIGNMENT);
		jPanel.add(jlResult, Component.CENTER_ALIGNMENT);
		jPanel.add(new JLabel(" "), Component.CENTER_ALIGNMENT);
		jPanel.setMaximumSize(new Dimension(10000, 100));
		return jPanel;
	}

	private void showDialog(String message, JLabel jLabel) {
		jLabel.setText(" ");
		JOptionPane.showMessageDialog(getTopLevelAncestor(), message, "Błąd", JOptionPane.WARNING_MESSAGE);
	}
}
