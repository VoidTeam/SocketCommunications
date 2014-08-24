package net.voidteam.networking.modules;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by Robby Duke on 8/23/14
 * Copyright (c) 2014
 */
public abstract class JavaModule extends WebSocketServer {
    public JavaModule(InetSocketAddress address) {
        super(address);
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onOpen(WebSocket webSocket, ClientHandshake clientHandshake);

    public abstract void onClose(WebSocket webSocket, int i, String s, boolean b);

    public abstract void onError(WebSocket webSocket, Exception e);

    public abstract void onMessage(WebSocket webSocket, String s);

    public void sendToAll(String text) {
        Collection<WebSocket> con = connections();
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }
}
