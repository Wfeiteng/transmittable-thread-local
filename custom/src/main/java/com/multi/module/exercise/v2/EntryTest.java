package com.multi.module.exercise.v2;

import com.multi.module.exercise.v2.MyTask;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntryTest {
    @Test
    public void test() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadLocal<Object> tl1 = new ThreadLocal<>();
        ThreadLocal<Object> tl2 = new ThreadLocal<>();

// ------------第一次调用 start -------------------
        tl1.set("1111");
        tl2.set("2222");

        executorService.execute(new MyTask(() -> {
            System.out.println("Task:" + Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        }, tl1, tl2));
        Thread.sleep(1000);
        tl1.remove();
        tl2.remove();
// ------------第一次调用 end -------------------

// ------------第二次调用 start -------------------
        tl1.set("3333");
        tl2.set("4444");
        executorService.execute(new MyTask(() -> {
            tl1.set("5555");
            System.out.println("Task:" + Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        }, tl1, tl2));
        Thread.sleep(2000);
        System.out.println("Task:" + Thread.currentThread().getName() + " tl1:" + tl1.get() + " tl2:" + tl2.get());
        tl1.remove();
        tl2.remove();
// ------------第二次调用 end -------------------
    }
}
