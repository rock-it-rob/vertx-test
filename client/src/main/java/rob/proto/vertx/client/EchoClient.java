package rob.proto.vertx.client;

public interface EchoClient
{
    void echo(String message);

    void shutdown() throws InterruptedException;
}
