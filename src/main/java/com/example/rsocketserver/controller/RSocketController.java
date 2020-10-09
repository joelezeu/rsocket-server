package com.example.rsocketserver.controller;

import com.example.rsocketserver.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Controller
public class RSocketController {

    @MessageMapping("request-response")
    Mono<Message> requestResponse(final Message message) {
        log.info("Recieved Message {} ", message);

        return Mono.just(new Message("My name is " + message.getMessage()));
    }

    @MessageMapping("request-stream")
    Flux<Message> stream(final Message message) {
        log.info("Recieved stream request: {} ", message);

        return Flux.interval(Duration.ofSeconds(1)).map(i -> new Message("My name is " + message.getMessage() + " " + i + " times")).log();
    }
}
