package tom.pathfinding;

import java.util.ArrayList;

class Space
{
    int value;
    int firstIndex;
    int secondIndex;
    Space[] neighbours = new Space[4];
    Space path;
    int length = Integer.MAX_VALUE;
    private final ArrayList<Space> changedLength;
    Square display;


    public Space(int value, ArrayList<Space> changedLength, int firstIndex, int secondIndex) {
        this.value = value;
        this.changedLength = changedLength;
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

    public void tellPathToNeighbours() {
        int newLength = this.length + this.value;
        for (Space neighbour : neighbours) {
            if (neighbour != null && neighbour != path && newLength < neighbour.length) {
                neighbour.length = newLength;
                neighbour.path = this;
                changedLength.add(neighbour);
            }
        }
    }
}
