package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.TweenAccessors.Value;
import com.maurice.virolLibgdx.TweenAccessors.ValueAccessor;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class Circle {

    private Vector2 gridPosition;
    private Vector2 actualPosition;
    private Vector2 tileDim;
    private boolean isOpponent = false;

    private int circleDia;
	private float rotation;
    private int value;
    private boolean inBlast = false;

    public float getBlastRadius() {
        return blastRadius.getValue();
    }
    public Vector2 getGridPosition() {
        return gridPosition;
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

    }

    public void blastcomplete(){
        value=0;
        CircleController.getInstance().addCircleValue((int)gridPosition.x+1,(int)gridPosition.y,isOpponent);
        CircleController.getInstance().addCircleValue((int)gridPosition.x-1,(int) gridPosition.y,isOpponent);
        CircleController.getInstance().addCircleValue((int)gridPosition.x,(int)gridPosition.y+1,isOpponent);
        CircleController.getInstance().addCircleValue((int)gridPosition.x,(int)gridPosition.y-1,isOpponent);
    }


	public void onClick(boolean byOpponent) {
        Gdx.app.log("CIRCLE", "Clicked circle:"+gridPosition.x+"=="+gridPosition.y);
        addValue(byOpponent);

	}

    public void addValue(boolean byOpponent){
        isOpponent = byOpponent;
        if(value<3) {
            AssetLoader.coin.play(0.005f);//30% volume
            value++;
            CircleController.getInstance().addNonBlastAnimation();
        }
        else{
            AssetLoader.blast.play();
            blast(byOpponent);
            CircleController.getInstance().addBlastAnimation();
        }
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

    public boolean isValid() {
        if(value==0) return true;
        if(isOpponent){
            if(GameWorld.getInstance().currPlayState== GameWorld.PlayState.OPPONENT){return true;}
        }else{
            if(GameWorld.getInstance().currPlayState== GameWorld.PlayState.PLAYER){return true;}
        }
        return false;
    }

    public int getCircleDia() {
        return circleDia;
    }
}
