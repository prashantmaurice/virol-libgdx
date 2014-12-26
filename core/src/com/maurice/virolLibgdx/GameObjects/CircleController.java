package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.utils.Timer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;

public class CircleController {

	private float rotation;
    public static float BLAST_TIME = 0.5f;//1 secs
    public static float NON_BLAST_TIME = 0.35f;//1 secs
    private static CircleController instance;
    public boolean isCurrMoveOpponent = false;
    public int runningAnimations = 0;

    public Circle[][] getCiclesArray() {
        return ciclesArray;
    }

    private Circle[][] ciclesArray;
    private int ROWS;
    private int COLUMNS;

	public CircleController(int x, int y) {
		this.ROWS = x;
		this.COLUMNS = y;
        instance=this;
	}
    public static CircleController getInstance(){
        return instance;
    }
    public Circle getCircle(int x, int y){
        return ciclesArray[x][y];
    }
    public void addCircleValue(int x, int y,boolean isOpponent){
        if((x>=ROWS)||(x<0)||(y>=COLUMNS)||(y<0)) return;
        ciclesArray[x][y].addValue(isOpponent);
    }
    public void restart(){
        createBoard(ROWS,COLUMNS);
    }
    public void createBoard(int boardX,int boardY){
        ciclesArray = new Circle[GameWorld.ROWS][GameWorld.COLUMNS];
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                ciclesArray[i][j] = new Circle(i,j,boardX/ROWS,boardY/COLUMNS);
            }
        }
    }

	public void update(float delta) {

		// Rotate counterclockwise
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                ciclesArray[i][j].update(delta);
            }
        }

	}
    public void move(int i, int j){
        if((i>=GameWorld.ROWS)||(i<0)) return;
        if((j>=GameWorld.COLUMNS)||(j<0)) return;
        if(ciclesArray[i][j].isValid()){
            if(GameWorld.getInstance().currPlayState == GameWorld.PlayState.PLAYER){
                changePlayState(GameWorld.PlayState.ANIM_PLAYER);
                ciclesArray[i][j].onClick(false);
            }else if(GameWorld.getInstance().currPlayState == GameWorld.PlayState.OPPONENT){
                changePlayState(GameWorld.PlayState.ANIM_OPPONENT);
                ciclesArray[i][j].onClick(true);
            }
            proceedNextMove();
        }
    }

    public void onclick(int screenX, int screenY) {
        if(runningAnimations>0) return;
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                if(ciclesArray[i][j].contains(screenX,screenY)){
                    move(i,j);
                    return;
                };
            }
        }
    }
    public void proceedNextMove(){
        isCurrMoveOpponent = !isCurrMoveOpponent;
    }
    public void addBlastAnimation(){
        runningAnimations++;
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                runningAnimations--;
                if(runningAnimations==0){
                    if(GameWorld.getInstance().currPlayState == GameWorld.PlayState.ANIM_PLAYER){
                        changePlayState(GameWorld.PlayState.OPPONENT);
                    }else if(GameWorld.getInstance().currPlayState == GameWorld.PlayState.ANIM_OPPONENT){
                        changePlayState(GameWorld.PlayState.PLAYER);
                    }
                }
            }
        };
        Timer y = new Timer();
        y.scheduleTask(task,CircleController.BLAST_TIME);
    }
    public void addNonBlastAnimation(){
        runningAnimations++;
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                runningAnimations--;
                if(runningAnimations==0){
                    if(GameWorld.getInstance().currPlayState == GameWorld.PlayState.ANIM_PLAYER){
                        changePlayState(GameWorld.PlayState.OPPONENT);
                    }else if(GameWorld.getInstance().currPlayState == GameWorld.PlayState.ANIM_OPPONENT){
                        changePlayState(GameWorld.PlayState.PLAYER);
                    }
                }
            }
        };
        Timer y = new Timer();
        y.scheduleTask(task,CircleController.NON_BLAST_TIME);
    }

    public void checkGameOver() {
        int sum = 0;
        int opponent = 0;
        int player = 0;
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                sum+=ciclesArray[i][j].getValue();
                if(ciclesArray[i][j].getValue()>0){
                    if(ciclesArray[i][j].isOpponent()){
                        opponent++;
                    }else{
                        player++;
                    }
                }
            }
        }
        if(sum>5){
            if((opponent==0)||(player==0)){
                AssetLoader.dead.play();
                GameWorld.getInstance().gameover(player == 0);
                System.out.println("GameOver called");
            }
        }
    }
    private boolean validPoint(int i,int j){
        if((i>=GameWorld.ROWS)||(i<0)) return false;
        if((j>=GameWorld.COLUMNS)||(j<0)) return false;
        return true;
    }

    private void changePlayState(GameWorld.PlayState state){
        System.out.println("PLAYSTATE: changed to "+state);
        GameWorld.getInstance().currPlayState = state;
    }
    public boolean hasSimilarGeneric(int i, int j,int x, int y) {
        if(!validPoint(i,j)) return false;
        if(!validPoint(x,y)) return false;
        int value = getCircle(i,j).getAbsoluteValue();
        int valueOther = getCircle(x,y).getAbsoluteValue();
        if(valueOther==0) return false;
        if(value==0) return false;//as no color for this
        return ((value*valueOther)>0);
    }
    public boolean hasSimilarLeft(int i, int j) {
        if(i==0) return false;
        int value = getCircle(i,j).getAbsoluteValue();
        int valueLeft = getCircle(i-1,j).getAbsoluteValue();
        if(valueLeft==0) return false;
        if(value==0) return false;//as no color for this
        return ((value*valueLeft)>0);
    }
    public boolean hasSimilarRight(int i, int j) {
        if(i== GameWorld.ROWS-1) return false;
        int value = getCircle(i,j).getAbsoluteValue();
        int valueLeft = getCircle(i+1,j).getAbsoluteValue();
        if(valueLeft==0) return false;
        if(value==0) return false;//as no color for this
        return ((value*valueLeft)>0);
    }
    public boolean hasSimilarTop(int i, int j) {
        if(j==0) return false;
        int value = getCircle(i,j).getAbsoluteValue();
        int valueLeft = getCircle(i,j-1).getAbsoluteValue();
        if(valueLeft==0) return false;
        if(value==0) return false;//as no color for this
        return ((value*valueLeft)>0);
    }
    public boolean hasSimilarBottom(int i, int j) {
        if(j==GameWorld.COLUMNS-1) return false;
        int value = getCircle(i,j).getAbsoluteValue();
        int valueLeft = getCircle(i,j+1).getAbsoluteValue();
        if(valueLeft==0) return false;
        if(value==0) return false;//as no color for this
        return ((value*valueLeft)>0);
    }
}
