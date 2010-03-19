// http://www.polygenelubricants.com/2010/03/modifying-static-final-fields-through.html

/* 
 *  "Six by nine. Forty two."
 *  "That's it. That's all there is."
 *  "I always thought something was fundamentally wrong with the universe."
 * 
 *                       The Restaurant at the End of the Universe
 *                                                (Douglas Adams)
 */

package ynamara.quirks;

public class UltimateAnswerToEverything {
	static Integer[] ultimateAnswer() {
		Integer[] ret = new Integer[256];
		java.util.Arrays.fill(ret, 42);
		return ret;
	}	
	public static void main(String args[]) throws Exception {
		EverythingIsTrue.setFinalStatic(
			Class.forName("java.lang.Integer$IntegerCache")
				.getDeclaredField("cache"),
			ultimateAnswer()
		);
		System.out.format("6 * 9 = %d", 6 * 9); // "6 * 9 = 42"
	}
}
