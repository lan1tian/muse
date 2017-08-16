# 调度系统重构详细设计文档

## 一、模块设计

### 1.1 DAGScheduler模块设计


DAGScheduler类图如下：

![uml_DAGScheduler](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/uml_DAGScheduler.png)

DAGScheduler中的DAGMap，其数据结构为Map[Integer, DAGJob]  
DAGScheduler中的runningMap，其数据结构为Map[Integer, DAGTask]  
JobDependStatus中的jobStatusMap，其数据结构为Map[Integer,Map[Integer,Boolean]]


#### 1.1.1 定时任务调度

时间调度器负责调度基于时间触发的任务，支持Cron表达式时间配置。

![时间调度器](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/time_based_scheduler_new.png)

时间调度器从数据库中加载周期性调度任务，或者从Rest API请求增加/修改/删除任务。根据任务配置的调度时间(CronExpression)，调度器定时计算出每个任务后面一段时间内（如：一天）的具体调度时间，并生成调度计划(Schedule Plan)。

- cron analyzer是一个cronTab表达式的语法分析器，输入是一个job，产出是一些task。

- task表会把时间最小的task排在最前面，数据结构上采用堆？

- cron schedule thread是一个线程，不断轮询task表，当满足时间就会提交给TaskScheduler。

#### 1.1.2 事件处理

DAGScheduler通过观察者模式进行事件处理，目的是把同步调用变成异步调用，跟外部调用者进一步解耦。并且为将来支持其他功能的监听提供了可扩展性。


##### 1.1.2.1 Event设计

![uml_event](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/uml_mvc_event.png)

如图所示，Event是一个接口，DAGEvent是一个抽象类，其子类有InitializeEvent,SuccessEvent,ScheduledEvent等。DAGEvent中主要有两个成员，jobid和taskid。

##### 1.1.2.2 Observable和Observer设计

![uml_observable](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/uml_mvc_observable.png)

如图所示，Observable是一个接口，相当于观察者模式中的主题，Listener是一个接口是观察者模式中的观察者。

Observable提供注册、移除、通知观察者的接口。EventBusObservable是一个抽象类，是基于google EventBus观察者模式中的主题。DAGScheduler是具体的实现类。

Listener可以有多种实现，订阅继承Event接口的事件进行处理。

#### 1.1.3 支持可扩展的依赖策略

DAGJob中有一个成员JobDependStatus，用来维护当前任务的依赖的状态，其内部数据结构主要是Map[Integer,Map[Integer,Boolean]], 表示Map[jobid,Map[taskid,status]]

比如c依赖于任务a和b，a每小时跑4次，b每小时跑1次，最终生成的依赖状态表如下表：

| jobid | taskid | 状态 |
| ------| ------ | ---- |
| joba  | taska1 |  T   | 
| joba  | taska2 |  T   |
| joba  | taska3 |  F   |  
| joba  | taska4 |  T   | 
| jobb  | taskb1 |  T   | 

然后根据任务的依赖策略判定依赖是否满足，当前支持的依赖策略有：ANYONE,LASTONE,ALL

- ANYONE表示b成功，a四次中任意一次成功都可以触发c;

- LASTONE表示b成功，a四次中最后一次成功才可以触发c；

- ALL表示b成功，a四次中全部成功才可以触发c；

这个任务当前依赖任务状态表会实时持久化到数据库中，当重跑历史任务或者系统异常重启的时候，也能获取之前依赖任务的状态。


### 1.2 ExecuteQueue模块设计

### 1.3 JobDispatcher模块设计

![Job Dispatcher](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/uml_job_dispatcher.png)

Job Dispatcher负责从Worker组中分配一个Worker，然后将任务发给此Worker执行。

JobDispatcher接口中只有一个select方法，具体Worker分配逻辑在此方法中实现，以支持对不同分配策略的支持。默认已实现的有轮询分配(RoundRobinJobDispatcher)、随机分配(RandomJobDispatcher)。

RoundRobinJobDispatcher：内部维护Worker的索引，分配完一个Worker后索引递增，当索引超过Worker数后归0从新开始计算，与索引位置对应的Worker即为此次任务分配的Worker。

