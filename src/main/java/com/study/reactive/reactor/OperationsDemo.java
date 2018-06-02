package com.study.reactive.reactor;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author keleguo
 * @date Created in 2018/5/21
 */
public class OperationsDemo {

    /**
     * interval方法周期性生成从0开始的的Long。周期从delay之后启动，每隔period时间返回一个加1后的Long。
     * 注意，interval方法返回的Flux运行在另外的线程中，main线程需要休眠或者阻塞之后才能看到周期性的输出。
     *
     * @throws InterruptedException e
     */
    @Test
    public void testInterval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1), Duration.ofSeconds(1)).subscribe(System.out::println);
        Thread.sleep(10000);
    }

    /**
     * defer构造出的Flux流，每次调用subscribe方法时，都会调用Supplier获取Publisher实例作为输入。
     * 如果Supplier每次返回的实例不同，则可以构造出和subscribe次数相关的Flux源数据流。
     * 如果每次都返回相同的实例，则和from(Publisher<? extends T> source)效果一样。
     */
    @Test
    public void testDefer() {
        AtomicInteger subscribeTime = new AtomicInteger(1);

        // 实现效果，返回的数据流为1～5乘以当前subscribe的次数
        Supplier<? extends Publisher<Integer>> supplier = () -> {
            Integer[] array = {1, 2, 3, 4, 5};
            int currentTime = subscribeTime.getAndIncrement();
            for (int i = 0; i< array.length; i++) {
                array[i] *= currentTime;
            }
            return Flux.fromArray(array);
        };

        Flux<Integer> deferedFlux = Flux.defer(supplier);

        subscribe(deferedFlux, subscribeTime);
        subscribe(deferedFlux, subscribeTime);
        subscribe(deferedFlux, subscribeTime);
    }

    private static void subscribe(Flux<Integer> deferedFlux, AtomicInteger subscribeTime) {
        System.out.println("subscribe time is: " + subscribeTime.get());
        deferedFlux.subscribe(System.out::println);
    }
}
