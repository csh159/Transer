package com.scott.annotionprocessor;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:58</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface TaskSubscriber {
    ProcessType[] processType() default ProcessType.TASK_DEFAULT;
    TaskType taskType() default TaskType.TYPE_UPLOAD;
}
