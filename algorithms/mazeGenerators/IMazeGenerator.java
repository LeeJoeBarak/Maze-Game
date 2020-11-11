package algorithms.mazeGenerators;

public interface IMazeGenerator {
    /**
     *
     * @param numOfrows the number of rows in the maze
     * @param numOfCols the number of columns in the maze
     * @return a maze with 1's and 0's representation for each cell, where wall=1 and passage=0
     */
    public Maze generate(int numOfrows,int numOfCols);

    /**
     * Calculate run time of generating maze
     *
     * @param numOfrows    the number of rows in the maze
     * @param numOfCols the number of columns in the maze
     * @return a long that holds the time it took to generate a maze in ms
     */
    public long measureAlgorithmTimeMillis(int numOfrows,int numOfCols);
}