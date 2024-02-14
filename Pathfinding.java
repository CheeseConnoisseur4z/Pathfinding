package tom.pathfinding;

import java.awt.*;
import java.util.*;


public class Pathfinding
{
    int size = -1;
    Space[][] spaceGrid;
    Space start;
    Space end;
    ArrayList<Space> changedLength = new ArrayList<>();
    boolean isPathfinding = false;

    final Color[] colors = {
            new Color(0x04FF00),
            new Color(0x84FF00),
            new Color(0xDDFF00),
            new Color(0xFFEA00),
            new Color(0xFFCC00),
            new Color(0xFF9100),
            new Color(0xFF5900),
            new Color(0xBB0000),
            new Color(0x5D0000),
            new Color(0x0)
    };


    public static void main(String[] args) {
        Pathfinding betterPathfinding = new Pathfinding();
        betterPathfinding.start();
    }

    public void start() {
        readInput();
        createSpaceGrid();
        tom.pathfinding.Interface anInterface = new Interface(spaceGrid, size);
        anInterface.frame.add(anInterface);
        anInterface.addInputReading(this);
        anInterface.frame.setSize(800, 800 + 23);
        anInterface.frame.setVisible(true);
    }

    public void startPathfinding() {
        start.tellPathToNeighbours();
        ArrayList<Space> copy = new ArrayList<>();
        while (changedLength.size() > 0) {
            copy.addAll(changedLength);
            changedLength.clear();
            copy.remove(end);
            copy.forEach(Space::tellPathToNeighbours);
        }
        printPath(end.path);
    }

    private void printPath(Space space) {
        if (space == null)
            return;
        space.display.color = Color.WHITE;
        printPath(space.path);
    }

    private void createSpaceGrid() {
        spaceGrid = new Space[size + 2][size + 2];
        int dimensions = 800 / size;
        for (int y = 1; y < size + 1; y++) {
            for (int x = 1; x < size + 1; x++) {
                spaceGrid[y][x] = new Space(1 + (int) (Math.random() * 9), changedLength, y, x);
                spaceGrid[y][x].display = new Square(x, y, dimensions, colors[(int) (Math.random() * 9)]);
            }
        }
        for (int y = 1; y < size + 1; y++) {
            for (int x = 1; x < size + 1; x++) {
                spaceGrid[y][x].neighbours[0] = spaceGrid[y][x - 1];
                spaceGrid[y][x].neighbours[1] = spaceGrid[y][x + 1];
                spaceGrid[y][x].neighbours[2] = spaceGrid[y - 1][x];
                spaceGrid[y][x].neighbours[3] = spaceGrid[y + 1][x];
            }
        }
    }

    private void readInput() {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                size = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("invalid input");
            }
        } while (size == -1);
    }
}