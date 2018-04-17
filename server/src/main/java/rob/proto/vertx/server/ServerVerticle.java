package rob.proto.vertx.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServerVerticle runs a tcp server
 *
 * @author Rob Benton
 */
public class ServerVerticle extends AbstractVerticle
{
    private static final Logger log = LoggerFactory.getLogger(ServerVerticle.class);

    private final int port;
    private final String host;

    /**
     * Constructs a new instance
     *
     * @param port int
     * @param host String
     */
    public ServerVerticle(int port, String host)
    {
        this.port = port;
        this.host = host;
    }

    @Override
    public void start()
    {
        HttpServerOptions options = new HttpServerOptions().setHost(host).setPort(port);
        HttpServer httpServer = vertx.createHttpServer(options);

        log.info("Starting server");
        httpServer.listen();
        log.info("Listening on " + host + ":" + port);
    }

    @Override
    public void stop()
    {
        log.info("Stopping server");
    }
}
