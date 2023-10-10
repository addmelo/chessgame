package chess;
import java.util.*;
public class Bishop extends ChessPieceC{
    public Bishop(ChessGame.TeamColor color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // bishop moves diagonally on any diagonal, it cannot move past pieces of its own color nor
        // can it move past enemies that it will attack.


        var validBishopMovesList = new ArrayList<ChessMove>();
        validBishopMoves(validBishopMovesList, board, myPosition);


        return validBishopMovesList;
    }

    public void validBishopMoves(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition) {
        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();
        // check upper right
        var range = Math.min(7-rowNum, 7-colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, 1, 1);
        // check upper left
        range = Math.min(7-rowNum, colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, 1, -1);
        // check bottom left
        range = Math.min(rowNum, colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, -1, -1);
        range = Math.min(rowNum, 7-colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, -1, 1);
    }

    public void checkDiagonal(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition, int rowNum, int colNum, int rangeToCheck, int rowOffset, int colOffset) {
        // Check to see how far we can go diagonally
        var newRowNum = rowNum;
        var newColNum = colNum;

        for (int i = 0; i < rangeToCheck; i++) {
            newRowNum += rowOffset;
            newColNum += colOffset;
            var newPos = new ChessPositionC(newRowNum, newColNum);
            if (board.getPiece(newPos) != null) {
                if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                    validPawnMoves.add(new ChessMoveC(myPosition, newPos));
                    break;
                }
                else {
                    break;
                }
            }
            else {
                validPawnMoves.add(new ChessMoveC(myPosition, newPos));
            }

        }

    }

}