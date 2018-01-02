package com.scott.transer.task;

import android.os.Looper;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.utils.Debugger;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:
 *      如果实现其他的传输器需要继承自该类
 * </p>
 */
public abstract class BaseTaskHandler implements ITaskHandler {

    protected ITaskHandlerListenner mListenner;
    private volatile boolean isExit = false;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private ThreadPoolExecutor mTaskHandleThreadPool;
    private volatile Task mTask;
    private HandleRunnable mHandleRunnable;
    private final long  MAX_DELAY_TIME = 1000;
    private final String TAG = BaseTaskHandler.class.getSimpleName();

    private long mLastCompleteLength = 0;
    private StateRunnable mStateRunnable;
    private Thread mStateThread;

    public BaseTaskHandler() {
        mStateRunnable = new StateRunnable();
        mHandleRunnable = new HandleRunnable();
    }

    @Override
    public void setHandlerListenner(ITaskHandlerListenner l) {
        mListenner = l;
    }

    @Override
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    @Override
    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    @Override
    public void setThreadPool(ThreadPoolExecutor threadPool) {
        mTaskHandleThreadPool = threadPool;
    }

    @Override
    public void setState(int state) { //user call
        _internalSetState(state);
    }

    private void _internalSetState(int state) {
        switch (state) {
            case TaskState.STATE_START:
                start();
                break;
            case TaskState.STATE_STOP:
                stop();
                break;
        }
    }

    //判断一片是否发送或接受成功
    protected  abstract boolean isPiceSuccessful();

    //判断任务是否成功
    protected abstract boolean isSuccessful();

    //从数据源中读取一片
    protected abstract byte[] readPice(Task task) throws Exception;

    //写入一片到目标
    protected abstract void writePice(byte[] datas,Task task) throws Exception;

    //传输开始前
    protected abstract void prepare(ITask task) throws Exception;

    //当前这片从数据源中实际读取的大小
    protected abstract int getPiceRealSize();

    protected  abstract long fileSize();


    private void handle(ITask task) throws Exception {

        mLastCompleteLength = task.getCompleteLength();

        //开始任务前准备任务数据，初始化源数据流
        prepare(task);

        if(fileSize() == 0) {
            isExit = true;
            return;
        }
        //获取到的源数据大小设置到task
        mTask.setLength(fileSize());
        mListenner.onStart(mTask);
        Debugger.error(TAG,"start ============= length = " + task.getLength() + "" +
                ",completeLength = " + task.getCompleteLength() + ",startOffset = " + task.getStartOffset() + ",endOffset = " + task.getEndOffset());

        while (!isExit) {

            byte[] datas = readPice((Task) task); // 从源中读取一片数据
            int piceSize = getPiceRealSize(); //获取当前读取一片的实际大小

            //如果读取到源数据的末尾
            if(piceSize == -1 || piceSize == 0) {
                mTask.setCompleteLength(mTask.getLength());
                isExit = true;
                break;
            }
            //设置读取的结束偏移量
            ((Task) task).setEndOffset(task.getStartOffset() + piceSize);
            writePice(datas,(Task) task); //写入实际读入的大小
            mTask.setCompleteLength(mTask.getEndOffset());
            mTask.setStartOffset(mTask.getEndOffset());
            Debugger.info(TAG,"length = " + task.getLength() + "" +
                    ",completeLength = " + task.getCompleteLength() + ",startOffset = " + task.getStartOffset() + ",endOffset = " + task.getEndOffset());

            if(isPiceSuccessful()) { //判断一片是否成功
                mListenner.onPiceSuccessful(mTask);
            } else {
                mTask.setState(TaskState.STATE_ERROR);
                mListenner.onError(TaskErrorCode.ERROR_PICE,mTask);
                isExit = true;
                break;
            }
        }

        if(!isSuccessful()) { //判断整个任务是否成功
            mTask.setState(TaskState.STATE_ERROR);
            mListenner.onError(TaskErrorCode.ERROR_FINISH,mTask);
        } else {
            mTask.setCompleteLength(mTask.getLength());
            mTask.setCompleteTime(System.currentTimeMillis());
            mTask.setState(TaskState.STATE_FINISH);
            mListenner.onFinished(mTask);
        }

        release(); //释放资源
    }

