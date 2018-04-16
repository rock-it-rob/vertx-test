package rob.proto.vertx.tcp;

import com.sun.corba.se.spi.activation.Server;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
        NetServer netServer = createServer();
        netServer.connectHandler(this::handleConnection);

        log.info("Starting server");
        netServer.listen();
        log.info("Listening on " + host + ":" + port);
    }

    @Override
    public void stop()
    {
        log.info("Stopping server");
    }

    /**
     * Creates and returns the tcp server (unstarted).
     *
     * @return NetServer
     */
    private NetServer createServer()
    {
        NetServerOptions options = new NetServerOptions().setPort(port).setHost(host);
        return getVertx().createNetServer(options);
    }

    private void handleConnection(NetSocket socket)
    {
        log.info("Handling socket");
        socket.handler(this::handleBuffer);
    }

    private void handleBuffer(Buffer buffer)
    {
        log.info("Received buffer: " + buffer.toString());
    }
}
