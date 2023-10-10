package chess;
import java.util.Collection;
import java.util.HashSet;

public class ChessPieceC implements ChessPiece {
    private ChessPiece.PieceType pieceType;
    private ChessGame.TeamColor teamColor;

    public ChessPieceC(ChessGame.TeamColor color, ChessPiece.PieceType piece) {
        pieceType = piece;
        teamColor = color;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
//        // Calculate move for each of the 6
//        ChessPiece myPiece = board.getPiece(myPosition);
//
//        if (myPiece.getPieceType() == PieceType.KING){
//            return kingValidMoves(board, myPosition);
//        }
//        return null;
//    }
//    public void setTeamColor(ChessGame.TeamColor color){
//        teamColor = color;
//    }
//    public void setPieceType(PieceType piece){
//        pieceType = piece;
//    }
//    private Collection<ChessMove> kingValidMoves(ChessBoard board, ChessPosition myPosition){
//        Collection<ChessMove> kingValidMoves = new HashSet<>();
//
//        // Define possible relative positions for a king's move
//        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
//        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
//
//        // Calculate all possible moves for the King
//        for (int i = 0; i < 8; i++) {
//            int newRow = myPosition.getRow() + rowOffsets[i];
//            int newCol = myPosition.getColumn() + colOffsets[i];
//
//            // Check if the new position is within the chessboard bounds
//            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
//                ChessPosition newPosition = new ChessPositionC(newRow, newCol);
//
//                if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != teamColor) {
//                    ChessMove newMove = new ChessMoveC(myPosition, newPosition);
//
//                    kingValidMoves.add(newMove);
//                }
//            }
//        }
//        return kingValidMoves;
//    }
//    private Collection<ChessMove> queenValidMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> queenValidMoves = new HashSet<>();
//        // Define the possible directions a Queen can move: horizontal, vertical, and diagonal
//        int[][] directions = {
//                {1, 0},   // Right
//                {-1, 0},  // Left
//                {0, 1},   // Up
//                {0, -1},  // Down
//                {1, 1},   // Diagonal up-right
//                {-1, 1},  // Diagonal up-left
//                {1, -1},  // Diagonal down-right
//                {-1, -1}  // Diagonal down-left
//        };
//
//        for (int[] direction : directions) {
//            int dx = direction[0];
//            int dy = direction[1];
//
//            for (int i = 1; i <= 7; i++) { // Queen can move up to 7 squares in any direction
//                int newRow = myPosition.getRow() + i * dx;
//                int newCol = myPosition.getColumn() + i * dy;
//
//                ChessPosition newPosition = new ChessPositionC(newRow, newCol);
//
//                if (newPosition.getRow() >= 8 || newPosition.getColumn() >=8) {
//                    // If the new position is out of the board bounds, stop in this direction
//                    break;
//                }
//
//                if (board.getPiece(newPosition) == null) {
//                    // If the position is empty, it's a valid move
//                    ChessMove newMove = new ChessMoveC(myPosition, newPosition);
//                    queenValidMoves.add(newMove);
//                } else if (board.getPiece(newPosition).getTeamColor() != teamColor) {
//                    // If there's an opponent's piece, you can capture it and then stop in this direction
//                    queenValidMoves.add(new ChessMoveC(myPosition, newPosition));
//                    break;
//                } else {
//                    // If there's your own piece in the way, you can't go further in this direction
//                    break;
//                }
//            }
//        }
//
//        return queenValidMoves;
//    }
//    private Collection<ChessMove> bishopValidMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> bishopValidMoves = new HashSet<>();
//        // Define the possible directions a bishop can move: diagonal
//        int[][] directions = {
//                {1, 1},   // Diagonal up-right
//                {-1, 1},  // Diagonal up-left
//                {1, -1},  // Diagonal down-right
//                {-1, -1}  // Diagonal down-left
//        };
//
//        for (int[] direction : directions) {
//            int dx = direction[0];
//            int dy = direction[1];
//
//            for (int i = 1; i <= 7; i++) { // bishop can move up to 7 squares in any diagonal
//                int newRow = myPosition.getRow() + i * dx;
//                int newCol = myPosition.getColumn() + i * dy;
//
//                ChessPosition newPosition = new ChessPositionC(newRow, newCol);
//
//                if (newPosition.getRow() >= 8 || newPosition.getColumn() >=8) {
//                    // If the new position is out of the board bounds, stop in this diagonal
//                    break;
//                }
//
//                if (board.getPiece(newPosition) == null) {
//                    // If the position is empty, it's a valid move
//                    ChessMove newMove = new ChessMoveC(myPosition, newPosition);
//                    bishopValidMoves.add(newMove);
//                } else if (board.getPiece(newPosition).getTeamColor() != teamColor) {
//                    // If there's an opponent's piece, you can capture it and then stop in this direction
//                    bishopValidMoves.add(new ChessMoveC(myPosition, newPosition));
//                    break;
//                } else {
//                    // If there's your own piece in the way, you can't go further in this direction
//                    break;
//                }
//            }
//        }
//
//        return bishopValidMoves;
//    }
//    private Collection<ChessMove> knightValidMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> knightValidMoves = new HashSet<>();
//        // Define the possible directions a knight can move
//        int[][] moves = {
//                {-2, -1}, {-2, 1},
//                {-1, -2}, {-1, 2},
//                {1, -2}, {1, 2},
//                {2, -1}, {2, 1}
//        };
//
//        for (int[] direction : moves) {
//            int dx = myPosition.getRow() + direction[0];
//            int dy = myPosition.getColumn() + direction[1];
//
//            ChessPosition newPosition = new ChessPositionC(dx, dy);
//
//            if (newPosition.getRow() >= 8 || newPosition.getColumn() >=8) {
//                // If the new position is out of the board bounds, stop in this direction
//                break;
//            }
//
//            if (board.getPiece(newPosition) == null) {
//                // If the position is empty, it's a valid move
//                ChessMove newMove = new ChessMoveC(myPosition, newPosition);
//                knightValidMoves.add(newMove);
//            } else if (board.getPiece(newPosition).getTeamColor() != teamColor) {
//                // If there's an opponent's piece, you can capture it and then stop in this direction
//                knightValidMoves.add(new ChessMoveC(myPosition, newPosition));
//                break;
//            } else {
//                // If there's your own piece in the way, you can't go further in this direction
//                break;
//            }
//
//        }
//
//        return knightValidMoves;
//    }
//    private Collection<ChessMove> rookValidMoves(ChessBoard board, ChessPosition myPosition) {
//        Collection<ChessMove> rookValidMoves = new HashSet<>();
//        // Define the possible directions a rook can move: horizontal and vertical
//        int[][] directions = {
//                {1, 0},   // Right
//                {-1, 0},  // Left
//                {0, 1},   // Up
//                {0, -1},  // Down
//        };
//
//        for (int[] direction : directions) {
//            int dx = direction[0];
//            int dy = direction[1];
//
//            for (int i = 1; i <= 7; i++) { // rook can move up to 7 squares in any direction
//                int newRow = myPosition.getRow() + i * dx;
//                int newCol = myPosition.getColumn() + i * dy;
//
//                ChessPosition newPosition = new ChessPositionC(newRow, newCol);
//
//                if (newPosition.getRow() >= 8 || newPosition.getColumn() >=8) {
//                    // If the new position is out of the board bounds, stop in this direction
//                    break;
//                }
//
//                if (board.getPiece(newPosition) == null) {
//                    // If the position is empty, it's a valid move
//                    ChessMove newMove = new ChessMoveC(myPosition, newPosition);
//                    rookValidMoves.add(newMove);
//                } else if (board.getPiece(newPosition).getTeamColor() != teamColor) {
//                    // If there's an opponent's piece, you can capture it and then stop in this direction
//                    rookValidMoves.add(new ChessMoveC(myPosition, newPosition));
//                    break;
//                } else {
//                    // If there's your own piece in the way, you can't go further in this direction
//                    break;
//                }
//            }
//        }
//
//        return rookValidMoves;
//    }
//
//}
//
