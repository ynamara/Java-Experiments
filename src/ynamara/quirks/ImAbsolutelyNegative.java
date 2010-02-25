package ynamara.quirks;

public class ImAbsolutelyNegative {
	public static void main(String[] args) {
		System.out.println(Math.abs(Integer.MIN_VALUE) < 0);
		// prints "true"
		
		System.out.println(Math.abs(Long.MIN_VALUE) < 0);
		// prints "true"
	}
}
