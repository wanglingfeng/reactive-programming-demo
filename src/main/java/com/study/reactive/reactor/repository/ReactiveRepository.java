package com.study.reactive.reactor.repository;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author keleguo
 * @date Created in 2018/5/18
 */
public interface ReactiveRepository<T> {

    Mono<Void> save(Publisher<T> publisher);

    Mono<T> findFirst();

    Flux<T> findAll();

    Mono<T> findById(String id);
}
