package algorithms.search;

import java.util.LinkedList;

/*  int getNumberOfNodesEvaluated();

    String getName();

    Solution solve(ISearchable domain);
}*/
public interface ISearchingAlgorithm {
    public Solution solve(ISearchable searchable);
    public String getName();
    public int getNumberOfNodesEvaluated();
}