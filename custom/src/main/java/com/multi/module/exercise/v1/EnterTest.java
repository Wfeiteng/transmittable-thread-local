package com.multi.module.exercise.v1;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnterTest {
    @Test
    public void test(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadLocal<Object> tl1 = new ThreadLocal<>();
        ThreadLocal<Object> tl2 = new ThreadLocal<>();


// ------------第一次调用 start -------------------
        tl1.set("1111");
        tl2.set("2222");

        executorService.execute(new MyTask(tl1, tl2));

        tl1.remove();
        tl2.remove();
// ------------第一次调用 end -------------------


// ------------第二次调用 start -------------------
        tl1.set("3333");
        tl2.set("4444");
        executorService.execute(new MyTask(tl1, tl2));
        tl1.remove();
        tl2.remove();
// ------------第二次调用 end -------------------

    }
}
