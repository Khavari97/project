package Maze;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class Main {

    enum BoardStatus {

        start,goal,any
    }

    static BoardStatus status;
    static String beadText = "start";
    
    static State initial; // state for start

    static State goal; // state for goal

    static ArrayList goalPath = new ArrayList(); // go from parent to root

    static final int m = 10; // rows
    static final int n = 10; // columns

    static boolean goalFound = false;
    private static PQueue frontier = new PQueue(m * n * 2000);
    private static PQueue closedList = new PQueue(m * n * 2000);

    static final Color backG = new Color(255, 222, 0);
    static final Color hoverback = new Color(255, 255, 255);

    static final Color wallH = new Color(255, 255, 255);
    static final Color wallP = new Color(0, 0, 0);

    static final Color path = new Color(26, 148, 9);

    static State[][] states = new State[m][n];

    static JLabel[][] beads = new JLabel[m][n];
    static JLabel[][] wx = new JLabel[m - 1][n];
    static JLabel[][] wy = new JLabel[m][n - 1];
    //call walls
    static WallX[][] wallx = new WallX[m - 1][n];
    static WallY[][] wally = new WallY[m][n - 1];
    static JFrame board;

    public static void main(String[] beans) {
        status = BoardStatus.any;
        generateBoard();

    }
    //Algorithm A*
    static State algorithm() {
        frontier.enqueue(initial);
        State current;
        while (!frontier.isEmpty()) {
            current = frontier.dequeue();

            System.out.println(current);

            current.expandNodes();
            System.out.println(frontier);
            for (State s : current.getChildren()) {
                if (s.isGoal()) {
                    return s;
                }
                s.addIfContinueNode();
            }
            System.out.println(frontier.getSize());
            closedList.enqueue(current);
        }
        return null;
    }
//print path
    public static void highlightPath(State goal) {
        if (goal == null) {
            System.out.println("Goal isnot");
            JOptionPane.showMessageDialog(null, "Goal isnot", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        State s = goal;
        System.out.print("final path : ");
        while (s != initial) {
            beads[s.getRow()][s.getCol()].setBackground(path);
            System.out.print(s + " -> ");
            s = s.getParent();
        }
        beads[s.getRow()][s.getCol()].setBackground(path);
        System.out.println(initial);
    }

    static void generateBoard() {
        board = new JFrame();
        //frame set up
        board.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        board.setBackground(new java.awt.Color(255, 255, 153));
        board.setMinimumSize(new java.awt.Dimension(800, 860));
        board.setPreferredSize(new java.awt.Dimension(800, 860));
        board.setResizable(false);
        board.setSize(new java.awt.Dimension(800, 860));
        board.setLayout(null);
        board.setLocationRelativeTo(null);
        board.setVisible(true);

        //Add action button
        JButton action = new JButton();
        action.setVisible(true);
        action.setSize(100, 30);
        action.setText("play");
        action.setOpaque(true);
        action.setLocation(310, 800);
        board.add(action);
        action.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                State result = algorithm();
                highlightPath(result);
            }
        });

        // added nodes
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                beads[i][j] = new JLabel();
                beads[i][j].setVisible(true);
                beads[i][j].setSize(60, 60);
                beads[i][j].setBackground(backG);
                beads[i][j].setOpaque(true);
                beads[i][j].setBorder(javax.swing.BorderFactory.createEtchedBorder());
                beads[i][j].setLocation(80 * j, 80 * i);
                beads[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                board.add(beads[i][j]);
                beads[i][j].repaint();
                int row = i;
                int col = j;

                beads[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        beads[row][col].removeMouseListener(this);
                        if (status == BoardStatus.any) {
                            beadText = "goal";
                            initial = new State(null, row, col, 0);
                            status = BoardStatus.start;
                        } else if (status == BoardStatus.start) {
                            status = BoardStatus.goal;
                            goal = new State(null, row, col, 0);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (status == BoardStatus.goal) {
                            beads[row][col].removeMouseListener(this);
                        } else {
                            beads[row][col].setText(beadText);
                        }

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        beads[row][col].setText("");
                    }
                });

            }
        }

        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                wx[i][j] = new JLabel();
                wallx[i][j] = new WallX(i, j);
                board.add(wx[i][j]);
            }
        }

        //setting up walls trace
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                wy[i][j] = new JLabel();
                wally[i][j] = new WallY(i, j);
                board.add(wy[i][j]);
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {

                wy[i][j].setVisible(true);
                wy[i][j].setSize(20, 60);
                wy[i][j].setOpaque(true);
                wy[i][j].setLocation(80 * j + 60, 80 * i);
                int wall_x = i;
                int wall_y = j;

                wy[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(wallP)) {
                            return;
                        }

                        wallx[wall_x][wall_y].setPlaced(true);
                        placeWall(wall, wall_x, wall_y, wallP);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(wallP)) {
                            return;
                        }
                        wall.setBackground(wallH);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(wallP)) {
                            return;
                        }
                        wall.setBackground(null);

                    }

                });
            }
        }

        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                wx[i][j].setVisible(true);
                wx[i][j].setSize(60, 20);
                wx[i][j].setOpaque(true);
                wx[i][j].setBackground(null);
                wx[i][j].setLocation(80 * j, 80 * i + 60);
                int wall_x = i;
                int wall_y = j;
                wx[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        wall.setVisible(true);
                        if (wall.getBackground().equals(wallP)) {
                            return;
                        }

                        wall.setVisible(true);
                        wall.repaint();
                        wallx[wall_x][wall_y].setPlaced(true);
                        placeWall(wall, wall_x, wall_y, wallP);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(wallP)) {
                            return;
                        }
                        wall.setBackground(wallH);
                        wall.setVisible(true);
                        wall.repaint();

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(wallP)) {
                            return;
                        }
                        wall.setBackground(null);

                    }

                });
            }
        }

    }

    static void placeWall(JLabel wall, int X, int Y, Color c) {
        wall.setBackground(c);
    }

    static void playAgain() {
        board.dispose();
    }

    public static State getInitial() {
        return initial;
    }

    public static void setInitial(State initial) {
        Main.initial = initial;
    }

    public static State getGoal() {
        return goal;
    }

    public static void setGoal(State goal) {
        Main.goal = goal;
    }

    public static State[][] getStates() {
        return states;
    }

    public static void setStates(State[][] states) {
        Main.states = states;
    }

    public static PQueue getFrontier() {
        return frontier;
    }

    public static void setFrontier(PQueue aFrontier) {
        frontier = aFrontier;
    }

    public static PQueue getClosedList() {
        return closedList;
    }

    public static void setClosedList(PQueue aClosedList) {
        closedList = aClosedList;
    }

}
