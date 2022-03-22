package testwork.throttling;

import testwork.throttling.config.ThrottlerProperties;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.LinkedList;
import java.util.concurrent.Future;

@EnableConfigurationProperties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {

    @Autowired
    public ThrottlerProperties throttlerProperties;
    @LocalServerPort
    private int port;

    @Test
    public void test() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        int countIp = 10;
        LinkedList<Future<?>> futures = new LinkedList<>();
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

        for (int i = 0; i < countIp; i++) {
            int ip = i;
            Future<?> future = executor.submit(() -> {

                callMaxSuccessNumberTimes(client, ip);

                сallAfterStop(client, ip);

                waitUntilResetCounter();

                successCall(client, ip);

            });
            futures.add(future);
        }

        waitFinishAllCalls(futures);
    }

    private void waitUntilResetCounter() {
        try {
            Thread.sleep(throttlerProperties.getIntervalInMinute() * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void successCall(WebTestClient client, int ip) {
        callRest(client, ip)
                .expectStatus()
                .isOk();
    }

    private void сallAfterStop(WebTestClient client, int ip) {
        callRest(client, ip)
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_GATEWAY);
    }

    private void callMaxSuccessNumberTimes(WebTestClient client, int ip) {
        for (int j = 0; j < throttlerProperties.getMaxCallPerSecond(); j++) {
            successCall(client, ip);
        }
    }

    private void waitFinishAllCalls(LinkedList<Future<?>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("test must not throw exception");
            }
        });
    }

    private WebTestClient.ResponseSpec callRest(WebTestClient client, int ip) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/testUrl")
                        .queryParam("ip", ip)
                        .build())
                .exchange();
    }
}
