## 表结构设计


### job表 

| 字段    | 类型    |  主键  |是否为NULL | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| jobId    | int(11) | key|F|   | 任务Id           | 
| jobName  | string  |    |F|'' | 任务名称          | 
| type     | tinyint  |    |F|1  | 任务类型 1：hive；2：shell；3：java； 4：MR； | 
| content  | string |     |F|'' | 任务内容          | 
| priorty  | tinyint |     |F|'' | 优先级          | 
| appName|string|   |F|''| 应用名称 |
| workerGroupId  | int(11) |     |F|'' | worker组ID| 
| reject_retries   | int32 | | F    | 0    | 任务被Worker拒绝时的重试次数      |
| reject_interval   | int32 | | F    | 3    | 任务被Worker拒绝时重试的间隔(秒) |
| failed_retries   | int32 | | F    | 0    | 任务运行失败时的重试次数      |
| failed_interval   | int32 | | F    | 3    | 任务运行失败时重试的间隔(秒) | 
| submitUser | string |     |F|'' | 提交用户           | 
| executUser | string |     |F|'' | 执行用户           | 
| createTime  | int(11)|     |F|0  |   创建时间      | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 


### jobDepend表
| 字段    | 类型   |  主键  | 是否为NULL | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| jobId  | int(11)   | key|F    |      | jobId           | 
| preJobId  | int(11) |key  | F    |   0   | 前置JobId    | 
| updateUser| string  | | F    |   ''   | 更新用户     | 
| updateTime| int(11) | | F    | 0     |   更新时间      | 


### jobDependStatus表
| 字段    | 类型   |  主键   | 是否为NULL   | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| jobId | int(11) |key|F    |      | jobId       | 
| preJobId  | int(11) |key  | F    |   0   | 前置JobId    | 
| preTaskId  | int(11) |key  | F    |   0   | 前置taskId  | 
| preStatus   | string |  | F    |  0    | 前置依赖的状态信息 0 未完成，1完成  | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 


### crontab表
| 字段    | 类型  |  主键  | 是否为NULL | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| cronId    | int(11) |key  | F    |      | cronId           | 
| jobId     | int(11) | | F    |   0   | 所属jobId           | 
| status    | tinyint |  | F    |   1   | 状态：0无效；1有效           | 
| exp       | string  | | F    |   ''   | cron表达式           | 
| startDate | int(8)  | | F    | 0     | 开始日期  YMD     | 
| endDate   | int(8)  | | F    |  0    | 结束日期  YMD     | 
| updateUser    | string  | | F    |   ''   | 更新用户     | 
| updateTime| int(11) | | F    | 0     |   更新时间      | 


### task表
| 字段    | 类型   |  主键   | 是否为NULL   | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| taskId | int(11) |key|F    |      | taskId       | 
| jobId   | int(11) |  | F    |      | 所属JobID          | 
| status  | tinyint | |F    |    1  | task状态： 1:waittng；2:ready；3:running；4:success；5:failed；6:killed   | 
| attemptNum | tinyint |     |F|0 | 尝试次数           | 
| attemptInfo | string |     |F|'' | 尝试信息,json格式| 
| submitUser | string |     |F|'' | 提交用户           | 
| executeUser | string |     |F|'' | 执行用户           | 
| executeTime  | int(11)|     |F|0  |   执行时间      | 
| dataTime  | int(11)|     |F|0  |   数据时间      | 
| createTime  | int(11)|     |F|0  |   创建时间      | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 



### worker表
| 字段     | 类型  |  主键    | 是否为NULL   | 默认值  | 描述  | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| workerId   | int(11) |key| F    |      | workerId       | 
| workerName | string  |   | F    |      | worker名称      | 
| ip         | string  |   | F    |      | ip地址          | 
| port        | int(8)  |   | F    |      | 端口          | 
| status      | tinyint  |   | F    |      | 状态。0：已下线；1：下线中；2：断线中； 3：上线。          | 
| createTime  | int(11)|     |F|0  |   创建时间      | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 


### workerGroup表
| 字段     | 类型  |  主键    | 必选   | 默认值  | 描述           | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| groupId    | int(11) |key | F    |      | workerGroupID    | 
| groupName  |  string |    | F    |      | group名称       |
| createTime  | int(11)|     |F|0  |   创建时间      | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 


### workerGroupRelation表
| 字段     | 类型   |  主键   | 必选   | 默认值  | 描述    | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| workerId  | int(11) | key|F    |      | workerID    | 
| groupId  |  string | key|F    |      | groupID      | 
| createTime  | int(11)|     |F|0  |   创建时间      | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 


### jobWorkerGroupRelation表
##### -- 描述job类型与worker组ID之间的关系。
| 字段     | 类型   |  主键   | 必选   | 默认值  | 描述    | 
| ------ | ------ | ---- | ---- | ------------ | ---- |
| jobType  | tinyint | key|F    |      | job类型    | 
| groupId  |  string | key|F    |      | work组ID      | 
| createTime  | int(11)|     |F|0  |   创建时间      | 
| updateTime  | int(11)|     |F|0  |   更新时间      | 












