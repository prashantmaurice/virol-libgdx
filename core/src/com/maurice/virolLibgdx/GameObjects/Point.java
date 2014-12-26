package com.maurice.virolLibgdx.GameObjects;

public class Point {
    public int x;
    public int y;

	public Point(int x, int y) {
		this.x = x;
        this.y = y;
	}
    public void addPoint(Point point){
        this.x+=point.x;
        this.y+=point.y;
    }
    public void addPoint(int x, int y){
        this.x+=x;
        this.y+=y;
    }


}