    @Override
    public void start() {
        synchronized (this) {
            //如果任务已经开始或完成则不重复开始
            if(TaskState.STATE_RUNNING == mTask.getState()) {
                //throw new IllegalStateException("current handler already started ...");
                Debugger.error(TAG,"current handler already started ...");
                return;
            }

            isExit = false;
            //如果设置了线程池则会在线程池中传输，否则会在当前线程中开始传输
            if (mTaskHandleThreadPool != null) {
                mTaskHandleThreadPool.execute(mHandleRunnable);
            } else {
                mHandleRunnable.run();
            }

            mStateThread = new Thread(mStateRunnable);
            mStateThread.setName("speed_" + getTask().getName() + "_thread");
            mStateThread.setDaemon(true);
            mStateThread.start();
            mTask.setState(TaskState.STATE_RUNNING);
        }
    }

    @Override
    public void stop() {

        //停止，完成,失败的任务不能停止
        if(TaskState.STATE_STOP == mTask.getState() ||
                TaskState.STATE_FINISH == mTask.getState() ||
                TaskState.STATE_ERROR == mTask.getState()) {
            return;
        }

        Debugger.error(TAG,"stop ============= length = " + mTask.getLength() + "" +
                ",completeLength = " + mTask.getCompleteLength() + ",startOffset = " + mTask.getStartOffset() + ",endOffset = " + mTask.getEndOffset());
        isExit = true;
        mTask.setState(TaskState.STATE_STOP);
        //mTaskHandleThreadPool.remove(mHandleRunnable);
        mListenner.onStop(mTask);
    }

    protected void release() {
    }

    @Override
    public int getState() {
        return mTask.getState();
    }

    @Override
    public ITask getTask() {
        return mTask;
    }

    @Override
    public void setTask(ITask task) {
        mTask = (Task) task;
    }

    @Override
    public TaskType getType() {
        return mTask.getType();
    }

    class HandleRunnable implements Runnable {

        @Override
        public void run() {
            try {
                handle(mTask);
            } catch (Exception e) {
                e.printStackTrace();
                mTask.setState(TaskState.STATE_ERROR);
                isExit = true;
                mListenner.onError(TaskErrorCode.ERROR_CODE_EXCEPTION,mTask);
            }
        }
    }

    //当前实际完成的长度，这个数值是比较及时的，可以用来显示速度和进度的变化
    protected  long getCurrentCompleteLength() {
        return mTask.getCompleteLength();
    }

    class StateRunnable implements Runnable {

        @Override
        public void run() {
            while (!isExit) {
                try {

                    Thread.sleep(MAX_DELAY_TIME);

                    ITask task = new TaskBuilder()
                            .setTaskId(mTask.getTaskId())
                            .setDestSource(mTask.getDestSource())
                            .setDataSource(mTask.getDataSource())
                            .setCompleteLength(getCurrentCompleteLength())
                            .setTaskType(mTask.getType())
                            .setLength(mTask.getLength())
                            .setGroupName(mTask.getGroupName())
                            .setState(mTask.getState())
                            .setGroupId(mTask.getGroupId())
                            .setSpeed((long) ((getCurrentCompleteLength() - mLastCompleteLength) / ( MAX_DELAY_TIME / 1000f)))
                            .build();
                    mTask.setSpeed(task.getSpeed());
                    if(getCurrentCompleteLength() == mLastCompleteLength) continue;
                    mListenner.onSpeedChanged((long) ((getCurrentCompleteLength() - mLastCompleteLength) / ( MAX_DELAY_TIME / 1000f)), task);
                    mLastCompleteLength = getCurrentCompleteLength();
                    Debugger.error(TAG,"speed = " + task.getSpeed());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
 }
