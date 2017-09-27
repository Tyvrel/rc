package tyvrel.mag.gui.view;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.core.model.Load;
import tyvrel.mag.gui.filter.DocumentCharacterFilter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class LoadsPanel extends BaseJPanel {
	private FactorsPanel factorsPanel;

	private JTable jTable;

	public LoadsPanel() {
		super("Obciążenie");
		setLayout(new GridLayout(1, 2));

		jTable = createJTable();

		add(createDataPanel());
		add(createPreviewPanel());
	}

	private JPanel createPreviewPanel() {
		return new BaseJPanel("Podgląd:") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2D = (Graphics2D) g;
				int recX = 20;
				int recY = 30;
				int width = getWidth() - 2 * recX;
				int height = getHeight() - recX - recY;

				int recH = Math.min(height, (int) (width / 1.7));

				g2D.setFont(new Font(g2D.getFont().getName(), g2D.getFont().getStyle(), recH / 12));

				int recW = recH / 2;
				int rein1X = recX;
				int rein1Y = recY + recW / 10;
				int rein2Y = recY + recH - recW / 5;
				int reinW = recW - recW / 10;
				int reinH = recW / 10;
				int arrowLength = recH / 20;
				int arcRadius = recW / 2;
				int offsetX = recH / 8;
				int arcY = recY + recH / 2 - arcRadius;
				int arc1X = recX + recW + offsetX - arcRadius;
				int arc2X = arc1X + arcRadius + offsetX;
				int line1X = arc2X + 2 * arcRadius + offsetX;
				int line1YB = arcY + 2 * arcRadius;
				int line2XA = line1X + offsetX + g.getFont()
						.getSize() * 2;
				int line2XB = line2XA + 2 * arcRadius;
				int line2Y = arcY + arcRadius;

				List<Shape> shapeList = new ArrayList<>();

				shapeList.add(new Rectangle(recX, recY, recW, recH));
				g2D.fill(new Rectangle(rein1X, rein1Y, reinW, reinH));
				g2D.fill(new Rectangle(rein1X, rein2Y, reinW, reinH));
				drawString(g, "A", "sA", recX + recW + recW / 10, rein1Y + recW / 20);
				drawString(g, "A", "sB", recX + recW + recW / 10, rein2Y + recW / 20);

				shapeList.add(new Arc2D.Double(arc1X, arcY, arcRadius * 2, arcRadius * 2, -90, 180, Arc2D.OPEN));
				shapeList.add(new Line2D.Double(arc1X + arcRadius, arcY, arc1X + arcRadius + arrowLength, arcY -
						arrowLength / 2));
				shapeList.add(new Line2D.Double(arc1X + arcRadius, arcY, arc1X + arcRadius + arrowLength, arcY +
						arrowLength / 2));
				drawString(g, "M", "B", arc1X + arcRadius, arcY + arcRadius);


				shapeList.add(new Arc2D.Double(arc2X, arcY, arcRadius * 2, arcRadius * 2, -90, 180, Arc2D.OPEN));
				shapeList.add(new Line2D.Double(arc2X + arcRadius, arcY + 2 * arcRadius, arc2X + arcRadius +
						arrowLength, arcY + 2 * arcRadius - arrowLength / 2));
				shapeList.add(new Line2D.Double(arc2X + arcRadius, arcY + 2 * arcRadius, arc2X + arcRadius +
						arrowLength, arcY + 2 * arcRadius + arrowLength / 2));
				drawString(g, "M", "A", arc2X + arcRadius, arcY + arcRadius);

				shapeList.add(new Line2D.Double(line1X, arcY, line1X, line1YB));
				shapeList.add(new Line2D.Double(line1X, line1YB, line1X - arrowLength / 2, line1YB - arrowLength));
				shapeList.add(new Line2D.Double(line1X, line1YB, line1X + arrowLength / 2, line1YB - arrowLength));
				drawString(g, "V", "Ed", line1X + g.getFont().getSize() / 4, arcY + arcRadius);

				shapeList.forEach(g2D::draw);
			}
		};
	}

	private void drawString(Graphics g, String text, String subscript, int x, int y) {
		Font font = g.getFont();
		int size = font.getSize();
		g.drawString(text, x, y);
		g.setFont(new Font(font.getName(), font.getStyle(), (int) (size * 0.75)));
		g.drawString(subscript, x + size / 2 + size / 4, y + size / 4);
		g.setFont(font);
	}

	private JPanel createDataPanel() {
		JPanel dataPanel = new BaseJPanel("Dane:");
		dataPanel.setLayout(new BorderLayout());
		factorsPanel = new FactorsPanel();
		dataPanel.add(factorsPanel, BorderLayout.PAGE_START);
		dataPanel.add(createTablePanel(), BorderLayout.CENTER);
		return dataPanel;
	}

	private JPanel createTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(createButtonTablePanel(), BorderLayout.PAGE_START);
		tablePanel.add(new JScrollPane(jTable), BorderLayout.CENTER);
		return tablePanel;
	}

	private JPanel createButtonTablePanel() {
		JPanel buttonTablePanel = new JPanel();
		buttonTablePanel.setLayout(new GridLayout(1, 2));
		JButton jbAddRow = new JButton("Dodaj wiersz");
		JButton jbDeleteRow = new JButton("Usuń zaznaczone wiersze");
		buttonTablePanel.add(jbAddRow);
		buttonTablePanel.add(jbDeleteRow);
		jbAddRow.addActionListener(e -> ((DefaultTableModel) jTable.getModel()).addRow(new Object[]{}));
		jbDeleteRow.addActionListener(e -> {
			int rowIndex = jTable.getSelectedRow();
			if (rowIndex >= 0)
				((DefaultTableModel) jTable.getModel()).removeRow(rowIndex);
		});
		return buttonTablePanel;
	}

	private JTable createJTable() {
		JTable jTable = new JTable(new LoadTableModel());

		Collections.list(jTable.getColumnModel()
				.getColumns())
				.forEach(tableColumn -> tableColumn.setCellEditor(new DefaultCellEditor(new JTextField() {
					{
						addAncestorListener(new AncestorListener() {
							@Override
							public void ancestorAdded(AncestorEvent event) {
								setText("");
							}

							@Override
							public void ancestorRemoved(AncestorEvent event) {
							}

							@Override
							public void ancestorMoved(AncestorEvent event) {
							}
						});
						((AbstractDocument) getDocument()).setDocumentFilter(new DocumentCharacterFilter());
					}
				})));

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setFillsViewportHeight(true);
		jTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		((DefaultTableModel) jTable.getModel()).addRow(new Object[]{});
		jTable.setValueAt("100", 0, 0);
		jTable.setValueAt("100", 0, 1);
		jTable.setValueAt("30", 0, 2);
		jTable.setValueAt("30", 0, 3);
		jTable.setValueAt("20", 0, 4);
		jTable.setValueAt("20", 0, 5);
		jTable.setValueAt("200", 0, 6);

		return jTable;
	}

	public Load[] getLoads() throws ImproperDataException {
		List<Load> list = new ArrayList<>();
		for (int i = 0; i < jTable.getRowCount(); i++) {

			double mEdB = getDouble(i, 0);
			double mEdA = getDouble(i, 1);
			double mcharB = getDouble(i, 2);
			double mcharA = getDouble(i, 3);
			double mquasiperma = getDouble(i, 4);
			double mquasipermb = getDouble(i, 5);
			double vEd = getDouble(i, 6);
			if (!(mEdB == 0 && mEdA == 0 && mcharB == 0 && mcharA == 0 && vEd == 0))
				list.add(new Load(mEdB, mEdA, mcharB, mcharA, mquasiperma, mquasipermb, vEd));
		}

		if (list.isEmpty()) list.add(new Load(0, 0, 0, 0, 0, 0, 0));
		return list.toArray(new Load[0]);
	}

	private double getDouble(int row, int col) throws ImproperDataException {
		Object o = jTable.getModel()
				.getValueAt(row, col);
		if (o == null) return 0;
		if (!(o instanceof String)) throw new ImproperDataException();
		String s = (String) o;
		if (s.isEmpty()) return 0;
		try {
			return Double.parseDouble(s.replace(",", ".")) * 1000;
		} catch (NumberFormatException e) {
			throw new ImproperDataException(e);
		}
	}

	public Factors getPartialFactors() throws ImproperDataException {
		return factorsPanel.getFactors();
	}

	public static class LoadTableModel extends DefaultTableModel {
		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public String getColumnName(int column) {
			String[] columnNames = {
					"<html>M<sub>EdB</sub> [kNm]</html>",
					"<html>M<sub>EdA</sub> [kNm]</html>",
					"<html>M<sub>EkB</sub> [kNm]</html>",
					"<html>M<sub>EkA</sub> [kNm]</html>",
					"<html>M<sub>EqpB</sub> [kNm]</html>",
					"<html>M<sub>EqpA</sub> [kNm]</html>",
					"<html>V<sub>Ed</sub> [kN]</html>"
			};
			return (column > columnNames.length - 1) ? "error" : columnNames[column];
		}
	}
}
