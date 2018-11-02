package shaftware.funwithstrangers;

public class CheckersLogic {

    private square[][] board = new square[8][8];

    enum square {OPEN, WHITE, BLACK, WKING, BKING}

    enum outcome {TIE, WHITE, BLACK, IN_PROGRESS}

    private square piece;
    private square kPiece;
    private square opPiece;
    private square opkPiece;
    private boolean turn;
    private boolean lastMoveJump = false;

    public CheckersLogic(square piece, boolean turn) {
        this.piece = piece;
        this.turn = turn;
        if (piece == square.WHITE) {
            opPiece = square.BLACK;
            opkPiece = square.BKING;
            kPiece = square.WKING;
        } else {
            opPiece = square.WHITE;
            opkPiece = square.WKING;
            kPiece = square.BKING;
        }
        initializeBoard();
    }

    public void setBoard(square[][] board) {
        this.board = board;
    }

    public square getPiece() {
        return piece;
    }

    public square[][] getBoard() {
        return board;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    //Method only used for testing purposes
    @Deprecated
    public void swapPiece() {
        if (piece == square.BLACK) {
            piece = square.WHITE;
            opPiece = square.BLACK;
            kPiece = square.WKING;
            opkPiece = square.BKING;
        } else {
            piece = square.BLACK;
            opPiece = square.WHITE;
            kPiece = square.BKING;
            opkPiece = square.WKING;
        }
    }


    //Sets board's initial piece placements
    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 != j % 2) {
                    board[i][j] = square.OPEN;
                    if (i <= 2)
                        board[i][j] = square.BLACK;
                    else if (i >= 5)
                        board[i][j] = square.WHITE;
                } else {
                    board[i][j] = square.OPEN;
                }
            }
        }
    }

    //checks whether there are any more valid moves available e.g. another jump that could be made
    public boolean checkEndTurn(Checkers.Move selectedMove) {
        if (!lastMoveJump)
            return true;
        if (validMove(selectedMove, new Checkers.Move(selectedMove.getRow() + 2, selectedMove.getCol() + 2), false)) {
            return false;
        } else if (validMove(selectedMove, new Checkers.Move(selectedMove.getRow() + 2, selectedMove.getCol() - 2), false)) {
            return false;
        } else if (validMove(selectedMove, new Checkers.Move(selectedMove.getRow() - 2, selectedMove.getCol() + 2), false)) {
            return false;
        } else if (validMove(selectedMove, new Checkers.Move(selectedMove.getRow() - 2, selectedMove.getCol() - 2), false)) {
            return false;
        }
        return true;
    }

    //TODO
