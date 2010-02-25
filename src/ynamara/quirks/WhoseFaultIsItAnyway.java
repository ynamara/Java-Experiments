/* http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4730877 */

package ynamara.quirks;

public class WhoseFaultIsItAnyway {
	public static void main(String[] args) {
		Error yours = new Error("You");
		Error mine = new Error("Me");
		yours.initCause(mine);
		mine.initCause(yours);
		mine.printStackTrace();
	}
}