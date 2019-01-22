import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AlphaBetaPruning extends Strategy{
    private int level;

    AlphaBetaPruning(int level){
        this.level = level;
    }

    private int makeMoveHelper(GameState currentState, int currentWeight, int levelCounter) {
        if (levelCounter <= 0){
            return Collections.max(currentState.getWeight().values());
        }
        else {
            ArrayList<Integer> allWeight = new ArrayList<>();
            HashMap<int[], Integer> nextMoves = currentState.getWeight();
            for (int[] move : nextMoves.keySet()){
                GameState nextState = currentState.getUpdatedGameState(move);
                allWeight.add(makeMoveHelper(nextState, nextMoves.get(move), levelCounter-=1));
            }
            return currentWeight -Collections.max(allWeight);
        }
    }

    @Override
    public GameState makeMove(GameState currentState) {
        int bestValue = Integer.MAX_VALUE;
        GameState bestNextState = null;
        HashMap<int[], Integer> nextMoves = currentState.getWeight();
        for (int[] move : nextMoves.keySet()){
            GameState nextState = currentState.getUpdatedGameState(move);
            int weight = makeMoveHelper(nextState, nextMoves.get(move), level);
            if (weight < bestValue){
                bestValue = weight;
                bestNextState = nextState;
            }
        }
        return bestNextState;
    }
}
