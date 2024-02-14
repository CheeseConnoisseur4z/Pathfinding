package tom.pathfinding;

import java.awt.*;

class Square
{
    int x;
    int y;
    int dimensions;
    Color color;

    public Square(int x, int y, int dimensions, Color color) {
        this.x = (x - 1) * dimensions;
        this.y = (y - 1) * dimensions;
        this.dimensions = dimensions;
        this.color = color;
    }
}
