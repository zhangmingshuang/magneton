package org.magneton.core.lang;

/**
 * .
 *
 * @author zhangmsh 2022/1/12
 * @since 2.
 */
public class UObject {

	private UObject() {

	}

	// ============== non null ================
	/**
	 * <p>
	 * Returns the first value in the array which is not {@code null}. If all the values
	 * are {@code null} or the array is {@code null} or empty then {@code null} is
	 * returned.
	 * </p>
	 *
	 * <pre>
	 * ObjectUtils.firstNonNull(null, null)      = null
	 * ObjectUtils.firstNonNull(null, "")        = ""
	 * ObjectUtils.firstNonNull(null, null, "")  = ""
	 * ObjectUtils.firstNonNull(null, "zz")      = "zz"
	 * ObjectUtils.firstNonNull("abc", *)        = "abc"
	 * ObjectUtils.firstNonNull(null, "xyz", *)  = "xyz"
	 * ObjectUtils.firstNonNull(Boolean.TRUE, *) = Boolean.TRUE
	 * ObjectUtils.firstNonNull()                = null
	 * </pre>
	 * @param <T> the component type of the array
	 * @param values the values to test, may be {@code null} or empty
	 * @return the first value from {@code values} which is not {@code null}, or
	 * {@code null} if there are no non-null values
	 */
	@SafeVarargs
	public static <T> T firstNonNull(final T... values) {
		if (values != null) {
			for (final T val : values) {
				if (val != null) {
					return val;
				}
			}
		}
		return null;
	}

}
