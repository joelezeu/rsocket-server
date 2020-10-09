package com.example.rsocketserver;

import com.example.rsocketserver.domain.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class RsocketServerApplicationTests {

    //	@Test
//	void contextLoads() {
//	}
//
    private static RSocketRequester rSocketRequester;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder, @LocalRSocketServerPort Integer port, @Autowired RSocketStrategies strategies) {
        //Connect to the RSocket Endpoint Via TCP
        rSocketRequester = builder.connectTcp("localhost", port).block();
    }

    @Test
    public void testRequestResponse() {
        Mono<Message> response = rSocketRequester.route("request-response")
                .data(new com.example.rsocketserver.domain.Message("Joel"))
                .retrieveMono(com.example.rsocketserver.domain.Message.class);

        //Using Step Verifer to check the response from the reactor package.
        StepVerifier.create(response)
                .consumeNextWith(message -> Assertions.assertEquals(message.getMessage(), "My name is Joel"))
                .verifyComplete();

    }

    @Test
    public void testRequestStream() {
        Flux<Message> stream = rSocketRequester.route("request-stream").data(new Message("Joel")).retrieveFlux(Message.class);

        StepVerifier.create(stream).consumeNextWith(message -> Assertions.assertEquals(message.getMessage(), "My name is Joel 0 times")).thenCancel().verify();
    }

}
