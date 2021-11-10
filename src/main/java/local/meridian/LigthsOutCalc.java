package local.meridian;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LigthsOutCalc {
   private boolean stateOfGrid[];
   private boolean savedGrid[];
   private int gridWidth;
   private List<int[]> steps;
   private List<int[]> bestMinSteps;
   private long minSteps;

   // for stepping variations
   private Counter counter;
   private int[] firstLineSteps;

   // for optional informations
   private LocalTime startTime; 
   private long calculatedVariation;


   public LigthsOutCalc(boolean[] data, int width, Counter counter) {
      this.savedGrid = data;
      this.gridWidth = width;
      this.counter = counter;
      initializing();
   }

   public LigthsOutCalc(int width, Counter counter) {
      this(randomize(width), width, counter);
   }

   private static boolean[] randomize(int width) {
      boolean[] data = new boolean[width * width];
      for (int i = 0; i < data.length; i++) {
         data[i] = Math.random() > 0.500 ? true : false;
      }
      return data;
   }

   // main calculator
   public long calculateMinSteps() {
      calculatedVariation = 0;
      startTime = LocalTime.now();
      do {
         printTemporaryCalculations();
         runChasingTheLights();
         if (isSolved() && steps.size() < minSteps)
            setNewBestShortedSteps();
         calculatedVariation++;

         steps.clear();
         stateOfGrid = Arrays.copyOf(savedGrid, savedGrid.length);
         firstLineSteps = counter.getCounters();
         if (firstLineSteps[0] < 0) // reach to end of calculations! jump out from the while cycle
            break; 

         for (int i = 0; i < firstLineSteps.length; i++) { // first steps for trying to solve whole grid
            toggleCells(firstLineSteps[i]);
         }
      } while (true);
      return minSteps;
   }

   public long getCalculatedVariation() {
      return calculatedVariation;
   }

   public List<int[]> getShortedSteps() {
      return bestMinSteps;
   }

   private void printTemporaryCalculations() {
      // --->>> optional information on variations already calculated
      if (LocalTime.now().minusSeconds(10).isAfter(startTime)) {
         startTime = startTime.plusSeconds(10L);
         System.out.println("Examined variations: " + calculatedVariation);
      } // ->>> .............................................. <<<---
   }

   private void initializing() {
      if (savedGrid.length % gridWidth != 0) {
         throw new IllegalArgumentException("Incorrect grid size!");
      }
      this.minSteps = Long.MAX_VALUE;
      this.steps = new ArrayList<>();
      this.bestMinSteps = new ArrayList<>();
      this.stateOfGrid = Arrays.copyOf(savedGrid, savedGrid.length);
   }

   private boolean isSolved() {
      for (int i = 0; i < stateOfGrid.length; i++)
         if (stateOfGrid[i])
            return false;

      return true;
   }

   private void setNewBestShortedSteps() {
      minSteps = steps.size();
      bestMinSteps = List.copyOf(steps);
   }

   private void toggleCells(int cellIndex) {
      stateOfGrid[cellIndex] = !(stateOfGrid[cellIndex]);
      storeStep(cellIndex);

      // also toggle neighbour cells
      int column = cellIndex % gridWidth;
      if (column > 0)
         // left side
         stateOfGrid[cellIndex - 1] = !(stateOfGrid[cellIndex - 1]);

      if (column < gridWidth - 1)
         // rigth side
         stateOfGrid[cellIndex + 1] = !(stateOfGrid[cellIndex + 1]);

      if (cellIndex > gridWidth - 1)
         // upper side
         stateOfGrid[cellIndex - gridWidth] = !(stateOfGrid[cellIndex - gridWidth]);

      if (cellIndex < stateOfGrid.length - gridWidth)
         // bottom side
         stateOfGrid[cellIndex + gridWidth] = !(stateOfGrid[cellIndex + gridWidth]);
   }

   private void storeStep(int cellIndex) {
      int x = cellIndex % gridWidth;
      int y = cellIndex / gridWidth;
      steps.add(new int[] { x, y });
   }

   private void runChasingTheLights() {
      for (int cellIndex = gridWidth; cellIndex < stateOfGrid.length; cellIndex++) {
         if (stateOfGrid[cellIndex - gridWidth])
            toggleCells(cellIndex);
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      String newLine = "\n";
      String closeLine = newLine;

      for (int i = 0; i < savedGrid.length; i++) {
         if (i % gridWidth == 0)
            sb.append(newLine);

         if (savedGrid[i])
            sb.append("#");
         else
            sb.append(".");
      }

      sb.append(closeLine);
      return sb.toString();
   }
}