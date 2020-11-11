package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable {
    private int rowIndex, colIndex;

    public Position(int x, int y) {
        this.rowIndex = x;
        this.colIndex = y;
    }

    /**
     * Overriding equals for Position
     *
     * @param obj should be Position
     * @return true if obj is Position and both its indices (row, col) have same value coordinates
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position))
            return false;
        Position pos = (Position)obj;
        return ((pos.getRowIndex() == this.rowIndex) && (pos.getColumnIndex() == this.colIndex));
    }

    /**
     * Overriding toString for Position object
     */
    @Override
    public String toString() { return "{" + rowIndex + ", " + colIndex + '}'; }

    /*getters*/
    public int getRowIndex(){ return this.rowIndex;}
    public int getColumnIndex(){ return this.colIndex;}

    /*setters*/
    public void setRowIndex(int x){ this.rowIndex =x;}
    public void setColumnIndex(int y){ this.colIndex =y;}
}