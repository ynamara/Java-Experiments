// http://www.polygenelubricants.com/2010/03/modifying-static-final-fields-through.html

package ynamara.quirks;

import java.lang.reflect.*;

public class EverythingIsTrue {
	static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}
	public static void main(String args[]) throws Exception {		
		setFinalStatic(Boolean.class.getField("FALSE"), true);

		System.out.format("Everything is %s", false); // "Everything is true"
	}
}