package com.example;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

/** This class represents a web socket client and is implemented as an actor to run
 *  as a separated process with its own message box.
 */
public class WebSocketClientActor extends UntypedAbstractActor {

    /** The web socket connection of the current client. */
    private final WebSocketClientEndpoint webSocketClientEndpoint;
    /** The reference of the sender actor.*/
    private ActorRef senderRef;

    /** Creates the web socket client as an actor.*/
    public WebSocketClientActor() throws URISyntaxException, IOException, DeploymentException {
        this.webSocketClientEndpoint =
                new WebSocketClientEndpoint( new URI( getWebSocketURI() ) );
        this.webSocketClientEndpoint.setOnMessageHandler( message -> this.senderRef.tell( message, getSelf() ));
    }

    /** Is called when a messages is received from another actor.
     *
     * @param message The received message.
     */
    @Override
    public void onReceive(Object message) throws IOException {
        this.senderRef = getSender();
        this.webSocketClientEndpoint.sendMessage( (String) message );
    }

    /** Returns the URI of the server's web socket from the config.properties file.
     *  The key within the config.properties file should be 'web_socket_uri'.
     * @return The URI of the server's web socket from the config.properties file.
     */
    private static String getWebSocketURI() {
        return ResourceBundle.getBundle("config").getString("web_socket_uri");
    }
}