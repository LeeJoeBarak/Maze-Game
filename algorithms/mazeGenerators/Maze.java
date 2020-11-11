package algorithms.mazeGenerators;

import java.io.Serializable;

public class Maze implements Serializable {
    //default serialVersion id
    private static final long serialVersionUID = 1L;

    private int numOfRows, numOfCols;
    private int[][] maze;
    private Position start, end;
    /* Java byte range: [-128, 127]
     *  16 bit unsigned range: [0, 65,535]
     * Maze dim. MAX_SIZE 10,000x10,000 :
     * Dec: 10,000  Hex: 2710  Binary: 00100111 00010000 -> 2 bytes
     */

    /**
     * constructor for maze
     *
     * @param start    Position object for entry point into the maze
     * @param end Position object for exit point out of the maze
     * @param mazeGrid a 2D int array for the maze's initial grid
     */
    public Maze(int[][] mazeGrid, Position start, Position end) {
        numOfRows = mazeGrid.length;
        numOfCols = mazeGrid[0].length;
        maze = mazeGrid;
        this.start = start;
        this.end = end;
    }

    /**
     * Constructor for Maze that gets a maze instance
     * represented as byte[]
     * @param bytearray byte[] in the same format returned from maze.toByteArray()*/
    public Maze(byte[] bytearray) { /* works ! */
        /* 1 <= elem / 255 <= 4     0 <= elem % 255 <= 254 */
        /* default values for creating new Positions  */
        start = new Position(-1,-1);
        end = new Position( - 1,  - 1);
        setAllAttributesFromByteArrayData(bytearray);
        maze = new int[numOfRows][numOfCols];
        int i, j, k = 12;
        for(i = 0; i < numOfRows; ++i) {
            for(j = 0; j < numOfCols; ++j) {
                maze[i][j] = (int)bytearray[k];
                k++;
            }
        }
    }//ctor


    /*getters */
    public Position getStartPosition(){return this.start;}
    public Position getGoalPosition(){return this.end;}
    public int getNumOfRows(){return numOfRows;}
    public int getNumOfCols(){return numOfCols;}
    public int[][] getMazeGrid(){ return this.maze; }
    /*end getters*/

    public boolean IsPositionIsAWay(Position p) {
        int colIndx = p.getColumnIndex();
        int rowIndx = p.getRowIndex();
        return ((colIndx >= 0) && (rowIndx >= 0) &&
                (colIndx < numOfCols) &&
                (rowIndx < numOfRows) &&
                (maze[rowIndx][colIndx] == 0));
    }//IsPositionIsAWay

    /**
     * represents the Maze object as byte array.
     * offset:
     * */
    public byte[] toByteArray() {/* works ! */
        byte[] bytearray = new byte[12 + numOfRows * numOfCols];
        /* writes all data members (except maze) to file, 2 bytes for each data member */
        writeHeaderToByteArray(bytearray);
        /* represent each grid cell using 1 byte (0/1) */
        int i, j, k = 12;
        for(i = 0; i < numOfRows; i++){
            for(j = 0; j < numOfCols; j++){
                /* https://stackoverflow.com/questions/2458495/how-are-integers-cast-to-bytes-in-java */
                bytearray[k] = (byte)maze[i][j];//java takes the last byte of the integer (8 LSBs)
                k++;
            }
        }
        return bytearray;
     }//toByteArray

    private void writeHeaderToByteArray(byte[] bytearray){
        /* need 4 bytes for numOfRows, numOfCols
        *  need 8 bytes for S=(i,j), E=(i, j) */
        bytearray[0] = (byte)(numOfRows / 255);//Q: can I get a shorter representation if I devide by 256 instead?
        bytearray[1] = (byte)(numOfRows % 255);
        bytearray[2] = (byte)(numOfCols / 255);
        bytearray[3] = (byte)(numOfCols % 255);
        bytearray[4] = (byte)(start.getRowIndex() / 255);
        bytearray[5] = (byte)(start.getRowIndex() % 255);
        bytearray[6] = (byte)(start.getColumnIndex() / 255);
        bytearray[7] = (byte)(start.getColumnIndex() % 255);
        bytearray[8] = (byte)(end.getRowIndex() / 255);
        bytearray[9] = (byte)(end.getRowIndex() % 255);
        bytearray[10] = (byte)(end.getColumnIndex() / 255);
        bytearray[11] = (byte)(end.getColumnIndex() % 255);
    }

