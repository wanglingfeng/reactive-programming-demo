package com.study.reactive.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author keleguo
 * @date Created in 2018/5/18
 */
public class ReactorSnippets {

    private static List<String> words = Arrays.asList(
            "the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog");

    @Test
    public void simpleCreation() {
        Flux<String> fewWords = Flux.just("Hello", "World");
        Flux<String> manyWords = Flux.fromIterable(words);

        fewWords.subscribe(System.out::println);
        System.out.println();
        manyWords.subscribe(System.out::println);
    }

    @Test
    public void findingMissingLetter() {
        Flux<String> manyLetters = Flux
                .fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE), (s, count) -> String.format("%2d. %s", count, s));

        manyLetters.subscribe(System.out::println);
    }

    @Test
    public void restoringMissingLetter() {
        Mono<String> missing = Mono.just("s");
        Flux<String> manyLetters = Flux
                .fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .concatWith(missing)
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE), (s, count) -> String.format("%2d. %s", count, s));

        manyLetters.subscribe(System.out::println);
    }

    @Test
    public void shortCircuit() {
        Flux<String> helloPauseWord = Mono
                .just("Hello")
                .concatWith(Mono.just("World").delaySubscriptionMillis(500));

        helloPauseWord.subscribe(System.out::println);
    }

    @Test
    public void blocks() {
        Flux<String> helloPauseWorld = Mono
                .just("Hello")
                .concatWith(Mono.just("World").delaySubscriptionMillis(500));

        helloPauseWorld.toStream().forEach(System.out::println);
    }

    @Test
    public void firstEmitting() {
        Mono<String> a = Mono.just("opps I'm late").delaySubscriptionMillis(450);
        Flux<String> b = Flux.just("let's get", "the party", "started").delayMillis(400);

        Flux.firstEmitting(a, b).toIterable().forEach(System.out::println);
    }
}
