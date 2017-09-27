package tyvrel.mag.core.factory.classification;

import tyvrel.mag.core.model.classification.Steel;

import java.util.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class SteelFactory {
	public static final Steel _34GS;
	public static final Steel RB400;
	public static final Steel RB400W;
	public static final Steel _20G2VY;
	public static final Steel RB500;
	public static final Steel RB500W;
	public static final Steel St3b500;
	public static final Steel BSt500KR;
	public static final Steel BSt500M;
	public static final Steel B500A;
	public static final Steel BSt500S;
	public static final Steel BSt500WR;
	public static final Steel B500B;
	public static final Steel B500SP;

	private static Map<String, Steel> steelMap = new HashMap<>();

	static {
		_34GS = new Steel(200000000000.0, 400000000.0, Steel.A);
		steelMap.put("_34GS", _34GS);
		RB400 = new Steel(200000000000.0, 400000000.0, Steel.A);
		steelMap.put("RB400", RB400);
		RB400W = new Steel(200000000000.0, 400000000.0, Steel.A);
		steelMap.put("RB400W", RB400W);
		_20G2VY = new Steel(200000000000.0, 490000000.0, Steel.A);
		steelMap.put("_20G2VY", _20G2VY);
		RB500 = new Steel(200000000000.0, 500000000.0, Steel.A);
		steelMap.put("RB500", RB500);
		RB500W = new Steel(200000000000.0, 500000000.0, Steel.A);
		steelMap.put("RB500W", RB500W);
		St3b500 = new Steel(200000000000.0, 500000000.0, Steel.A);
		steelMap.put("St3b500", St3b500);
		BSt500KR = new Steel(200000000000.0, 500000000.0, Steel.A);
		steelMap.put("BSt500KR", BSt500KR);
		BSt500M = new Steel(200000000000.0, 500000000.0, Steel.A);
		steelMap.put("BSt500M", BSt500M);
		B500A = new Steel(200000000000.0, 500000000.0, Steel.A);
		steelMap.put("B500A", B500A);
		BSt500S = new Steel(200000000000.0, 500000000.0, Steel.B);
		steelMap.put("BSt500S", BSt500S);
		BSt500WR = new Steel(200000000000.0, 500000000.0, Steel.B);
		steelMap.put("BSt500WR", BSt500WR);
		B500B = new Steel(200000000000.0, 500000000.0, Steel.B);
		steelMap.put("B500B", B500B);
		B500SP = new Steel(200000000000.0, 500000000.0, Steel.C);
		steelMap.put("B500SP", B500SP);
	}

	public Steel get(String name) {
		return Optional.ofNullable(steelMap.get(name))
				.orElseThrow(() -> new IllegalArgumentException("Symbol " + name + " was not found"));
	}

	public Set<String> getAll() {
		return steelMap.keySet();
	}

}
