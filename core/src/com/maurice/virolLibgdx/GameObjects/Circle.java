package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Circle {

    public Vector2 getGridPosition() {
        return gridPosition;
    }

    private Vector2 gridPosition;
    private Vector2 actualPosition;
    private Vector2 tileDim;

    public int getCircleDia() {
        return circleDia;
    }

    private int circleDia;
	private float rotation;
    private int value;


	private com.badlogic.gdx.math.Circle boundingCircle;


    public Vector2 getActualPosition() {
        return actualPosition;
    }

    public Circle(int x, int y, int dimX,int dimY) {
		gridPosition = new Vector2(x, y);
        tileDim = new Vector2(dimX, dimY);
        circleDia = Math.min(dimX,dimY);
        actualPosition = new Vector2(x*dimX, y*dimY);
		boundingCircle = new com.badlogic.gdx.math.Circle();
        value=0;
	}

	public void update(float delta) {

		// Rotate counterclockwise
        rotation -= 100 * delta*value;

	}

	public void updateReady(float runTime) {
	}

	public void onClick() {
        Gdx.app.log("CIRCLE", "Clicked circle:"+gridPosition.x+"=="+gridPosition.y);
        if(value<3)value++;
	}

	public void die() {
	}

	public void onRestart(int y) {
		rotation = 0;
		gridPosition.y = y;
	}

	public float getX() {
		return gridPosition.x;
	}

	public float getY() {
		return gridPosition.y;
	}


	public float getRotation() {
		return rotation;
	}
    public Vector2 getTileDim() {
        return tileDim;
    }


    public boolean contains(int screenX, int screenY) {
        if((screenX>actualPosition.x)&(screenX<actualPosition.x+tileDim.x)){
            if((screenY>actualPosition.y)&(screenY<actualPosition.y+tileDim.y)){
                return true;
            }
        }
        return false;
    }

    public int getValue() {
        return value;
    }
}
