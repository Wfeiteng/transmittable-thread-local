package com.multi.module.exercise.v3;

public class MyTask implements Runnable {
    private Runnable bizTask;
    private Object captureValue;

    public MyTask(Runnable bizTask) {
        this.bizTask = bizTask;
        captureValue = MyThreadLocal.DataTransmit.capture();
    }


    @Override
    public void run() {
        Object backup = MyThreadLocal.DataTransmit.replayAndBackup(captureValue);
        try {
            bizTask.run();
        } finally {
            MyThreadLocal.DataTransmit.restore(backup);
        }

    }
}
