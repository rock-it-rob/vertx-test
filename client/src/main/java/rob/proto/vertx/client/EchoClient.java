package rob.proto.vertx.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoClient extends AbstractVerticle
{
    private static final Logger log = LoggerFactory.getLogger(EchoClient.class);

    private final String hostname;
    private final int port;
    private HttpClient client;

    public EchoClient(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;

        log.info("Creating connection to: " + hostname + ":" + port);
    }

    @Override
    public void start()
    {
        HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2)
                .setSsl(false).setUseAlpn(false).setDefaultHost(hostname).setDefaultPort(port);
        client = vertx.createHttpClient(options);
    }

    public void echo(String message)
    {
        client.post("/", this::handleResponse).end(message);
    }

    private void handleResponse(HttpClientResponse response)
    {
        response.bodyHandler(buffer ->
                log.info("Recieved: " + buffer.toString())
        );
    }
}
