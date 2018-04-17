package rob.proto.vertx.server;

import com.google.protobuf.InvalidProtocolBufferException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import rob.proto.vertx.api.Echo;

public class WorkerVerticle extends AbstractVerticle
{
    private static final Logger log = LoggerFactory.getLogger(WorkerVerticle.class);

    @Value("${event.bus.request}")
    private String address;

    @Override
    public void start()
    {
        vertx.eventBus().consumer(address, this::handler);
    }

    private void handler(Message<Buffer> message)
    {
        Echo.EchoRequest echoRequest = null;
        try
        {
            echoRequest = Echo.EchoRequest.parseFrom(message.body().getBytes());
        } catch (InvalidProtocolBufferException e)
        {
            message.fail(1, e.getMessage());
        }

        Echo.EchoResponse echoResponse = Echo.EchoResponse.newBuilder()
            .setMessage("echo: " + echoRequest.getMessage()).build();
        log.info("Received: " + echoRequest);
        message.reply(Buffer.buffer(echoResponse.toByteArray()));
    }
}
