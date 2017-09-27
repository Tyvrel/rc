package tyvrel.mag.core.factory.classification;

import tyvrel.mag.core.model.classification.AbstractClassification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public abstract class AbstractClassificationFactory<T extends AbstractClassification> {
	protected Map<String, T> abstractClassificationMap = new HashMap<>();

	/**
	 * Returns an immutable instance of child of AbstractClassification of symbol <code>symbol</code>
	 *
	 * @param symbol class' symbol, cannot be null
	 * @return immutable child of AbstractClassification of symbol <code>symbol</code>
	 * @throws NullPointerException     if symbol is null
	 * @throws IllegalArgumentException if symbol was not found.
	 */
	public T get(String symbol) {
		if (symbol == null) throw new NullPointerException("symbol cannot be null");
		T abstractClass = abstractClassificationMap.get(symbol);
		if (abstractClass == null) throw new IllegalArgumentException("Symbol " + symbol + " was not found");
		return abstractClass;
	}

	public Set<T> getAll() {
		return new HashSet<>(abstractClassificationMap.values());
	}
}
