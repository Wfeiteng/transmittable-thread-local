package com.multi.module.exercise.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyTask implements Runnable {
    private final Map<ThreadLocal<Object>, Object> threadLocalValues;

    public MyTask(ThreadLocal<Object>... threadLocals) {
        this.threadLocalValues = new HashMap<>(threadLocals.length);
        capture(threadLocals);
    }

    private void capture(ThreadLocal<Object>[] threadLocals) {
        for (ThreadLocal<Object> threadLocalValue : threadLocals) {
            threadLocalValues.put(threadLocalValue, threadLocalValue.get());
        }
    }

    private Object replay() {
        Map<ThreadLocal<Object>, Object> backup = new HashMap<>();
        for (Map.Entry<ThreadLocal<Object>, Object> entry : threadLocalValues.entrySet()) {
            backup.put(entry.getKey(), entry.getKey().get());
            entry.getKey().set(entry.getValue());
        }
        return backup;
    }

    private void restore(Object obj) {
        Map<ThreadLocal<Object>, Object> backup = (Map<ThreadLocal<Object>, Object>) obj;
        for (Map.Entry<ThreadLocal<Object>, Object> entry : backup.entrySet()) {
            entry.getKey().set(entry.getValue());
        }
    }

    @Override
    public void run() {
        Object backup = replay();
        try {
            doBiz();
        } finally {
            restore(backup);
        }
    }

    private void doBiz() {
        System.out.println("MyTask.run(),the thread name:" + Thread.currentThread().getName() + " the threadLocal value:");
        // 打印父线程提交任务时的ThreadLocal值
        Set<ThreadLocal<Object>> threadLocals = threadLocalValues.keySet();
        for (ThreadLocal<Object> threadLocal : threadLocals) {
            System.out.println(threadLocal.get());
        }
    }


}
