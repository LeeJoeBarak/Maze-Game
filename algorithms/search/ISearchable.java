package algorithms.search;
import algorithms.mazeGenerators.Position;

import java.util.LinkedList;
/*
    AState getStartState();

    AState getGoalState();

    ArrayList<AState> getAllSuccessors(AState s);
*/
public interface ISearchable {
    public void resetBoolArray();
    public boolean checkPosition(Position p);
    public int getNumOfRows();
    public int getNumOfCols();
    public Position getStartPosition();
    public Position getGoalPosition();

    //public ArrayList<AState> getAllSuccessors(AState s)
    public LinkedList<AState> getAllPossibleStates(AState state);//! don't change method name! required for auto testing
}
