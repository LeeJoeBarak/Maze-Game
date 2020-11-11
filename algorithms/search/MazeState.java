package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.io.Serializable;
import java.util.LinkedList;

public class MazeState extends AState implements Serializable {

    public MazeState(Position currentPosition, AState parentState, int cost){
        super(currentPosition, parentState, cost);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MazeState))
            return false;
        return ((AState)obj).currentPosition.equals(this.currentPosition);
    }

}
