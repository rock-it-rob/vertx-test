package rob.proto.vertx.grpc.client;

public interface EchoClient
{
    void echo(String message);

    void shutdown() throws InterruptedException;
}
