package tyvrel.mag.core.factory;

import tyvrel.mag.core.exception.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public interface Factory<T> {
	T build() throws ImproperDataException, LSException;
}
