package com.maurice.virolLibgdx.Networking;

import com.badlogic.gdx.Gdx;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by maurice on 19/12/14.
 */
public class NetworkManager {
    private int SERVER_PORT = 9000;
    com.github.nkzawa.socketio.client.Socket socket;
    private int MY_ID;
    private ArrayList<Integer> playersOnline = new ArrayList<Integer>();
    public NetworkManager(){
        establishSocket();
    }

    public void establishSocket(){
        socket = null;
        try {
            socket = IO.socket("http://192.168.0.3:9000");


            socket.on(com.github.nkzawa.socketio.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gdx.app.log("NETWORK", "Socket connected..!");
                }

            }).on("new connection", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject) args[0];
                    Gdx.app.log("NETWORK", "received" + obj.toString());
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
                    JSONArray obj = (JSONArray)args[0];
                    playersOnline.clear();
                    for(int i=0;i<obj.length();i++){
                        try {
                            playersOnline.add(obj.getInt(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Gdx.app.log("NETWORK", "received all users"+obj+"->"+playersOnline.toString());
                }

            }).on(com.github.nkzawa.socketio.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gdx.app.log("NETWORK", "Socket disconnected..!");
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


}
