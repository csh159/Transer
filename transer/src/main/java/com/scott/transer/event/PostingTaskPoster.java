package com.scott.transer.event;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ITaskEventDispatcher;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskType;

import java.util.List;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/25</P>
 * <P>Email: shilec@126.com</p>
 */

class PostingTaskPoster implements ITaskPoster {

    PostingTaskPoster() {

    }

    @Override
    public void enqueue(ITaskEventDispatcher dispatcher, TaskType taskType, ProcessType type, List<ITask> taskList) {
        dispatcher.dispatchTasks(taskType,type,taskList);
    }
}
