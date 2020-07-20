package com.example.end2end;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class BaseTest {

    private static ActorSystem actorSystem;

    @BeforeClass
    public static void setUp() {
        actorSystem = ActorSystem.create("web_socket_test_automation_system");
    }

    @AfterClass
    public static void tearDown() {
        actorSystem.terminate();
        actorSystem = null;
    }

    @Test
    public void echoTest() {
        new TestKit(actorSystem) {
            {
                final ActorRef clientReference = actorSystem.actorOf( Props.create(WebSocketClientActor.class) );
                final String msg = "Hello Server";
                // Stimulation: Send echo to server's web socket
                clientReference.tell(msg, getRef() );
                // Tests {
                // The expected message should be equal to msg
                expectMsg(Duration.ofSeconds(2), msg);
                // No more messages are expected.
                expectNoMessage();
                // }
            }
        };
    }
}
