package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.maurice.virolLibgdx.TweenAccessors.Value;
import com.maurice.virolLibgdx.TweenAccessors.ValueAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class Circle {

    public Vector2 getGridPosition() {
        return gridPosition;
    }

    private Vector2 gridPosition;
    private Vector2 actualPosition;
    private Vector2 tileDim;
    private boolean isOpponent = false;

    public int getCircleDia() {
        return circleDia;
    }

    private int circleDia;
	private float rotation;
    private int value;
    private boolean inBlast = false;


    public float getBlastRadius() {
        return blastRadius.getValue();
    }

    private Value blastRadius = new Value();;
    private TweenManager manager = new TweenManager();




    public Vector2 getActualPosition() {
        return actualPosition;
    }

    public Circle(int x, int y, int dimX,int dimY) {
		gridPosition = new Vector2(x, y);
        tileDim = new Vector2(dimX, dimY);
        circleDia = Math.min(dimX,dimY);
        actualPosition = new Vector2(x*dimX, y*dimY);
        value=0;

        //blast stuff
        blastRadius.setValue(0);

	}

	public void update(float delta) {

		// Rotate counterclockwise
        rotation -= 100 * delta*value;
        manager.update(delta);
        if((inBlast)&&(blastRadius.getValue()==circleDia)){
            inBlast = false;
            blastcomplete();
        }

	}

    public void blast(boolean byOpponent){
        inBlast=true;
        blastRadius.setValue(0);
        Tween.registerAccessor(Value.class, new ValueAccessor());
        Tween.to(blastRadius, -1, CircleController.BLAST_TIME).target(circleDia)
                .ease(TweenEquations.easeOutQuad).start(manager);
        CircleController.getInstance().addBlastAnimation();
    }
    public void blastcomplete(){
        value=0;
        CircleController.getInstance().addCircleValue((int)gridPosition.x+1,(int)gridPosition.y,isOpponent);
        CircleController.getInstance().addCircleValue((int) gridPosition.x - 1, (int) gridPosition.y, isOpponent);
        CircleController.getInstance().addCircleValue((int)gridPosition.x,(int)gridPosition.y+1,isOpponent);
        CircleController.getInstance().addCircleValue((int)gridPosition.x,(int)gridPosition.y-1,isOpponent);
    }


	public void updateReady(float runTime) {
	}
	public void onClick(boolean byOpponent) {
        Gdx.app.log("CIRCLE", "Clicked circle:"+gridPosition.x+"=="+gridPosition.y);
        addValue(byOpponent);
	}
    public void addValue(boolean byOpponent){
        isOpponent = byOpponent;
        if(value<3)value++;
        else{
            blast(byOpponent);
        }

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

    public boolean inBlast() {
        return inBlast;
    }
    public boolean isOpponent(){ return isOpponent;}

    public boolean isValid(boolean isCurrMoveOpponent) {
        if(value==0) return true;
        if((value<=3)&&(isOpponent!=isCurrMoveOpponent)) return false ;
        return true;
    }
}
