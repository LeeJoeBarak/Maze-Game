package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BreadthFirstSearch extends ASearchingAlgorithm {

    @Override
    public String getName() {
        return "BreadthFirstSearch";
    }

    /**
     * Solves a search problem
     * @param searchable the problem to solve
     * @return a solution to the search problem
     */
    public Solution solve(ISearchable searchable){
        LinkedList<AState> listOfState=new LinkedList<AState>();
        ArrayList<AState> solution=new ArrayList<AState>();
        LinkedList<AState> PossibleStates;
        Position startPosition = searchable.getStartPosition();
        Position goalPosition = searchable.getGoalPosition();
        AState startState = new MazeState(startPosition,null,0);
        AState goalState = new MazeState(goalPosition,null,0);
        searchable.resetBoolArray();
        listOfState.add(startState);
        while(!listOfState.isEmpty() && !listOfState.contains(goalState)){
            /* using First-in-First-Out Q */
            AState CurrentState = listOfState.removeFirst();
            this.numberOfNodesEvaluated++;
            PossibleStates = searchable.getAllPossibleStates(CurrentState);
            listOfState.addAll(PossibleStates);//diff from DFS in that it explores all neighbors first instead of going deep (builds a level graph)
        }
        AState tmpState = null;
        for(AState currState : listOfState){
            if(currState.equals(goalState)) tmpState=currState;
        }
        /* no solution was found */
        if(tmpState == null) {
            //should have traversed ALL originally empty nodes in the maze
            numberOfNodesEvaluated = solution.size();
            return new Solution(solution);
        }
        /* retrace the path to return (starting from goal and going back) */
        else{
            while (tmpState!=null){
                solution.add(tmpState);
                tmpState=tmpState.getPrevState();
            }
            //numberOfNodesEvaluated = solution.size();
            Collections.reverse(solution);
            return new Solution(solution);
        }
    }
}