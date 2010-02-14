/* http://ynamara.blogspot.com/2010/02/factory-method-pattern-for-java.html */

package ynamara.xfact;

import java.util.*;

public class Exceptions {
	
	/* (X) x.initCause(c) ==> chain(x, c) */
	public static <X extends Throwable> X chain(X exc, Throwable cause) {
		exc.initCause(cause);
		return exc;
	}

	/* throw chain(x, c) ==> translate(c, x) */
	public static <X extends Throwable> void translate(Throwable cause, X exc) throws X {
		throw chain(exc, cause);
	}
	
	public static String outOfBoundsMessage(int index, int lo, int hiX) {
		return String.format("%d out of [%d,%d)", index, lo, hiX);
	}
	
	public static ArrayIndexOutOfBoundsException arrayIndexOutOfBounds(int index, Object arr) {
		return new ArrayIndexOutOfBoundsException(
			outOfBoundsMessage(index, 0, java.lang.reflect.Array.getLength(arr))
		);
	}
		
	public static IndexOutOfBoundsException indexOutOfBounds(int index, int lo, int hiX) {
		return new IndexOutOfBoundsException(
			outOfBoundsMessage(index, lo, hiX)
		);
	}

	public static NoSuchElementException noSuchElement() {
		return noSuchElement(null);
	}
	public static NoSuchElementException noSuchElement(String msg) {
		return new NoSuchElementException(msg);
	}

	/* Pops out any StackTraceElement originating from this class
	 * from the top of the stack trace of an exception */
	public static <X extends Throwable> X filteredStackTrace(X exc) {
		StackTraceElement[] stack = exc.getStackTrace();
		int count = 0;
		for (StackTraceElement elem : stack) {
			if (elem.getClassName().equals(Exceptions.class.getName())) {
				count++;
			} else {
				break;
			}
		}
		exc.setStackTrace(Arrays.copyOfRange(stack, count, stack.length));
		return exc;
	}
	
}