package com.example.springwebfluxjava;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Locale;

@Slf4j
public class Test1 {

    @Test
    void tset2() {
        UriComponentsBuilder uri = UriComponentsBuilder.newInstance().scheme("https").host("www.naver.com");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        RestTemplate restTemplate = new RestTemplate();

        Mono.fromSupplier(() ->
                        restTemplate.exchange(uri.build().toUri(), HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class)
                )
                .map(response -> {
                    System.out.println(response.getBody());
                    return "hello";
                })
                .subscribe(
                        data -> System.out.println("data = " + data),
                        error -> System.out.println("error = " + error),
                        () -> System.out.println("end")
                );
        // todo 왜 just에서 발생한 예외는 리액티브 스트림 내에서 발생한 예외가 아니고 위의 메소드는 리액티브 스트림 내에서 발생한 예외인가!
//        Mono.just(
//                        restTemplate.exchange(uri.build().toUri(), HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class)
//                ).map(response -> {
//                    System.out.println(response.getBody());
//                    return "hello";
//                })
//                .subscribe(
//                        data -> System.out.println("data = " + data),
//                        error -> System.out.println("error = " + error),
//                        () -> System.out.println("end"));


    }

    @Test
    void test3() {
        Mono.just("1")
                .handle((value, sink) -> {
                    // 어떤 조건에서 의도적으로 예외 발생
                    sink.error(new RuntimeException("map 단계에서 발생한 예외"));
                })
                .subscribe(
                        data -> System.out.println("data = " + data),
                        error -> System.out.println(error.getMessage()),
                        () -> System.out.println("complete")
                );
    }

    @Test
    public void test1() {
        Flux.just("A", "B", "C")
                .map(data -> data.toLowerCase(Locale.ROOT))
                .subscribe(System.out::println);

    }
}
