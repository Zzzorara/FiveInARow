import java.util.*;


public class FiveInARowGameState extends GameState {
    private static HashMap<String[], Integer> occurredCase = new HashMap<>();

    FiveInARowGameState() {
        super();
        checkerBoard = new int[15][15];
    }

    @Override
    public ArrayList<int[]> getAvailableMoves() {
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
        int[][] direction = {{1, 0}, {0, 1}, {1, 1}, {1, -1}}; // {row, column, positive diagonal, negative diagonal}
        int lastUser = 1;
        if (userId == 1) {
            lastUser = 2;
        }
        for (int j = 0; j < 4; j++) {
            boolean reachEndRight = false;
            boolean reachEndLeft = false;
            int counter = 1;
            for (int i = 1; i < 6; i++) {
                if (!reachEndRight) {
                    int rowIndex = lastMove[0] + i * direction[j][0];
                    int colIndex = lastMove[1] + i * direction[j][1];
                    if (rowIndex < 0 || rowIndex > 14 || colIndex < 0 || colIndex > 14) {
                        reachEndRight = true;
                    } else if (checkerBoard[rowIndex][colIndex] != lastUser) {
                        reachEndRight = true;
                    } else {
                        counter += 1;
                    }
                }
                if (!reachEndLeft) {
                    int rowIndex = lastMove[0] - i * direction[j][0];
                    int colIndex = lastMove[1] - i * direction[j][1];
                    if (rowIndex < 0 || rowIndex > 14 || colIndex < 0 || colIndex > 14) {
                        reachEndLeft = true;
                    } else if (checkerBoard[rowIndex][colIndex] != lastUser) {
                        reachEndLeft = true;
                    } else {
                        counter += 1;
                    }
                }
            }
            if (counter > 4) {
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
        if (userId == 1) {
            newState.setUserId(2);
        } else {
            newState.setUserId(1);
        }
        newState.setCheckerBoard(clonedCheckerBoard);
        newState.setLastMove(userInput);
        return newState;
    }

    // Function for Alpha-Beta Pruning Strategy; put it here because this method of getting weight
    // is only for the game Five in a Row. Thus, following the open-close principle, need to
    // put this method here for the convenience of future changes.

    @Override
    HashMap<int[], Integer> getWeight() { // always get the top five choices
        LinkedHashMap<Integer, List<String>> caseTable = new LinkedHashMap<>();
        LinkedHashMap<List<Integer>, Integer> weightTable = new LinkedHashMap<>();
        // Following data from Internet, organized and shared by zjh776
        caseTable.put(1, List.of("11111"));
        caseTable.put(2, List.of("011110"));
        caseTable.put(3, List.of("011112", "0101110", "0110110"));
        caseTable.put(4, List.of("01110", "010110"));
        caseTable.put(5, List.of("001112", "010112", "011012", "10011", "10101", "2011102"));
        caseTable.put(6, List.of("00110", "01010", "010010"));
        caseTable.put(7, List.of("000112", "001012", "010012", "10001", "2010102", "2011002"));
        caseTable.put(8, List.of("211112", "21112", "2112"));
        weightTable.put(List.of(1), 100000);
        weightTable.put(List.of(2), 10000);
        weightTable.put(List.of(3, 3), 10000);
        weightTable.put(List.of(3, 4), 10000);
        weightTable.put(List.of(4, 4), 5000);
        weightTable.put(List.of(4, 5), 1000);
        weightTable.put(List.of(3), 500);
        weightTable.put(List.of(4), 200);
        weightTable.put(List.of(6, 6), 100);
        weightTable.put(List.of(5), 50);
        weightTable.put(List.of(6, 7), 10);
        weightTable.put(List.of(7), 3);
        weightTable.put(List.of(8), -5);
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
        ArrayList<int[]> moves = getAvailableMoves();
        HashMap<int[], Integer> MovesWeights = new HashMap<>();
        for (int[] onePoint : moves) {
            int weight;
            String[] allDirections = getAllDirections(onePoint);
            if (occurredCase.containsKey(allDirections)) {
                weight = occurredCase.get(allDirections);
                weight += locationWeight[onePoint[0]][onePoint[1]];
            } else {
                Integer[] allCases = new Integer[4];
                for (int j = 0; j < allDirections.length; j++) {
                    String direction = allDirections[j];
                    for (Integer cases : caseTable.keySet()) {
                        List<String> allCombo = caseTable.get(cases);
                        for (String combo : allCombo) {
                            if (direction.contains(combo) || new StringBuilder(direction).reverse().toString().contains(combo)) {
                                allCases[j] = cases;
                                break;
                            }
                        }
                    }
                }
                List<Integer> allCasesList = Arrays.asList(allCases);
                weight = locationWeight[onePoint[0]][onePoint[1]];
                for (List<Integer> eachCase : weightTable.keySet()) {
                    if (allCasesList.containsAll(eachCase)) {
                        occurredCase.put(allDirections, weightTable.get(eachCase));
                        weight += weightTable.get(eachCase);
                        break;
                    }
                }
            }
            MovesWeights.put(onePoint, weight);
        }
        return MovesWeights;
    }

    private String[] getAllDirections(int[] index) {
        int[][] direction = {{1, 0}, {0, 1}, {1, 1}, {1, -1}}; // {row, column, positive diagonal, negative diagonal}
        boolean reachEndLeft = false;
        boolean reachEndRight = false;
        String[] allDirections = new String[4];
        for (int i = 0; i < 4; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 1; j < 6; j++) {
                if (!reachEndLeft) {
                    int rowIndex = index[0] - j * direction[i][0];
                    int colIndex = index[1] - j * direction[i][1];
                    if (rowIndex < 0 || rowIndex > 14 || colIndex < 0 || colIndex > 14) {
                        reachEndLeft = true;
                    } else {
                        if (userId == 2){
                            if (checkerBoard[rowIndex][colIndex] == 2){
                                line.append(1);
                            }
                            else if (checkerBoard[rowIndex][colIndex] == 1){
                                line.append(2);
                            }
                            else {
                                line.append(0);
                            }
                        }
                        else {
                            line.append(checkerBoard[rowIndex][colIndex]);
                        }
                    }
                }
                if (!reachEndRight) {
                    int rowIndex = index[0] + j * direction[i][0];
                    int colIndex = index[1] + j * direction[i][1];
                    if (rowIndex < 0 || rowIndex > 14 || colIndex < 0 || colIndex > 14) {
                        reachEndRight = true;
                    } else {
                        if (userId == 2){
                            if (checkerBoard[rowIndex][colIndex] == 2){
                                line.insert(0,1);
                            }
                            else if (checkerBoard[rowIndex][colIndex] == 1){
                                line.insert(0,2);
                            }
                            else {
                                line.insert(0,0);
                            }
                        }
                        else {
                            line.insert(0,checkerBoard[rowIndex][colIndex]);
                        }
                    }
                }
            }
            allDirections[i] = line.toString();
        }
        return allDirections;
    }

}
