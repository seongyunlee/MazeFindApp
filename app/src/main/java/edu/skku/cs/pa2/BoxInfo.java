package edu.skku.cs.pa2;

import android.util.Log;

import java.time.LocalDate;

public class BoxInfo {
    int left=0;
    int right=0;
    int top=0;
    int bottom=0;
    static int border_width = 10;





    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }
    public BoxInfo(int type){
        Log.d("Box_Info",Integer.toString(type));
        if(type/8==1){
            top=border_width;
            type=type%8;
        }
        if(type/4==1){
            left=border_width;
            type=type%4;
        }
        if(type/2==1){
            bottom=border_width;
            type=type%2;
        }
        if(type==1){
            right=border_width;
        }
    }
}
