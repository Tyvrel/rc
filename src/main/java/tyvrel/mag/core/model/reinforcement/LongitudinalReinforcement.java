package tyvrel.mag.core.model.reinforcement;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes longitudinal reinforcement
 */
public class LongitudinalReinforcement {
	private Reinforcement asb;
	private Reinforcement asa;

	/**
	 * Creates an instance of the longitudinal reinforcement
	 *
	 * @param asb top reinforcement
	 * @param asa bottom reinforcement
	 */
	public LongitudinalReinforcement(Reinforcement asb, Reinforcement asa) {
		this.asb = asb;
		this.asa = asa;
	}

	/**
	 * Merges two longitudinal reinforcements and returns as new object
	 *
	 * @param as1 one longitudinal reinforcement to merge
	 * @param as2 another longitudinal reinforcement to merge
	 * @return merged longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	public static LongitudinalReinforcement merge(LongitudinalReinforcement as1, LongitudinalReinforcement as2) throws
			ImproperDataException {
		if (as2 == null) return as1;
		if (as1 == null) return as2;
		Reinforcement asB = Reinforcement.merge(as1.getAsb(), as2.getAsb());
		Reinforcement asA = Reinforcement.merge(as1.getAsa(), as2.getAsa());
		return new LongitudinalReinforcement(asB, asA);
	}

	/**
	 * Returns top reinforcement
	 *
	 * @return top reinforcement
	 */
	public Reinforcement getAsa() {
		return asa;
	}

	/**
	 * Returns bottom reinforcement
	 *
	 * @return bottom reinforcement
	 */
	public Reinforcement getAsb() {
		return asb;
	}

	@Override
	public String toString() {
		return "LongitudinalReinforcement{" +
				"asb=" + asb +
				", asa=" + asa +
				'}';
	}
}
