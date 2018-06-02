package com.study.reactive.rxjava;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author keleguo
 * @date Created in 2018/5/16
 */
public class ObservableBuildTests {

    public static void main(String[] args) {
        log("Before");
        // subscribe()实际上阻塞了客户端线程，除非某些操作符需要，RxJava不会隐式地在任何线程池中运行您的代码
        Observable.range(5, 3).subscribe(ObservableBuildTests::log);
        log("After");

        System.out.println();

        Observable<Integer> ints = Observable.create(observableEmitter -> {
            log("Create");
            observableEmitter.onNext(5);
            observableEmitter.onNext(6);
            observableEmitter.onNext(7);
            observableEmitter.onComplete();
            log("Completed");
        });
        log("Starting");
        ints.subscribe(i -> log("Element: " + i));
        log("Exit");

        System.out.println();

        // 每次调用subscribe()时，都会执行create()中的订阅处理程序
        // 如果您想避免为每个订阅者调用create()，并且简单地重用已经计算过的事件，使用cache()操作符
        Observable<Integer> ints2 = Observable.create(subscriber -> {
            log("Create");
            subscriber.onNext(42);
            subscriber.onComplete();
        });
        log("Starting");
        ints2.subscribe(i -> log("Element A: " + i));
        ints2.subscribe(i -> log("Element B: " + i));
        log("Exit");

        System.out.println();

        /*Observable<BigInteger> naturalNumbers = Observable.create(subscriber -> {
            Runnable r = () -> {
                BigInteger i = BigInteger.ZERO;
                while (!subscriber.isDisposed()) {
                    subscriber.onNext(i);
                    i = i.add(BigInteger.ONE);
                }
            };
            new Thread(r).start();
        });
        log("Starting");
        naturalNumbers.subscribe(ObservableBuildTests::log);*/

        System.out.println();

        // time()即在一定延迟之后，发送0L，而interval()就是以规定的延迟为循环，从0L开始发射递增数字
        Observable.timer(1, TimeUnit.SECONDS).subscribe(ObservableBuildTests::log);
        Observable.interval(1_000_000 / 60, TimeUnit.MICROSECONDS).subscribe(ObservableBuildTests::log);
    }

    private static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
