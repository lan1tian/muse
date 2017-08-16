Rest API
======

## 一、任务基本描述

1. 任务称为job，拥有唯一的jobId. 可以配置时间表达式由时间触发，也可以配置依赖表达式由前置依赖触发。可以周期性执行或一次性执行。
2. job的每一次执行（周期性可以有多次，一次性任务就一次）称为作业，即task，拥有唯一的taskId。 task有状态，比如等待、准备、执行中、成功、失败等。
3. job可以配置失败重试策略，task如果失败可以进行失败重试，叫做attempt. 每一次attempt，jobId和taskId都不会变，attemptId自增一。

## 二、接入规范  

1. 联系@鸣人，@光明，@无崖，注册App  
2. 通过注册的appKey生成token
3. 通过restAPI接口接入调度系统

## 三、接口描述

* request: 所有接口参照如下json格式，在parameters这个参数实现自己的json

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| user (String) | 用户名  |   是   |     |
| appToken (String)|timestamp+MD5(appKey+timestamp)| 是 |  |
| appName (String)| 业务系统名称| 是 |  |
| parameters (String) | json格式参数map | 是 | | 

* response: 所有接口返回值按照如下格式

| 参数  | 解释  | 
| ------ | ------  |
| code (int) | 错误码，0:成功，1:失败，9999:系统异常 | 
| msg (String)| 错误信息|
| data (String)| json格式返回值 |

---

### 1. 提交job

* 接口地址：http://sentinel.bigdata.service.mogujie.org/api/job/submit
* 方法POST
* request parameters参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| jobName (String) | 任务名称  |   是   |     |
| jobType (String)| 任务类型，比如hive,presto,shell等| 是 |  |
| status (int)| 任务状态，1:有效； 2:无效；3：过期；4：垃圾箱；5:暂停| 否 | 1 |
| contentType (int)| 执行内容类型， 1: 文本，2:脚本，3: jar包 | 否 | 1:文本 | 
| content (String)| 执行内容 | 是 | |
| params (String)| json格式扩展参数 | 否 | {} | 
| appName (String)| 业务名称 | 是 | |
| workerGroupId (int)| 工作组Id | 否 | 1|
| department (String)| 部门 | 否 | 空 |
| bizGroups (String)| 业务线Id串，比如",1,2," | 否 | 空|
| priority (int)| 优先级 | 否 | 1 |
| isTemp (boolean)| 是否是一次性临时任务 | 否 | 0 |
| activeStartDate (long)| 起始日期时间戳(毫秒) | 否 | 0 |
| activeEndDate (long)| 结束日期时间戳(毫秒)| 否 | 0 |
| expiredTime (int)| 过期调度时间（秒），超过该时间没调度起来，则不调度| 否 | 24小时 | 
| failedAttempts(int)| 失败重试次数 | 否 | 0 |
| failedInterval(int)| 失败重试间隔（秒）| 否 | 3 |
| dependencyList(list of DependencyEntry) | 依赖关系列表 | 否 | null |
| scheduleExpressionList(list of ScheduleExpressionEntry) |时间表达式列表 | 否 | null |

* DependencyEntry参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| operatorMode(int)   | 操作类型，1:ADD; 2:EDIT; 3:DELETE  |   是  |     |
| preJobId (long)|前置依赖任务ID| 是 |  |
| commonStrategy(int)| 通用依赖策略。1:ALL, 2:LASTONE, 3:ANYONE| 否 | 1:ALL |
| offsetStrategy(String)| 偏移依赖策略 | 否 | cd(当天)| 

* ScheduleExpressionEntry参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| operatorMode(int)   | 操作类型，1:ADD; 2:EDIT; 3:DELETE  |   是  |     |
| expressionId (long)|时间表达式ID| 否 | 0 |
| expressionType(int)| 时间表达式类型，1:cron; 2:rate; 3:delay; 4:ISO8601| 否 | 1:crontab|
| expression(String)| 时间表达式 | 否 | 空 | 

* response返回data说明

| 参数  | 解释  | 
| ------ | ------  |
| jobId (long) | 提交任务返回的jobId |

 
---

### 2. 根据jobId查询taskId列表

---

### 3. 根据jobId查询task状态（只适用于一次性任务）

---

### 4. 根据taskId查询task状态

* 接口地址：http://sentinel.bigdata.service.mogujie.org/api/task/query/status
* 方法POST
* request parameters参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| taskId (long) | 作业ID  |   是   |     |

* response返回data说明

| 参数  | 解释  | 
| ------ | ------  |
| taskId (long) | taskId |
| status (int) | task状态，1:waiting；2:ready；3:running；4:success；5:failed；6:killed；99:removed |

---

### 5. kill task

* 接口地址：http://sentinel.bigdata.service.mogujie.org/api/task/kill
* 方法POST
* request parameters参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| taskIds (list of Long) | 作业ID链表  |   是   |     |


---

### 6. 查询task执行结果

---

### 7. 分批查询task执行结果（结果很大的情况下）

* 接口地址：http://sentinel.bigdata.service.mogujie.org/api/log/readResult
* 方法POST
* request parameters参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| taskId (long) | 作业ID  |   是   |     |
| attemptId (int) | 尝试ID  |   否   |  1   |

* response返回data说明

| 参数  | 解释  | 
| ------ | ------  |
| log (String) | 执行结果 |
| offset (long) | 本次查询的偏移|
| isEnd (boolean) | 是否查询结束|


---

### 8. 查询task日志

--- 

### 9. 分批查询task日志（日志很大的情况下）

* 接口地址：http://sentinel.bigdata.service.mogujie.org/api/log/readExecuteLog
* 方法POST
* request parameters参数列表

| 参数  | 解释  | 是否必要  | 默认值 | 
| ------ | ------ | ----   | ----  |
| taskId (long) | 作业ID  |   是   |     |
| attemptId (int) | 尝试ID  |   否   |  1   |

* response返回data说明

| 参数  | 解释  | 
| ------ | ------  |
| log (String) | 日志 |
| offset (long) | 本次查询的偏移|
| isEnd (boolean) | 是否查询结束|


## 四、使用示例