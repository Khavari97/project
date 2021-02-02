package Maze;

import javax.swing.JLabel;

public abstract class Wall {

    private boolean placed;
    private int row;
    private int col;

    public Wall(int row, int col) {
        this.placed = false;
        this.row = row;
        this.col = col;
    }

    public boolean placed() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
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

}
class WallX extends Wall {

    public WallX(int row, int col) {
        super(row,col);
    }
}
class WallY extends Wall {

    public WallY(int row, int col) {
        super(row, col);
    }

}

