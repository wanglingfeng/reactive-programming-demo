package com.study.reactive.rxjava;

import com.study.reactive.reactor.OperationsDemo;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 指定线程:
 * observerOn(Schedulers) 指定观察者Observer在哪个线程执行
 * subscribeOn(Scheduler) 指定被观察者Observable在哪个线程执行
 *
 * @author <a href="mailto:pushu@2dfire.com">pushu</a>
 * @since 2018/6/22
 */
public class RxJavaThreadTargetOnTest {

    /**
     * subscribeOn指定生产者线程，消费者执行线程和生产者执行线程为同一线程
     * observeOn指定消费者线程，消费者执行线程和生产者执行线程为不同线程
     *
     * @throws InterruptedException e
     */
    @Test
    public void subscribeOnAndObserveOnThread() throws InterruptedException {
        RxJavaThreadTargetOnTest r = new RxJavaThreadTargetOnTest();
        System.out.println("Start");
        r.scheduler();
        System.out.println("End");
        Thread.sleep(3000);
        System.out.println("Sleep End");
    }

    private void scheduler() {
        Observable<String> observable = Observable.create(observableEmitter -> {
            System.out.println("Producer: " + Thread.currentThread().getName());
            observableEmitter.onNext("Haha");
            observableEmitter.onComplete();
        });
        observable
                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
                .subscribe(s -> {
            System.out.println("Receiver: " + Thread.currentThread().getName());
            System.out.println("receive data:" + s);
        });
    }

    @Test
    public void subscribeUseSubThreadToSendValueToObservable() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(1);

        service.execute(() -> {
            System.out.println("current:" + Thread.currentThread().getName());

            Observable.just("aa").delay(100, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.from(service))
                    .subscribeOn(Schedulers.from(service))
                    .subscribe(s -> {
                        System.out.println(s);
                        System.out.println("test: " + Thread.currentThread().getName());
                    });

            System.out.println("123123");

            for (;;) {

            }
        });

        Thread.sleep(10 * 1000);
    }
}