package chess;
import java.util.*;
public class ChessMoveC implements ChessMove{
    private ChessPosition startPosition = null;
    private ChessPosition endPosition = null;
    private ChessPiece.PieceType promoType;

    public ChessMoveC(ChessPosition start, ChessPosition end) {
        startPosition = start;
        endPosition = end;
        promoType = null;
    }
    public ChessMoveC(ChessPosition start, ChessPosition end, ChessPiece.PieceType type) {
        startPosition = start;
        endPosition = end;
        promoType = type;
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {

        return promoType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveC that = (ChessMoveC) o;
        return Objects.equals(startPosition, that.startPosition) && Objects.equals(endPosition, that.endPosition) && promoType == that.promoType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promoType);
    }
}
