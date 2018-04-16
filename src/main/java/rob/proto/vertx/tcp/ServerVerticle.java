package rob.proto.vertx.tcp;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServerVerticle runs a tcp server
 * @author Rob Benton
 */
public class ServerVerticle extends AbstractVerticle
{
    private static final Logger log = LoggerFactory.getLogger(ServerVerticle.class);

    @Override
    public void start()
    {
        log.info("Starting server");
    }

    @Override
    public void stop()
    {
        log.info("Stopping server");
    }
}
