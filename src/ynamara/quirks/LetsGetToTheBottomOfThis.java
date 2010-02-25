package ynamara.quirks;

import java.util.Arrays;

public class LetsGetToTheBottomOfThis {
	
	public static Object[] selfContainingArray() {
		Object[] arr = new Object[1];
		arr[0] = arr;
		return arr;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(
				Arrays.deepToString(
					selfContainingArray()
				)
			);
		} catch (StackOverflowError e) {
			System.out.println(e);
		} // prints "[[...]]"

		try {
			System.out.println(
				Arrays.deepHashCode(
					selfContainingArray()
				)
			);
		} catch (StackOverflowError e) {
			System.out.println(e);
		} // stack overflows

		try {
			System.out.println(
				Arrays.deepEquals(
					selfContainingArray(),
					selfContainingArray()
				)
			);
		} catch (StackOverflowError e) {
			System.out.println(e);
		} // stack overflows
	}
	
}