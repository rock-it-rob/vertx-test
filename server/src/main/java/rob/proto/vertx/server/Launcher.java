package rob.proto.vertx.server;

import io.vertx.core.Vertx;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Launcher
{
    private static final String SPRING_XML = "classpath:server-spring.xml";

    public static void main(String[] args)
    {
        try (
                GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(SPRING_XML)
        )
        {
            Vertx vertx = Vertx.vertx();
            ServerVerticle serverVerticle = applicationContext.getBean(ServerVerticle.class);
            vertx.deployVerticle(serverVerticle);
        }
    }
}
