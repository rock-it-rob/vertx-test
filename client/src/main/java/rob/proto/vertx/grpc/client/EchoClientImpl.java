package rob.proto.vertx.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import rob.proto.vertx.grpc.api.Echo;
import rob.proto.vertx.grpc.api.EchoServiceGrpc;

import java.util.concurrent.TimeUnit;

public class EchoClientImpl implements EchoClient
{
    private final String hostname;
    private final int port;
    private final ManagedChannel channel;
    private final EchoServiceGrpc.EchoServiceStub stub;

    public EchoClientImpl(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
        channel = ManagedChannelBuilder.forAddress(this.hostname, this.port).build();
        stub = EchoServiceGrpc.newStub(channel);
    }

    @Override
    public void shutdown() throws InterruptedException
    {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    @Override
    public void echo(String message)
    {
        Echo.EchoRequest request = Echo.EchoRequest.newBuilder().setMessage(message).build();
        stub.echo(request, new StreamObserver<Echo.EchoResponse>()
        {
            @Override
            public void onNext(Echo.EchoResponse echoResponse)
            {
                System.out.println("Received response: " + echoResponse.getMessage());
            }

            @Override
            public void onError(Throwable throwable)
            {
                throw new RuntimeException(throwable);
            }

            @Override
            public void onCompleted()
            {

            }
        });
    }
}
