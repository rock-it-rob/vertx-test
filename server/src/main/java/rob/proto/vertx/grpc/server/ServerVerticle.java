package rob.proto.vertx.grpc.server;

import io.grpc.stub.StreamObserver;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rob.proto.vertx.grpc.api.Echo;
import rob.proto.vertx.grpc.api.EchoServiceGrpc;

import java.io.IOException;

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
    public void start() throws IOException
    {
        EchoServiceGrpc.EchoServiceImplBase service = new EchoServiceGrpc.EchoServiceImplBase()
        {
            @Override
            public void echo(Echo.EchoRequest request, StreamObserver<Echo.EchoResponse> responseObserver)
            {
                final String result = "echo: " + request.getMessage();
                Echo.EchoResponse response = Echo.EchoResponse.newBuilder().setMessage(result).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };

        VertxServer server = VertxServerBuilder.forAddress(vertx, host, port)
            .addService(service).build();

        log.info("Starting server");
        server.start();
        log.info("Listening on " + host + ":" + port);
    }

    @Override
    public void stop()
    {
        log.info("Stopping server");
    }
}
