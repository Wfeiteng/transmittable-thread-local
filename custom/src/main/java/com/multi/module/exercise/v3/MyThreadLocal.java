package com.multi.module.exercise.v3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyThreadLocal<T> extends InheritableThreadLocal<T> {
    private static MyThreadLocal<HashSet<MyThreadLocal<Object>>> holder =
            new MyThreadLocal<HashSet<MyThreadLocal<Object>>>() {
                @Override
                protected HashSet<MyThreadLocal<Object>> initialValue() {
                    return new HashSet<>();
                }
            };

    private void addThisToHolder() {
        if (!this.holder.get().contains(this)) {
            this.holder.get().add((MyThreadLocal<Object>) this);
        }
    }

    private void removeThisFromHolder() {
        holder.get().remove(this);
    }

    @Override
    public void set(T value) {
        super.set(value);
        addThisToHolder();
    }

    @Override
    public void remove() {
        super.remove();
        removeThisFromHolder();
    }

    // todo未清除当前线程的上下文，保持回放一致。简化版。
    public static class DataTransmit {
        public static Map<ThreadLocal<Object>, Object> capture() {
            Map<ThreadLocal<Object>, Object> threadLocalValues = new HashMap<>();
            for (MyThreadLocal<Object> threadLocal : holder.get()) {
                threadLocalValues.put(threadLocal, threadLocal.get());
            }
            return threadLocalValues;
        }

        public static Object replayAndBackup(Object captureValue) {
            Map<ThreadLocal<Object>, Object> backup = new HashMap<>();
            // 将当前子线程内容备份backup
            for (MyThreadLocal<Object> objectMyThreadLocal : holder.get()) {
                backup.put(objectMyThreadLocal, objectMyThreadLocal.get());
            }
            Map<ThreadLocal<Object>, Object> captures = (Map<ThreadLocal<Object>, Object>) captureValue;
            // 重放父线程内容
            for (Map.Entry<ThreadLocal<Object>, Object> entry : captures.entrySet()) {
                ThreadLocal<Object> threadLocal = entry.getKey();
                threadLocal.set(entry.getValue());
            }
            return backup;
        }

        public static void restore(Object obj) {
            Map<ThreadLocal<Object>, Object> backup = (Map<ThreadLocal<Object>, Object>) obj;
            for (Map.Entry<ThreadLocal<Object>, Object> entry : backup.entrySet()) {
                ThreadLocal<Object> threadLocal = entry.getKey();
                threadLocal.set(entry.getValue());
            }
        }
    }
}
