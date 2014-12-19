package com.maurice.virolLibgdx.Networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by maurice on 19/12/14.
 */
public class NetworkManager {
    private int SERVER_PORT = 4451;
    public NetworkManager(){
        // setup a server thread where we wait for incoming connections
        // to the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocketHints hints = new ServerSocketHints();
                ServerSocket server = Gdx.net.newServerSocket(Net.Protocol.TCP, SERVER_PORT, hints);
                // wait for the next client connection
                Socket client = server.accept(null);
                // read message and send it back
                try {
                    String message = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                    Gdx.app.log("PingPongSocketExample", "got client message: " + message);
                    client.getOutputStream().write("PONG\n".getBytes());
                } catch (IOException e) {
                    Gdx.app.log("PingPongSocketExample", "an error occured", e);
                }
            }
        }).start();

        // create the client send a message, then wait for the
        // server to reply
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
