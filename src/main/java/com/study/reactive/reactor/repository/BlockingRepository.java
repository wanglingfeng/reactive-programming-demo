package com.study.reactive.reactor.repository;

/**
 * @author keleguo
 * @date Created in 2018/5/19
 */
public interface BlockingRepository<T> {

    void save(T value);

    T findFirst();

    Iterable<T> findAll();

    T findById(String id);
}
