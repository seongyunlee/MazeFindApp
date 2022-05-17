package edu.skku.cs.pa2.util;

import androidx.annotation.NonNull;

public class Player {
    int row;

    @NonNull
    @Override
    public String toString() {
        return Integer.toString(row)+","+Integer.toString(col);
    }
    int direction; //up,right,bottom,left ~ 0,1,2,3
    int col;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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

    public Player(int row, int col,int direction) {
        this.row = row;
        this.col = col;
        this.direction =direction;
    }
}