RandomJobDispatcher：随机生成一个Worker数以内的整数作为Worker索引，与此索引位置对应的Worker即为此次任务分配的Worker。

### 1.3 dao模块设计

### 1.4 service模块设计

调度系统有四个service，master,worker,logserver和restfulserver. 其中master,worker,logserver通过rpc协议通信，使用akka框架，其akka架构图如下：

![akka_service](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/akka_service.png)

如上图所示，sentinel master内部有ServerActor，DAGSchedulerActor, HeartBeatActor，和JobMetricsRoutingActor。HeartBeatActor用来接收slave发送过来的心跳信息，由HeartBeatManager来维护所有client的信息。ServerActor作为master对外的唯一actor，只负责转发消息。DAGSchedulerActor是调度器的actor，接收ServerActor和JobMetricsActor发送过来的消息进行任务调度，并提交任务给ExecuteQueue模块。

JobMetricsRoutingActor对jobId进行哈希，把任务状态和进度路由给具体的JobMetricsActor。JobMetricsActor把任务状态写到DB中，来持久化任务状态，把任务进度反馈给前段。

LogRoutingActor和JobStatusRoutingActor类似，只是路由功能。由具体的LogWriterActor来写log，LogReadActor来读log。

ClientActor负责接收任务，并从线程池中取线程进行任务的执行。

### 1.5 Job模块设计

![Job](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/uml_job.png)

Job为任务的抽象类，主要包括4个接口：preExecute()、execute()、postExecute()、kill()，作用分别为：

preExecute()：执行任务之前的预处理，如：数据库连接、数据清理等，此方法在execute()之前调用；

execute()：任务的主要执行方法，具体执行内容在此方法中实现，如：执行HiveQL等。JobContext中包含了任务运行所需的输入参数，如：任务类型、执行命令、任务名称、扩展参数等。任务执行中输出的日志通过调用LogCollector的方法将日志发送给LogServer，LogServer收到后将日志持久化至存储系统中。此方法调用时向Server汇报”执行中“状态，执行完成时向Server汇报”成功“或”失败“状态。执行过程中可调用ProgressReporter汇报任务进度。

postExecute()：任务运行完成后的处理，如：报警等，此方法在execute()之后调用；

kill()：终止任务。

通过实现不同的Job抽象类可以支持多种类型任务的运行，默认实现的任务包括：Shell、Hive、Presto、Java、MapReduce等。

#### 1.5.1 hive job
hive job继承shell job，通过fock一个子进程，运行shell命令的方式调用hive cli或者beeline来运行hive任务。比如hive cli使用“hive -e sql”命令运行job。

hive cli运行在worker本地，hive的执行结果作为shell的标准输出会写到logserver中。

本地shell进程会等待hive输出完所有日志才会结束，即hive任务执行完成。

通过shell进程结束的错误码返回任务状态，征程结束即返回成功，反之失败。

#### 1.5.2 java job

#### 1.5.3 mr job

## 二、流程设计

### 2.1 提交任务

### 2.2 Kill任务

![Kill Job](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/uml_kill_job.png)

RestServer收到Kill请求转发给Server，然后Server将Kill请求发送到执行对应任务的Worker，由Worker调用job的kill()方法终止任务，完成后向Server返回响应。

### 2.3 重跑任务

1. 如果是原地重跑，taskid不变
2. 如果是手动重跑，由外部系统生成新的jobid和依赖关系，提交到调度系统。


### 2.4 修改任务

1. 如果修改job内容，即job表，不会影响调度逻辑
2. 如果修改cron表，会重新刷新task表，已经提交的任务不做回退
3. 如果修改依赖关系，修改jobDependency和taskDependency表，并且发送ModifyEvent给DAGScheduler重新刷新依赖关系。

### 2.5 升级处理

不考虑RollingUpgrade，目标做到无状态重启，重启服务不丢失任务状态。

#### 2.5.1 master升级

确保每次更新任务状态和依赖任务的状态，都实时更新到DB中。确保每次生成和修改执行计划，都实时更新到DB的task表中。

