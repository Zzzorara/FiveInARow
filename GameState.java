import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameState {

    int[] lastMove = {0, 0};
    int userId;
    int[][] checkerBoard;

    public abstract ArrayList<int[]> getAvailableMoves();

    abstract boolean isOver();

    abstract GameState getUpdatedGameState(int[] userInput);

    abstract HashMap<int[], Integer> getWeight();

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int[][] getCheckerBoard() {
        return checkerBoard;
    }

    public void setCheckerBoard(int[][] checkerBoard) {
        this.checkerBoard = checkerBoard;
    }

    public void setLastMove(int[] lastMove){
        this.lastMove = lastMove;
    }

}