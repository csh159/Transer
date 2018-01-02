# Transer
是一个传输框架,目前支持：
- 支持 HTTP/HTTPS 断点续传下载
- 支持 HTTP/HTTPS 大文件分片上传
- 支持 类EventBus的task状态变更通知，支持三种线程的订阅模式
- 支持 任务分组，分用户

## 简单的下载或上传:

下载:
```` java 
   mHandler = new DefaultHttpDownloadHandler();
        //创建一个任务
        ITask task = new TaskBuilder()
                .setName("test.zip") //设置任务名称
                .setDataSource(URL)  //设置数据源
                .setDestSource(FILE_PATH) //设置目标路径
                .build();
        mHandler.setTask(task);

        //设置请求参数
        Map<String,String> params = new HashMap<>();
        params.put("path","test.zip");
        mHandler.setParams(params);
        mHandler.setHandlerListenner(new SimpleTaskHandlerListenner());

        //设置一个线程池去下载文件，如果不设置，则会在当前线程进行下载。
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,3,
                6000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10000));
        mHandler.setThreadPool(threadPool);
````
上传:
```` java 
   mHandler = new DefaultHttpUploadHandler();
        //创建一个任务
        ITask task = new TaskBuilder()
                .setName("test.zip") //设置任务名称
                .setDataSource(FILE_PATH)  //设置数据源
                .setDestSource(URL) //设置目标路径
                .build();
        mHandler.setTask(task);

        //设置请求参数
        Map<String,String> params = new HashMap<>();
        params.put("path","test.zip");
        mHandler.setParams(params);
        mHandler.setHandlerListenner(new SimpleTaskHandlerListenner());

        //设置一个线程池去下载文件，如果不设置，则会在当前线程进行下载。
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,3,
                6000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10000));
        mHandler.setThreadPool(threadPool);
````

## 使用任务管理:
1.创建任务

```` java
ITask task = new TaskBuilder()
                .setTaskType(task_type)  //任务类型
                .setDataSource(source)   //任务数据源 (下在任务为要下载的服务文件链接，上传任务为要上传的本地文件路径)
                .setDestSource(dest)     //任务目标源 (下载任务为保存的本地路径，上传任务为服务器地址)
                .setName(NAME)           //任务名称
                .build();

        ITaskCmd cmd = new TaskCmdBuilder()
                .setTaskType(task_type) //任务类型
                .setProcessType(ProcessType.TYPE_ADD_TASK) //操作类型(添加任务)
                .setTask(task) //任务信息
                .build();

        TaskEventBus.getDefault().execute(cmd); //执行命令
````
2.开始任务

```` java
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTaskType(task_type) //任务类型
                .setState(TaskState.STATE_START)
                .setProcessType(ProcessType.TYPE_CHANGE_TASK) //操作类型(添加任务)
                .setTask(task) //任务信息
                .build();

        TaskEventBus.getDefault().execute(cmd); //执行命令
````
3.结束/暂停 任务
```` java
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTaskType(task_type) //任务类型
                .setState(TaskState.STATE_START)
                .setProcessType(ProcessType.TYPE_CHANGE_TASK) //操作类型(添加任务)
                .setTask(task) //任务信息
                .build();

        TaskEventBus.getDefault().execute(cmd); //执行命令
````
4.接收任务变更通知
- 在Activity,Fragement,Service,Dialog 等 onResume 或 onStart 中:

````java
TaskEventBus.getDefault().regesit(this);
````
- 在 onPause , onStop 中使用 

````java
TaskEventBus.getDefault().unregesit(this);
````
- 添加一个方法，参数为List<ITask> tasks,并且添加注解TaskSubscriber

````java 
@TaskSubscriber
public void onTasksChanged(List<ITask> tasks) {
       //TODO update ui on any processtype
}
````

- TaskScriber
> 默认情况下TaskScriber 会接受所有任务变更的消息，也可以指定只接受某个操作的消息例如：

````java
@TaskSubscriber(taskType = TYPE_DOWNLOAD, processType = TYPE_CHANGE_TASK)
public void onTasksChanged(List<ITask> tasks) {
       //TODO update ui in posting thread
}
````
>也可以指定消息接收的线程，默认为发送消息的线程，例如:

````java
@TaskSubscriber(taskType = TYPE_DOWNLOAD, processType = TYPE_CHANGE_TASK,threadMode = ThreadMode.MODE_MAIN)
public void onTasksChanged(List<ITask> tasks) {
       //TODO update ui in main thread
}
````
## 配置传输服务(TranferService的onCreate 中)

```` java
        TaskEventBus.init(getApplicationContext()); //初始化消息管理器
        DaoHelper.init(getApplicationContext());    //初始化数据库
        mContext = getApplicationContext();

        mTaskManagerProxy = new TaskManagerProxy();  
        mTaskManagerProxy.setProcessCallback(this);
        //Taskprocessor 为在内存中处理任务，TaskdDbProcessor 为数据库操作， 可以 实现ITaskProcessor 替换默认处理器
        mTaskManagerProxy.setTaskProcessor(new TaskProcessorProxy(new TaskProcessor(),new TaskDbProcessor())); 
        
        mTaskManagerProxy.setTaskManager(new TaskManager()); 
        //可以继承BaseTaskHandler 实现新的传输处理器
        mTaskManagerProxy.setTaskHandler(TaskType.TYPE_HTTP_DOWNLOAD, DefaultHttpDownloadHandler.class); //设置下载器
        mTaskManagerProxy.setTaskHandler(TaskType.TYPE_HTTP_UPLOAD, DefaultHttpUploadHandler.class); //设置上传器

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,3,
                6000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10000));
        mTaskManagerProxy.setThreadPool(TaskType.TYPE_HTTP_UPLOAD, threadPool);//设置上传线程池

        threadPool = new ThreadPoolExecutor(3,3,
                6000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10000));
        mTaskManagerProxy.setThreadPool(TaskType.TYPE_HTTP_DOWNLOAD,threadPool); //下载线程池

        mTaskManagerProxy.setHeaders(new HashMap<String, String>()); //设置请求头
        mTaskManagerProxy.setParams(new HashMap<String, String>()); //设置url参数

````

接下来将会增加的功能：
- 其他方式的文件传输支持
- 优化性能

服务端测试Demo详见：
- https://github.com/shilec/WebDemo
