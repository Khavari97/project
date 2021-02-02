package Maze;

import java.util.HashSet;
import java.util.Set;

public class State {

    private int row;
    private int col;
    private int gpath;//pathcost
     private Set<State> children;
    private State parent;

    public State(State parent, int row, int col, int gpath) {
        this.parent = parent;
        this.row = row;
        this.col = col;
        this.gpath = gpath;
    }

    public Set<State> getChildren() {
        return children;
    }

    public void setChildren(Set<State> children) {
        this.children = children;
    }

    public State getParent() {
        return parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getG() {
        return gpath;
    }

    public void setG(int g) {
        this.gpath = g;
    }

    void expandNodes() {
        // if we have not wall x and y expand nodes
        children = new HashSet<State>();
        //left
       
            if (!Main.wally[row][col - 1].placed()) {
                children.add(new State(this, row, col - 1, gpath + 1));
            }
       
        //right
        
            if (!Main.wally[row][col].placed()) {
                children.add(new State(this, row, col + 1, gpath + 1));
            }
        
        //top
       
            if (!Main.wallx[row - 1][col].placed()) {
                children.add(new State(this, row - 1, col, gpath + 1));
            }
       
        //bottom
        
            if (!Main.wallx[row][col].placed()) {
                children.add(new State(this, row + 1, col, gpath + 1));
            }
        
    }
    //return rows and collomns
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    //  manhattan heuristic method
    int getHeuristic() {
        return Math.abs(row - Main.goal.row) + Math.abs(col - Main.goal.col);
    }

    int getF() {
        return gpath + getHeuristic();
    }

    public boolean continueNode() {
        //there are 2 q
        //open list gostaresh nodes
        PQueue frontier = Main.getFrontier();
        //closedlist node show before
        PQueue closedList = Main.getClosedList();
        State sameOnFrontier = frontier.getPositionSuccessor(this);
        State sameOnClosed = closedList.getPositionSuccessor(this);

        if (sameOnFrontier != null && sameOnFrontier.getF() <= this.getF()) {
            return false;
        } else if (sameOnClosed != null && sameOnClosed.getF() <= this.getF()) {
            return false;
        }
        // Otherwise
        return true;
    }
    //is goal?
    public boolean isGoal() {
        State goal = Main.getGoal();
        if (row == goal.getRow() && col == goal.getCol()) {
            return true;
        }
        return false;
    }

    public void addIfContinueNode() {
        if (continueNode()) {
            Main.getFrontier().enqueue(this);
        }
    }
}
