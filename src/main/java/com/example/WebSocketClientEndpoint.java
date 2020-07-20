package com.example;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.ClientEndpoint;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.util.ResourceBundle;

@ClientEndpoint
public class WebSocketClientEndpoint {
    /** The session between the current client endpoint and the server endpoint. */
    private final Session session;
    /** A message handler to handle incoming messages. */
    private MessageHandler.Whole<String> messageHandler;

    public WebSocketClientEndpoint(URI serverEndpointURI) throws IOException, DeploymentException {
        this.session = ClientManager.createClient().connectToServer(this, serverEndpointURI );
    }

    /** The onMessage method annotated with @OnMessage is called each time a message is received from server.
     *
     * @param message The message received from server.
     * @param session The current session between the current client endpoint and the server endpoint.
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (this.messageHandler != null)
            this.messageHandler.onMessage(message);
    }

    /** Sends given message to the server endpoint.
     *
     * @param message The message to be send to the server endpoint.
     */
    public void sendMessage(String message) throws IOException {
            this.session.getBasicRemote().sendText(message);
    }

    /** Sets the message handler.
     *
     * @param handler The message handler to handle incoming messages.
     */
    public void setOnMessageHandler(MessageHandler.Whole<String> handler) {
        this.messageHandler = handler;
    }
}
