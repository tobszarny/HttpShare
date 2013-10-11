package pl.biltech.httpshare.util;

/**
 * Based on org.springframework.util.Assert
 * 
 * @author bilu
 * 
 */
public abstract class Assert {

	private Assert() {
	}

	public static void assertTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertTrue(boolean expression) {
		assertTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void assertNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertNull(Object object) {
		assertNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void assertNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertNotNull(Object object) {
		assertNotNull(object, "[Assertion failed] - the object argument must not be null");
	}
}
