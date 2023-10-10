package chess;
import java.util.*;
public class ChessPositionC implements ChessPosition{
    private int pieceRow = -1;
    private int pieceColumn = -1;
    public ChessPositionC(int row, int column){
        pieceRow = row;
        pieceColumn = column;
    }
    @Override
    public int getRow() {
        return pieceRow;
    }


    @Override
    public int getColumn() {
        return pieceColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionC that = (ChessPositionC) o;
        return pieceRow == that.pieceRow && pieceColumn == that.pieceColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceRow, pieceColumn);
    }

}
