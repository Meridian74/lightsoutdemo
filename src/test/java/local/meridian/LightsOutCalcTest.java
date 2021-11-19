package local.meridian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class LightsOutCalcTest {

   @Test
   public void testWithPreparedDatas1() {
      boolean[] testData = { false, true, false, true, false, false, true, true, true, true, true, true, false, true,
            true, true, true, true, true, true, true, true, true, true, true };

      int gridWidth = 5;
      LigthsOutCalc game = new LigthsOutCalc(testData, gridWidth);
      long steps = game.calculateMinSteps();
      assertEquals(8, steps);
   }

   @Test
   public void testWithPreparedDatas2() {
      boolean[] testData = { false, false, false, false, false, false, true, false, false, false, true, true, true,
            true, true, false, false, true, true, false, false, false, true, false, false };

      int gridWidth = 5;
      LigthsOutCalc game = new LigthsOutCalc(testData, gridWidth);
      long steps = game.calculateMinSteps();
      assertEquals(9, steps);
   }

   @Test
   public void testWithPreparedDatas3() {
      boolean[] testData = { true, true, true, false, false, true, false, false, false, false, false, false, false,
            false, false, false };

      int gridWidth = 4;
      LigthsOutCalc game = new LigthsOutCalc(testData, gridWidth);
      long steps = game.calculateMinSteps();
      assertEquals(1, steps);
   }

   @Test
   public void testWithWrongSizeOfGrid() {
      boolean[] wrongSizeGrid = { false, true, false, true, false };
      int gridWidth = 2;
      assertThrows(IllegalArgumentException.class, () -> new LigthsOutCalc(wrongSizeGrid, gridWidth));
   }

   @Test
   public void shoudOneStepGrid() {
      boolean[] testData = { false, false, false, false, false, false, true, false, false, false, true, true, true,
            false, false, false, true, false, false, false, false, false, false, false, false };

      int gridWidth = 5;
      LigthsOutCalc game = new LigthsOutCalc(testData, gridWidth);
      long steps = game.calculateMinSteps();
      assertEquals(1, steps);
   }

   @Test
   public void shouldZeroStepGrid() {
      boolean[] testData = { false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, false, false };

      int gridWidth = 5;
      LigthsOutCalc game = new LigthsOutCalc(testData, gridWidth);
      long steps = game.calculateMinSteps();
      assertEquals(0, steps);
   }

   @Test
   @RepeatedTest(20)
   public void testRandomGridAndAlwaysFindSolution() {
      // init gameboard random set of lights
      int gridWidth = (int) (Math.random() * 14 + 2);
      int gridLength = gridWidth * gridWidth;
      boolean[] randomizedGrid = new boolean[gridLength];
      LigthsOutCalc game = new LigthsOutCalc(randomizedGrid, gridWidth);
      for (int i = 0; i < gridLength; i++) {
         int pos = (int) Math.random() * gridLength;
         game.toggleCells(pos);
      }

      // test
      long steps = game.calculateMinSteps();
      assertTrue(steps < gridLength);
   }



}
