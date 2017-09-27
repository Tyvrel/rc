package tyvrel.mag.gui.view;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.model.CrossSection;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class CrossSectionPreviewPanel extends BaseJPanel {
	private CrossSection crossSection;

	private Stroke strokeDim = new BasicStroke(1);
	private Stroke strokeLine = new BasicStroke(2);
	private Stroke strokeBar = new BasicStroke(3);

	public CrossSectionPreviewPanel() {
		super("Podgląd przekroju");
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (crossSection == null) return;
		Graphics2D g2D = (Graphics2D) g;

		int scale = 500;

		int h = (int) (crossSection.getShape().getH() * scale);
		int b = (int) (crossSection.getShape().getB() * scale);
		int cNom = (int) (crossSection.getCnom() * scale);
		int fisw = (int) (crossSection.getAsw().getPhi() * scale);
		int legs = (int) (crossSection.getAsw().getNleg());
		int fiB = (int) (crossSection.getAs().getAsb().getPhi() * scale);
		int nB = (int) (crossSection.getAs().getAsb().getN());
		int fiA = (int) (crossSection.getAs().getAsa().getPhi() * scale);
		int nA = (int) (crossSection.getAs().getAsa().getN());

		int dimLineOffset = 20;
		int arrowSize = 5;

		int sectionX = 50;
		int sectionY = 50;

		int stirrupX = sectionX + cNom + fisw / 2;
		int stirrupY = sectionY + cNom + fisw / 2;
		int stirrupW = b - 2 * cNom - fisw;
		int stirrupH = h - 2 * cNom - fisw;

		int additionalLegs = Math.max(0, legs - 2);
		int fiswOffset = stirrupW / (additionalLegs + 1);

		g2D.setStroke(strokeLine);
		g2D.drawRect(sectionX, sectionY, b, h);
		g2D.setStroke(strokeBar);
		g2D.drawRect(stirrupX, stirrupY, stirrupW, stirrupH);

		drawStirrups(g2D, stirrupX, stirrupW, stirrupY, stirrupH,
				additionalLegs, fiswOffset, fisw, sectionX + b + dimLineOffset);

		drawFiA(g2D, stirrupX + fisw / 2, stirrupY + fisw / 2, fiA, nA, (stirrupW - fisw - fiA) / (nA - 1), sectionX +
				b + dimLineOffset);

		drawFiB(g2D, stirrupX + fisw / 2, stirrupY + stirrupH - fisw / 2 - fiB, fiB, nB, (stirrupW - fisw - fiB) / (nB
				- 1), sectionX + b + dimLineOffset);

		drawString(g2D, "c", "nom", " = " + String.format("%.0f", crossSection.getCnom() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY);
		drawString(g2D, "\u00D8", "m.min.A",
				" = " + String.format("%.0f", crossSection.getAs().getAsb().getPhimmin() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5));
		drawString(g2D, "\u00D8", "m.min.B",
				" = " + String.format("%.0f", crossSection.getAs().getAsb().getPhimmin() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 2));
		drawString(g2D, "\u00D8", "m.min.SW",
				" = " + String.format("%.0f", crossSection.getAsw().getPhimmin() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 3));
		drawString(g2D, "l", "bd.A",
				" = " + String.format("%.0f", crossSection.getAs().getAsa().getLbd() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 4));
		drawString(g2D, "l", "bd.B",
				" = " + String.format("%.0f", crossSection.getAs().getAsb().getLbd() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 5));
		drawString(g2D, "l", "bd.SW",
				" = " + String.format("%.0f", crossSection.getAsw().getLbd() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 6));
		drawString(g2D, "l", "0.A",
				" = " + String.format("%.0f", crossSection.getAs().getAsa().getL0() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 7));
		drawString(g2D, "l", "0.B",
				" = " + String.format("%.0f", crossSection.getAs().getAsb().getL0() * 1000) + "mm",
				sectionX + b + dimLineOffset + 150, sectionY + (int) (g.getFont().getSize() * 1.5 * 8));


		drawHDim(g2D, sectionX - dimLineOffset, sectionY, sectionX - dimLineOffset, sectionY + h, arrowSize);
		drawBDim(g2D, sectionX, sectionY + h + dimLineOffset + g.getFont().getSize(),
				sectionX + b, sectionY + h + dimLineOffset + g.getFont().getSize(), arrowSize);

	}

	private void drawString(Graphics2D g, String text1, String subscript, String text2, int x, int y) {
		Font gFont = g.getFont();
		int gSize = gFont.getSize();
		int gWidth = (int) (gSize * 0.8);
		int subSize = (int) (gSize * 0.75);
		int subWidth = (int) (subSize * 0.8);
		Font subFont = new Font(gFont.getName(), gFont.getStyle(), subSize);

		g.drawString(text1, x, y);
		g.setFont(subFont);
		g.drawString(subscript, x + text1.length() * gWidth, y + gSize / 4);
		g.setFont(gFont);
		g.drawString(text2, x + text1.length() * gWidth + subscript.length() * subWidth, y);
	}

	private void drawStirrups(Graphics2D g2D, int stirrupX, int stirrupW, int stirrupY, int
			stirrupH, int additionalLegs, int fiswOffset, int fisw, int endDimX) {
		if (additionalLegs > 0) {
			int addStirrupX = stirrupX + fiswOffset;
			for (int i = 0; i < additionalLegs; i++) {
				g2D.setStroke(strokeBar);
				g2D.drawLine(addStirrupX, stirrupY, addStirrupX, stirrupY + stirrupH);
				g2D.setStroke(strokeDim);
				g2D.drawLine(addStirrupX, stirrupY + stirrupH / 2, addStirrupX + fisw, stirrupY + stirrupH / 2 + fisw);
				addStirrupX += fiswOffset;
			}
		}
		g2D.setStroke(strokeDim);
		g2D.drawLine(stirrupX, stirrupY + stirrupH / 2, stirrupX + fisw, stirrupY + stirrupH / 2 + fisw);
		g2D.drawLine(stirrupX + stirrupW, stirrupY + stirrupH / 2,
				stirrupX + stirrupW + fisw, stirrupY + stirrupH / 2 + fisw);
		g2D.drawLine(stirrupX + fisw, stirrupY + stirrupH / 2 + fisw, endDimX, stirrupY + stirrupH / 2 + fisw);
		g2D.drawString(String.format("%.0f", crossSection.getAsw().getNleg()) + "-cięte"
						+ "\u00D8" + String.format("%.0f", crossSection.getAsw().getPhi() * 1000)
						+ " co " + String.format("%.1f", roundHalf(100 / (crossSection.getAsw().getN() - 1))) + "cm",
				endDimX, stirrupY + stirrupH / 2 + fisw);
	}

	private double roundHalf(double d) {
		return Math.round(d * 2) / 2.0;
	}

	private void drawFiA(Graphics2D g2D, int fiAX, int fiAY, int fiA, int nA, int fiAXoffset, int endDimX) {
		int tempFiAX = fiAX;
		for (int i = 0; i < nA; i++) {
			g2D.setStroke(strokeBar);
			g2D.drawOval(tempFiAX, fiAY, fiA, fiA);
			g2D.setStroke(strokeDim);
			g2D.drawLine(tempFiAX + fiA / 2, fiAY + fiA, tempFiAX + fiA / 2 + fiA, fiAY + 2 * fiA);
			tempFiAX += fiAXoffset;
		}
		g2D.setStroke(strokeDim);
		g2D.drawLine(fiAX + fiA / 2 + fiA, fiAY + 2 * fiA, endDimX, fiAY + 2 * fiA);
		try {
			g2D.drawString(String.format("%.0f", crossSection.getAs().getAsa().getN())
							+ "\u00D8" + String.format("%.0f", crossSection.getAs().getAsa().getPhi() * 1000)
							+ " co " + String.format("%.1f", roundHalf(crossSection.getAsaSpacing() * 100)) + "cm",
					endDimX, fiAY + 2 * fiA);
		} catch (ImproperDataException | LSException e) {
		}
	}

	private void drawFiB(Graphics2D g2D, int fiBX, int fiBY, int fiB, int nB, int fiBXoffset, int endDimX) {
		int tempFiAX = fiBX;
		for (int i = 0; i < nB; i++) {
			g2D.setStroke(strokeBar);
			g2D.drawOval(tempFiAX, fiBY, fiB, fiB);
			g2D.setStroke(strokeDim);
			g2D.drawLine(tempFiAX + fiB / 2, fiBY, tempFiAX + fiB / 2 + fiB, fiBY - fiB);
			tempFiAX += fiBXoffset;
		}
		g2D.setStroke(strokeDim);
		g2D.drawLine(fiBX + fiB / 2 + fiB, fiBY - fiB, endDimX, fiBY - fiB);
		try {
			g2D.drawString(String.format("%.0f", crossSection.getAs().getAsb().getN()) +
							"\u00D8" + String.format("%.0f", crossSection.getAs().getAsb().getPhi() * 1000)
							+ " co " + String.format("%.1f", roundHalf(crossSection.getAsbSpacing() * 100)) + "cm",
					endDimX, fiBY - fiB);
		} catch (ImproperDataException | LSException e) {
		}
	}

	private void drawHDim(Graphics2D g2D, int hDimX1, int hDimY1, int hDimX2, int hDimY2, int arrowSize) {
		g2D.setStroke(strokeDim);
		g2D.draw(new Line2D.Double(hDimX1, hDimY1, hDimX2, hDimY2));
		g2D.draw(new Line2D.Double(hDimX1 - arrowSize, hDimY1 + arrowSize, hDimX1 + arrowSize, hDimY1 - arrowSize));
		g2D.draw(new Line2D.Double(hDimX2 - arrowSize, hDimY2 + arrowSize, hDimX2 + arrowSize, hDimY2 - arrowSize));
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.quadrantRotate(3);
		Font gFont = g2D.getFont();
		Font rotatedFont = new Font(gFont.getName(), gFont.getStyle(), gFont.getSize());
		rotatedFont = rotatedFont.deriveFont(affineTransform);
		g2D.setFont(rotatedFont);
		g2D.drawString("h = " + String.format("%.0f", crossSection.getShape().getH() * 1000) + "mm",
				hDimX1 - gFont.getSize() / 2, hDimY1 + (hDimY2 - hDimY1) / 2);
		g2D.setFont(gFont);
	}

	private void drawBDim(Graphics2D g2D, int bDimX1, int bDimY1, int bDimX2, int bDimY2, int arrowSize) {
		g2D.setStroke(strokeDim);
		g2D.draw(new Line2D.Double(bDimX1, bDimY1, bDimX2, bDimY2));
		g2D.draw(new Line2D.Double(bDimX1 - arrowSize, bDimY1 + arrowSize, bDimX1 + arrowSize, bDimY1 - arrowSize));
		g2D.draw(new Line2D.Double(bDimX2 - arrowSize, bDimY2 + arrowSize, bDimX2 + arrowSize, bDimY2 - arrowSize));
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.quadrantRotate(3);
		g2D.drawString("b = " + String.format("%.0f", crossSection.getShape().getB() * 1000) + "mm",
				bDimX1 + (bDimX2 - bDimX1) / 2, bDimY1 - g2D.getFont().getSize() / 2);
	}

	public CrossSection getCrossSection() {
		return crossSection;
	}

	public void setCrossSection(CrossSection crossSection) {
		this.crossSection = crossSection;
		repaint();
	}
}
