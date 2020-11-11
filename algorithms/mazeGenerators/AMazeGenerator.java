package algorithms.mazeGenerators;

public abstract  class  AMazeGenerator implements IMazeGenerator {
    /**
     * Calculate run time of generating maze
     *
     * @param numOfrows    the number of rows in the maze
     * @param numOfCols the number of columns in the maze
     * @return the time it takes to generate a maze in milliseconds
     */
    @Override
    public long measureAlgorithmTimeMillis(int numOfrows, int numOfCols) {
        long t1 = System.currentTimeMillis();
        generate(numOfrows,numOfCols);
        long t2 = System.currentTimeMillis();
        return t2 - t1;
    }


    public void GeneratePosition(int numOfrows, int numOfCols, Position start, Position end){
        int rowIndex = (int)(Math.random()*4); // 0.0 <= rowIndex < 1.0 + casting to int
        int colIndex;
        Position tempPosition;

        tempPosition = setRandomPositionCoordinates(rowIndex, numOfrows, numOfCols);
        /*init start Position*/
        start.setRowIndex(tempPosition.getRowIndex());
        start.setColumnIndex(tempPosition.getColumnIndex());
        /*init end Position - loop until end.rowIndex != start.rowIndex AND end.colIndex != start.colIndex*/
        do {
            colIndex = (int)(Math.random()*4);
            tempPosition = setRandomPositionCoordinates(colIndex, numOfrows, numOfCols);
            /*init end Position*/
            end.setRowIndex(tempPosition.getRowIndex());
            end.setColumnIndex(tempPosition.getColumnIndex());
        }while (start.getRowIndex() == end.getRowIndex() || start.getColumnIndex() == end.getColumnIndex());
    }

    /**
     * helper function for GeneratePosition that produces random start/end position indices
     * using a randomly generated index parameter
     *
     * @param numOfrows number of rows in maze
     * @param numOfCols  number of columns in maze
     * @param index  a randomly generated index in range of maze dimensions
     * @return a Position object with  randomly generated (row, col) indices
     */
    public Position setRandomPositionCoordinates(int index, int numOfrows, int numOfCols){
        if(index==0) return new Position(0 , (int)(Math.random() * (numOfCols-1)) );
        if(index==1) return new Position((int)(Math.random() * (numOfrows-1)) ,numOfCols-1);
        if(index==2) return new Position(numOfrows-1 , (int)(Math.random() * (numOfCols-1)) + 1 );
        if(index==3) return new Position((int)(Math.random() * (numOfrows-1)) + 1 ,0);
        return null;
    }
}