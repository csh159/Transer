package com.scott.transer.task;

import com.scott.annotionprocessor.TaskType;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-20 16:23</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public final class TaskTypeConverter implements PropertyConverter<TaskType,Integer> {

    @Override
    public TaskType convertToEntityProperty(Integer databaseValue) {
        TaskType taskType = TaskType.TYPE_UPLOAD;
        switch (databaseValue) {
            case 0:
                taskType = TaskType.TYPE_UPLOAD;
                break;
            case 1:
                taskType = TaskType.TYPE_DOWNLOAD;
                break;
        }
        return taskType;
    }

    @Override
    public Integer convertToDatabaseValue(TaskType entityProperty) {
        int value = 0;
        switch (entityProperty) {
            case TYPE_DOWNLOAD:
                value = 1;
                break;
            case TYPE_UPLOAD:
                value = 0;
                break;
        }
        return value;
    }
}