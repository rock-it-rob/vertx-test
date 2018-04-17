package rob.proto.vertx.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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

    private void handler(Message<String> message)
    {
        final String content = message.body();
        log.info("Received: " + content);
        message.reply("echo: " + content);
    }
}
