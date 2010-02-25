/* http://www.polygenelubricants.com/2010/02/beware-when-reflecting-on-java-arrays.html */

package ynamara.quirks;

import java.util.Arrays;

public class TheMirrorDoesLie {
   public static void main(String args[]) {
      int[] arr = { 1, 2, 3, };
      System.out.println(arr.length);
      System.out.println(Arrays.toString(arr.clone()));

      /* http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5047859 */
      try {
    	  arr.getClass().getField("length");
      } catch (Exception e) {
    	  e.printStackTrace();
      }

      /* http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4987375 */
      try {
    	  arr.getClass().getMethod("clone");
      } catch (Exception e) {
          e.printStackTrace();
      }
   }
}
