package com.example.springwebfluxjava;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class Test2 {

    @Test
    void test1() {
        Flux.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .filter(number -> number % 2 == 0) // filter의 결과가 true인 값들이 downstream으로 전달된다.
                .subscribe(System.out::println);


        Flux.concat(
                        Flux.just("Venus"),
                        Flux.just("Earth")
                ).collectList()
                .subscribe(System.out::println);
    }
}
