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

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void isNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNotNull(Object object) {
		isNotNull(object, "[Assertion failed] - the object argument must not be null");
	}
}
