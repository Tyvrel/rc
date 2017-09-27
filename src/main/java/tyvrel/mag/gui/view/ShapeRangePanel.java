package tyvrel.mag.gui.view;

import tyvrel.mag.gui.component.RangeTextField;
import tyvrel.mag.core.model.Range;
import tyvrel.mag.core.exception.ImproperDataException;

import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ShapeRangePanel extends BaseJPanel {
	private RangeTextField rtfHeight;
	private RangeTextField rtfWidth;

	public ShapeRangePanel() {
		super("Geometria");
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(50);
		setLayout(flowLayout);

		rtfHeight = new RangeTextField("Wysokość [m]:", "0.3", "0.6", "0.1");
		rtfWidth = new RangeTextField("Szerokość [m]:", "0.3", "0.6", "0.1");
		add(rtfHeight);
		add(rtfWidth);
	}

	public double getCrossSectionWidth() throws ImproperDataException {
		return rtfWidth.getRange().getFrom();
	}

	public Range getWidthRange() throws ImproperDataException {
		return rtfWidth.getRange();
	}

	public double getCrossSectionHeight() throws ImproperDataException {
		return rtfHeight.getRange().getFrom();
	}

	public Range getHeightRange() throws ImproperDataException {
		return rtfHeight.getRange();
	}
}
