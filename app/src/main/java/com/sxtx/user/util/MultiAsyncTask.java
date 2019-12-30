package com.sxtx.user.util;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.SoftReference;
import java.util.ArrayDeque;

/**
 * 用于执行多个异步任务，并且在所有任务完成后统一回调的任务类（Java 8 使用编程效率更佳）
 * 使用时先实例化该类，传入监听类，然后调用 addTask() 类添加任务，最后执行 execute() 方法。
 * 支持超时回调，默认超时时间5000毫秒
 */
public class MultiAsyncTask implements OnSingleTaskFinishListener {

    private ArrayDeque<SoftReference<AsyncTask>> taskQueue = new ArrayDeque<>();

    private InvokeMode invokeMode;

    private OnTasksFinishListener listener;

    private final Object counterLock = new Object();

    private final Object executeLock = new Object();

    private int finishedTaskNumber = 0;

    private int timeOut =10000;

    private boolean isExecuting = false;

    public MultiAsyncTask(InvokeMode invokeMode, OnTasksFinishListener listener) {
        this.invokeMode = invokeMode;
        this.listener = listener;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public void onTaskFinish() {
        synchronized (counterLock){
            switch (invokeMode){
                case SYNC:
                    if(taskQueue.isEmpty()){
                        finish();
                    }else{
                        SoftReference<AsyncTask> task = taskQueue.pollFirst();
                        ImageUrlRequestUtil.Companion.getSingleExecutorService().execute(task.get());
                    }
                    break;
                case ASYNC:
                    finishedTaskNumber ++;
                    if(finishedTaskNumber == taskQueue.size()){
                       finish();
                    }
                    break;
            }
        }
    }

    @Override
    public void onTimeOut() {
        synchronized (executeLock) {
            if (isExecuting) {
                isExecuting = false;
                try {
                    Class.forName("android.os.Build");
                    if (Build.VERSION.SDK_INT != 0) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onTimeOut();
                            }
                        });
                        return;
                    }
                } catch (ClassNotFoundException ignored) {
                }
                listener.onTimeOut();
            }
        }
    }

    /**
     * 清除队列，防止内存泄漏
     */
    public void stop(){
        for (SoftReference<AsyncTask> task:taskQueue){
            task.get().finishTask();
        }
        taskQueue.clear();

    }

    private void finish(){
        try{
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0)
            {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinish();
                    }
                });
                return;
            }
        } catch (ClassNotFoundException ignored){
        }
        listener.onFinish();
        isExecuting = false;
    }

    public void addTask(AsyncTask task){
        SoftReference<AsyncTask> softTask=new SoftReference<>(task);
        //执行任务时不允许中途添加任务
        if (!isExecuting){
            softTask.get().setListener(this);
            softTask.get().setTimeOut(timeOut);
            taskQueue.add(softTask);
        }
    }

    public void execute(){
        synchronized (executeLock){
            isExecuting = true;
            finishedTaskNumber = 0;
            if(taskQueue.isEmpty()){
                return;
            }
            switch (invokeMode){
                case SYNC:
                     SoftReference<AsyncTask> task = taskQueue.pollFirst();
                     ImageUrlRequestUtil.Companion.getSingleExecutorService().execute(task.get());
                     break;
                case ASYNC:
                    for(SoftReference<AsyncTask> task1 : taskQueue){
                        ImageUrlRequestUtil.Companion.getSingleExecutorService().execute(task1.get());
                     }
                    break;
            }
        }
    }

    /**
     * 单个异步任务类，需继承后实现 taskRun() 方法，并且需要在异步任务完成后调用 finishTask()， 否则不会被认为
     * 任务已完成，当做超时处理
     */

    public abstract static class AsyncTask implements Runnable{
        private OnSingleTaskFinishListener listener;

        private int timeOut = -1;

        private boolean isTaskFinished = false;

        public void setListener(OnSingleTaskFinishListener listener){
            this.listener = listener;
        }

        public void setTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }

        @Override
        public void run() {

            if (taskRun()) {
                if (timeOut != -1) {
              /*      try {
                        Thread.sleep(timeOut);
                        if(!isTaskFinished){
                            listener.onTimeOut();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                }
            }else {
                listener.onTimeOut();
            }

        }

        public abstract boolean taskRun();

        /**
         * 完成异步任务时调用该方法
         */
        public void finishTask(){
            isTaskFinished = true;
            if(listener != null){
                listener.onTaskFinish();
            }
        }
    }

    /**
     * 执行模式，同步与异步
     */
    public enum InvokeMode{
        SYNC, ASYNC
    }

    /**
     * 所有任务执行完成后回调
     */
    public interface OnTasksFinishListener{
        void onFinish();

        void onTimeOut();
    }
}

interface OnSingleTaskFinishListener{
    void onTaskFinish();

    void onTimeOut();
}