//checks to see if the new move is valid
//checks everything: jumps, whether the opposing piece is jumped over, etc
    public boolean validMove(Checkers.Move selectedMove, Checkers.Move destinationMove, boolean initiateMove) {
        if (selectedMove == null || destinationMove == null)
            return false;
        //checks to see if the coordinates are even on the board. Should never reach here
        if (!validCoordinates(selectedMove.getRow(), selectedMove.getCol()))
            return false;
        if (!validCoordinates(destinationMove.getRow(), destinationMove.getCol()))
            return false;

        //if the selected piece is the players...
        square selected = board[selectedMove.getRow()][selectedMove.getCol()];
        square destination = board[destinationMove.getRow()][destinationMove.getCol()];
        if (selected == piece || selected == kPiece) {
            //if the destination piece is open...
            if (destination == square.OPEN) {
                //TODO
                //Check if jumps and if over opponent piece
                int rowDiff = Math.abs(selectedMove.getRow() - destinationMove.getRow());
                int colDiff = Math.abs(selectedMove.getCol() - destinationMove.getCol());
                if (rowDiff == 1 && colDiff == 1) {
                    //if only moving the piece without jumping...

                    //if wanting to move in wrong direction get out
                    if (!correctMoveDirection(selectedMove, destinationMove))
                        return false;
                    if (initiateMove) {
                        if (selected == kPiece) {
                            board[selectedMove.getRow()][selectedMove.getCol()] = square.OPEN;
                            board[destinationMove.getRow()][destinationMove.getCol()] = kPiece;
                        } else {
                            board[selectedMove.getRow()][selectedMove.getCol()] = square.OPEN;
                            board[destinationMove.getRow()][destinationMove.getCol()] = piece;
                        }
                        makeKing(destinationMove);
                    }
                    lastMoveJump = false;
                    return true;
                } else if (rowDiff == 2 && colDiff == 2) {
                    //TODO
                    //if jumping over a piece...

                    //if wanting to move in wrong direction get out
                    if (!correctMoveDirection(selectedMove, destinationMove) && !lastMoveJump)
                        return false;
                    //Calculate that piece's coordinates
                    lastMoveJump = true;
                    int rowShift = 0, colShift = 0;

                    if (selectedMove.getRow() > destinationMove.getRow())
                        rowShift = -1;
                    else if (selectedMove.getRow() < destinationMove.getRow())
                        rowShift = 1;

                    if (selectedMove.getCol() > destinationMove.getCol())
                        colShift = -1;
                    else if (selectedMove.getCol() < destinationMove.getCol())
                        colShift = 1;

                    if (Math.abs(rowShift) != 1 || Math.abs(colShift) != 1)
                        return false;


                    //check if the middle piece is opponents piece
                    int middleRow = rowShift + selectedMove.getRow();
                    int middleCol = colShift + selectedMove.getCol();
                    if (board[middleRow][middleCol] != opPiece && board[middleRow][middleCol] != opkPiece)
                        return false;

                    if (initiateMove) {
                        board[middleRow][middleCol] = square.OPEN;

                        if (board[selectedMove.getRow()][selectedMove.getCol()] == kPiece) {
                            board[selectedMove.getRow()][selectedMove.getCol()] = square.OPEN;
                            board[destinationMove.getRow()][destinationMove.getCol()] = kPiece;
                        } else {
                            board[selectedMove.getRow()][selectedMove.getCol()] = square.OPEN;
                            board[destinationMove.getRow()][destinationMove.getCol()] = piece;
                        }
                        makeKing(destinationMove);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void makeKing(Checkers.Move move) {
        if (piece == square.WHITE) {
            if (move.getRow() == 0)
                board[move.getRow()][move.getCol()] = square.WKING;
        } else if (piece == square.BLACK)
            if (move.getRow() == 7)
                board[move.getRow()][move.getCol()] = square.BKING;
    }

    private boolean correctMoveDirection(Checkers.Move selectedMove, Checkers.Move destinationMove) {
        if (board[selectedMove.getRow()][selectedMove.getCol()] == square.WKING || board[selectedMove.getRow()][selectedMove.getCol()] == square.BKING)
            return true;
        if (piece == square.WHITE) {
            if (destinationMove.getRow() < selectedMove.getRow())
                return true;
        } else if (piece == square.BLACK)
            if (destinationMove.getRow() > selectedMove.getRow())
                return true;
        return false;
    }

    //returns true if the coordinates are valid, false otherwise
    private boolean validCoordinates(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            return true;
        }
        return false;
    }

    public outcome checkWinner() {
        int black = 0, white = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == square.BLACK) {
                    black++;
                } else if (board[i][j] == square.WHITE) {
                    white++;
                }
            }
        }
        if (black == 0)
            return outcome.WHITE;
        if (white == 0)
            return outcome.BLACK;

        //TODO
        //make efficient
        //checks for tie
        for (int i = 0; i < 64; i++){
            for (int j = 0; j < 64; j++){
                if (validMove(new Checkers.Move(i / 8, i % 8), new Checkers.Move(j / 8, j % 8), false))
                    return outcome.IN_PROGRESS;
            }
        }
        return outcome.TIE;
    }

}