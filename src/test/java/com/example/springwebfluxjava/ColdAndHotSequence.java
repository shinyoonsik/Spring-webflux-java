package com.example.springwebfluxjava;

import ch.qos.logback.core.util.TimeUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ColdAndHotSequence {

    @Test
    void coldSequence_test() {
        // cold sequence: subscriber가 구독할 때마다 새로운 데이터 스트림을 생성
        Flux<Integer> coldSequence = Flux.just(1, 2, 3, 4);

        coldSequence.subscribe(data -> System.out.println("A. data = " + data));
        coldSequence.subscribe(data -> System.out.println("B. data = " + data));
    }

    @Test
    void hotSequence_test() throws InterruptedException {
        // hot sequence: subscriber가 구독할 때마다 새로운 스트림이 생성되는 것이 아닌 구독한 시점부터 emit된 데이터를 수신하는 시퀀스. 즉 subscriber들은 sequence를 공유한다
        Flux<String> hotSequence = Flux.fromStream(Stream.of("singer A", "singer B", "singer C", "singer D", "singer E", "singer F", "singer G"))
                .delayElements(Duration.ofSeconds(1))
                .share();

        hotSequence.subscribe(data -> System.out.println("A. data = " + data));

        Thread.sleep(2500);

        hotSequence.subscribe(data -> System.out.println("B. data = " + data));

        Thread.sleep(8000);
    }
}