master启动的时候，包括standbyserver切换为active的时候，做如下事情：   
1. load jobDependency表，重建DAG表  
2. load task表，恢复ready的任务  
3. load cron表，恢复定时任务    
4. 恢复依赖任务有两种方法。  
   1）把jobDependStatus持久化，恢复这个表。  
   2）通过计算。扫描所有的依赖任务，对于每一个依赖任务，找出上一次的task和依赖的task，进而计算出这一次应该依赖哪些task，再把状态恢复到JobDependStatus中。

针对第4点，我比较倾向于第一种方法，虽然多了一个存储表，但是并没有多少存储代价。第二种方法在依赖任务开始提交的时候，同样要把依赖的taskid持久化，所以存储代价上是一样的。第一种方法恢复速度快，不需要扫描全表。第一种方法恢复的时候不需要复杂的计算，只需要把数据库中的表load进来就可以了。另外，第二种方法也无法保证计算的正确性，第一种方法所见即所得，不需要去验证正确性。


#### 2.5.2 worker升级

每次任务执行完成会在本地生成状态文件，当向master汇报完状态，会把该文件删除。

每次worker启动的时候会扫描该文件，发现有状态是成功或失败的，重新向master发送状态，然后删除该文件。

发现该文件是running，根据任务类型去获取任务状态，如果能获取到并且是成功或失败，重新向master发送状态，然后删除该文件。

如果状态是running，向master发送失败状态，必要场合，释放该任务相关的资源（比如kill yarn上的任务）。

如果无法获取任务状态，向master发送失败状态，必要场合，释放该任务相关的资源（比如kill yarn上的任务）。


### 2.6 logServer 处理

logServer支持多个，有哪些可用的logServer可以通过配置获得。

log存储
存储在HDFS中。

log命名

log文件命名格式为： 

执行中的文件：
jobID + taskId + attemptID. tmp

执行完毕的日志：
jobID + taskId + attemptID. log


### 2.7 全局配置
zookeeper中的配置


### 2.8 接入认证
##### 2.8.1 业务方接入认证
通过 app 与appKey 来认证。

每个app方有个appKey来验证。

#### 2.8.2 worker验证
worker通过 workerKey，来验证。

server会验证worker的 组id，ip，端口号，key是否OK。



### 2.9 异常处理
#### 2.9.1 server端的异常处理
- server重启

master/stand by HA切换处理

恢复上次运行的状态
包括，DAG表，task表，jobDependStatus表等

检查时间触发器，恢复重启过程中未触发的时间事件。

与worker取得联系，继续接受worker消息。

- worker失联处理

超过3分钟联系不上worker，则把该worker上的任务重新发到其他worker执行。
（先把任务设置为失败，然后重新执行任务）

![worker_miss](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/worker_miss.png)

#### 2.9.2 worker端的异常处理
- worker重启
恢复执行中的任务，并向server继续发送消息。

如果任务不可恢复，则汇报任务执行失败。

- server失联处理

超过3分钟联系不上server，则把执行中的任务都kill掉。

![worker_miss](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/server_miss.png)

### 2.10 master暂停处理

master维护一个可动态配置的任务并发度，和当前正在running的任务数。每次jobDispatcher模块派发任务的时候都会比较当前任务数是否达到最大并发度，如果超过了则每隔5秒进行sleep，把jobDispatcher阻塞住。因为消费者还没有消费完，ExecuteQueue就不会继续发送事件。

对于暂停，可以临时把并发度调为0，则任务将不会再进行分发。


### 2.11 定时任务的多次触发

把这种问题归类为一种情况，即任务可以配置一个属性，超过多少时间没有执行的，则skip掉，不再进行调度。默认情况下，不管超过多少时间都要执行。


## 三、内部接口设计

Server、Worker、LogServer、RestServer之间的通信均采用Netty、Protocol Buffers。


### 3.1 提交任务

- RestServer -> Server

请求:

