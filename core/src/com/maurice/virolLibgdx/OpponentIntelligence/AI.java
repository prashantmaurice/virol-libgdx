package com.maurice.virolLibgdx.OpponentIntelligence;

import com.badlogic.gdx.Gdx;
import com.maurice.virolLibgdx.GameObjects.CircleController;
import com.maurice.virolLibgdx.GameObjects.Point;
import com.maurice.virolLibgdx.GameWorld.GameWorld;

import java.util.ArrayList;

/**
 * Created by maurice on 24/12/14.
 *
 * All Opponent cards are negative in array
 */
public class AI {
    public static Point getNextMove(CircleController circleController){
        Integer[][] ciclesArrayInt = new Integer[GameWorld.ROWS][GameWorld.COLUMNS];
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                ciclesArrayInt[i][j] = circleController.getCircle(i,j).getValue();
                if(circleController.getCircle(i,j).isOpponent()){
                    ciclesArrayInt[i][j]*=-1;
                }
            }
        }

        //GET PROBABLE POINTS
        ArrayList<Point> validPoints = getValidPoints(ciclesArrayInt,true);

        //CALCULATE BEST MOVE WITH MIN SCORE
        float curr_bestScore = 100000;
        Point curr_BestMove = validPoints.get(0);
        Gdx.app.log("AI2", "=============================================");
        for(int i=0;i<validPoints.size();i++){
            Point opponentMove = validPoints.get(i);
            float score = getScoreAfter2Moves(ciclesArrayInt,opponentMove);
//            Gdx.app.log("AI2", "Opponent Score of ("+opponentMove.x+","+opponentMove.y+") is "+score);
            if(score<curr_bestScore){
//                Gdx.app.log("AI2", "Best move set to ("+opponentMove.x+","+opponentMove.y+") with score "+score);
                curr_bestScore = score;
                curr_BestMove = opponentMove;
            }
        }
        Gdx.app.log("AI2", "Best Opponent move set to ("+curr_BestMove.x+","+curr_BestMove.y+") with score "+curr_bestScore);
        Gdx.app.log("AI2", "=============================================");
        

        //RETURN CURRENT BEST MOVE
        return curr_BestMove;
    }

    private static float getScoreAfter2Moves(Integer[][] ciclesArrayInt,Point opponentMove) {
        Integer[][] afterOpponent = getCirclesArrayAfter(ciclesArrayInt,opponentMove,true);
        ArrayList<Point> validPoints = getValidPoints(afterOpponent,false);

        //CALCULATE BEST MOVE WITH MAX SCORE
        float curr_bestScore = -100000;
//        Gdx.app.log("AI2", "afterOpponent : "+printDoubleArray(afterOpponent));
//        Gdx.app.log("ERROR", "validPoints : "+validPoints.toString());
//        Gdx.app.log("AI2", "validPoints : "+validPoints.size());
        Point curr_BestMove = validPoints.get(0);
        for(int i=0;i<validPoints.size();i++){
            Point playerMove = validPoints.get(i);
            Integer[][] afterPlayer = getCirclesArrayAfter(afterOpponent, playerMove, false);
//            Gdx.app.log("AI2", "afterPlayer : "+printDoubleArray(afterPlayer));
            float score = calculateScore(afterPlayer);
//            Gdx.app.log("AI2", "Player Score of ("+playerMove.x+","+playerMove.y+") is "+score);
            if(score>curr_bestScore){
                curr_bestScore = score;
                curr_BestMove = playerMove;
            }
        }
//        Gdx.app.log("AI2", "Best player's move set to ("+curr_BestMove.x+","+curr_BestMove.y+") with score "+curr_bestScore);
        return curr_bestScore;
    }

    private static ArrayList<Point> getValidPoints(Integer[][] ciclesArrayInt,boolean isOpponent){
        ArrayList<Point> validPoints = new ArrayList<Point>();
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                if(isOpponent){
                    if(ciclesArrayInt[i][j]<=0){
                        Point temp = new Point(i,j);
                        validPoints.add(temp);
                    }
                }else{
                    if(ciclesArrayInt[i][j]>=0){
                        Point temp = new Point(i,j);
                        validPoints.add(temp);
                    }
                }
            }
        }
        return validPoints;
    }
    private static Integer[][] getCirclesArrayAfter(Integer[][] OldcirclesArrayInt, Point move, boolean isOpponent){
        //COPY ARRAY
        Integer[][] NewcirclesArrayInt = new Integer[GameWorld.ROWS][GameWorld.COLUMNS];
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                NewcirclesArrayInt[i][j] = OldcirclesArrayInt[i][j];
            }
        }

        //FIND NEW ARRAY AFTER MOVE
        addValue(NewcirclesArrayInt,move,isOpponent);

        return NewcirclesArrayInt;
    }
    private static void addValue(Integer[][] NewcirclesArrayInt,Point move, boolean isOpponent){




        if((move.x<0)|(move.y<0)|(move.x>=GameWorld.ROWS)|(move.y>=GameWorld.COLUMNS)){
            return;
        }else if(NewcirclesArrayInt[move.x][move.y]==0){
            if(isOpponent){
                NewcirclesArrayInt[move.x][move.y]--;
            }else{
                NewcirclesArrayInt[move.x][move.y]++;
            }
        }else if(NewcirclesArrayInt[move.x][move.y]==3){
            NewcirclesArrayInt[move.x][move.y]=0;
            addValue(NewcirclesArrayInt,new Point(move.x+1,move.y),isOpponent);
            addValue(NewcirclesArrayInt,new Point(move.x-1,move.y),isOpponent);
            addValue(NewcirclesArrayInt,new Point(move.x,move.y+1),isOpponent);
            addValue(NewcirclesArrayInt,new Point(move.x,move.y-1),isOpponent);
        }else if(NewcirclesArrayInt[move.x][move.y]==-3){
            NewcirclesArrayInt[move.x][move.y]=0;
            addValue(NewcirclesArrayInt,new Point(move.x+1,move.y),isOpponent);
            addValue(NewcirclesArrayInt,new Point(move.x-1,move.y),isOpponent);
            addValue(NewcirclesArrayInt,new Point(move.x,move.y+1),isOpponent);
            addValue(NewcirclesArrayInt,new Point(move.x,move.y-1),isOpponent);
        }else if(NewcirclesArrayInt[move.x][move.y]<0){
            NewcirclesArrayInt[move.x][move.y]--;
            if(!isOpponent) NewcirclesArrayInt[move.x][move.y]*=-1;
        }else if(NewcirclesArrayInt[move.x][move.y]>0){
            NewcirclesArrayInt[move.x][move.y]++;
            if(isOpponent) NewcirclesArrayInt[move.x][move.y]*=-1;
        }

    }

    private static float calculateScore(Integer[][] ciclesArrayInt){
        float score=0;
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                float addScore = 0;
                addScore += ciclesArrayInt[i][i];
                if((j==0)|(j>=GameWorld.COLUMNS)){
                    addScore/=2;
                }
                if((i==0)|(i>=GameWorld.ROWS)){
                    addScore/=2;
                }
                score+=addScore;
            }
        }
        return score;
    }
    public static float calculateScore(CircleController circleController){
        Integer[][] ciclesArrayInt = new Integer[GameWorld.ROWS][GameWorld.COLUMNS];
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                ciclesArrayInt[i][j] = circleController.getCircle(i,j).getValue();
                if(circleController.getCircle(i,j).isOpponent()){
                    ciclesArrayInt[i][j]*=-1;
                }
            }
        }
        return calculateScore(ciclesArrayInt);
    }
    private static String printDoubleArray(Integer[][] ciclesArrayInt){
       String finalString = "\n";
        for(int i=0;i<GameWorld.ROWS;i++){
            String temp = "[";
            for(int j=0;j<GameWorld.COLUMNS;j++){
                temp+=ciclesArrayInt[i][j]+",";
            }
            finalString+="{"+temp+"}\n";
        }
        return finalString;
    }

}
