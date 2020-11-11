package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.LinkedList;

/** Object Adapter from Maze to ISearchable (generic search problem that can be solved by an
 * ISearchingAlgorithm) */
public class SearchableMaze implements ISearchable{
    Maze maze;
    boolean[][] visited;

    public SearchableMaze(Maze maze){
        this.maze = maze;
        visited = new boolean[maze.getNumOfRows()][maze.getNumOfCols()];
    }

    /**
     * resets SearchableMaze's visit tracking 2D array  */
    @Override
    public void resetBoolArray() {
        visited = new boolean[maze.getNumOfRows()][maze.getNumOfCols()];
    }

    public boolean checkPosition(Position p){
        return maze.IsPositionIsAWay(p);
    }

    /* getters */
    @Override
    public Position getStartPosition() {
        return maze.getStartPosition();
    }
    @Override
    public Position getGoalPosition() {
        return maze.getGoalPosition();
    }

    public int getNumOfRows(){
        return maze.getNumOfRows();
    }
    public int getNumOfCols(){
        return maze.getNumOfCols();
    }
    public Maze getMaze() { return this.maze; }
    /* end getters */

    /**
     *  creates edges from given AState to EACH reachable neighbor of it (in the searchTree)
     *  and checks its neighbors as 'visited' afterwards
     * @param state the state to explore its neighbors. Should be MazeState.
     * @return LinkedList<AState> Of Possible Successor States
     */
    public LinkedList<AState> getAllPossibleStates(AState state){
        /*check that state is a MazeState*/
        if (!(state instanceof MazeState)) { return  null; }
        LinkedList<AState> listOfPossibleSuccessorStates = new LinkedList<AState>();
        Position statePosition = state.getPosition();
        markPositionAsVisited(state.getPosition());
        Position p1,p2,p3,p4,p5,p6,p7,p8;
        p1=new Position(statePosition.getRowIndex()-1,statePosition.getColumnIndex());
        p2=new Position(statePosition.getRowIndex()-1,statePosition.getColumnIndex()+1);
        p3=new Position(statePosition.getRowIndex(),statePosition.getColumnIndex()+1);
        p4=new Position(statePosition.getRowIndex()+1,statePosition.getColumnIndex()+1);
        p5=new Position(statePosition.getRowIndex()+1,statePosition.getColumnIndex());
        p6=new Position(statePosition.getRowIndex()+1,statePosition.getColumnIndex()-1);
        p7=new Position(statePosition.getRowIndex(),statePosition.getColumnIndex()-1);
        p8=new Position(statePosition.getRowIndex()+1,statePosition.getColumnIndex()-1);
        if(maze.IsPositionIsAWay(p1) && wasNotVisited(p1))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p1, state,10+state.getCost()));//p1 = neighbor of state; state = curr position in maze aka parent position of p1 in searchTree
            markPositionAsVisited(p1);
        }
        /*check if top-right-hand-diagonal position hasn't been visited plus that there's an L-shaped path to reach that position */
        if(maze.IsPositionIsAWay(p2) && (maze.IsPositionIsAWay(p1) || maze.IsPositionIsAWay(p3)) && wasNotVisited(p2))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p2,state,15+state.getCost()));
            markPositionAsVisited(p2);
        }
        if(maze.IsPositionIsAWay(p3)&& wasNotVisited(p3))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p3,state,10+state.getCost()));
            markPositionAsVisited(p3);
        }
        if(maze.IsPositionIsAWay(p4)&&(maze.IsPositionIsAWay(p3)||maze.IsPositionIsAWay(p5))&& wasNotVisited(p4))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p4,state,15+state.getCost()));
            markPositionAsVisited(p4);
        }
        if(maze.IsPositionIsAWay(p5)&& wasNotVisited(p5))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p5,state,10+state.getCost()));
            markPositionAsVisited(p5);
        }
        if(maze.IsPositionIsAWay(p6)&&(maze.IsPositionIsAWay(p5)||maze.IsPositionIsAWay(p7))&& wasNotVisited(p6))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p6,state,15+state.getCost()));
            markPositionAsVisited(p6);
        }
        if(maze.IsPositionIsAWay(p7)&& wasNotVisited(p7))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p7,state,10+state.getCost()));
            markPositionAsVisited(p7);
        }
        if(maze.IsPositionIsAWay(p8)&&(maze.IsPositionIsAWay(p7)||maze.IsPositionIsAWay(p1))&& wasNotVisited(p8))
        {
            listOfPossibleSuccessorStates.add(new MazeState(p8,state,15+state.getCost()));
            markPositionAsVisited(p8);
        }
        return listOfPossibleSuccessorStates;
    }//getAllPossibleStates

    /**
     * @param p Position to check if was marked visited or not.
     * @return true if position was not visited yet, else false. */
    private boolean wasNotVisited(Position p){
        //f==f -> true t==f->false
        //return !visited[p.getRowIndex()][p.getColumnIndex()]
        return visited[p.getRowIndex()][p.getColumnIndex()] == false;
    }
    /**
     * @param p Position to mark as 'visited' */
    private void markPositionAsVisited(Position p){
        visited[p.getRowIndex()][p.getColumnIndex()]=true;
    }

}


