package com.scott.transer.event;

import com.scott.transer.processor.ITaskCmd;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 16:35</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

interface ICmdEventDispatcher {
    void dispatchCmd(ITaskCmd cmd);
}
