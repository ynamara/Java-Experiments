/* http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4730877 */

package ynamara.quirks;

public class CausalLoop {

	public static void main(String[] args) {
		Throwable t1 = new Throwable("You");
		Throwable t2 = new Throwable("Me");
		t1.initCause(t2);
		t2.initCause(t1);
		t1.printStackTrace();
	}

}
