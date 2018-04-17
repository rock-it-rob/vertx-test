package rob.proto.vertx.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

    @Autowired
    private WorkerVerticle workerVerticle;

    @Value("${event.bus.request}")
    private String requestBusAddress;

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
        // Deploy the worker verticle.
        DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle(workerVerticle, deploymentOptions);

        // Create the http server.
        HttpServerOptions options = new HttpServerOptions().setHost(host).setPort(port);
        HttpServer httpServer = vertx.createHttpServer(options);

        // Set request handler
        httpServer.requestHandler(this::handleServerRequest);

        log.info("Starting server");
        httpServer.listen();
        log.info("Listening on " + host + ":" + port);
    }

    @Override
    public void stop()
    {
        log.info("Stopping server");
    }

    private void handleServerRequest(HttpServerRequest request)
    {
        request.bodyHandler(buffer -> {
            vertx.eventBus().<Buffer>send(requestBusAddress, buffer,
                reply -> {
                    if (reply.succeeded())
                    {
                        request.response().end(reply.result().body());
                    } else
                    {
                        final String cause = reply.cause().getMessage();
                        log.error(cause);
                        request.response().setStatusCode(500).setStatusMessage(cause).end();
                    }
                });
        });
    }
}