    private void setAllAttributesFromByteArrayData(byte[] bytearray){
        /*Java doesn't support unsigned, so apply bitwise-AND conversion*/
        numOfRows = byteToUnsignedInt(bytearray[0]) * 255 + byteToUnsignedInt(bytearray[1]);
        numOfCols = byteToUnsignedInt(bytearray[2]) * 255 + byteToUnsignedInt(bytearray[3]);

        start.setRowIndex( byteToUnsignedInt(bytearray[4]) * 255 + byteToUnsignedInt(bytearray[5]) );
        start.setColumnIndex( byteToUnsignedInt(bytearray[6]) * 255 + byteToUnsignedInt(bytearray[7]) );
        end.setRowIndex( byteToUnsignedInt(bytearray[8]) * 255 + byteToUnsignedInt(bytearray[9]) );
        end.setColumnIndex(  byteToUnsignedInt(bytearray[10]) * 255 + byteToUnsignedInt(bytearray[11]) );
    }

    /*
     *int i = 200; // 0000 0000 0000 0000 0000 0000 1100 1000 (200)
     * byte b = (byte) 200; // 1100 1000 (-56 by Java specification, 200 by convention)
     * System.out.println(b & 0xFF); // "200"
     * Will print a positive int 200 because bitwise AND with 0xFF will
     * zero all the 24 most significant bits that:
     * a) were added during upcasting to int which took place silently
     *    just before evaluating the bitwise AND operator.
     *    So the `b & 0xFF` is equivalent with `((int) b) & 0xFF`.
     * b) were set to 1s because of "sign extension" during the upcasting
     *
     * 1111 1111 1111 1111 1111 1111 1100 1000 (the int)
     * &
     * 0000 0000 0000 0000 0000 0000 1111 1111 (the 0xFF)
     * =======================================
     * 0000 0000 0000 0000 0000 0000 1100 1000 (200)
     */
    public static int byteToUnsignedInt(byte b) {
        return (b & 0xFF);
    }
    /**
     * prints the maze similarly to bitmap(1s and 0s) with S and E symbolizing Start and End respectively
        print format:
        0 -Emtpy Cell; 1 -Wall; S - start; E - end
        */
    public void print(){
        for(int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                if (i == start.getRowIndex() && j == start.getColumnIndex()) System.out.print("S");//(i, j)==start
                else if (i == end.getRowIndex() && j == end.getColumnIndex()) System.out.print("E");//(i, j)==end
                else System.out.print(maze[i][j]);//(i, j)==1 or (i, j)==0
            }
            System.out.println("");//newline separates rows
        }
    }

    /**
     * Prints the maze in a visualized symbolic pattern, where X symbolizes WALLS.
     */
    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                if (i == start.getRowIndex() && j == start.getColumnIndex()) { sb.append("S  "); }
                else if (i == end.getRowIndex() && j == end.getColumnIndex()) { sb.append("E  "); }
                else{
                    sb.append(maze[i][j] == 1 ? "X" : " ");
                    sb.append("  ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }//symbolicMaze

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                sb.append(maze[i][j] == 1 ? "1" : "0");
            }
            System.out.println("");//newline separates rows
        }
        String s = "  S={" + start.getRowIndex() + "," + start.getColumnIndex() + "}  ";
        String e = "E={"+end.getRowIndex()+","+end.getColumnIndex()+"}";
        sb.append(s);
        sb.append(e);
        return sb.toString();
    }
}//Class Maze