package restaurantrush;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ManagerInput input;
        if (args.length > 0) {
            input = new ScriptedInput(args[0]); // scripted mode
        } else {
            input = new RestaurantCLI(new Scanner(System.in), System.out); // interactive mode
        }

        GameController controller = new GameController(input);
        controller.run();
    }
}
