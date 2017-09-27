package tyvrel.mag.gui.view;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.dimensioning.CrossSectionsFactory;
import tyvrel.mag.core.factory.others.PriceFactory;
import tyvrel.mag.core.model.*;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.ApplicationFrame;
import tyvrel.mag.gui.component.datahandler.DataHandlerException;
import tyvrel.mag.gui.component.datahandler.DataHandlerFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

import static java.lang.String.*;
import static java.util.Optional.ofNullable;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class CrossSectionsPanel extends BaseJPanel {

	private CrossSection[] crossSections;

	private CrossSectionPreviewPanel crossSectionPreviewPanel;

	public CrossSectionsPanel() {
		super("Przekrój");
		setLayout(new GridLayout(1, 2));

		add(createCrossSectionTablePanel());
		crossSectionPreviewPanel = new CrossSectionPreviewPanel();
		add(crossSectionPreviewPanel);
	}

	private JPanel createCrossSectionTablePanel() {
		JPanel jPanel = new BaseJPanel("Lista przekrojów");
		jPanel.setLayout(new BorderLayout());
		JTable jTable = createJTable();
		jPanel.add(createJButton(jTable), BorderLayout.PAGE_START);
		jPanel.add(new JScrollPane(jTable), BorderLayout.CENTER);
		return jPanel;
	}


	private CrossSectionsFactory createCrossSectionsFactory() throws DataHandlerException {
		DataHandlerFacade dataHandlerFacade = ((ApplicationFrame) getTopLevelAncestor()).getDataHandlerFacade();
		return new CrossSectionsFactory(
				dataHandlerFacade.getFias(),
				dataHandlerFacade.getFibs(),
				dataHandlerFacade.getFisws(),
				dataHandlerFacade.getWidthRange(),
				dataHandlerFacade.getHeightRange(),
				dataHandlerFacade.getConcreteClassifications(),
				dataHandlerFacade.getExposureClassifications(),
				dataHandlerFacade.getLoads(),
				dataHandlerFacade.getLongitudinalReinforcementSteel(),
				dataHandlerFacade.getShearReinforcementSteel(),
				dataHandlerFacade.getPartialFactors(),
				dataHandlerFacade.is100yWorkingLife(),
				dataHandlerFacade.isQualityEnsured(),
				dataHandlerFacade.isHighAir(),
				dataHandlerFacade.getDgNomMax()
		);
	}

	private JButton createJButton(JTable jTable) {
		JButton jButton = new JButton("Oblicz");
		jButton.addActionListener(e -> {
			((DefaultTableModel) jTable.getModel()).setRowCount(0);
			crossSectionPreviewPanel.setCrossSection(null);
			try {
				CrossSectionsFactory csf = createCrossSectionsFactory();
				crossSections = csf.build();
				if (crossSections.length == 0) {
					String message = "<html>Brak przekrojów, które spełniałyby oczekiwane wymagania.</html>";
					JOptionPane.showMessageDialog(this, message, "Informacja", JOptionPane.INFORMATION_MESSAGE);
				}
				for (CrossSection crossSection : crossSections) {
					((DefaultTableModel) jTable.getModel()).addRow(crossSectionToStringArray(crossSection));
				}
			} catch (Exception e1) {
				String message = "<html>Algorym zakończył się niepowodzeniem:<br>" + e1.getClass()
						.getSimpleName() + " " + ofNullable(e1.getMessage()).orElse("") + "</html>";
				JOptionPane.showMessageDialog(this, message, "Błąd", JOptionPane.WARNING_MESSAGE);
			}
		});
		return jButton;
	}

	private String[] crossSectionToStringArray(CrossSection crossSection) throws DataHandlerException,
			ImproperDataException, LSException {
		DataHandlerFacade dataHandlerFacade = ((ApplicationFrame) getTopLevelAncestor()).getDataHandlerFacade();
		Reinforcement asB = crossSection.getAs()
				.getAsb();
		Reinforcement asA = crossSection.getAs()
				.getAsa();
		ShearReinforcement asw = crossSection.getAsw();
		String[] stringArray = new String[12];
		stringArray[0] = format("%.2f", new PriceFactory(crossSection, dataHandlerFacade.getPriceList()).build());
		stringArray[1] = format("%.0f", crossSection.getShape().getB() * 1000);
		stringArray[2] = format("%.0f", crossSection.getShape().getH() * 1000);
		stringArray[3] = format("%.3f", crossSection.getShape().getA());
		stringArray[4] = crossSection.getConcreteClassification()
				.getSymbol();
		stringArray[5] = dataHandlerFacade.getLongitudinalReinforcementSteelType();
		stringArray[6] = format("%.2f", asB.getA() * 10000);
		stringArray[7] = format("%.0f", asB.getN()) + "\u00D8" + format("%.0f", asB.getPhi() * 1000) +
				" co " + format("%.1f", roundHalf(crossSection.getAsbSpacing() * 100)) + "cm";
		stringArray[8] = format("%.2f", asA.getA() * 10000);
		stringArray[9] = format("%.0f", asA.getN()) + "\u00D8" + format("%.0f", asA.getPhi() * 1000) +
				" co " + format("%.1f", roundHalf(crossSection.getAsaSpacing() * 100)) + "cm";
		stringArray[10] = format("%.2f", asw.getA() * 10000);
		stringArray[11] = format("%.0f", asw.getNleg()) + "-cięte" + "\u00D8" + format("%.0f", asw.getPhi() * 1000) +
				" co " + format("%.1f", roundHalf(1 / asw.getN() * 100)) + "cm";
		return stringArray;
	}

	private double roundHalf(double d) {
		return Math.round(d * 2) / 2.0;
	}

	private JTable createJTable() {
		JTable jTable = new JTable(new LoadTableModel());
		int[] prefferedColumnWidth = {70, 45, 45, 45, 45, 60, 70, 110, 70, 110, 70, 150};
		TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<>(jTable.getModel());
		for (int i = 0; i < prefferedColumnWidth.length; i++) {
			jTable.getColumnModel()
					.getColumn(i)
					.setPreferredWidth(prefferedColumnWidth[i]);

			tableRowSorter.setComparator(i, this::compare);
		}
		jTable.setFillsViewportHeight(true);

		jTable.setRowSorter(tableRowSorter);
		jTable.getSelectionModel()
				.addListSelectionListener(e -> {
					int rowIndex = jTable.getSelectedRow();
					if (rowIndex < 0) return;
					rowIndex = jTable.convertRowIndexToModel(rowIndex);
					crossSectionPreviewPanel.setCrossSection(crossSections[rowIndex]);
				});
		return jTable;
	}

	private int compare(Object o1, Object o2) {
		try {
			double d1 = Double.parseDouble(o1.toString()
					.replaceAll(",", "."));
			double d2 = Double.parseDouble(o2.toString()
					.replaceAll(",", "."));
			return Double.compare(d1, d2);
		} catch (NumberFormatException e) {
			return o1.toString()
					.compareTo(o2.toString());
		}
	}

	public static class LoadTableModel extends DefaultTableModel {
		@Override
		public int getColumnCount() {
			return 12;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}


		@Override
		public String getColumnName(int column) {
			switch (column) {
				case 0:
					return "Cena [zł/m]";
				case 1:
					return "b [mm]";
				case 2:
					return "h [mm]";
				case 3:
					return "A [m2]";
				case 4:
					return "Beton";
				case 5:
					return "Stal";
				case 6:
					return "<html>A<sub>sB</sub> [cm2]</html>";
				case 7:
					return "";
				case 8:
					return "<html>A<sub>sA</sub> [cm2]</html>";
				case 9:
					return "";
				case 10:
					return "<html>A<sub>sw</sub> [cm2]</html>";
				case 11:
					return "";
				default:
					return "error";
			}
		}
	}

}
