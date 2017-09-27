package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.others.ConcreteCoverFactory;
import tyvrel.mag.core.factory.classification.AbrasionClassificationFactory;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.factory.classification.ExposureClassificationFactory;
import tyvrel.mag.core.factory.classification.StructuralClassificationFactory;
import tyvrel.mag.core.model.classification.AbrasionClassification;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.gui.component.LabeledComboBox;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.BaseClassificationPanel;
import tyvrel.mag.gui.view.BaseJPanel;
import tyvrel.mag.gui.view.ReinforcementDiameterPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ConcreteCoverCard extends BaseJPanel {

	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel = new
			BaseClassificationPanel<>("Beton", new ConcreteClassificationFactory(), false);
	private BaseClassificationPanel<ExposureClassification> exposureClassificationPanel = new
			BaseClassificationPanel<>("Klasy ekspozycji", new ExposureClassificationFactory(), true);
	private BaseClassificationPanel<AbrasionClassification> abrasionClassificationPanel = new
			BaseClassificationPanel<>("Klasy ścieralności", new AbrasionClassificationFactory(), false);
	private ReinforcementDiameterPanel reinforcementDiameterPanel = new ReinforcementDiameterPanel("Średnica " +
			"zbrojenia", false);
	private LabeledComboBox lcbSurfaceType;
	private LabeledComboBox lcbWorkingLife;
	private JCheckBox jckSpecialQualityAssurance;

	private JCheckBox jckHighAir;
	private LabeledTextField ltfMaxAggregateDiameter;
	private JCheckBox jckIsSlab;
	private JCheckBox jckIsExposureShort;
	private LabeledTextField ltfDeltacdev;
	private JCheckBox jcbDefaultdeltacdev;

	public ConcreteCoverCard(String name) {
		super(name);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		c.weighty = 0.1;

		add(concreteClassificationPanel, c);
		add(exposureClassificationPanel, c);
		add(abrasionClassificationPanel, c);
		add(reinforcementDiameterPanel, c);
		c.weighty = 0.5;
		add(createPropertiesPanel(), c);
		c.weighty = 0.1;

		JPanel jPanel = new JPanel();
		JLabel jLabel = new JLabel();
		JButton jButton = new JButton("Oblicz");
		jButton.addActionListener(e -> {
			try {
				jLabel.setText(
						"Obliczona otulina wynosi: " + String.format("%.0f", calculateConcreteCover() * 1000) + "mm");
			} catch (ImproperDataException e1) {
				jLabel.setText("");
				JOptionPane.showMessageDialog(getTopLevelAncestor(), "Algorym zakończył się niepowodzeniem", "Błąd",
						JOptionPane.WARNING_MESSAGE);
			} catch (LSException e1) {
				jLabel.setText("");
				JOptionPane.showMessageDialog(getTopLevelAncestor(), "Stan graniczny przekroczony", "Błąd",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		jPanel.add(jButton);
		jPanel.add(jLabel);
		add(jPanel, c);
	}

	public JPanel createPropertiesPanel() {
		JPanel propertiesPanel = new BaseJPanel("Dodatkowe właściwości");

		lcbSurfaceType = new LabeledComboBox("Rodzaj powierzchni", new String[]{
				"Typowa powierzchnia betonu",
				"Beton jest kładziony na innym betonie o typowej powierzchni",
				"Beton jest kładziony na innym betonie o szorskiej powierzchni",
				"Powierzchnia betonu jest nierówna",
				"Powierzchnia betonu będzie podlegała ścieraniu",
				"Beton jest kładziony na nierównej powierzchni",
				"Beton jest kładziony na nierównej, przygotowanej powierzchni"
		});

		lcbWorkingLife = new LabeledComboBox("Projektowy okres użytkowania:", new String[]{"50 " +
				"lat", "100 lat"});
		jckSpecialQualityAssurance = new JCheckBox("Zapewniona specjalna kontrola jakości");
		jckHighAir = new JCheckBox("Stosuje się zawartość powietrza wyższą niż 4%");
		ltfMaxAggregateDiameter = new LabeledTextField("Maksymalna średnica kruszywa [mm]:", "32");
		jckIsSlab = new JCheckBox("Element ma kształt płyty");
		jckIsExposureShort = new JCheckBox("Czas wystawienia na środowisko zewnętrzne jest mniejsze niż 28 dni");
		ltfDeltacdev = new LabeledTextField("<html>\u0394c<sub>dev</sub> [mm]</html>", "10");
		jcbDefaultdeltacdev = new JCheckBox("<html>Domyślne \u0394c<sub>dev</sub></html>");
		ltfDeltacdev.getjTextField()
				.setEnabled(false);
		jcbDefaultdeltacdev.setSelected(true);

		JPanel deltacdevPanel = new JPanel();
		deltacdevPanel.add(ltfDeltacdev);
		deltacdevPanel.add(jcbDefaultdeltacdev);

		jcbDefaultdeltacdev.addActionListener(e -> {
			if (jcbDefaultdeltacdev.isSelected()) ltfDeltacdev.getjTextField()
					.setEnabled(false);
			else ltfDeltacdev.getjTextField()
					.setEnabled(true);
		});

		propertiesPanel.add(lcbSurfaceType);
		propertiesPanel.add(lcbWorkingLife);
		propertiesPanel.add(jckSpecialQualityAssurance);
		propertiesPanel.add(jckHighAir);
		propertiesPanel.add(ltfMaxAggregateDiameter);
		propertiesPanel.add(jckIsSlab);
		propertiesPanel.add(jckIsExposureShort);
		propertiesPanel.add(deltacdevPanel);
		return propertiesPanel;
	}

	public double calculateConcreteCover() throws ImproperDataException, LSException {
		int surfaceType;
		switch (lcbSurfaceType.getjComboBox()
				.getSelectedIndex()) {
			case 0:
				surfaceType = ConcreteCoverFactory.TYPICAL_SURFACE;
				break;
			case 1:
				surfaceType = ConcreteCoverFactory.ANOTHER_CONCRETE_SURFACE;
				break;
			case 2:
				surfaceType = ConcreteCoverFactory.ANOTHER_CONCRETE_SURFACE;
				break;
			case 3:
				surfaceType = ConcreteCoverFactory.UNEVEN_CONCRETE_SURFACE;
				break;
			case 4:
				surfaceType = ConcreteCoverFactory.SURFACE_PRONE_TO_ABRASION;
				break;
			case 5:
				surfaceType = ConcreteCoverFactory.UNEVEN_SURFACE;
				break;
			case 6:
				surfaceType = ConcreteCoverFactory.PREPARED_UNEVEN_SURFACE;
				break;
			default:
				throw new ImproperDataException();
		}

		return new ConcreteCoverFactory(
				new StructuralClassificationFactory(),
				new ExposureClassificationFactory(),
				new ConcreteClassificationFactory(),
				reinforcementDiameterPanel.getDiameters()[0],
				exposureClassificationPanel.getClassifications(ExposureClassification[]::new),
				concreteClassificationPanel.getClassifications(ConcreteClassification[]::new)[0],
				surfaceType,
				lcbWorkingLife.getjComboBox().getSelectedItem().equals("100 lat"),
				jckIsSlab.isSelected(),
				jckSpecialQualityAssurance.isSelected(),
				jckHighAir.isSelected(),
				jckIsExposureShort.isSelected(),
				lcbSurfaceType.getjComboBox().getSelectedIndex() == 2,
				abrasionClassificationPanel.getClassifications(AbrasionClassification[]::new)[0],
				(jcbDefaultdeltacdev.isSelected()) ? -1 : ltfDeltacdev.getDouble() / 1000,
				ltfMaxAggregateDiameter.getDouble()
		).build();
	}
}
