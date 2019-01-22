import java.util.ArrayList;


public class GreedyStrategy implements Strategy {
    @Override
    public GameState makeMove(GameState currentState) {
        int maxDiff = Integer.MIN_VALUE;
        GameState waitState = null;
        GameState bestStat = currentState.toNextStates().get(0);
        ArrayList<GameState> nextLevel = currentState.toNextStates();
        for (GameState state : nextLevel) {
            int weight = state.getLastMovesWeight();
            if (weight >= 100000) {
                return state;
            } else if (weight >= 10000) {
                boolean noOneKill = true;
                for (GameState nextState : state.toNextStates()){
                    if (nextState.getLastMovesWeight() >= 100000){
                        noOneKill = false;
                        break;
                    }
                }
                if (noOneKill){waitState = state;}
            } else {
                int nextMax = Integer.MIN_VALUE;
                for (GameState nextState : state.toNextStates()){
                    if (nextMax < nextState.getLastMovesWeight()){
                        nextMax = nextState.getLastMovesWeight();
                    }
                }
                int diff = state.getLastMovesWeight()-nextMax;
                if (diff > maxDiff && nextMax < 10000){
                    bestStat = state;
                    maxDiff = diff;
                }
            }

        }
        if (waitState != null){
            return waitState;
        }
        return bestStat;
    }
}
