package com.maurice.virolLibgdx.GameObjects;

import com.badlogic.gdx.utils.Timer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;

public class CircleController {

	private float rotation;
    public static float BLAST_TIME = 0.5f;//1 secs
    private static CircleController instance;
    private boolean isCurrMoveOpponent = false;
    private int runningAnimations = 0;

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

    public void onclick(int screenX, int screenY) {
        if(runningAnimations>0) return;
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                if(ciclesArray[i][j].contains(screenX,screenY)){
                    if(ciclesArray[i][j].isValid(isCurrMoveOpponent)){
                        ciclesArray[i][j].onClick(isCurrMoveOpponent==true);
                        proceedNextMove();
                    }
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
            }
        };
        Timer y = new Timer();
        y.scheduleTask(task,CircleController.BLAST_TIME);
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
                GameWorld.getInstance().gameover(player==0);
                System.out.println("GameOver called");
            }
        }
    }
}
