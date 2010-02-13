package ynamara.quirks;

import java.util.Arrays;

public class ArrayReflection {
   public static void main(String args[]) {
      int[] arr = { 1, 2, 3, };
      System.out.println(arr.length);
      System.out.println(Arrays.toString(arr.clone()));

      // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5047859
      try {
    	  System.out.println(arr.getClass().getField("length"));
      } catch (Exception e) {
    	  e.printStackTrace();
      }

      // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4987375
      try {
    	  System.out.println(arr.getClass().getMethod("clone"));
      } catch (Exception e) {
          e.printStackTrace();
      }
   }
}
