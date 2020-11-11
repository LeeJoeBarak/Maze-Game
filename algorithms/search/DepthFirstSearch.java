package algorithms.search;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class DepthFirstSearch extends ASearchingAlgorithm {

    @Override
    public String getName() { return "DepthFirstSearch"; }
    /**
     * Solves a search problem
     * @param searchable the problem to solve
     * @return a solution to the search problem
     */
    public Solution solve(ISearchable searchable){
        if (searchable == null) { return null; }
        searchable.resetBoolArray();//init SearchableMaze.visited

        LinkedList<AState> listOfStates = new LinkedList<AState>();
        ArrayList<AState> solution = new ArrayList<AState>();
        Position startPosition = searchable.getStartPosition();
        Position goalPosition = searchable.getGoalPosition();
        AState startState = new MazeState(startPosition,null,0);
        AState goalState = new MazeState(goalPosition,null,0);
        LinkedList<AState> allPossibleSuccessorslist;
        listOfStates.add(startState);
        while (!listOfStates.isEmpty() && !listOfStates.contains(goalState)){
            /* using sort of Last-In-First-Out Q (similar to stack) */
            AState CurrentState = listOfStates.removeFirst();
            this.numberOfNodesEvaluated++;
            allPossibleSuccessorslist = searchable.getAllPossibleStates(CurrentState);//returns: LinkedList<AState> where AState IS A MazeState
            allPossibleSuccessorslist.addAll(listOfStates);
            listOfStates = allPossibleSuccessorslist;
        }
        /*while loop ended because either the list contains goal OR the list is empty and we didn't reach goal(aka No Valid Path exists)*/
        AState tmpState = null;
        /*find goal state */
        for(AState currState : listOfStates){
            if(currState.equals(goalState)){
                tmpState = currState;
               // break;
            }
        }
        /* no solution was found */
        if(tmpState == null) {
           //should have traversed ALL originally empty nodes in the maze
            numberOfNodesEvaluated = solution.size();
            return new Solution(solution);
        }
        /* retrace the path to return (starting from goal and going back) */
        else {
            while (tmpState != null){
                solution.add(tmpState);
                tmpState = tmpState.getPrevState();
            }
            //numberOfNodesEvaluated = solution.size();
            Collections.reverse(solution);
            return new Solution(solution);
        }
    }
}