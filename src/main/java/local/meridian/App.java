package local.meridian;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Hello in the Lights Out Game!");
        
        int width = 6;
        LigthsOutCalc game = new LigthsOutCalc(width);

        long steps = game.calculateMinSteps();

        System.out.println("Generated grid was:");
        System.out.println(game.toString());

        String information = (steps == Long.MAX_VALUE) ? 
                "Cannot solve this grid" : "Shortest solution: " + steps;
        System.out.println(information);
        System.out.println("Examined variations: " + game.getMaxVariation());

    }
}
