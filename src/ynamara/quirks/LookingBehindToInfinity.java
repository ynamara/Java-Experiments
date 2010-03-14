/* http://www.polygenelubricants.com/2010/03/looking-behind-as-far-as-eyes-could-see.html */

package ynamara.quirks;

public class LookingBehindToInfinity {
	public static void main(String args[]) {
		System.out.println(
		   java.util.Arrays.deepToString(
		      "This works! Yes, really!! Try it! JUST DO IT!!!".split("(?<=!++)")
		   )
		); // [This works!,  Yes, really!!,  Try it!,  JUST DO IT!!!]	
	}	
}
