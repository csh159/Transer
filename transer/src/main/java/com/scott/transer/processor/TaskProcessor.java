package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.ITaskHandlerHolder;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.TaskHandlerHolder;
import com.scott.transer.task.TaskState;
import com.scott.transer.utils.Debugger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:38</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskProcessor implements ITaskProcessor {

    private List<ITaskHolder> mTasks;
    private ITaskManager mTaskManager;
    private final String TAG = TaskProcessor.class.getSimpleName();

    @Override
    public void setTaskManager(ITaskManager manager) {
        mTaskManager = manager;
        mTasks = manager.getTasks();
    }

    @Override
    public void addTask(ITask task) {
        ITaskHolder holder = new TaskHandlerHolder();
        holder.setTask(task);
        mTasks.add(holder);
    }

    @Override
    public void addTasks(List<ITask> tasks) {
        for(ITask task : tasks) {
            ITaskHolder holder = new TaskHandlerHolder();
            holder.setTask(task);
            mTasks.add(holder);
        }
    }

    @Override
    public void deleteTask(String taskId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getTaskId() == taskId) {
                mTasks.remove(holder);
                holder.setState(TaskState.STATE_STOP);
                break;
            }
        }
    }

    @Override
    public void deleteGroup(String groupId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getGroupId() == groupId) {
                mTasks.remove(holder);
                holder.setState(TaskState.STATE_STOP);
            }
        }
    }

    @Override
    public void deleteTasks(String[] taskIds) {
        for(ITaskHolder holer : mTasks) {
            for(String taskId : taskIds) {
                if(holer.getTask().getTaskId() == taskId) {
                    mTasks.remove(holer);
                    holer.setState(TaskState.STATE_STOP);
                }
            }
        }
    }

    @Override
    public void deleteCompleted(TaskType type) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getState() == TaskState.STATE_FINISH &&
                    holder.getType() == type) {
                mTasks.remove(holder);
                holder.setState(TaskState.STATE_STOP);
            }
        }
    }

    @Override
    public void delete(int state,TaskType type) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getState() == state && holder.getType() == type) {
                mTasks.remove(holder);
                holder.setState(TaskState.STATE_STOP);
            }
        }
    }

    @Override
    public void deleteAll(TaskType type) {

        Iterator<ITaskHolder> iterator = mTasks.iterator();
        while (iterator.hasNext()) {
            ITaskHolder next = iterator.next();
            if(next.getType() == type) {
                next.setState(TaskState.STATE_STOP);
                iterator.remove();
            }
        }
    }

    @Override
    public ITask getTask(String taskId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getTaskId() == taskId) {
                return holder.getTask();
            }
        }
        return null;
    }

    @Override
    public List<ITask> getTasks(String[] taskIds) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder task : mTasks) {
            for(String taskId : taskIds) {
                if(task.getTask().getTaskId() == taskId) {
                    tasks.add(task.getTask());
                }
            }
        }
        return tasks;
    }

    @Override
    public List<ITask> getGroup(String groupId) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getGroupId() == groupId) {
                tasks.add(holder.getTask());
            }
        }
        return tasks;
    }

    @Override
    public List<ITask> getAllTasks(TaskType type) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder holder : mTasks) {
            if(type == holder.getType()) {
                tasks.add(holder.getTask());
            }
        }
        return tasks;
    }

    @Override
    public List<ITask> getTasks(int state,TaskType type) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder holder : mTasks) {
            if(holder.getState() == state && type == holder.getType()) {
                tasks.add(holder.getTask());
            }
        }
        return tasks;
    }

    @Override
    public void changeTaskState(int state, String taskId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getTaskId() == taskId) {
                changeState(holder,state);
                break;
            }
        }
    }

    private void changeState(ITaskHolder holder,int state) {
        ITaskHandlerHolder handlerHolder = (ITaskHandlerHolder) holder;
        if(handlerHolder.getTaskHandler() == null) {
            handlerHolder.setTaskHandler(mTaskManager.getTaskHandler(holder.getType()));
        }
        handlerHolder.setState(state);
    }

    @Override
    public void changeTaskStateWithOutSave(int state, String taskId) {
        changeTaskState(state,taskId);
    }

    @Override
    public void changeTasksState(int state, String[] taskId) {
        for(ITaskHolder holder : mTasks) {
            for(String id : taskId) {
                if(holder.getTask().getTaskId() == id) {
                    changeState(holder,state);
                }
            }
        }
    }

    @Override
    public void changeTasksState(int state, String groupId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getGroupId() == groupId) {
                changeState(holder,state);
            }
        }
    }

    @Override
    public void changeAllTasksState(int state,TaskType type) {
        for(ITaskHolder holder : mTasks) {
            if(type == holder.getType()) {
                changeState(holder, state);
            }
        }
    }

    @Override
    public void updateTask(ITask task) {
        Debugger.error(TAG,"speed = " + task.getSpeed());
        for(ITaskHolder holder : mTasks) {
            ITask task1 = holder.getTask();
            if(task1.getTaskId() == task.getTaskId()) {
                holder.setTask(task);
                if(holder.getState() == TaskState.STATE_FINISH) {
                    ((ITaskHandlerHolder)holder).setTaskHandler(null);
                }
                break;
            }
        }
    }

    @Override
    public void updateTaskWithoutSave(ITask task) {
        updateTask(task);
    }
}