| 字段         | 类型     | 必选   | 默认值  | 描述        | 
| :--------- | ------ | ---- | ---- | --------- |
| job_name   | string | T    |      | 任务名称      |
| cron_expression   | string | F    |      | cron表达式，如：0 0 23 * * ?      | 
| dependency_jobids | int32 | F    |      | 依赖任务ID，可以多个      |  
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| user       | string | T    |      | 提交任务的用户名称 | 
| job_type   | string | T    |      | 任务类型，如：hive、shell、mapreduce      | 
| command    | string | T    |      | 执行命令      | 
| group_id   | int32  | T    |      | Worker组ID | 
| priority   | int32  | F    | 1    | 任务优先级，取值范围1-10。后端执行系统可根据此值映射成自己对应的优先级      |
| reject_retries   | int32  | F    | 0    | 任务被Worker拒绝时的重试次数      |
| reject_interval   | int32  | F    | 3    | 任务被Worker拒绝时重试的间隔，单位：秒 |
| failed_retries   | int32  | F    | 0    | 任务运行失败时的重试次数      |
| failed_interval   | int32  | F    | 3    | 任务运行失败时重试的间隔，单位：秒      | 
| start_time   | string  | F    |     | 起始调度时间，格式：yyyy-MM-dd HH:mm:ss   | 
| end_time   | string  | F    |     | 结束调度时间，格式：yyyy-MM-dd HH:mm:ss     |  
| parameters | map | F    |      | 扩展参数，用于支持不同类型任务执行需要的额外参数，如：权限验证等      | 

响应:

| 字段      | 类型     | 必选   | 默认值  | 描述          | 
| ------- | ------ | ---- | ---- | ----------- | 
| job_id  | int64  | T    |    | 任务ID        |
| success | bool | T    |      | 是否提交成功 | 


- Server -> Worker

请求:

| 字段         | 类型     | 必选   | 默认值  | 描述        | 
| :--------- | ------ | ---- | ---- | --------- |
| full_id   | string | T    |      | 全部ID，(jobId+taskId+attemptId)      |
| job_name   | string | T    |      | 任务名称      | 
| app_name   | string | T    |      | 应用名称，如：XRay      |
| user       | string | T    |      | 提交任务的用户名称 | 
| job_type   | string | T    |      | 任务类型，如：hive、shell、mapreduce      | 
| command    | string | T    |      | 执行命令      |
| priority   | int32  | F    | 1    | 任务优先级，取值范围1-10。后端执行系统可根据此值映射成自己对应的优先级      | 
| parameters | map | F    |      | 扩展参数，用于支持不同类型任务执行需要的额外参数，如：权限验证等      | 

响应:

| 字段      | 类型     | 必选   | 默认值  | 描述          | 
| ------- | ------ | ---- | ---- | ----------- | 
| accept  | bool   | T    |      | 提交的任务是否被接受 | 
| message | string | F    |      | 描述消息，用于说明任务被拒绝的原因。任务被接受时此字段为空            |


### 3.2 修改任务

- RestServer -> Server

请求:

| 字段         | 类型     | 必选   | 默认值  | 描述        | 
| :--------- | ------ | ---- | ---- | --------- |
| job_id  | int64  | T    |    | 任务ID        |
| job_name   | string | F    |      | 任务名称      |
| cron_expression   | string | F    |      | cron表达式，如：0 0 23 * * ?      | 
| dependency_jobids | int32 | F    |      | 依赖任务ID，可以多个      |  
| app_name   | string | F    |      | 应用名称，如：XRay      | 
| app_key    | string | F    |      | 应用授权Key   | 
| user       | string | F    |      | 提交任务的用户名称 | 
| job_type   | string | F    |      | 任务类型，如：hive、shell、mapreduce      | 
| command    | string | F    |      | 执行命令      | 
| group_id   | int32  | F    |      | Worker组ID | 
| priority   | int32  | F    | 1    | 任务优先级，取值范围1-10。后端执行系统可根据此值映射成自己对应的优先级      |
| reject_retries   | int32  | F    | 0    | 任务被Worker拒绝时的重试次数      |
| reject_interval   | int32  | F    | 3    | 任务被Worker拒绝时重试的间隔，单位：秒 |
| failed_retries   | int32  | F    | 0    | 任务运行失败时的重试次数      |
| failed_interval   | int32  | F    | 3    | 任务运行失败时重试的间隔，单位：秒      | 
| start_time   | string  | F    |     | 起始调度时间，格式：yyyy-MM-dd HH:mm:ss   | 
| end_time   | string  | F    |     | 结束调度时间，格式：yyyy-MM-dd HH:mm:ss     |  
| parameters | map | F    |      | 扩展参数，用于支持不同类型任务执行需要的额外参数，如：权限验证等      | 

