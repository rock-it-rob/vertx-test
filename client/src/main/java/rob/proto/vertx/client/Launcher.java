package rob.proto.vertx.client;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Launcher
{
    private static final Logger log = LoggerFactory.getLogger(Launcher.class);
    private static final String SPRING_XML = "classpath:client-spring.xml";
    private static final int CALLS = 100;

    public static void main(String[] args) throws InterruptedException
    {
        try (
                GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(SPRING_XML)
        )
        {
            EchoClient echoClient = applicationContext.getBean(EchoClient.class);
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(echoClient);
            Thread.sleep(2000);
            for (int i = 0; i < CALLS; ++i)
            {
                final String message = "Sending call " + i;
                System.out.println("Sending message: " + message);
                echoClient.echo(message);
            }
        }
    }
}
