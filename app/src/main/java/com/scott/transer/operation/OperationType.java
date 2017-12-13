package com.scott.transer.operation;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:49</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public enum OperationType {
    TYPE_ADD_TASK,
    TYPE_ADD_TASKS,
    TYPE_DELETE_TASK,
    TYPE_DELETE_TASKS_GROUP,
    TYPE_DELETE_TASKS_SOME,
    TYPE_DELETE_TASKS_ALL,
    TYPE_DELETE_TASKS_COMPLETED,
    TYPE_DELETE_TASKS_STATE,
    TYPE_QUERY_TASK,
    TYPE_QUERY_TASKS_SOME,
    TYPE_QUERY_TASKS_ALL,
    TYPE_QUERY_TASKS_GROUP,
    TYPE_QUERY_TASKS_STATE,
    TYPE_QUERY_TASKS_COMPLETED,
    TYPE_CHANGE_TASK,
    TYPE_CHANGE_TASK_GROUP,
    TASK_CHANGE_TASK_ALL,
    TASK_CHANGE_TASK_SOME
}
