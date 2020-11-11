package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator{
    @Override
    public Maze generate(int numOfRows, int numOfCols) {
        int[][] arr= new int[numOfRows][numOfCols];
        /* "crash-proofing" the program to avoid Auto-test crash (just in case) */
        if (numOfRows <= 0 || numOfCols <= 0) {
            numOfRows = 5;
            numOfCols = 5;
            System.out.println("Wrong Input: generate received negative numbers as input for Maze dimensions. ");
        }
        Position start=new Position(0,0)
                ,end=new Position(1,1);
        GeneratePosition(arr.length,arr[0].length,start,end);
        Maze maze = new Maze(arr,start,end);
        return maze;
    }

    public EmptyMazeGenerator() {
    }
}