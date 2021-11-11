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
   private long maxVariation;

   // for optional informations
   private LocalTime startTime;
   

   public LigthsOutCalc(boolean[] data, int width) {
      this.savedGrid = data;
      this.gridWidth = width;
      initializing();
   }

   public LigthsOutCalc(int width) {
      this(randomize(width), width);
   }

   private static boolean[] randomize(int width) {
      boolean[] data = new boolean[width * width];
      for (int i = 0; i < data.length; i++) {
         data[i] = Math.random() > 0.500 ? true : false;
      }
      return data;
   }

   public List<int[]> getBestMinSteps() {
      return bestMinSteps;
   }

   public long getMaxVariation() {
      return maxVariation;
   }

   // main calculator
   public long calculateMinSteps() {
      // init temp data for calc
      int[] firstLineSteps = new int[gridWidth];
      startTime = LocalTime.now();

      // check all variations
      for (int currentStep = 0; currentStep < maxVariation; currentStep++) {
         // init next calculation
         steps.clear();
         stateOfGrid = Arrays.copyOf(savedGrid, savedGrid.length);

         // make first steps before running the Chasing of Lights
         setFirstLineStepsfromBinary(firstLineSteps, currentStep);
         for (int i = 0; i < firstLineSteps.length; i++)
            if (firstLineSteps[i] == 1)
               toggleCells(i);

         // try to solve the grid
         runChasingTheLights();
         if (isSolved() && steps.size() < minSteps)
            setNewBestShortedSteps();

         // -->> show interim information if the counting takes a long time
         printTemporaryCalculations(currentStep);
      }
      return minSteps;
   }

   private void setFirstLineStepsfromBinary(int[] firstLineSteps, int currentStep) {
      long reducer = maxVariation >> 1;
      for (int i = (gridWidth - 1); i >= 0; i--) {
         currentStep -= reducer;
         if (currentStep >= 0) {
            firstLineSteps[i] = 1;
         } else {
            currentStep += reducer;
            firstLineSteps[i] = 0;
         }
         reducer = reducer >> 1;
      }
   }
   
   private void printTemporaryCalculations(long currentStep) {
      // --->>> optional information on variations already calculated
      if (LocalTime.now().minusSeconds(10).isAfter(startTime)) {
         startTime = startTime.plusSeconds(10L);
         System.out.println("Examined variations: " + currentStep);
      } // ->>> .............................................. <<<---
   }

   private void initializing() {
      if (savedGrid.length % gridWidth != 0) {
         throw new IllegalArgumentException("Incorrect grid size!");
      }
      this.minSteps = Long.MAX_VALUE;
      this.maxVariation = (long) Math.pow(2, gridWidth);
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