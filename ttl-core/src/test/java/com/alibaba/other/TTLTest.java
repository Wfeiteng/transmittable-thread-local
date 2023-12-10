package com.alibaba.other;

import com.alibaba.ttl3.TransmittableThreadLocal;
import com.alibaba.ttl3.executor.TtlExecutors;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TTLTest {

    public static ThreadLocal<String> threadLocal = new TransmittableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "A";
        }
    };
    public static ThreadLocal<Integer> threadLocal2 = new TransmittableThreadLocal<Integer>();

    @Test
    public void testTTL() throws Exception {

        threadLocal.set("B");
        threadLocal2.set(999);


        ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));
        executorService.submit(() -> {
                    threadLocal.set("F");
                    threadLocal2.set(888);
                    System.out.println("子线程ThreadLocal：" + threadLocal.get());
                    System.out.println("子线程ThreadLocal2:" + threadLocal2.get());
                }
        ).get();
        Thread.sleep(1000);

       /* threadLocal.set("C");
        System.out.println("父线程修改ThreadLocal为" + threadLocal.get());
        executorService.submit(() -> System.out.println("子线程ThreadLocal：" + threadLocal.get())).get();
        Thread.sleep(1000);

        executorService.submit(() -> {
            threadLocal.set("D");
            System.out.println("子线程修改ThreadLocal为" + threadLocal.get());
        });
        Thread.sleep(1000);

        executorService.submit(() -> System.out.println("子线程ThreadLocal：" + threadLocal.get()));
        Thread.sleep(1000);*/
        while (true) ;
    }
}