响应:

| 字段      | 类型     | 必选   | 默认值  | 描述          | 
| ------- | ------ | ---- | ---- | ----------- | 
| job_id  | int64  | T    |    | 任务ID        |
| success | bool | T    |      | 是否提交成功 | 


### 3.3 删除任务

- RestServer -> Server

请求：

| 字段     | 类型    | 必选   | 默认值  | 描述   | 
| ------ | ----- | ---- | ---- | ---- | 
| job_id | int64 | T    |      | 任务ID | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否删除成功 | 



### 3.4 Task状态汇报

- Worker -> Server

请求：

| 字段        | 类型    | 必选   | 默认值  | 描述   | 
| --------- | ----- | ---- | ---- | ---- | 
| full_id    | int64 | T    |      | 全部ID，(jobId+taskId+attemptId) | 
| status    | int32 | F    | -1   | 状态   | 
| timestamp | int64 | F    | 0    | 时间戳  | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否请求成功 | 


### 3.5 Task进度汇报

- Worker -> Server

请求：

| 字段      | 类型     | 必选   | 默认值  | 描述                  | 
| ------- | ------ | ---- | ---- | ------------------- | 
| full_id      | string | T    |      | 全部ID，(jobId+taskId+attemptId)        | 
| progress    | double  | T    |      | 任务完成进度           |

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否请求成功 |


### 3.6 获取task一览

- RestServer -> Server

| 字段    | 类型     | 必选   | 默认值  | 描述     | 
| ----- | ------ | ---- | ---- | ------ |
| job_id | long   | T    |      | 任务ID   | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| task_id | long数组   | T    |      | taskID数组   | 


### 3.7 Task状态查询

- RestServer -> Server

请求：

| 字段      | 类型     | 必选   | 默认值  | 描述                  | 
| ------- | ------ | ---- | ---- | ------------------- | 
| task_id | int64   | T    |      |taskID | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| status | int32 | T    |      | 状态 |



### 3.8 终止Task

- RestServer -> Server

请求：

| 字段     | 类型    | 必选   | 默认值  | 描述   | 
| ------ | ----- | ---- | ---- | ---- | 
| task_id | int64 | T    |      | taskID | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否终止成功 | 

- Server -> Worker

请求：

| 字段     | 类型    | 必选   | 默认值  | 描述   | 
| ------ | ----- | ---- | ---- | ---- | 
| full_id | String | T    |      | fullID(jobId+taskId+attemptId) | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否终止成功 | 




### 3.9 日志写入

- Worker -> LogServer

请求：

| 字段     | 类型     | 必选   | 默认值  | 描述                 | 
| ------ | ------ | ---- | ---- | ------------------ | 
| full_id | string  | T    |      | fullID (jobId+taskId+attemptId) | 
| log    | string | T    |      | 日志内容               | 
| type   | int32  | T    |      | 日志类型：1-stdout、2-stderr | 
| is_end | bool   | T    |      | 日志写请求是否结束          | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否请求成功 | 

### 3.10 日志读取

- RestServer -> LogServer

请求:

| 字段     | 类型    | 必选   | 默认值  | 描述                 | 
| ------ | ----- | ---- | ---- | ------------------ | 
| task_id | int64 | T    |      | taskID               | 
| type   | int32 | T    |      | 日志类型：stdout、stderr | 
| offset | int64 | F    | 0    | 日志内容的字节偏移量         | 
| lines  | int32 | F    | 100  | 日志读取的行数            | 

响应:

| 字段     | 类型     | 必选   | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | 
| is_end | bool   | T    |      | 是否请求成功       | 
| log    | string | F    |      | 日志内容         | 
| offset | int64  | T    |      | 当前日志内容的字节偏移量 | 


### 3.11 Worker注册

- Worker -> Server

请求：

| 字段       | 类型     | 必选   | 默认值  | 描述          | 
| -------- | ------ | ---- | ---- | ----------- | 
| key      | string | T    |      | Worker授权Key | 
| group_id | int32  | T    |      | Worker组ID   |

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否注册成功 | 

