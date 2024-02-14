package tom.pathfinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    public void addInputReading(Pathfinding pathfinding) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double mouseX = e.getX();
                double mouseY = e.getY();
                double d = (double) (spaceGrid[0].length - 2) / 800;
                int yIndex = (int) ((mouseY * d) + 1);
                int xIndex = (int) ((mouseX * d)  + 1);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    pathfinding.start = spaceGrid[yIndex][xIndex];
                    pathfinding.start.value = -1;
                    pathfinding.start.length = 0;
                    pathfinding.start.display.color = Color.WHITE;
                    repaint();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    for (int y = 1; y < size + 1; y++) {
                        for (int x = 1; x < size + 1; x++) {
                            spaceGrid[y][x].length = Integer.MAX_VALUE;
                            spaceGrid[y][x].path = null;
                        }
                    }
                    pathfinding.changedLength.clear();
                    pathfinding.start.value = -1;
                    pathfinding.start.length = 0;
                    pathfinding.end = spaceGrid[yIndex][xIndex];
                    pathfinding.end.value = Integer.MAX_VALUE / 2;
                    pathfinding.end.display.color = Color.WHITE;
                    repaint();
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !pathfinding.isPathfinding) {
                pathfinding.isPathfinding = true;
                pathfinding.startPathfinding();
                repaint();
                pathfinding.isPathfinding = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_R) {
                pathfinding.end = null;
                pathfinding.start = null;
                for (int y = 1; y < size + 1; y++) {
                    for (int x = 1; x < size + 1; x++) {
                        spaceGrid[y][x].value = 1 + (int) (Math.random() * 9);
                        spaceGrid[y][x].display.color = pathfinding.colors[spaceGrid[y][x].value - 1];
                    }
                }
                repaint();
            }
            return false;
        });
    }

}
