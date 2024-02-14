package tom.projects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;


public class Pathfinding
{
    int size = -1;
    Space[][] spaceGrid;
    Space start;
    Space end;
    ArrayList<Space> changedLength = new ArrayList<>();
    private Interface mainPanel;
    boolean isPathfinding = false;

    final private Color[] colors = {
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
        mainPanel = new Interface(spaceGrid, size);
        mainPanel.frame.add(mainPanel);
        addMouseInterface();
        mainPanel.frame.setSize(800, 800 + 23);
        mainPanel.frame.setVisible(true);
    }

    public void startPathfinding() {
        int c = 0;
        start.tellPathToNeighbours();
        ArrayList<Space> copy = new ArrayList<>();
        while (changedLength.size() > 0) {
            c++;
            copy.addAll(changedLength);
            changedLength.clear();
            copy.remove(end);
            copy.forEach(Space::tellPathToNeighbours);
            System.out.println(100 * c / (spaceGrid.length * 2) + "%");
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

    private void addMouseInterface() {
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double mouseX = e.getX();
                double mouseY = e.getY();
                double d = (double) (spaceGrid[0].length - 2) / 800;
                int yIndex = (int) ((mouseY * d) + 1);
                int xIndex = (int) ((mouseX * d)  + 1);
                System.out.println(yIndex + " : " + xIndex);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    start = spaceGrid[yIndex][xIndex];
                    start.value = -1;
                    start.length = 0;
                    start.display.color = Color.WHITE;
                    mainPanel.repaint();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    for (int y = 1; y < size + 1; y++) {
                        for (int x = 1; x < size + 1; x++) {
                            spaceGrid[y][x].length = Integer.MAX_VALUE;
                            spaceGrid[y][x].path = null;
                        }
                    }
                    changedLength.clear();
                    start.value = -1;
                    start.length = 0;
                    end = spaceGrid[yIndex][xIndex];
                    end.value = Integer.MAX_VALUE / 2;
                    end.display.color = Color.WHITE;
                    mainPanel.repaint();
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !isPathfinding) {
                isPathfinding = true;
                startPathfinding();
                mainPanel.repaint();
                isPathfinding = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_R) {
                end = null;
                start = null;
                for (int y = 1; y < size + 1; y++) {
                    for (int x = 1; x < size + 1; x++) {
                        spaceGrid[y][x].value = 1 + (int) (Math.random() * 9);
                        spaceGrid[y][x].display.color = colors[spaceGrid[y][x].value - 1];
                    }
                }
                mainPanel.repaint();
            }
            return false;
        });
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

class Interface extends JPanel
{
    Space[][] spaceGrid;
    int dimensions;
    int size;
    JFrame frame = createFrame();
    Container container;


    public Interface(Space[][] spaceGrid, int size) {
        this.spaceGrid = spaceGrid;
        this.size = size;
        this.dimensions = 800 / size;
    }

    public JFrame createFrame() {
        JFrame frame = new JFrame("better pathfinding");
        frame.setSize(0, 0);
        frame.setVisible(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        return frame;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int y = 1; y < size + 1; y++) {
            for (int x = 1; x < size + 1; x++) {
                Square square = spaceGrid[y][x].display;
                g2.setColor(square.color);
                g2.fillRect(square.x, square.y, square.dimensions, square.dimensions);
            }
        }
    }

}

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