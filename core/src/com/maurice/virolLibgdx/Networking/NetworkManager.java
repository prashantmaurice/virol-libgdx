package com.maurice.virolLibgdx.Networking;

import com.badlogic.gdx.Gdx;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.maurice.virolLibgdx.GameWorld.GameWorld;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by maurice on 19/12/14.
 */
public class NetworkManager {
    private static int SERVER_PORT = 7444;
    private static String SERVER_URL = "http://192.168.0.3";
    com.github.nkzawa.socketio.client.Socket socket;
    private static String TAG = "SOCKETT";
    private int MY_ID;
    private int userId1;
    private int userId2;
    private ArrayList<Integer> playersOnline = new ArrayList<Integer>();
    public NetworkManager(){
        establishSocket();
        Gdx.app.log(TAG, "Socket Failed...!");
    }

    public void establishSocket(){
        socket = null;
        GameWorld.currOnlineState = GameWorld.OnlineState.CONNECTING;
        try {
            socket = IO.socket(SERVER_URL+":"+SERVER_PORT);
            socket.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gdx.app.log(TAG, "Socket connected..!");
                    GameWorld.currOnlineState = GameWorld.OnlineState.CONNECTED;
                }

            }).on("new connection", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject) args[0];
                    Gdx.app.log(TAG, "received" + obj.toString());
                    try {
                        MY_ID = obj.getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("all users", "hi");

                }

            }).on("all users", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONArray obj = (JSONArray) args[0];
//                    playersOnline.clear();
//                    for (int i = 0; i < obj.length(); i++) {
//                        try {
//                            playersOnline.add(obj.getInt(i));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    Gdx.app.log(TAG, "received all users" + obj);
                }
            }).on(com.github.nkzawa.socketio.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gdx.app.log(TAG, "Socket disconnected..!");
                    GameWorld.currOnlineState = GameWorld.OnlineState.DISCONNECTED;
                }

            })
            //PROTOCOL STARTS HERE================================
            .on("waiting for another free user", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    GameWorld.currOnlineState = GameWorld.OnlineState.WAITING_OPPONENT;
                    Gdx.app.log(TAG, "Waiting for opponent:");
                }

            }).on("u wanna be opponent", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        JSONObject request = (JSONObject) args[0];
                        Gdx.app.log(TAG, "received: u wanna be opponent:" + request.toString());
                        JSONObject reply = new JSONObject();
                        if (GameWorld.OnlineState.WAITING_OPPONENT == GameWorld.currOnlineState) {
                            int userId1 = request.getInt("userId1");
                            reply.put("accept", true);
                            reply.put("userId1", userId1);
                            reply.put("userId2", MY_ID);
                            socket.emit("u wanna be opponent", reply);
                            Gdx.app.log(TAG, "sent: u wanna be opponent:" + reply.toString());
                        } else {
                            reply.put("accept", false);//this is very rare
                            socket.emit("u wanna be opponent", reply);
                        }
                        Gdx.app.log(TAG, "Waiting for opponent:");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on("opponent connected", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject command = (JSONObject) args[0];
                    try {
                        userId1 = command.getInt("userId1");
                        userId2 = command.getInt("userId2");

                        GameWorld.currOnlineState = GameWorld.OnlineState.OPPONENT_CONNECTED;
                        Gdx.app.log(TAG, "opponent connected:");
                        sendStartOnlineGameCmd();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on("opponent disconnected", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    GameWorld.currOnlineState = GameWorld.OnlineState.OPPONENT_DISCONNECTED;
                    Gdx.app.log(TAG, "opponent disconnected:");
                    GameWorld.getInstance().opponentDisconnected();
                }

            })
            //GAME COMMANDS HERE
            .on("command", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject command = (JSONObject) args[0];
                    Gdx.app.log(TAG, "server: command:"+command.toString());
                    try {
                        int userId1 = command.getInt("userId1");
                        int userId2 = command.getInt("userId2");

                        JSONObject move = command.getJSONObject("move");
                        String moveType = move.getString("moveType");
                        if (moveType.equals("startOnlineGame")) {
                            GameWorld.getInstance().startOnlineGameMain(userId1==MY_ID);
                        }else if(moveType.equals("move")){
                            int x = move.getInt("x");
                            int y = move.getInt("y");
                            GameWorld.getInstance().onlineGameCommandMove(x,y);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });


            socket.connect();
            Gdx.app.log(TAG, "Socket connecting...");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Gdx.app.log(TAG, "Socket Failed...!");
            GameWorld.currOnlineState = GameWorld.OnlineState.DISCONNECTED;
        }
    }
    public void requestFreeUser(){
        try {
            JSONObject request = new JSONObject();
            request.put("id", MY_ID);
            socket.emit("request free user", request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendStartOnlineGameCmd(){
        JSONObject move = new JSONObject();
        try {
            move.put("moveType","startOnlineGame");
            sendOpponentCommand(move);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendOpponentCommand(JSONObject obj){
        JSONObject request = new JSONObject();
        try {
            request.put("userId1",userId1);
            request.put("userId2",userId2);
            request.put("move",obj);
            socket.emit("command", request);
            Gdx.app.log(TAG, "User: command:"+request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void setMoveServer(int i, int j) {
        JSONObject move = new JSONObject();
        try {
            move.put("moveType","move");
            move.put("x",i);
            move.put("y",j);
            sendOpponentCommand(move);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
