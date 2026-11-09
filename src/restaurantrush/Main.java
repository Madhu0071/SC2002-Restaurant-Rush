package restaurantrush;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ManagerInput input;
        if (args.length > 0) {
            input = new ScriptedInput(args[0]);
        } else {
            input = new RestaurantCLI(new Scanner(System.in), System.out); 
        }

        GameController controller = new GameController(input);
        controller.run();
    }
}
