import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GameMain {
    private HashMap<String, GameState> allGames;
    private GameState gameState;


    GameMain(){
        allGames = new HashMap<>();
        allGames.put("Five-in-One-Row", new FiveInARowGameState());

    }

    public static void main(String[] args) {
        Strategy strategy = new GreedyStrategy();
        GameMain main = new GameMain();
        Scanner scanner = new Scanner(System.in);
        System.out.println("available games:");
        for (String s : main.allGames.keySet()){
            System.out.println(s);
        }
        System.out.println("Enter the game name:");
        String gameName = scanner.next();
        while (!main.allGames.keySet().contains(gameName)){
            System.out.println("Incorrect game name!!");
            gameName = scanner.next();
        }
        main.gameState = main.allGames.get(gameName);
        main.gameState.setUserId(1);
        System.out.println("Enter Y to move first");
        if ("Y".equalsIgnoreCase(scanner.next())){
            main.printCheckBoard();
            int[] move = new int[2];
            System.out.println("Please enter the x coordinate of your move");
            move[1] = scanner.nextInt();
            System.out.println("Please enter the y coordinate of your move");
            move[0] = scanner.nextInt();
            main.gameState = main.gameState.getUpdatedGameState(move);
            main.printCheckBoard();
        }
        while (!main.gameState.isOver()){
            main.gameState = strategy.makeMove(main.gameState);
            if (main.gameState.isOver()){
                break;
            }
            System.out.println("Robort has make move");
            main.printCheckBoard();
            int[] move = new int[2];
            System.out.println("Please enter the x coordinate of your move");
            move[1] = scanner.nextInt();
            System.out.println("Please enter the y coordinate of your move");
            move[0] = scanner.nextInt();
            ArrayList<int[]> avaMoves = main.gameState.getAvailableMoves();
            while (!inTheList(move, avaMoves)){
                System.out.println("It is not a valid move");
                System.out.println("Please enter the x coordinate of your move");
                move[0] = scanner.nextInt();
                System.out.println("Please enter the y coordinate of your move");
                move[1] = scanner.nextInt();
            }
            main.gameState = main.gameState.getUpdatedGameState(move);
            main.printCheckBoard();
        }
    }

    void printCheckBoard(){
        for (int[] line : gameState.getCheckerBoard()){
            System.out.println(Arrays.toString(line));
        }
    }

    static boolean inTheList(int[] array, ArrayList<int[]> list){
        for (int[] arr : list){
            if (Arrays.equals(arr, array)){
                return true;
            }
        }
        return false;
    }
}
