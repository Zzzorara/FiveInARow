import java.util.ArrayList;


public class GreedyStrategy implements Strategy {
    @Override
    public GameState makeMove(GameState currentState) {
        int maxDiff = Integer.MIN_VALUE;
        int maxWait = Integer.MIN_VALUE;
        int[] waitMove = null;
        int[] bestMove = currentState.getAvailableMoves().get(0);
        ArrayList<int[]> allMoves = currentState.getAvailableMoves();
        for (int[] move : allMoves) {
            int weight = currentState.getMovesWeight(move);
            if (weight >= 100000) {
                return currentState.getUpdatedGameState(move);
            } else if (weight >= 10000 && currentState.getUpdatedGameState(move).getBestMoveWeight() < 100000 && currentState.getMovesWeight(move) > maxWait) {
                waitMove = move;
                maxWait = currentState.getMovesWeight(move);
            } else {
                GameState nextState = currentState.getUpdatedGameState(move);
                int diff = currentState.getMovesWeight(move) - nextState.getBestMoveWeight();
                if (diff > maxDiff) {
                    bestMove = move;
                    maxDiff = diff;
                }
            }

        }
        if (waitMove != null) {
            return currentState.getUpdatedGameState(waitMove);
        }
        return currentState.getUpdatedGameState(bestMove);
    }
}
