import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GreedyStrategy extends Strategy {
    @Override
    public GameState makeMove(GameState currentState) {
        boolean noOneKill = true;
        ArrayList<GameState> waitList = new ArrayList<>();
        int maxDiff = Integer.MIN_VALUE;
        GameState bestStat = null;
        HashMap<int[], Integer> moves = currentState.getWeight();
        for (int[] move : moves.keySet()) {
            GameState thisState = currentState.getUpdatedGameState(move);
            int weight = moves.get(move);
            if (weight >= 100000) {
                return thisState;
            } else if (weight >= 10000) {
                waitList.add(thisState);
            } else {
                int nextMax = Collections.max(thisState.getWeight().values());
                if (nextMax < 10000) {
                    int diff = weight - nextMax;
                    if (diff > maxDiff) {
                        maxDiff = diff;
                        bestStat = thisState;
                    }
                }
                else {
                    noOneKill = false;
                }
            }

        }
        if (waitList.size() != 0 && noOneKill) {
            return waitList.get(0);
        }
        return bestStat;
    }
}
