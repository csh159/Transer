package com.scott.transer.event;

import com.scott.annotionprocessor.ITaskEventDispatcher;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskType;
import com.scott.annotionprocessor.ThreadMode;

import java.lang.reflect.Method;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-26 16:49</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

class ScribeMethod {

    ThreadMode threadMode;

    ITaskEventDispatcher dispatcher;

    ProcessType processType;

    TaskType taskType;

    boolean isHasParams;
}
