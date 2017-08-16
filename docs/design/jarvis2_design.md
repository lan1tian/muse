# 调度系统重构设计说明书

## 一、引言

数据平台现有的调度系统(Jarvis)、哨兵系统(Sentinel)存在一些设计与实现上的不足，缺乏足够的稳定性与扩展性，不利于后期维护。本次重构将改进系统整体设计，以Akka开源框架为核心，提升系统稳定性及扩展性。



## 二、总体设计

### 2.1 需求概述

- 系统具备足够的稳定性、扩展性，支持任务的并行调度，保证任务不重复、不错漏执行

- 提高系统的容错性，主要组件无状态化，以便于服务的快速恢复

- 支持多种类型（自定义扩展）的任务执行，如：Hive、Shell、MapReduce、Spark等

- 不同类型的任务具有各自的任务接收策略，以支持对后端系统（如：Hadoop）的特定控制，如：流控、Load、CPU使用率、内存使用率等

- 任务支持重试、重试次数、重试间隔的配置

- 支持任务类型：纯时间任务，单亲纯依赖任务，多亲时间+依赖任务

- 支持依赖配置：runtime依赖，offset依赖

- 支持历史任务按照新的依赖关系重跑

- 在一个task ready之前，对该job配置关系的任何修改（比如调度时间和依赖关系）能够动态生效。

- 支持细粒度周期（小于天）任务的调度

- 支持对不同调度周期的任务依赖配置

- 统一的日志收集中心，更好地支持日志的随机读写（offset + lines），并改进对有实时读取日志需求应用的支持（如：Ironman）

- 支持任务的非功能性扩展，如：用户权限验证

- 开放系统接口，对外提供REST API



### 2.2 系统设计

![系统设计](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/jarvis_design.png)



#### 2.2.1 系统组成

![系统设计](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/server_topology.png)

- Server

>与worker,rest server,DB交互

>接收rest server的请求，添加、修改任务

>核心模块由Scheduler和Dispatcher组成，负责任务的调度和分发。

>监听Worker的注册信息

>接收Worker发送的心跳汇报

>以HA方式运行

>无状态，持久化信息维护在DB中，异常重启可以重建所有job和task

- Worker

>向Server注册信息

>根据接收策略决定是否接受Server发送的任务

>执行接受的任务并管理其生命周期

>周期性向Server汇报心跳、任务统计信息等

>将任务运行过程中输出的日志发送给LogServer存储

- LogServer

> 无状态，LogServer可以配置多个，通过提供一个serverList的方式轮询或者hash选择一个进行访问。

> 写操作的时候，logserver具有事务性。必须保证同一个文件的所有写操作都在同一台logserver上进行。

- RestServer

>无状态，可以配置多个，通过nginx进行负载均衡

>restServer和server分开来的好处和logserver一样。

>与Server、Worker、LogServer进行数据交互，提供统一的REST API（任务调度、任务修改、状态查询、日志查询、Worker的上下线等）

- WebUI

>为用户提供Web操作界面，如：应用接入、配置任务、任务查看、重跑任务等

- 数据库

>存储接入应用的配置信息

>存储任务的配置信息（类型、调度时间、依赖、创建者等），任务的执行记录等


### 2.3 模块设计

#### 2.3.1 调度器(Scheduler)设计

调度器作为调度系统的心脏，主要作用是进行任务的调度。在该系统设计中，能实现对纯时间任务，单亲纯依赖任务，多亲时间+依赖任务的调度。

调度器由三个子模块组成：TimeScheuler, DAGScheduler和TaskScheduler。

三个调度器协同工作，共同完成对各种任务的调度，当完成对一个job的调度之后，提交一个task给Dispatcher。

调度器（Scheduler）和分发器（Dispatcher）之间是生产者和消费者的关系，Scheduler提交task给Dispatcher。只要Dispatcher空闲，就会拿走进行分发。


![调度器设计](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/scheduler_design.png)

如上图所示：  

- TimeScheduler对纯时间任务进行调度，内部维护一个定时器每隔一秒扫描是否有任务调度时间已经到达。当添加一个任务的时候计算其下一次时间，当时间到达的时候，提交给TaskScheduler，并自动计算自己下一次时间接着调度。

