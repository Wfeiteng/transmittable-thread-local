package com.multi.module.exercise.v31;

import com.multi.module.exercise.v3.MyTask;
import com.multi.module.exercise.v3.MyThreadLocal;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntryTest {
    @Test
    public void test() throws InterruptedException {
        ExecutorServiceWraper executorService = new ExecutorServiceWraper(Executors.newFixedThreadPool(1));
        ThreadLocal<Object> tl1 = new MyThreadLocal<>();
        ThreadLocal<Object> tl2 = new MyThreadLocal<>();


// ------------第一次调用 start -------------------
        tl1.set("1111");
        tl2.set("2222");

        executorService.execute(() -> {
            tl1.set("aaaa");
            tl2.set("bbbb");
            // do biz
            System.out.println(Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        });
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        tl1.remove();
        tl2.remove();
// ------------第一次调用 end -------------------


// ------------第二次调用 start -------------------
        tl1.set("3333");
        tl2.set("4444");

        executorService.execute(() -> {
            // do biz
            tl1.set("ffff");
            tl2.set("gggg");
            System.out.println(Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        });
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        tl1.remove();
        tl2.remove();
// ------------第二次调用 end -------------------
    }
}
