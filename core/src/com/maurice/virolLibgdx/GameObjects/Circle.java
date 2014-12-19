package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Circle {

    public Vector2 getPosition() {
        return position;
    }

    private Vector2 position;
	private float rotation;
	private float width;
	private float height;


	private com.badlogic.gdx.math.Circle boundingCircle;

	public Circle(float x, float y) {
//		this.width = width;
//		this.height = height;
		position = new Vector2(x, y);
		boundingCircle = new com.badlogic.gdx.math.Circle();
	}

	public void update(float delta) {

		// Rotate counterclockwise
        rotation -= 300 * delta;

	}

	public void updateReady(float runTime) {
//		position.y = 2 * (float) Math.sin(7 * runTime) + originalY;
	}

	public void onClick() {
        Gdx.app.log("CIRCLE", "Clicked");
	}

	public void die() {
	}

	public void onRestart(int y) {
		rotation = 0;
		position.y = y;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getRotation() {
		return rotation;
	}



}
