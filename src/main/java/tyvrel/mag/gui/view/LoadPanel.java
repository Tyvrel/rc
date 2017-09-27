package tyvrel.mag.gui.view;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.gui.component.LabeledTextField;

import static tyvrel.mag.core.exception.Precondition.nonNeg;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class LoadPanel extends BaseJPanel {
	private LabeledTextField ltfMa;
	private LabeledTextField ltfMb;

	public LoadPanel() {
		super("Obciążenie");
		ltfMa = new LabeledTextField("Moment zginający rozciągajacy górę przekroju [kNm]: ", "100");
		ltfMb = new LabeledTextField("Moment zginający rozciągajacy dół przekroju [kNm]: ", "100");
		add(ltfMa);
		add(ltfMb);
	}

	public double getMa() throws ImproperDataException, LSException {
		return nonNeg(() -> ltfMa.getDouble() * 1000);
	}

	public double getMb() throws ImproperDataException, LSException {
		return nonNeg(() -> ltfMb.getDouble() * 1000);
	}
}
