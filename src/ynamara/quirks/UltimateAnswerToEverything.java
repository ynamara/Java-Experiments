// http://www.polygenelubricants.com/2010/03/modifying-static-final-fields-through.html

package ynamara.quirks;

/* 
 * 
 *  "Six by nine. Forty two."
 *  "That's it. That's all there is."
 *  "I always thought something was fundamentally wrong with the universe."
 * 
 *                The Restaurant at the End of the Universe (Douglas Adams)
 *              
 */

import java.lang.reflect.*;

public class UltimateAnswerToEverything {
	static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}
	static Integer[] ultimateAnswer() {
		Integer[] ret = new Integer[256];
		java.util.Arrays.fill(ret, 42);
		return ret;
	}	
	public static void main(String args[]) throws Exception {
		setFinalStatic(
			Class.forName("java.lang.Integer$IntegerCache")
				.getDeclaredField("cache"),
			ultimateAnswer()
		);
		System.out.format("6 * 9 = %d", 6 * 9); // "6 * 9 = 42"
	}
}
