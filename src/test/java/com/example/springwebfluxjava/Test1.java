package com.example.springwebfluxjava;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Locale;

@Slf4j
public class Test1 {

    @Test
    void API요청_예외_테스트_sequence안에서_예외가_잡히지않음() {
        UriComponentsBuilder uri = UriComponentsBuilder.newInstance().scheme("https").host("www.naver.com");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        RestTemplate restTemplate = new RestTemplate();

        // todo 왜 just에서 발생한 예외는 리액티브 스트림(sequenec) 내에서 발생한 예외가 아니고 위의 메소드는 리액티브 스트림 내에서 발생한 예외인가!
        // just의 경우, 결과 data를 받아서 mono객체를 생성하므로 아래의 경우 data에 담을 코드 자체가 문제이다. 쉽게 구분하기 제일 아래의 코드로 보면 이해가 쉽다
        // 반면, fromSupplier()의 경우 supplier람는 함수형 인터페이스를 전달 받도록 설계되었다. 즉, 동작을 정의하는 람다 표현식을 전달받아 mono객체 생성시 람다를 실행한다.
        try{
            Mono.just(
                            restTemplate.exchange(uri.build().toUri(), HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class)
                    ).map(response -> {
                        System.out.println(response.getBody());
                        return "hello";
                    })
                    .subscribe(
                            data -> System.out.println("data = " + data),
                            error -> System.out.println("error = " + error),
                            () -> System.out.println("end"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        // 보기 편한 코드
        ResponseEntity<String> result = restTemplate.exchange(uri.build().toUri(), HttpMethod.GET, new HttpEntity<String>(httpHeaders), String.class);
        Mono.just(
                        result
                ).map(response -> {
                    System.out.println(response.getBody());
                    return "hello";
                })
                .subscribe(
                        data -> System.out.println("data = " + data),
                        error -> System.out.println("error = " + error),
                        () -> System.out.println("end"));


    }


    @Test
    void API요청_예외_테스트_sequence안에서_예외가_잡힘() {
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
