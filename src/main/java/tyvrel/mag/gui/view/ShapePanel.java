package tyvrel.mag.gui.view;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.gui.component.LabeledTextField;

import static tyvrel.mag.core.exception.Precondition.notNull;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ShapePanel extends BaseJPanel {
	private LabeledTextField ltfWidth;
	private LabeledTextField ltfHeight;

	public ShapePanel() {
		super("Geometria");
		ltfWidth = new LabeledTextField("Szerokość przekroju [m]: ", "0.3");
		ltfHeight = new LabeledTextField("Wysokość przekroju [m]: ", "0.6");
		add(ltfWidth);
		add(ltfHeight);
	}

	public Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> new Shape(ltfWidth.getDouble(), ltfHeight.getDouble()));
	}
}
