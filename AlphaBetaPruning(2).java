import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AlphaBetaPruning implements Strategy {
    final private AtomicInteger topAlpha = new AtomicInteger(Integer.MIN_VALUE);

    @Override
    public GameState makeMove(GameState currentState) {
        ArrayList<GameState> nextStates = currentState.toNextStates();
        Collections.sort(nextStates);
        GameState maxState = nextStates.get(0);
        int subListLength = (nextStates.size() - 1) / (Runtime.getRuntime().availableProcessors() + 1);
        ArrayList<FutureTask<Pair<GameState, Integer>>> allTask = new ArrayList<>();
        ArrayList<GameState> passToThreadStates = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int sizeCounter = 0;
        for (GameState state : nextStates) {
            passToThreadStates.add(state);
            sizeCounter++;
            if (sizeCounter == subListLength) {
                CallableStateEvaluator evaluator = new CallableStateEvaluator(passToThreadStates);
                FutureTask<Pair<GameState, Integer>> futureTask = new FutureTask<>(evaluator);
                executorService.submit(futureTask);
                allTask.add(futureTask);
                sizeCounter = 0;
                passToThreadStates = new ArrayList<>();
            }
        }
        int maxValue = Integer.MIN_VALUE;
        executorService.shutdown();
        for (Future<Pair<GameState, Integer>> future : allTask) {
            try {
                Pair<GameState, Integer> pair = future.get();
                if (pair.getValue() > maxValue) {
                    maxState = pair.getKey();
                    maxValue = pair.getValue();
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        topAlpha.set(Integer.MIN_VALUE);
        return maxState;
    }


    class CallableStateEvaluator implements Callable<Pair<GameState, Integer>> {

        ArrayList<GameState> inputState;
        int reDepth = 2; //will be determinate by the complexity method in future version.

        CallableStateEvaluator(ArrayList<GameState> state) {
            inputState = state;
        }

        @Override
        public Pair<GameState, Integer> call() {
            GameState optimalState = null;
            int optimalWeight = Integer.MIN_VALUE;
            for (GameState state : inputState) {
                int weightForState = recursiveHelper(state, topAlpha.get(), Integer.MAX_VALUE, reDepth, false);
                topAlpha.set(Math.max(weightForState, topAlpha.get()));
                if (weightForState > optimalWeight) {
                    optimalState = state;
                    optimalWeight = weightForState;
                }
            }
            return new Pair<>(optimalState, optimalWeight);

        }

        private int recursiveHelper(GameState currentState, int alpha, int beta, int depth, boolean maxTurn) {
            if (depth <= 0 || currentState.isOver()) {
                if (!maxTurn) {
                    return -currentState.getStateWeight();
                }
                return currentState.getStateWeight();
            } else {
                int weight = Integer.MAX_VALUE;
                if (maxTurn) {
                    weight = Integer.MIN_VALUE;
                }
                ArrayList<GameState> gameStates = currentState.toNextStates();
                for (GameState gameState : gameStates) {
                    int nextWeight = recursiveHelper(gameState, alpha, beta, depth - 1, !maxTurn);
                    if (maxTurn) {
                        weight = Math.max(weight, nextWeight);
                        topAlpha.set(Math.max(nextWeight, topAlpha.get()));
                    } else {
                        weight = Math.min(weight, nextWeight);
                        beta = Math.min(nextWeight, beta);
                    }
                    if (topAlpha.get() >= beta) {
                        break;
                    }
                }
                return weight;

            }
        }
    }
}
