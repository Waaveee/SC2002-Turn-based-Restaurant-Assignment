package stage1;

import java.util.Scanner;

/**
 * Restaurant Rush - Stage 1 (Java 8 or later; no external libraries).
 * From the Java stuff folder: javac stage1/*.java
 * Play from Java stuff: java stage1.Main
 * Reproducible runs: java stage1.Main --demo-victory / --demo-defeat
 * Verified demo victory: 5 paid, $75.00, 74.40 average satisfaction.
 * Verified demo defeat: 0 paid, $0.00, N/A average; both runs use 18 turns.
 * Each stage has its own package so the editor cannot mix up the classes.
 * Inside this stage folder, use: javac *.java then java -cp .. stage1.Main
 *
 * Food rule: arrival IDs repeat Pasta, Burger, Salad. The manager selects
 * customers, tables and kitchen work, but never dishes.
 * Demo victory manager: serve READY orders, else take a seated customer's order,
 * else seat the oldest queued customer. Chef works on the oldest unfinished order.
 * Demo defeat manager: all staff wait. Both modes execute the real 18-turn loop.
 */
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
            System.out.println("Usage: java stage1.Main [--demo-victory | --demo-defeat]");
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
