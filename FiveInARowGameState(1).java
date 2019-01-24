import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


class FiveInARowGameState extends GameState {
    private static ConcurrentHashMap<List<String>, Integer> occurredCase = new ConcurrentHashMap<>();

    FiveInARowGameState() {
        super();
        checkerBoard = new int[15][15];
    }

    @Override
    ArrayList<int[]> getAvailableMoves() {
        ArrayList<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (checkerBoard[i][j] == 0) {
                    int[] move = {i, j};
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    @Override
    boolean isOver() {
        List<String> allDirections = getAllDirections(lastMove, toOpponentId());
        for (String oneDirection : allDirections) {
            if (oneDirection.contains("11111")) {
                return true;
            }
        }

        return getAvailableMoves().size() == 0;
    }

    @Override
    GameState getUpdatedGameState(int[] userInput) {
        int[][] clonedCheckerBoard = new int[15][15];
        for (int i = 0; i < 15; i++) {
            clonedCheckerBoard[i] = checkerBoard[i].clone();
        }
        clonedCheckerBoard[userInput[0]][userInput[1]] = userId;
        FiveInARowGameState newState = new FiveInARowGameState();
        newState.setUserId(toOpponentId());
        newState.setCheckerBoard(clonedCheckerBoard);
        newState.setLastMove(userInput);
        return newState;
    }


    @Override
    int getMovesWeight(int[] move) { // always get the top five choices
        List<String> allDirections = getAllDirections(move, userId);
        int[][] locationWeight = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0},
                {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0},
                {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        int weight = locationWeight[move[0]][move[1]];
        if (occurredCase.containsKey(allDirections)) {
            weight += occurredCase.get(allDirections);
        } else {
            LinkedHashMap<Integer, List<String>> caseTable = new LinkedHashMap<>();
            LinkedHashMap<String, Integer> weightTable = new LinkedHashMap<>();
            // Following data from Internet, organized and shared by zjh776
            caseTable.put(1, List.of("11111"));
            caseTable.put(2, List.of("011110"));
            caseTable.put(3, List.of("011112", "0101110", "0110110"));
            caseTable.put(4, List.of("01110", "010110"));
            caseTable.put(5, List.of("001112", "010112", "011012", "10011", "10101", "2011102"));
            caseTable.put(6, List.of("00110", "01010", "010010"));
            caseTable.put(7, List.of("000112", "001012", "010012", "10001", "2010102", "2011002"));
            caseTable.put(8, List.of("211112", "21112", "2112"));
            weightTable.put("1", 100000);
            weightTable.put("2", 20000);
            weightTable.put("33", 10000);
            weightTable.put("34", 10000);
            weightTable.put("44", 5000);
            weightTable.put("45", 1000);
            weightTable.put("3", 500);
            weightTable.put("4", 200);
            weightTable.put("66", 100);
            weightTable.put("5", 50);
            weightTable.put("67", 10);
            weightTable.put("6", 5);
            weightTable.put("7", 3);
            weightTable.put("8", -5);
            StringBuilder allCases = new StringBuilder();
            for (String direction : allDirections) {
                outerLoop:
                for (Integer cases : caseTable.keySet()) {
                    List<String> allCombo = caseTable.get(cases);
                    for (String combo : allCombo) {
                        if (direction.contains(combo) || new StringBuilder(direction).reverse().toString().contains(combo)) {
                            allCases.append(cases);
                            break outerLoop;
                        }
                    }
                }
            }
            for (String eachCase : weightTable.keySet()) {
                if (allCases.toString().contains(eachCase) || allCases.reverse().toString().contains(eachCase)) {
                    occurredCase.put(allDirections, weightTable.get(eachCase));
                    weight += weightTable.get(eachCase);
                    break;
                }
            }
        }
        return weight;
    }

     private List<String> getAllDirections(int[] index, int userID) {
        int[][] direction = {{1, 0}, {0, 1}, {1, 1}, {1, -1}}; // {row, column, positive diagonal, negative diagonal}
        List<String> allDirections = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            boolean reachEndLeft = false;
            boolean reachEndRight = false;
            StringBuilder line = new StringBuilder();
            line.append(1);
            for (int j = 1; j < 6; j++) {
                if (!reachEndLeft) {
                    int rowIndex = index[0] - j * direction[i][0];
                    int colIndex = index[1] - j * direction[i][1];
                    if (rowIndex < 0 || rowIndex > 14 || colIndex < 0 || colIndex > 14) {
                        reachEndLeft = true;
                        line.append(2);
                    } else {
                        line.append(toUniformInt(checkerBoard[rowIndex][colIndex], userID));
                    }
                }
                if (!reachEndRight) {
                    int rowIndex = index[0] + j * direction[i][0];
                    int colIndex = index[1] + j * direction[i][1];
                    if (rowIndex < 0 || rowIndex > 14 || colIndex < 0 || colIndex > 14) {
                        reachEndRight = true;
                        line.insert(0, 2);
                    } else {
                        line.insert(0, toUniformInt(checkerBoard[rowIndex][colIndex], userID));
                    }
                }
            }
            allDirections.add(line.toString());
        }
        return allDirections;
    }

}
