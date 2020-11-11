package algorithms.search;

import algorithms.mazeGenerators.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BestFirstSearch extends ASearchingAlgorithm {

    /**
     * overrides ISearchingAlgorithm("parent class") getName() */
    @Override
    public String getName() {
        return "BestFirstSearch";
    }

    @Override
    public Solution solve(ISearchable searchable) {
        searchable.resetBoolArray();
        PriorityQueue<AState> queueOfState = new PriorityQueue<>(10000,(s1, s2)->s1.getCost()-s2.getCost());
        ArrayList<AState> solution = new ArrayList<>();
        Position startPosition = searchable.getStartPosition();
        Position goalPosition = searchable.getGoalPosition();
        AState startState = new MazeState(startPosition,null,0);
        AState goalState = new MazeState(goalPosition,null,0);
        LinkedList<AState> PossibleStates;
        queueOfState.add(startState);
        while (!queueOfState.isEmpty() && !queueOfState.contains(goalState)){
            /* using priority-Q with key = cost of edges */
            AState CurrentState = queueOfState.remove();
            this.numberOfNodesEvaluated++;
            PossibleStates = searchable.getAllPossibleStates(CurrentState);
            queueOfState.addAll(PossibleStates);
        }
        AState tmpState = null;
        int costOfPath = 0;
        for(AState currState : queueOfState){
            if(currState.equals(goalState)) tmpState=currState;
        }
        /* no solution was found */
        if(tmpState == null) {
            //should have traversed ALL originally empty(=0) nodes in the maze
            numberOfNodesEvaluated = solution.size();
            return new Solution(solution);
        }
        /* retrace the path to return (starting from goal and going back) */
        else {
            costOfPath = tmpState.getCost();
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
