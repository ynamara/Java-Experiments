package ynamara.quirks;

import java.lang.reflect.Method;

public class InfiniteMirror {

	public static void main(String args[]) throws Exception {
		Method m = Method.class.getMethod(
			"invoke", Object.class, Object[].class
		);
		Object[] a = { m, null };
		a[1] = a;
		m.invoke(m, a);
	}
	
}