- DAGScheduler对依赖任务进行调度，内部通过一个JobGraph维护了各个job之间的父子关系。当有task状态反馈的时候，TaskScheduler会进行状态处理，并且会触发DAGScheduler进行后续任务的调度。对DAGScheduler的任意一次依赖触发，都会进行依赖检查，如果通过依赖检查，根据不同的job类型进行后续操作：如果是单亲纯依赖任务，直接提交给TaskScheduler；如果是时间+依赖任务，提交给TimeScheduler进行时间调度。

- TaskScheduler对task进行调度，接收task状态反馈，进行持久化、失败重试、后续任务触发等逻辑。TimeScheduler和DAGScheduler相当于生产者，输入job；TaskScheduler相当于消费者，输出为task. 为了处理重跑等逻辑，TaskScheduler内部还维护了一个TaskGraph进行task的依赖触发，不会干扰DAGScheduler的正常调度。


#### 2.3.2 分发器(Dispatcher)设计

Dispatcher通过push的方式，由任务分发器按照可扩展的分发策略，主动推送任务给某一个worker执行任务。

Dispatcher由ExecuteQueue,TaskDispatcher,RetryScheduler 3个子模块组成。

![分发器设计](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/dispatcher_design.png)

- ExecuteQueue维护已经进入ready状态的task，由Scheduler提交，由TaskDispatcher进行消费。在该设计中，支持按照优先级先后顺序进行分发任务，所以内部实现是一个优先级队列。每次会把优先级最高的任务拿出给TaskDispatcher进行消费。

- TaskDispatcher从执行队列中获取task，通过可扩展的分发策略提交给某一个worker进行执行。默认的分发策略：轮询分发策略(RoundRobin)、随机分发策略(Random)。同时，TaskDispatcher还支持流控策略。

- RetryScheduler负责对拒绝的任务进行重试，通过配置的拒绝策略，延迟一段时间后再重新提交给执行队列。

	
#### 2.3.3 job executor设计

TODO	


#### 2.3.4 任务接受策略(Job Accept Strategy)

任务接受策略用于控制Worker或任务后端执行系统的负载，Worker根据负载情况决定是否接受Server发送的任务。

- 任务接受策略可自定义扩展

- 接收策略分为公用和任务自定义两种，公用的接收策略每种任务类型都会匹配，同时每种类型的任务可定义各自不同的接受策略

- 接收策略以链式方式执行，当其中任一接收策略没有通过，则失败，返回拒绝。

- 默认实现的任务接受策略：

Worker：LoadStrategy、CPUStratery、MemoryStrategy

Hive任务：YarnStrategy



### 2.4 处理流程

#### 2.4.1 任务执行流程

![任务执行流程](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/job_execution_flow.png)



#### 2.4.2 任务终止流程

![任务终止流程](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/kill_job.png)



#### 2.4.3 任务重跑流程

调度系统的server不提供任务重跑的逻辑，由外部系统自己计算需要重跑的任务，然后传给调度系统。

![任务重跑流程](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/job_rerun.png)  

如上图所示是外部系统重跑的逻辑

针对第3点，比如任务b依赖于任务a1，a2，a3，T表示运行周期，则  T(b) = min(T(a1), T(a2), T(a3))



#### 2.4.4 调度计划修改流程

![调度计划修改流程](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/plan_modify.png)  

如上图，当修改任务的内容的时候，只需要修改数据库中的内容，不会影响调度逻辑。调度系统不会在内存中维护任务的执行内容，每次会去数据库中动态拿。

当修改定时任务的执行时间时，需要把TimeScheduler中对应的任务删除，再重新添加。

当修改依赖任务的依赖关系时，需要修改内存中维护的任务原信息中依赖关系，并重新计算对应的父亲和孩子。（如果必要的话这里要做环路检测）



#### 2.4.5 Executor交互流程

