package chess;
public class ChessBoardC implements ChessBoard{
    private ChessPiece[][] board = new ChessPiece[8][8];
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()][position.getColumn()] = null;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    @Override
    public void resetBoard() {
        ChessGame.TeamColor color = null;

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if ((i == 0) || (i == 7)) {
                    if (i == 0) {
                        color = ChessGame.TeamColor.WHITE;
                    }
                    if (i == 7) {
                        color = ChessGame.TeamColor.BLACK;
                    }
                    if ((j == 0) || (j == 7)) {board[i][j] = new Rook(color);}
                    if ((j == 1) || (j == 6)) {board[i][j] = new Knight(color);}
                    if ((j == 2) || (j == 5)) {board[i][j] = new Bishop(color);}
                    if (j == 3) {board[i][j] = new Queen(color);}
                    if (j == 4) {board[i][j] = new King(color);}

                }
                // Set up Pawns
                else if ((i == 1) || (i == 6)) {
                    if (i == 1) {
                        color = ChessGame.TeamColor.WHITE;
                    }
                    if (i == 6) {
                        color = ChessGame.TeamColor.BLACK;
                    }
                    board[i][j] = new Pawn(color);
                }
                // Set all else to null.
                else {
                    board[i][j] = null;
                }
            }

        }
    }
}
