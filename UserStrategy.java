import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UserStrategy implements Strategy {

    @Override
    public GameState makeMove(GameState currentState) {
        int[] move = new int[2];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the x coordinate of your move");
        move[1] = scanner.nextInt();
        System.out.println("Please enter the y coordinate of your move");
        move[0] = scanner.nextInt();
        while (! arrayContain(move,currentState.getAvailableMoves())){
            System.out.println("Invalid Move!!");
            System.out.println("Please enter the x coordinate of your move");
            move[1] = scanner.nextInt();
            System.out.println("Please enter the y coordinate of your move");
            move[0] = scanner.nextInt();
        }
        return currentState.getUpdatedGameState(move);
        }

    private static boolean arrayContain(int[] array, ArrayList<int[]> list){
        for (int[] arr : list){
            if (Arrays.equals(arr, array)){
                return true;
            }
        }
        return false;
    }
}
