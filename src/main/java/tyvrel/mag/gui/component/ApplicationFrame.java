package tyvrel.mag.gui.component;

import tyvrel.mag.gui.cards.*;
import tyvrel.mag.gui.component.datahandler.DataHandlerFacade;
import tyvrel.mag.gui.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class ApplicationFrame extends JFrame {
	private DataHandlerFacade dataHandlerFacade;
	private OthersPanel othersPanel = new OthersPanel();
	private MaterialsPanel materialsPanel = new MaterialsPanel();
	private LoadsPanel loadsPanel = new LoadsPanel();
	private CrossSectionsPanel crossSectionsPanel = new CrossSectionsPanel();
	private PricePanel pricePanel = new PricePanel();

	public ApplicationFrame() {
		super();
		dataHandlerFacade = new DataHandlerFacade(this);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(700, 700);

		CardLayout cardLayout = new CardLayout();
		JPanel cardPanel = new JPanel();
		cardPanel.setLayout(cardLayout);
		setJMenuBar(createMenuBar(cardPanel));

		Set<JComponent> cardSet = new HashSet<>();
		modifyCardsSet(cardSet);

		JPanel menuCard = new BaseJPanel("Menu");
		modifyMenuCard(menuCard, cardPanel);
		cardPanel.add(menuCard, "Menu");

		cardSet.forEach(jComponent -> cardPanel.add(jComponent, jComponent.getName()));
		add(cardPanel);
		cardLayout.show(cardPanel, "Menu");
	}

	public void run() {
		setVisible(true);
	}

	public JMenuBar createMenuBar(JPanel cardPanel) {
		return new MenuBarFactory() {
			@Override
			public void menuClickPerformed() {
				((CardLayout) cardPanel.getLayout()).show(cardPanel, "Menu");
			}
		}.build();
	}

	public void modifyMenuCard(JPanel jPanel, JPanel cardPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		JPanel pDimensioning = new BaseJPanel("Wymiarowanie");
		pDimensioning.add(new CardButton("Wymiarowanie belki", cardPanel));
		jPanel.add(pDimensioning);

		JPanel pLongitudinalReinforcement = new BaseJPanel("Zbrojenie podłużne");
		pLongitudinalReinforcement.add(new CardButton("Wymiarowanie zbrojenia podłużnego", cardPanel));
		pLongitudinalReinforcement.add(new CardButton("Minimalne zbrojenie podłużne", cardPanel));
		pLongitudinalReinforcement.add(new CardButton("Minimalne zbrojenie podłużne ze względu na kontrolę " +
				"zarysowania", cardPanel));
		pLongitudinalReinforcement.add(new CardButton("Maksymalne zbrojenie podłużne", cardPanel));
		pLongitudinalReinforcement.add(new CardButton("Długość zakładu zbrojenia podłużnego", cardPanel));
		pLongitudinalReinforcement.add(new CardButton("Długość zakotwienia zbrojenia podłużnego", cardPanel));
		jPanel.add(pLongitudinalReinforcement);

		JPanel pShearReinforcement = new BaseJPanel("Zbrojenie na ścinanie");
		pShearReinforcement.add(new CardButton("Wymiarowanie zbrojenia na ścinanie", cardPanel));
		pShearReinforcement.add(new CardButton("Minimalne zbrojenie na ścinanie", cardPanel));
		pShearReinforcement.add(new CardButton("Maksymalne zbrojenie na ścinanie", cardPanel));
		pShearReinforcement.add(new CardButton("Długość zakotwienia zbrojenia na ścinanie", cardPanel));
		jPanel.add(pShearReinforcement);

		JPanel pStress = new BaseJPanel("Naprężenia");
		pStress.add(new CardButton("Naprężenia przekroju w I fazie", cardPanel));
		pStress.add(new CardButton("Naprężenia przekroju w II fazie", cardPanel));
		pStress.add(new CardButton("Naprężenia przekroju", cardPanel));
		pStress.add(new CardButton("Ograniczenie naprężeń", cardPanel));
		jPanel.add(pStress);

		JPanel pOthers = new BaseJPanel("Inne");
		pOthers.add(new CardButton("Otulina", cardPanel));
		pOthers.add(new CardButton("Współczynnik pełzania", cardPanel));
		pOthers.add(new CardButton("Liniowy współczynnik pełzania", cardPanel));
		pOthers.add(new CardButton("Szerokość rys", cardPanel));
		pOthers.add(new CardButton("Sprawdzanie rys", cardPanel));
		jPanel.add(pOthers);
	}

	public void modifyCardsSet(Set<JComponent> cardSet) {
		cardSet.add(createCrossSectionCard());

		cardSet.add(new LongitudinalReinforcementCard("Wymiarowanie zbrojenia podłużnego"));
		cardSet.add(new MinimumLongitudinalReinforcementCard("Minimalne zbrojenie podłużne"));
		cardSet.add(new MinimumCrackLongitudinalReinforcementCard("Minimalne zbrojenie podłużne ze względu na " +
				"kontrolę zarysowania"));
		cardSet.add(new MaximumLongitudinalReinforcementCard("Maksymalne zbrojenie podłużne"));
		cardSet.add(new LapLengthCard("Długość zakładu zbrojenia podłużnego"));
		cardSet.add(new LongitudinalReinforcementAnchorageLengthCard("Długość zakotwienia zbrojenia podłużnego"));

		cardSet.add(new ShearReinforcementCard("Wymiarowanie zbrojenia na ścinanie"));
		cardSet.add(new MinimumShearReinforcementCard("Minimalne zbrojenie na ścinanie"));
		cardSet.add(new MaximumShearReinforcementCard("Maksymalne zbrojenie na ścinanie"));
		cardSet.add(new ShearReinforcementAnchorageLengthCard("Długość zakotwienia zbrojenia na ścinanie"));

		cardSet.add(new UncrackedStressCard("Naprężenia przekroju w I fazie"));
		cardSet.add(new CrackedStressCard("Naprężenia przekroju w II fazie"));
		cardSet.add(new StressCard("Naprężenia przekroju"));
		cardSet.add(new StressLimitationCard("Ograniczenie naprężeń"));

		cardSet.add(new ConcreteCoverCard("Otulina"));
		cardSet.add(new CreepCoefficientCard("Współczynnik pełzania"));
		cardSet.add(new LinearCreepCoefficientCard("Liniowy współczynnik pełzania"));
		cardSet.add(new CrackWidthCard("Szerokość rys"));
		cardSet.add(new CrackWidthVerificationCard("Sprawdzanie rys"));
	}

	public JComponent createCrossSectionCard() {
		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.setName("Wymiarowanie belki");

		jTabbedPane.add(materialsPanel);
		jTabbedPane.add(othersPanel);
		jTabbedPane.add(loadsPanel);
		jTabbedPane.add(pricePanel);
		jTabbedPane.add(crossSectionsPanel);
		return jTabbedPane;
	}

	public DataHandlerFacade getDataHandlerFacade() {
		return dataHandlerFacade;
	}


	public PricePanel getPricePanel() {
		return pricePanel;
	}


	public ShapeRangePanel getShapeRangePanel() {
		return othersPanel.getShapeRangePanel();
	}


	public OthersPanel getOthersPanel() {
		return othersPanel;
	}


	public MaterialsPanel getMaterialsPanel() {
		return materialsPanel;
	}


	public LoadsPanel getLoadsPanel() {
		return loadsPanel;
	}

}
