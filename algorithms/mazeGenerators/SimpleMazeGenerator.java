package algorithms.mazeGenerators;


import java.util.ArrayList;

public class SimpleMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int numOfRows, int numOfCols) {
        /* "crash-proofing" the program to avoid Auto-test crash (just in case) */
        if (numOfRows <= 0 || numOfCols <= 0) {
            numOfRows = 5;
            numOfCols = 5;
            System.out.println("Wrong Input: generate received negative numbers as input for Maze dimensions. ");
        }
        int[][] arr= new int[numOfRows][numOfCols];
        Position start=new Position(0,0)
                ,end=new Position(1,1);
        GeneratePosition(arr.length,arr[0].length,start,end);

        ArrayList<ArrayList<Integer>> mylist = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int posrow = start.getRowIndex(), poscul =start.getColumnIndex();
        temp.add(posrow); temp.add(poscul);
        mylist.add(temp);
        while (posrow!=end.getRowIndex()){
            if(posrow<end.getRowIndex()) posrow+=1;
            else  posrow-=1;
            ArrayList<Integer> temp2 = new ArrayList<Integer>();
            temp2.add(posrow);
            temp2.add(poscul);
            mylist.add(temp2);
        }
        while (poscul!=end.getColumnIndex()){
            if(poscul<end.getColumnIndex()) poscul+=1;
            else  poscul-=1;
            ArrayList<Integer> temp2 = new ArrayList<Integer>();
            temp2.add(posrow);
            temp2.add(poscul);
            mylist.add(temp2);
        }
        for(int i=0;i<numOfRows;i++)
            for(int j=0;j<numOfCols;j++){
                ArrayList<Integer> temp2 = new ArrayList<Integer>();
                temp2.add(i); temp2.add(j);
                if(!mylist.contains(temp2)){
                    if(Math.random()*50>25)
                        arr[i][j]=1;
                    else
                        arr[i][j]=0;
                }
            }
        Maze maze = new Maze(arr,start,end);
        return maze;
    }

}