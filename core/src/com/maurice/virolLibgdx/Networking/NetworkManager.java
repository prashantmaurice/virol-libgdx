package com.maurice.virolLibgdx.Networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


/**
 * Created by maurice on 19/12/14.
 */
public class NetworkManager {
    private int SERVER_PORT = 9000;
    com.github.nkzawa.socketio.client.Socket socket;
    public NetworkManager(){
        // setup a server thread where we wait for incoming connections
        // to the server
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ServerSocketHints hints = new ServerSocketHints();
//                ServerSocket server = Gdx.net.newServerSocket(Net.Protocol.TCP, SERVER_PORT, hints);
//                // wait for the next client connection
//                Socket client = server.accept(null);
//                // read message and send it back
//                try {
//                    String message = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
//                    Gdx.app.log("PingPongSocketExample", "got client message: " + message);
//                    client.getOutputStream().write("PONG\n".getBytes());
//                } catch (IOException e) {
//                    Gdx.app.log("PingPongSocketExample", "an error occured", e);
//                }
//            }
//        }).start();

        // create the client send a message, then wait for the
        // server to reply
//        SocketHints hints = new SocketHints();
//        final Socket client = Gdx.net.newClientSocket(Net.Protocol.TCP, "192.168.0.3", SERVER_PORT, hints);
//        try {
//            client.getOutputStream().write("PING\n".getBytes());
//
//            String response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
//            Gdx.app.log("PingPongSocketExample", "got server message: " + response);
//        } catch (IOException e) {
//            Gdx.app.log("PingPongSocketExample", "an error occured", e);
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String response = null;
//                    InputStreamReader reader = new InputStreamReader(client.getInputStream());
//                    response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
//                    Gdx.app.log("PingPongSocketExample", "got server message: " + response);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


        //====================================================


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
    //                socket.emit("foo", "hi");
    //                socket.disconnect();
                }

            }).on("new connection", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject)args[0];
                    Gdx.app.log("NETWORK", "received"+obj.toString());

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


    public void getAllUsersInWifi(){
        SocketHints hints = new SocketHints();
        Socket client = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", SERVER_PORT, hints);
        try {
            client.getOutputStream().write("PING\n".getBytes());
            String response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Gdx.app.log("PingPongSocketExample", "got server message: " + response);
        } catch (IOException e) {
            Gdx.app.log("PingPongSocketExample", "an error occured", e);
        }
    }
}
