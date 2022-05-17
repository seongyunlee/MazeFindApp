package edu.skku.cs.pa2.util;

public class MazeInfo {

    String name;
    Integer size;

    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MazeInfo(String name,Integer size) {
        this.name= name;
        this.size = size;

    }
}