### 3.12 Worker心跳汇报

- Worker -> Server

请求：

| 字段       | 类型     | 必选   | 默认值  | 描述          | 
| -------- | ------ | ---- | ---- | ----------- |
| task_num  | int32  | T    |      | 正在运行的task数    | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否请求成功 | 

### 3.13 Worker上下线

- RestServer -> Server

请求：

| 字段      | 类型     | 必选   | 默认值  | 描述                  | 
| ------- | ------ | ---- | ---- | ------------------- | 
| ip      | string | T    |      | ip地址         | 
| port    | int32  | T    |      | 端口          | 
| offline | bool   | T    |      | 状态：True-下线，False-上线 | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否请求成功 | 


- server -> worker

请求：

| 字段      | 类型     | 必选   | 默认值  | 描述                  | 
| ------- | ------ | ---- | ---- | ------------------- | 
| offline | bool   | T    |      | 状态：True-下线，False-上线 | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否请求成功 | 




## 四、外部接口设计

RestServer对外提供REST API。


### 4.1 提交任务

接口：/api/job/submit

Method：POST

| 字段       | 类型     | 必选   | 默认值  | 描述        | 
| -------- | ------ | ---- | ---- | --------- | 
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| job_name   | string | T    |      | 任务名称      |
| cron_expression   | string | F    |      | cron表达式，如：0 0 23 * * ?      | 
| dependency_jobids | int32 | F    |      | 依赖任务ID，可以多个      |  
| user       | string | T    |      | 提交任务的用户名称 | 
| job_type   | string | T    |      | 任务类型，如：hive、shell、mapreduce      | 
| command    | string | T    |      | 执行命令      | 
| group_id   | int32  | T    |      | Worker组ID |
| reject_retries   | int32  | F    | 0    | 任务被Worker拒绝时的重试次数      |
| reject_interval   | int32  | F    | 3    | 任务被Worker拒绝时重试的间隔，单位：秒      |
| failed_retries   | int32  | F    | 0    | 任务运行失败时的重试次数      |
| failed_interval   | int32  | F    | 3    | 任务运行失败时重试的间隔，单位：秒    | 
| start_time   | string  | F    |     | 起始调度时间，格式：yyyy-MM-dd HH:mm:ss   | 
| end_time   | string  | F    |     | 结束调度时间，格式：yyyy-MM-dd HH:mm:ss     |  
| priority   | int32  | F    | 1    | 任务优先级，取值范围1-10。后端执行系统可根据此值映射成自己对应的优先级      | 
| parameters | map | F    |      | 扩展参数，用于支持不同类型任务执行需要的额外参数，如：权限验证等      | 

响应:

| 字段      | 类型     | 必选   | 默认值  | 描述          | 
| ------- | ------ | ---- | ---- | ----------- | 
| job_id  | int64  | F    | -1   | 任务ID        |
| message | string | F    |      | 描述消息            |


### 4.2 修改任务

接口：/api/job/edit

Method：POST

| 字段       | 类型     | 必选   | 默认值  | 描述        | 
| -------- | ------ | ---- | ---- | --------- | 
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| job_name   | string | T    |      | 任务名称      |
| job_id   | int64 | T    |      | 任务ID|
| cron_expression   | string | F    |      | cron表达式，如：0 0 23 * * ?      | 
| dependency_jobids | int32 | F    |      | 依赖任务ID，可以多个      |  
| user       | string | F    |     | 提交任务的用户名称 | 
| job_type   | string | F    |      | 任务类型，如：hive、shell、mapreduce      | 
| command    | string | F    |      | 执行命令      | 
| group_id   | int32  | F    |      | Worker组ID |
| reject_retries   | int32  | F    | 0    | 任务被Worker拒绝时的重试次数      |
| reject_interval   | int32  | F    | 3    | 任务被Worker拒绝时重试的间隔，单位：秒 |
| failed_retries   | int32  | F    | 0    | 任务运行失败时的重试次数      |
| failed_interval   | int32  | F    | 3    | 任务运行失败时重试的间隔，单位：秒      | 
| start_time   | string  | F    |     | 起始调度时间，格式：yyyy-MM-dd HH:mm:ss   | 
| end_time   | string  | F    |     | 结束调度时间，格式：yyyy-MM-dd HH:mm:ss     |  
| priority   | int32  | F    | 1    | 任务优先级，取值范围1-10。后端执行系统可根据此值映射成自己对应的优先级      | 
| parameters | map | F    |      | 扩展参数，用于支持不同类型任务执行需要的额外参数，如：权限验证等      | 

