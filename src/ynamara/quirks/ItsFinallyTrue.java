/* http://www.polygenelubricants.com/2010/02/final-field-in-java-is-it-really.html */
/* http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5059911 */

package ynamara.quirks;

import java.lang.reflect.Field;

public class ItsFinallyTrue {
   
   public final boolean dream1 = false;
   public final Boolean dream2 = false;
   
   @Override public String toString() {
      return dream1 + "-" + dream2;
   }

   public static void main(String[] args)
         throws IllegalAccessException, SecurityException {

      ItsFinallyTrue dreamer = new ItsFinallyTrue();
      System.out.println(dreamer);
      
      for (Field dream : dreamer.getClass().getDeclaredFields()) {
         dream.setAccessible(true);
         dream.set(dreamer, true);
      }
      System.out.println(dreamer);
   }
}