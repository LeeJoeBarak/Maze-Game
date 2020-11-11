package algorithms.mazeGenerators;

import java.util.LinkedList;
import java.util.Random;

public class MyMazeGenerator extends AMazeGenerator {

    public static final int WALL    = 1;
    public static final int PASSAGE = 0;
    int[][] maze;
    Random random = new Random();

    /**
     * Generates a maze
     *
     * @param numOfRows - number of rows in the maze
     * @param numOfCols - number of columns in the maze
     * @return a generated maze
     */
    @Override
    public Maze generate(int numOfRows, int numOfCols) {
        maze = new int[numOfRows][numOfCols];
        Position start = new Position(0, 0);
        Position end = new Position(1, 1);
        /* "crash-proofing" the program to avoid Auto-test crash (just in case) */
        if (numOfRows <= 0 || numOfCols <= 0) {
            numOfRows = 5;
            numOfCols = 5;
            System.out.println("Wrong Input: generate received negative numbers as input for Maze dimensions. ");
        }
        /* if dimensions has exactly(!) one edge with length 2 */
        if((numOfRows == 2 || numOfCols == 2) && !(numOfRows == 2 && numOfCols == 2)){
            int tempx = random.nextInt(4);//val between 0 (inclusive) and 4 (exclusive)
            /* choose start position randomly */
            start = randomStartPositionChooser(tempx,numOfRows,numOfCols);
            /* init end Position - loop until end.rowIndex != start.rowIndex AND end.colIndex != start.colIndex*/
            do{
                end = new Position(random.nextInt(numOfRows), random.nextInt(numOfCols));
            }while (start.getRowIndex() == end.getRowIndex() || start.getColumnIndex() == end.getColumnIndex());
        }
        else {
            GeneratePosition(maze.length, maze[0].length, start, end);
        }
        /* if dimensions are 2 x 2 create the maze "manually" and return the maze*/
        if( numOfRows == 2 && numOfCols == 2){
            int[] f = {start.getRowIndex(), start.getColumnIndex(), end.getRowIndex(), end.getColumnIndex()};//note: generatePosition was already called
            int rows, cols;
            do{
                rows = random.nextInt(2);
                cols = random.nextInt(2);
            }while ((cols == f[1] && rows == f[0]) || (cols == f[3] && rows == f[2]));
            maze[rows][cols] = 1;
            return new Maze(maze, start ,end);
        }//2x2 end

        /* make maze grid all 1s (walls) */
        for(int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                maze[i][j] = 1;
            }
        }

        prim(numOfRows, numOfCols, maze, start, end);
        return new Maze(maze,start,end);
    }//generate

    /**
     * generates a random maze grid based on Prim's algorithm
     *
     * @param width the number of rows in the maze grid
     * @param height the number of columns in the maze grid
     * @param maze a 2d int array of width x height size
     * @param start start Position
     * @param end end Position
     * @return a maze
     */

    private void prim(int width, int height, int[][] maze, Position start, Position end){
        LinkedList<int[]> listOfWall = new LinkedList<>();
        int x = start.getRowIndex();
        int y = start.getColumnIndex();
        listOfWall.add(new int[]{x,y,x,y});
        while ( !listOfWall.isEmpty() ){
            int[] possibleWay = listOfWall.remove( random.nextInt( listOfWall.size() ) );
            x = possibleWay[2];
            y = possibleWay[3];
            if ( maze[x][y] == WALL )
            {
                maze[possibleWay[0]][possibleWay[1]] = maze[x][y] = PASSAGE;
                if ( x >= 2 && maze[x-2][y] == WALL )
                    listOfWall.add( new int[]{x-1,y,x-2,y} );
                if ( y >= 2 && maze[x][y-2] == WALL )
                    listOfWall.add( new int[]{x,y-1,x,y-2} );
                if ( x < width-2 && maze[x+2][y] == WALL )
                    listOfWall.add( new int[]{x+1,y,x+2,y} );
                if ( y < height-2 && maze[x][y+2] == WALL )
                    listOfWall.add( new int[]{x,y+1,x,y+2} );
                if((possibleWay[0]!=start.getRowIndex()&&possibleWay[1]!= start.getColumnIndex()&& (possibleWay[0]==0||possibleWay[1]==0||possibleWay[0]==maze.length-1||possibleWay[1]==maze[0].length-1))){
                    end.setRowIndex(possibleWay[0]);
                    end.setColumnIndex(possibleWay[1]);
                }
            }
        }
        /* attempted fix for 2xn or nx2 grids*/
        maze[start.getRowIndex()][start.getColumnIndex()] = 0;
        maze[end.getRowIndex()][end.getColumnIndex()]=0;

    }//prim

    private Position randomStartPositionChooser(int temp, int x, int y){
        if(temp==0) return new Position(0,0);
        if(temp==1) return new Position(x-1,0);
        if(temp==2) return new Position(x-1,y-1);
        if(temp==3) return new Position(0,y-1);
        return null;
    }

    /**
     * override toString for Maze generated by MyMaze generation Algorithm
     * */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                sb.append(maze[i][j] == 1 ? "1" : "0");
            }
            System.out.println("");//newline separates rows
        }
        return sb.toString();
        /*StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                sb.append(maze[i][j] == 1 ? "*" : " ");
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();*/
    }
}