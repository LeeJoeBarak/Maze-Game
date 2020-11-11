package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.io.Serializable;

public abstract class AState implements Serializable {
        protected Position currentPosition;
        protected AState prevState;//was 'FatherState'
        protected int cost;

        /**
         * Constructor for AState
         *
         * @param currentPosition    Position object of the current state
         * @param prevState the previous state that got us to this state
         * @param cost     the cost to get to this state
         */
        public AState(Position currentPosition, AState prevState, int cost){
            this.currentPosition=currentPosition;
            this.prevState =prevState;
            this.cost=cost;
        }//AState

        @Override
        public abstract boolean equals(Object obj);

        /**
         * Overriding toString to represent AState
         * @return string representation of currState, prevState, cost
         */
        @Override
        public String toString() {
            if(prevState == null) return currentPosition.toString();
            return currentPosition.toString() + " " +  prevState.getPosition().toString() + " " + cost;
        }

        /* getters */
        public AState getPrevState(){ return prevState; }

        public Position getPosition(){ return currentPosition; }

        public int getCost(){ return cost; }
        /* end getters */
}

