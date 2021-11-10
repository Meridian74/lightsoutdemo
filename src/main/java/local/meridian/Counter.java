package local.meridian;

public class Counter {
   private int maxValue;
   private int[] counters;
   private int edgeMarker = 0;
   private int currentCounter = 0;

   public Counter(int length) {
      if (length > 0)
         this.counters = new int[length];
      else
         throw new IllegalArgumentException("Incorrect length value for a counter!");
      this.maxValue = length;
      resetCounters(length - 1);
   }

   public int[] getCounters() {
      if (edgeMarker == maxValue) {
         return new int[] { -1 };
      }
      int[] values;
      values = loadValues();
      stepCounters();
      return values;
   }

   private int[] loadValues() {
      int[] result = new int[edgeMarker + 1];
      for (int i = 0; i <= edgeMarker; i++)
         result[i] = counters[i];
      return result;
   }

   private void stepCounters() {
      boolean steppingSuccessed = false;
      do {
         if (currentCounter == edgeMarker) {
            if (counters[currentCounter] < maxValue - 1) {
               counters[currentCounter]++;
               steppingSuccessed = true;
               currentCounter = 0;
            } else {
               resetCounters(edgeMarker);
               edgeMarker++;
               steppingSuccessed = true;
               currentCounter = 0;
            }

         } else if (counters[currentCounter] + 1 < counters[currentCounter + 1]) {
            counters[currentCounter]++;
            steppingSuccessed = true;
            currentCounter = 0;
         } else {
            counters[currentCounter] = currentCounter;
            currentCounter++;
            steppingSuccessed = false;
         }

      } while (!steppingSuccessed);
   }

   private void resetCounters(int edgeMarker) {
      for (int i = 0; i <= edgeMarker; i++) {
         counters[i] = i;
      }
   }

}
