package makeasquare;

import java.util.*;

public class mThreading implements Runnable {
    //dimensions of board
    static int boardYdim = 4;
    static int boardXdim = 4;
    //dimensions of pieces
    static int pieceYdim = 4;
    static int pieceXdim = 4;
    public HashMap<Integer, ArrayList<int[][]>> piecesThread = new HashMap<Integer, ArrayList<int[][]>>();
    public int[] usablePieces;
    public ArrayList<int[][]> solutionsThread = new ArrayList<int[][]>();
    public int[][] board;
    public int depth = 0;

    public mThreading(HashMap<Integer, ArrayList<int[][]>> pieces, int[] usablePieces) {
        this.piecesThread = pieces;
        this.usablePieces = usablePieces;
    }

    public static void solve(int[][] board, int[] usablePieces, int depth, ArrayList<int[][]> solutions, HashMap<Integer, ArrayList<int[][]>> pieces) {
        //for every piece in pieces
        for (int index = 0; index < usablePieces.length; index++) {
            ArrayList<int[][]> permutations = pieces.get(usablePieces[index]);

            //for each permutation for the given piece
            for (int perm = 0; perm < permutations.size(); perm++) {

                //for each point on the board
                for (int boardY = 0; boardY < boardYdim; boardY++) {
                    for (int boardX = 0; boardX < boardXdim; boardX++) {

                        //create a copy of the board -- to edit 
                        int[][] newBoard = new int[board.length][board[0].length];
                        for (int y = 0; y < board.length; y++) {
                            for (int x = 0; x < board[0].length; x++) {
                                newBoard[y][x] = board[y][x];
                            }
                        }

                        //try to place piece
                        boolean returnValue = placePiece(boardY, boardX, usablePieces[index],
                                permutations.get(perm), newBoard);

                        //if piece has been placed
                        if (returnValue) {
                            int[] newPieces = new int[usablePieces.length - 1];
                            int indexCounter = 0;

                            //create a new int[] containing all pieces other than the one that was just placed
                            for (int i = 0; i < usablePieces.length; i++) {
                                if (usablePieces[i] != usablePieces[index]) {
                                    newPieces[indexCounter] = usablePieces[i];
                                    indexCounter++;
                                }
                            }

                            //if newPieces is empty -- no pieces remain
                            if (newPieces.length == 0) {

                                //always add the first solution
                                if (solutions.isEmpty()) {
                                    solutions.add(newBoard);
                                } //check if solution already exists
                                else if (doesSolutionExist(newBoard, solutions) == false) {
                                    solutions.add(newBoard);
                                }
                            } //recursion
                            else {
                                solve(newBoard, newPieces, depth + 1, solutions, pieces);
                            }
                        }
                    }
                }
            }
        }
    }

    //tries to place a piece on the board
    //if placed successfully, returns true
    //if piece cannot be placed, returns false
    public static boolean placePiece(int boardY, int boardX, int currentPiece, int[][] currentPerm, int[][] currentBoard) {

        //for each point in the piece
        for (int pieceY = 0; pieceY < pieceYdim; pieceY++) {
            for (int pieceX = 0; pieceX < pieceXdim; pieceX++) {

                //if the piece has a filled square
                if (currentPerm[pieceY][pieceX] != 0) {

                    int y = boardY + pieceY;

                    //check y boundary
                    if (y >= boardYdim) {
                        return false;
                    }
                    int x = boardX + pieceX;

                    //check x boundary
                    if (x >= boardXdim) {
                        return false;
                    }

                    //check if board has empty spot
                    if (currentBoard[y][x] != 0) {
                        return false;
                    }

                    currentBoard[y][x] = currentPiece;
                }
            }
        }
        return true;
    }

    public static boolean doesSolutionExist(int[][] sol, ArrayList<int[][]> solutions) {
        boolean match;

        for (int index = 0; index < solutions.size(); index++) {
            match = true;
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 2; x++) {
                    if ((solutions.get(index))[y][x] != sol[y][x]) {
                        match = false;
                    }
                }
            }
            if (match == true) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        board = new int[boardYdim][boardXdim];
        //start solving
        solve(board, usablePieces, depth, solutionsThread, piecesThread);
        System.out.println("Done");
    }

}
