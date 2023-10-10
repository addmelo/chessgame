package chess;
import java.util.*;
public class ChessGameC implements ChessGame{
    private ChessBoardC board = new ChessBoardC();
    private ChessGame.TeamColor currTurn = TeamColor.WHITE;
    @Override
    public TeamColor getTeamTurn() {
        return currTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        currTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }
        else{
            var colorOfMovePiece = board.getPiece(startPosition).getTeamColor();
            var kingPos = findKing(colorOfMovePiece);
            //var kingPos = findKing(this.getTeamTurn());
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, colorOfMovePiece);
            if (listOfMovesThatCanAttackKing.size() > 1) {
                if (kingPos != startPosition) {
                    return new ArrayList<ChessMove>();
                }
                else {
                    var listOfKingMoves = placesKingCanMove(kingPos, colorOfMovePiece);
                    if (!listOfKingMoves.isEmpty()) {
                        return listOfKingMoves;
                    }
                    else {
                        return new ArrayList<ChessMove>();
                    }
                }
            }
            else if (listOfMovesThatCanAttackKing.size() == 1) {
                // Piece is King
                if (startPosition.equals(kingPos)) {
                    var listOfKingMoves = placesKingCanMove(kingPos, colorOfMovePiece);
                    if (listOfKingMoves.isEmpty()) {
                        return new ArrayList<ChessMove>();
                    }
                    else {
                        return listOfKingMoves;
                    }

                }
                // Piece isn't King
                else {
                    var piecesCanBlockOrAttack = memberCanBlockOrCaptureAttacker(listOfMovesThatCanAttackKing, colorOfMovePiece);
                    var listOfValidMoves = new ArrayList<ChessMove>();
                    for (ChessMove move: piecesCanBlockOrAttack) {
                        var newPos = move.getStartPosition();
                        if (newPos.equals(startPosition)) {
                            listOfValidMoves.add(move);
                        }
                    }
                    if (listOfValidMoves.isEmpty()) {
                        return new ArrayList<ChessMove>();
                    }
                    else {
                        return listOfValidMoves;
                    }
                }

            }
            else {
                var listOfAllPieceMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
                var finalListValidMoves = new ArrayList<ChessMove>();
                for (ChessMove move: listOfAllPieceMoves) {
                    if (!moveCompromisesSafety(move, colorOfMovePiece)) {
                        finalListValidMoves.add(move);
                    }
                }
                if (finalListValidMoves.isEmpty()) {
                    return new ArrayList<ChessMove>();
                }
                else {
                    return finalListValidMoves;
                }
            }
        }

    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var startPos = move.getStartPosition();
        var endPos = move.getEndPosition();
        if (board.getPiece(startPos).getTeamColor() != this.getTeamTurn()) {
            throw new InvalidMoveException();
        }
        var validMoves = validMoves(startPos);
        if (validMoves.isEmpty()) {
            throw new InvalidMoveException();
        }
        else {
            if (validMoves.contains(move)) {
                if (board.getPiece(endPos) == null) {
                    board.addPiece(endPos, returnCorrectPiece(move));
                    board.removePiece(startPos);
                    var currentTeamColor = this.getTeamTurn();
                    if (currentTeamColor == TeamColor.WHITE) {
                        this.setTeamTurn(TeamColor.BLACK);
                    } else {
                        this.setTeamTurn(TeamColor.WHITE);
                    }
                }
                else {
                    board.removePiece(endPos);
                    board.addPiece(endPos, returnCorrectPiece(move));
                    board.removePiece(startPos);
                    var currentTeamColor = this.getTeamTurn();
                    if (currentTeamColor == TeamColor.WHITE) {
                        this.setTeamTurn(TeamColor.BLACK);
                    } else {
                        this.setTeamTurn(TeamColor.WHITE);
                    }
                }
            }
            else {
                throw new InvalidMoveException();
            }
        }
    }

    public ChessPiece returnCorrectPiece(ChessMove move) {
        if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (move.getPromotionPiece() != null) {
                return new ChessPieceC(this.getTeamTurn(), move.getPromotionPiece());
            }
            else {
                return (ChessPieceC) board.getPiece(move.getStartPosition());
            }
        }
        else {
            return (ChessPieceC) board.getPiece(move.getStartPosition());
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            var kingPos = findKing(teamColor);
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
            return !listOfMovesThatCanAttackKing.isEmpty();
        }
        else {
            var kingPos = findKing(teamColor);
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
            return !listOfMovesThatCanAttackKing.isEmpty();
        }
    }
    public Collection<ChessMove> piecesHaveKingInCheck(ChessPosition kingPos, TeamColor kingColor) {
        var listOfMovesThatCanAttackKing = new ArrayList<ChessMove>();
        if (kingPos == null) {
            return listOfMovesThatCanAttackKing;
        }

        // check if a knight can attack me
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 2, 1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 2, -1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 1, 2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 1, -2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -2, 1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -2, -1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -1, 2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -1, -2);

        // check if rook or queen can attack me
        // moves right
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 7-kingPos.getColumn(), 1, 0);
        //moves left
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kingPos.getColumn(), -1, 0);
        // moves up
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 7-kingPos.getRow(), 0, 1);
        // moves down
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kingPos.getRow(), 0, -1);


        // check if bishop or queen can attack me
        // down and right
        var range = Math.min(kingPos.getRow(), 7-kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, 1, -1);
        // down and left
        range = Math.min(kingPos.getRow(), kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, -1, -1);
        // up and left
        range = Math.min(7-kingPos.getRow(), kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, -1, 1);
        // up and right
        range = Math.min(7-kingPos.getRow(), 7-kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, 1, 1);

        // check if a pawn can attack me. depends on whether I'm white or black
        pawnAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor);

        // check if opposing king can attack me.
        var kRow = kingPos.getRow();
        var kCol = kingPos.getColumn();
        // King attacked from above
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow+1, kCol);
        // King attacked from above and right
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow+1, kCol+1);
        // King attacked from right
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow, kCol+1);
        // King attacked from below and right
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow-1, kCol+1);
        // King attacked from below
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow-1, kCol);
        // King attacked from below and left
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow-1, kCol-1);
        // King attacked from left
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow, kCol-1);
        // King attacked from above and left
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow+1, kCol-1);

        return listOfMovesThatCanAttackKing;
    }
    public void kingAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int newRow, int newCol) {
        if (((newRow < 8) && (newRow >= 0)) && ((newCol < 8) && (newCol >= 0))) {
            var newPos = new ChessPositionC(newRow, newCol);
            var potKing = this.board.getPiece(newPos);
            if ((potKing != null) && (potKing.getTeamColor() != kingColor) && (potKing.getPieceType()== ChessPiece.PieceType.KING)) {
                listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
            }
        }
    }
    public void pawnAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor) {
        if (kingColor == TeamColor.WHITE) {
            if (kingPos.getRow() != 7) {
                if (kingPos.getColumn() != 7) {
                    var newPos = new ChessPositionC(kingPos.getRow() + 1, kingPos.getColumn() + 1);
                    var pieceUpAndRight = this.board.getPiece(newPos);
                    if ((pieceUpAndRight != null) && (pieceUpAndRight.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceUpAndRight.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                    }
                }
                if (kingPos.getColumn() != 0) {
                    var newPos = new ChessPositionC(kingPos.getRow() + 1, kingPos.getColumn() - 1);
                    var pieceUpAndLeft = this.board.getPiece(newPos);
                    if ((pieceUpAndLeft != null) && (pieceUpAndLeft.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceUpAndLeft.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                    }
                }
            }
        }
        else {
            if (kingPos.getRow() != 0) {
                if (kingPos.getColumn() != 7) {
                    var newPos = new ChessPositionC(kingPos.getRow() - 1, kingPos.getColumn() + 1);
                    var pieceBelowAndRight = this.board.getPiece(newPos);
                    if ((pieceBelowAndRight != null) && (pieceBelowAndRight.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceBelowAndRight.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                    }
                }
                if (kingPos.getColumn() != 0) {
                    var newPos = new ChessPositionC(kingPos.getRow() - 1, kingPos.getColumn() - 1);
                    var pieceBelowAndLeft = this.board.getPiece(newPos);
                    if ((pieceBelowAndLeft != null) && (pieceBelowAndLeft.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceBelowAndLeft.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                    }
                }
            }
        }
    }
    public void knightAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int rowChanger, int colChanger){
        var rowNum = kingPos.getRow();
        var colNum = kingPos.getColumn();

        var newRow = rowNum + rowChanger;
        if ((newRow < 8) && (newRow) >= 0) {
            var newCol = colNum + colChanger;
            if ((newCol < 8) && (newCol >=0)) {
                var newPos = new ChessPositionC(newRow, newCol);
                var potentialKnight = this.board.getPiece(newPos);
                if ((potentialKnight != null) && (potentialKnight.getPieceType() == ChessPiece.PieceType.KNIGHT) && (potentialKnight.getTeamColor() != kingColor)) {
                    listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                }
            }
        }
    }

    public void rookOrQueenAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int range, int colChanger, int rowChanger) {
        var newRow = kingPos.getRow();
        var newCol = kingPos.getColumn();

        for (int i = 0; i < range; i++) {
            newRow += rowChanger;
            newCol += colChanger;
            var newPos = new ChessPositionC(newRow, newCol);
            var potRookOrQueen = this.board.getPiece(newPos);
            if ((potRookOrQueen != null) && ((potRookOrQueen.getPieceType() == ChessPiece.PieceType.ROOK) || (potRookOrQueen.getPieceType() == ChessPiece.PieceType.QUEEN)) && (potRookOrQueen.getTeamColor() != kingColor)) {
                listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                break;
            }
            else if (potRookOrQueen != null) {
                break;

            }
        }
    }
    public void bishopOrQueenAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int rangeToCheck, int colChanger, int rowChanger) {
        // Check to see how far we can go diagonally
        var newRowNum = kingPos.getRow();
        var newColNum = kingPos.getColumn();

        for (int i = 0; i < rangeToCheck; i++) {
            newRowNum += rowChanger;
            newColNum += colChanger;
            var newPos = new ChessPositionC(newRowNum, newColNum);
            var potBishopOrQueen = this.board.getPiece(newPos);
            if ((potBishopOrQueen != null) && (potBishopOrQueen.getTeamColor() != kingColor) && ((potBishopOrQueen.getPieceType() == ChessPiece.PieceType.BISHOP) || (potBishopOrQueen.getPieceType() == ChessPiece.PieceType.QUEEN))) {
                listOfMovesThatCanAttackKing.add(new ChessMoveC(newPos, kingPos));
                break;
            }
            else if (potBishopOrQueen != null) {
                break;
            }

        }
    }


    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        var kingPos = findKing(teamColor);
        var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
        if (listOfMovesThatCanAttackKing.size() > 1) {
            // See if king can move somewhere safely.
            return placesKingCanMove(kingPos, teamColor).isEmpty();
        }
        else if (listOfMovesThatCanAttackKing.size() == 1) {
            var kingCanMove = !placesKingCanMove(kingPos, teamColor).isEmpty();
            var piecesCanBlockOrAttack = !memberCanBlockOrCaptureAttacker(listOfMovesThatCanAttackKing, teamColor).isEmpty();
            // piece can block or attack for king?
            if ((kingCanMove) && (piecesCanBlockOrAttack)) {
                return false;
            }
            else { return true; }
        }
        else { return false; }

    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        else {
            var placesKingCanMove = placesKingCanMove(findKing(teamColor), teamColor);
            if (placesKingCanMove.isEmpty()) {
                return true;
            }
            else {
                return false;
            }

        }
    }


    @Override
    public void setBoard(ChessBoard board) {
        this.board = (ChessBoardC)board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
    public ChessPosition findKing(TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var newPos = new ChessPositionC(i, j);
                if (this.board.getPiece(newPos) != null) {
                    if (this.board.getPiece(newPos).getPieceType() == ChessPiece.PieceType.KING) {
                        if (this.board.getPiece(newPos).getTeamColor() == color) {
                            return newPos;
                        }
                    }
                }
            }
        }

        return null;
    }
    public Collection<ChessMove> placesKingCanMove(ChessPosition kingPos, TeamColor kingColor) {
        var listOfPosKingCanMove = new ArrayList<ChessMove>();
        var listOfKingMoves = (ArrayList<ChessMove>) this.board.getPiece(kingPos).pieceMoves(this.board, kingPos);
        for (ChessMove currMove : listOfKingMoves) {
            var listOfAttackers = piecesHaveKingInCheck(currMove.getEndPosition(), kingColor);
            if (listOfAttackers.isEmpty()) {
                listOfPosKingCanMove.add(currMove);
            }
        }
        return listOfPosKingCanMove;
    }
    public Collection<ChessMove> memberCanBlockOrCaptureAttacker(Collection<ChessMove> listOfMovesThatCanAttackKing, TeamColor kingColor) {
        // There will only be one attack move when we enter this function.

        var movesThatCanAttackKingAsArrayList = (ArrayList<ChessMove>) listOfMovesThatCanAttackKing;
        var moveAttackKing = movesThatCanAttackKingAsArrayList.get(0);
        var pieceThatCanAttackKing = board.getPiece(moveAttackKing.getStartPosition()).getPieceType();

        // find all positions between piece and king and the pos of the piece itself.
        var setOfSpaces = new HashSet<ChessPosition>();

        getSetOfSpaceBetweenAttackerAndKing(setOfSpaces, moveAttackKing);

        // Get the list of all possible piece moves of our team's pieces, except for king
        var listOfPiecesOfSameColor = new ArrayList<ChessPositionC>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var newPos = new ChessPositionC(i, j);
                if (board.getPiece(newPos) != null) {
                    if ((board.getPiece(newPos).getTeamColor() == kingColor) && (newPos != moveAttackKing.getEndPosition())) {
                        listOfPiecesOfSameColor.add(newPos);
                    }
                }
            }
        }

        // List that contains all move to protect king from capture via blocking or attacking. (doesn't include the king)
        var listOfAllTeamMoves = new ArrayList<ChessMove>();
        for (ChessPosition pos: listOfPiecesOfSameColor) {
            var tempListOfMoves = board.getPiece(pos).pieceMoves(this.board, pos);
            for (ChessMove tempMove: tempListOfMoves) {
                if (setOfSpaces.contains(tempMove.getEndPosition())) {
                    listOfAllTeamMoves.add(tempMove);
                }
            }
        }

        // See which of our moves don't compromise the king's safety
        var finalListValidMoves = new ArrayList<ChessMove>();
        for (ChessMove move: listOfAllTeamMoves) {
            if (!moveCompromisesSafety(move, kingColor)) {
                finalListValidMoves.add(move);
            }
        }

        return finalListValidMoves;


    }
    public boolean moveCompromisesSafety(ChessMove move, TeamColor kingColor) {
        // this function assumes that the move can be made.

        // if king isn't on board, return false
        if (findKing(kingColor) == null) {
            return false;
        }

        // make move
        var oldEndPos = this.board.getPiece(move.getEndPosition());
        var oldStartPiece = this.board.getPiece(move.getStartPosition());
        if (oldEndPos == null) {
            this.board.addPiece(move.getEndPosition(), oldStartPiece);
            this.board.removePiece(move.getStartPosition());
            if (isInCheck(kingColor)) {
                this.board.removePiece(move.getEndPosition());
                this.board.addPiece(move.getStartPosition(), oldStartPiece);
                return true;
            } else {
                this.board.removePiece(move.getEndPosition());
                this.board.addPiece(move.getStartPosition(), oldStartPiece);
                return false;
            }
        } else {
            var oldEndPiece = oldEndPos;
            this.board.removePiece(move.getEndPosition());
            this.board.addPiece(move.getEndPosition(), oldStartPiece);
            this.board.removePiece(move.getStartPosition());
            if (isInCheck(kingColor)) {
                this.board.removePiece(move.getEndPosition());
                this.board.addPiece(move.getStartPosition(), oldStartPiece);
                this.board.addPiece(move.getEndPosition(), oldEndPiece);
                return true;
            } else {
                this.board.removePiece(move.getEndPosition());
                this.board.addPiece(move.getStartPosition(), oldStartPiece);
                this.board.addPiece(move.getEndPosition(), oldEndPiece);
                return false;
            }
        }
    }
    public void getSetOfSpaceBetweenAttackerAndKing(Collection<ChessPosition> setOfSpaces, ChessMove moveAttackKing) {
        var attackerS = moveAttackKing.getStartPosition();
        var attackerE = moveAttackKing.getEndPosition();

        if (piecePosIsHorToKing(attackerS, attackerE)) {
            horHelper(setOfSpaces, attackerS, attackerE);
        }
        else if (piecePosIsVerToKing(attackerS, attackerE)) {
            verHelper(setOfSpaces, attackerS, attackerE);
        }
        else if (piecePosIsDiagToKing(attackerS, attackerE)) {
            diagHelper(setOfSpaces, attackerS, attackerE);
        }
        else {
            setOfSpaces.add(attackerS);
        }
    }
    public boolean piecePosIsHorToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getRow() == kingPos.getRow());
    }

    public void horHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, ChessPosition kingPos) {
        if (attackerS.getColumn() > kingPos.getColumn()) {
            var range = attackerS.getColumn() - kingPos.getColumn();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionC(attackerS.getRow(), attackerS.getColumn()-i));
            }
        }
        else {
            var range = kingPos.getColumn() - attackerS.getColumn();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionC(attackerS.getRow(), attackerS.getColumn()+i));
            }
        }
    }

    public boolean piecePosIsVerToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getColumn() == kingPos.getColumn());
    }

    public void verHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, ChessPosition kingPos) {
        if (attackerS.getRow() > kingPos.getRow()) {
            var range = attackerS.getRow() - kingPos.getRow();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionC(attackerS.getColumn(), attackerS.getRow()-i));
            }
        }
        else {
            var range = kingPos.getRow() - attackerS.getRow();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionC(attackerS.getColumn(), attackerS.getRow()+i));
            }
        }
    }

    public boolean piecePosIsDiagToKing(ChessPosition piecePos, ChessPosition kingPos) {
        var slope = (piecePos.getRow()-kingPos.getRow())/(piecePos.getColumn()-kingPos.getColumn());
        return ((slope == 1) || (slope == -1));
    }

    public void diagHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, ChessPosition kingPos) {
        var slope = (attackerS.getRow()-kingPos.getRow())/(attackerS.getColumn()-kingPos.getColumn());
        if (slope == 1) {
            if (attackerS.getColumn() < kingPos.getColumn()) {
                var range = kingPos.getColumn() - attackerS.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, 1, 1);
            }
            else {
                var range = attackerS.getColumn() - kingPos.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, -1, -1);
            }
        }
        else {
            if (attackerS.getColumn() < kingPos.getColumn()) {
                var range = kingPos.getColumn() - attackerS.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, -1, 1);
            }
            else {
                var range = attackerS.getColumn() - kingPos.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, 1, -1);
            }
        }
    }

    public void diagHelperHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, int range, int rowChanger, int colChanger) {
        for (int i = 0; i < range; i++) {
            setOfSpaces.add(new ChessPositionC(attackerS.getRow()+(rowChanger*i), attackerS.getColumn()+(colChanger*i)));
        }
    }
}
