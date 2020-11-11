package algorithms.search;
import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {

        ArrayList<AState> Path;
        //we produce the path(ArrayList) inside DFS, BFS, BestFS and just (inject??) pass it to the Solution class
        // Doesn't it kindda defeats the purpose of having an entire Solution type for just holding a list and returning it?
        public Solution(ArrayList<AState> Path){ this.Path = Path; }

        public ArrayList<AState> getSolutionPath(){ return Path; }

}