响应:

| 字段      | 类型     | 必选   | 默认值  | 描述          | 
| ------- | ------ | ---- | ---- | ----------- | 
| success | bool | T    |      | 是否成功 | 
| message | string | F    |      | 描述消息            |


### 4.3 删除任务

接口：/api/job/delete

Method：POST

请求：

| 字段     | 类型    | 必选   | 默认值  | 描述   | 
| ------ | ----- | ---- | ---- | ---- | 
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| job_id | int64 | T    |      | 任务ID | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否删除成功 | 
| message | string | F    |      | 描述消息            |



### 4.4 获取task一览

接口：/api/job/getTaskList

Method：GET

| 字段    | 类型     | 必选   | 默认值  | 描述     | 
| ----- | ------ | ---- | ---- | ------ |
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   |
| job_id | long   | T    |      | 任务ID   | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| task_id | long   | T    |      | taskID   | 



### 4.5 获取task状态

接口：/api/task/status

Method：GET

| 字段    | 类型     | 必选   | 默认值  | 描述     | 
| ----- | ------ | ---- | ---- | ------ |
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   |
| task_id | long   | T    |      | taskID   | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| task_id | long   | T    |      | 任务ID   | 
| status | int32 | T    |      | 状态 |


### 4.6 终止Task

接口：/api/task/kill

Method：POST

请求：

| 字段     | 类型    | 必选   | 默认值  | 描述   | 
| ------ | ----- | ---- | ---- | ---- | 
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| task_id | int64 | T    |      | taskID | 

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ | 
| success | bool | T    |      | 是否终止成功 | 



### 4.7 读取日志

接口：/api/log

Method：GET

| 字段    | 类型     | 必选   | 默认值  | 描述     | 
| ----- | ------ | ---- | ---- | ------ | 
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| task_id | int64 | T    |      | taskID               | 
| type   | int32 | T    |      | 日志类型：stdout、stderr | 
| offset | int64 | F    | 0    | 日志内容的字节偏移量         | 
| lines  | int32 | F    | 100  | 日志读取的行数            |

响应:

| 字段     | 类型     | 必选   | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | 
| task_id | int64 | T    |      | taskID               | 
| type   | int32 | T    |      | 日志类型：stdout、stderr | 
| is_end | bool   | T    |      | 是否请求成功       | 
| log    | string | F    |      | 日志内容         | 
| offset | int64  | T    |      | 当前日志内容的字节偏移量 |

### 4.8 下载日志

接口：/api/log/download

Method：GET

| 字段    | 类型     | 必选   | 默认值  | 描述     | 
| ----- | ------ | ---- | ---- | ------ |
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   |
| task_id | long   | T    |      | taskID   |
| type   | int32 | T    |      | 日志类型：stdout、stderr | 

响应：文件


### 4.9 Worker上下线

接口：/api/worker/status

Method：POST

| 字段     | 类型     | 必选   | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ |
| app_name   | string | T    |      | 应用名称，如：XRay      | 
| app_key    | string | T    |      | 应用授权Key   | 
| ip     | string | T    |      | Worker IP地址  | 
| port   | int    | T    |      | Worker 端口    | 
| status | int    | T    |      | 状态：1-上线，0-下线 |

响应：

| 字段      | 类型   | 必选   | 默认值  | 描述     | 
| ------- | ---- | ---- | ---- | ------ |
| ip     | string | T    |      | Worker IP地址  | 
| port   | int    | T    |      | Worker 端口    | 
| success | bool | T    |      | 是否请求成功 | 


## 五、表结构设计

[表结构设计](http://gitlab.mogujie.org/bigdata/jarvis2/blob/master/docs/design/jarvis2_design_db.md)