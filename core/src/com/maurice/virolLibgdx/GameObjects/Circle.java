package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.Gdx;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.TweenAccessors.Value;
import com.maurice.virolLibgdx.TweenAccessors.ValueAccessor;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class Circle {

    private Point gridPosition;
    private Point actualPosition;
    private Point tileDim;
    private boolean isOpponent = false;

    private int circleDia;
	private float rotation;
    private int value;
    private boolean inBlast = false;
    private int MAXVALUE = 3;
    public boolean leftArrowOn = true;
    public boolean rightArrowOn = true;
    public boolean topArrowOn = true;
    public boolean bottomArrowOn = true;

    public float getBlastRadius() {
        return blastRadius.getValue();
    }

    private Value blastRadius = new Value();;
    private TweenManager manager = new TweenManager();

    public Point getActualPosition() {
        return actualPosition;
    }

    public Circle(int x, int y, int dimX,int dimY) {
		gridPosition = new Point(x, y);
        tileDim = new Point(dimX, dimY);
        circleDia = Math.min(dimX,dimY);
        actualPosition = new Point(x*dimX, y*dimY);
        value=0;

        //blast stuff
        blastRadius.setValue(0);

        //corners stuff
        if((x==GameWorld.ROWS-1)||(x==0)) {
            MAXVALUE--;
            if(x==0) leftArrowOn = false;
            else rightArrowOn = false;
        }
        if((y==GameWorld.COLUMNS-1)||(y==0)) {
            MAXVALUE--;
            if(y==0) topArrowOn = false;
            else bottomArrowOn = false;
        }
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
        CircleController.getInstance().addCircleValue(gridPosition.x+1,gridPosition.y,isOpponent);
        CircleController.getInstance().addCircleValue(gridPosition.x-1,gridPosition.y,isOpponent);
        CircleController.getInstance().addCircleValue(gridPosition.x,gridPosition.y+1,isOpponent);
        CircleController.getInstance().addCircleValue(gridPosition.x,gridPosition.y-1,isOpponent);
    }


	public void onClick(boolean byOpponent) {
        Gdx.app.log("CIRCLE", "Clicked circle:"+gridPosition.x+"=="+gridPosition.y);
        addValue(byOpponent);
	}

    public void addValue(boolean byOpponent){
        isOpponent = byOpponent;

        if(value<MAXVALUE) {
            AssetLoader.coin.play(0.005f,2,0);//30% volume
            value++;
            CircleController.getInstance().addNonBlastAnimation();
        }
        else{
            AssetLoader.blast.play();
            blast(byOpponent);
            CircleController.getInstance().addBlastAnimation();
        }
    }

	public float getRotation() {
		return rotation;
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

    public int getAbsoluteValue() {
        if(isOpponent) return value*-1;
        else return value;
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
