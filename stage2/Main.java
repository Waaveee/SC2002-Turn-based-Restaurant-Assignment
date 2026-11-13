package stage2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        RestaurantUI ui;

        if(args.length == 0){
            ui = new RestaurantUI(sc);
        }
        else if(args.length == 1 && args[0].equals("--demo-victory")){
            ui = new DemoUI(sc, true);
        }
        else if(args.length == 1 && args[0].equals("--demo-defeat")){
            ui = new DemoUI(sc, false);
        }
        else{
            System.out.println("Usage: java stage2.Main [--demo-victory | --demo-defeat]");
            sc.close();
            return;
        }

        Restaurant restaurant = new Restaurant();
        ArrivalSchedule schedule = new ArrivalSchedule();
        IOrderChoiceStrategy choiceStrategy = new DeterministicOrderChoiceStrategy();
        GameMgr game = new GameMgr(restaurant, ui, schedule, choiceStrategy);
        game.runGame();
        sc.close();
    }
}