![Executor交互流程](http://gitlab.mogujie.org/bigdata/jarvis2/raw/master/docs/design/img/job_executor.png)

Job是一个抽象类，包括preExecute()、execute()、postExecute()3个抽象方法，根据各自需求实现这些方法以此来支持不同类型的任务，其中JobContext包含了任务运行参数，如：ID、名称、类型以及其它扩展参数等。
Server将任务提交至Worker，然后Worker分别先后执行preExecute()、execute()、postExecute()方法，并且Worker在执行前后会向Server汇报任务状态，任务运行过程中生成的日志由LogCollector发送给LogServer，最终由LogServer保存至存储系统中(可以是HDFS、HBase等)。

### 2.5 涉及技术

- 分布式：Akka

- 依赖注入：Spring

- 数据库连接池：HikariCP

- DAO：MyBatis

- 内部通讯协议：Netty 、Protocol Buffers

- RESTful：Jersey

- 其它：guava

### 2.6 FAQ

- 系统启动的时候做什么？

>worker启动的时候会向master发送心跳，同时扫描本地文件系统，发现有任务没有发送成功的，再次发送。

>master启动的时候，接收worker发送过来的心跳，如果通过权限校验则把worker加入workerManager中。同时从DB load任务原信息，在内存中计算任务的孩子和父亲，并把定时任务加入到TimeScheduler中。

- 任务如何调度？

>任务分为定时任务和依赖任务，定时任务通过配置crontab表达式定义自己的启动时间，依赖任务通过配置依赖关系，当前置依赖都满足的时候触发依赖任务。

>当系统启动的时候，会把所有任务的原信息从数据库中load到内存中来，并计算任务的孩子和父亲，维护在内存中。如果任务配置了crontab表达式，则加入TimeScheduler中。

>TimeScheduler是一个时间调度器，满足时间就会提交任务到TaskScheduler中。

>TaskScheduler内部维护一个executeQueue和jobDispatcher，jobDispatcher并发地从executeQueue中取任务执行。

>当jobDispatcher取到一个任务，就会获取这个任务的孩子是谁，然后把孩子注册到DAGScheduler的observer中。

>当某个任务执行成功，TaskScheduler的statusManager就会向DAGScheduler发送successEvent，某个jobListener监听到自己的依赖有这个成功的任务时，就会更新自己依赖任务的状态，当依赖全部满足时，就会提交任务到TaskScheduler中。

- 任务如何分发？

>任务分发器从执行队列中通过多线程方式并发取任务，然后通过可扩展的分发策略，从workerManager中选一个负载比较小的worker，发送任务。

>任务分发器取任务的时候会优先选取优先级比较高的任务。

- 任务如何工作？

>任务运行在worker本地，当worker接收master发送过来的任务时，会根据任务类型启动相应的任务。如果是shell类型的任务，会fork一个进程来执行。

- 任务的生命周期和持久化？

>任务(Task)状态：等待中（WAITING），准备中(READY)，运行中(RUNNING)，成功（SUCCESS），失败(FAILED)，删除（KILLED）

> ready包含4个子阶段：ready.queue(队列中)  ready.dispatching(分发中)  ready.accepted(接受) ready.Rejected(拒绝了)。

>特别的：任务从server提交到client端，还有个隐含的状态，提交中(SUBMITTING)，但是这个状态极其短暂，发过去之后不是接收就是拒绝，所以没有持久化在数据库中。
rejected(拒绝)状态也很快，马上切换为 dispatching(分发中)

>任务持久化在数据库中，每一次更新任务状态都确保能更新到数据库中

>当任务生成执行计划的时候，任务初始状态为WAITING。当任务进入执行队列时，状态更新为READY。当任务被work接收时，状态更新为ACCEPTED，反之更新为REJECTED。当worker开始运行任务的时候，状态更新为RUNNING。当任务运行成功后，状态更新为SUCCESS，反之更新为FAILED。

- 任务如何标识？

>表结构设计中，主要有job表(任务的配置表，包括任务的cron表达式，起始时间等)，task表(任务的每一次的执行)，所以在调度系统的数据结构中，有jobid和taskid两个字段。

>因为任务支持重试策略，还有attemptid。

>jobid，taskid和attemptid在调度系统中分成三个字段存储，这样和存成一个字段的好处是，每次新增一个taskid或者attemptid，不需要先split。

>最终反映在jobname上，jobname = originjobname_jobid_taskid_attemptid，这样可以唯一标识一个job，在yarn上观察和kill都很方便，也支持现有bgmonitor通过jobname唯一标识一个任务的需求。