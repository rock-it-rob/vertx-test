package rob.proto.vertx.client;

import com.google.protobuf.InvalidProtocolBufferException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rob.proto.vertx.api.Echo;

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
        Echo.EchoRequest echoRequest = makeRequest(message);
        //log.debug("request=" + echoRequest);
        log.debug("EchoRequest=" + formatEchoRequest(echoRequest));
        Buffer buffer = Buffer.buffer(echoRequest.toByteArray());
        //log.info("Sending: " + buffer);
        client.post("/", this::handleResponse).end(buffer);
    }

    private Echo.EchoRequest makeRequest(String s)
    {
        return Echo.EchoRequest.newBuilder().setMessage(s).build();
    }

    private void handleResponse(HttpClientResponse response)
    {
        response.bodyHandler(buffer ->
            {
                try
                {
                    Echo.EchoResponse echoResponse = Echo.EchoResponse.parseFrom(
                        buffer.getBytes()
                    );
                    log.info("Recieved: " + echoResponse);
                    log.debug("EchoResponse=" + formatEchoResponse(echoResponse));
                }
                catch (InvalidProtocolBufferException e)
                {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    private String formatEchoRequest(Echo.EchoRequest request)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : request.toByteArray())
        {
            sb.append((int) b);
            sb.append(' ');
        }

        return sb.toString();
    }

    private String formatEchoResponse(Echo.EchoResponse response)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : response.toByteArray())
        {
            sb.append((int) b);
            sb.append(' ');
        }

        return sb.toString();
    }
}
