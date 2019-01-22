import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GameMain {

    public static void main(String[] args) {
        HashMap<String, GameState> allGames = new HashMap<>();
        HashMap<String, Strategy> allStrategy = new HashMap<>();
        allGames.put("1", new FiveInARowGameState());
        allStrategy.put("user", new UserStrategy());
        allStrategy.put("greedy", new GreedyStrategy());
        Scanner scanner = new Scanner(System.in);
        System.out.println("available games:");
        for (String s : allGames.keySet()){
            System.out.println(s);
        }
        System.out.println("available strategies:");
        for (String s : allStrategy.keySet()){
            System.out.println(s);
        }
        System.out.println("Enter the game name:");
        String gameName = scanner.next();
        while (!allGames.keySet().contains(gameName)){
            System.out.println("Incorrect game name!!");
            gameName = scanner.next();
        }
        GameState gameState = allGames.get(gameName);
        gameState.setUserId(1);
        System.out.println("Enter the strategy for user1:");
        String strategyName1 = scanner.next();
        while (!allStrategy.keySet().contains(strategyName1)){
            System.out.println("Incorrect strategy name!!");
            strategyName1 = scanner.next();
        }
        Strategy strategy1 = allStrategy.get(strategyName1);
        System.out.println("Enter the strategy for user2:");
        String strategyName2 = scanner.next();
        while (!allStrategy.keySet().contains(strategyName2)){
            System.out.println("Incorrect strategy name!!");
            strategyName2 = scanner.next();
        }
        Strategy strategy2 = allStrategy.get(strategyName2);
        System.out.println("Enter Y if user1 move first");
        if ("Y".equalsIgnoreCase(scanner.next())){
            printCheckBoard(gameState);
            gameState = strategy1.makeMove(gameState);
            System.out.println("user1 has make move" + Arrays.toString(gameState.lastMove));
            printCheckBoard(gameState);
        }
        while (!gameState.isOver()){
            gameState = strategy2.makeMove(gameState);
            System.out.println("user2 has make move" + Arrays.toString(gameState.lastMove));
            printCheckBoard(gameState);
            if (gameState.isOver()){
                break;
            }
            gameState = strategy1.makeMove(gameState);
            System.out.println("user1 has make move" + Arrays.toString(gameState.lastMove));
            printCheckBoard(gameState);
            }
        }

    private static void printCheckBoard(GameState gameState){
        for (int[] line : gameState.getCheckerBoard()){
            System.out.println(Arrays.toString(line));
        }
    }
}
