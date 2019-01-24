import java.util.ArrayList;
import java.util.Arrays;

public abstract class GameState implements Comparable<GameState> {

    int[] lastMove = {0, 0};
    int userId;
    int[][] checkerBoard;
    int stateWeight;

    abstract ArrayList<int[]> getAvailableMoves();

    abstract boolean isOver();

    abstract GameState getUpdatedGameState(int[] userInput);

    abstract int getMovesWeight(int[] move);

    void setUserId(int userId) {
        this.userId = userId;
    }

    int getBestMoveWeight(){
        int weight = -10; //less than the worst weight;
        for (int[] move : getAvailableMoves()){
            int movesWeight = getMovesWeight(move);
            if (movesWeight > weight){
                weight = movesWeight;
            }
        }
        return weight;
    }

    int getStateWeight(){
        int i = 0;
        int s = 0;
        for (int[] move : getAvailableMoves()){
            i += getMovesWeight(move);
            GameState nextState = getUpdatedGameState(move);
            for (int[] nextMove : nextState.getAvailableMoves()){
                s += nextState.getMovesWeight(nextMove);
            }
        }
        return i - s/getAvailableMoves().size();
    }

    int[][] getCheckerBoard() {
        return checkerBoard;
    }

    void setCheckerBoard(int[][] checkerBoard) {
        this.checkerBoard = checkerBoard;
    }

    void setLastMove(int[] lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public int compareTo(GameState other) {
        if (this.getBestMoveWeight() > other.getBestMoveWeight()) {
            return 1;
        } else if (this.getBestMoveWeight() < other.getBestMoveWeight()) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "User" + userId + "made move: " + Arrays.toString(lastMove);
    }

    ArrayList<GameState> toNextStates() {
        ArrayList<int[]> moves = new ArrayList<>(getAvailableMoves());
        ArrayList<GameState> states = new ArrayList<>();
        for (int[] move : moves) {
            states.add(getUpdatedGameState(move));
        }
        return states;
    }

    static int toUniformInt(int input, int id) {
        if (id == 2) {
            if (input == 1) {
                return 2;
            } else if (input == 2) {
                return 1;
            }
        }
        return input;
    }

    int toOpponentId() {
        int opponentId = 1;
        if (userId == 1) {
            opponentId = 2;
        }
        return opponentId;
    }

}