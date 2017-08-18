### Case 1: 对于过去偏移的依赖的任务触发   
 
| 10.10  | 10.11  | 10.12  | 10.13 | 10.14 |
| ------ | ------ | ----   | ----  | ----  |
| a1     | a2     |   a3   |  a4   |  a5  |
|        |  b2    |   b3   |  b4   |     |

如果a1失败了，重跑a1的时候应该触发b2,b3,b4重跑。  
如果不用TaskGraph如何做？  
1)	根据a1的调度时间，和B->A的依赖表达式d(-3,0)算出反向表达式d(0,3)，然后算出a1应该被哪一个range的b依赖。  
2)	通过range和b的cron表达式，算出这个range内b的所有执行计划  
3)	通过执行计划去数据库里搜索，如果没有找到相应的task，则进行依赖检查  
4)	如果通过依赖检查，新建task。  

但是这种方案也有几个问题：  
1)	首先DAGScheduler依赖检查变得复杂了，本来每次做依赖检查的时候，对于一个job来说，只会新起一个task，但是对于offset依赖任务，可能会新起好几个  
2)	如果调度时间改变过，可能会有问题。比如a1当天跑成功了，b2第二天也跑成功了，时间为1:00。然后把B时间从1:00改为2:00。之后重跑a1失败了。之后几天b3,b4因为a1失败也没有跑。然后重跑a1，根据上面的做法算出b2~b4，并且调度时间为2:00，但是对于b2来说，当天已经有一个1:00跑成功了，不需要再触发跑了。  
3)	a1重跑的时候如何确定当天b的调度时间已经到了呢，比如a1是10.10号的任务，当天10.12号，当天把a1重跑，会触发10.11~10.13的b跑，可是今天才10.12号呢，不能把10.13的b跑起来。  

### Case 2: 时间+依赖任务触发  
当前做法，对于每一个DAGJob，时间到的时候打一个flag，如果通过依赖检查再复位。这种做法有一个bug  

```
A   B  
 \ /  
  C
```
   
   如果a1失败了, b1成功了，之后C时间到了，time flag=true，没有通过依赖检查，time flag还是true; 第二天a2成功了，b2也成功了，看到C的time flag=true，就会通过依赖检查，然后复位为false，这个时候重跑a1，发现时间没有满足，所以也不会通过依赖检查。  
还有一种情况，第一天同上，第二天C时间到了，time flag=true, a2和b2通过依赖检查，之后time flag=false。之后再重跑a1，也无法再触发c1了。

解决方案：只有纯依赖任务才加入TimeScheduler，时间+依赖任务通过依赖检查之后，算出自己的调度时间，加入TimeScheduler. TimeScheduler时间到的任务不再走DAGScheduler做依赖检查，直接提交给TaskScheduler.  
这种方案理论上可行，关键是如何算出子任务的调度时间？

### Case 3: 系统中断一段时间重启后能恢复所有任务  
对于时间任务来说，由TimeScheduler进行调度，对于一个job来说，每次算出下一次调度时间，加入TimeScheduler中。当时间到的时候，拿走，算出下一次时间放进去。  
举例：比如a1一点开始跑，计算出下一次2:00加入TimeScheduler中。在1:30的时候系统中断，6:00才恢复。TimeScheduler扫描到2:00小于当前时间，拿走，并计算出下一次3:00加进来。3:00也小于当前时间，拿走，再把4:00加进来…然后依赖任务也会自动触发。整个调度过程由调度器自动完成，不需要人工参与。

### Case 4: 异常重启流程  
当前server重启可以做到恢复大部分任务和状态，但是有一种情况可能会丢失状态，即DAGDependChecker中对runtime task的依赖，是没有持久化的。  
这部分我是想反正是runtime的，上次调度依赖丢了，还会有下次触发他，他也没强制要求必须依赖于上一次，所以就没有持久化。这部分需要根据业务场景再考虑下，如果要持久化，也是可以做的。

### Case 5: 纯时间任务如何实现？  

新加入的时候以当前时间算下一次，之后每次时间到了从TimeScheduler拿走，算下一次加入

### Case 6: 纯依赖任务如何实现？

由父任务成功后触发

### Case 6.1: 依赖单个任务？

前一个任务跑完，后一个任务就跑

### Case 6.2: 依赖多个任务，且都是runtime的依赖

前面任务跑完，后面就跑

### Case 6.3: 依赖多个任务，且都是past offset依赖



### Case 6.4: 依赖多个任务，且都是current offset依赖

### Case 6.5 依赖多个任务，且都是future offset依赖 （暂不支持）

### Case 6.6 依赖多个任务，有runtime和past offset依赖

### Case 6.7 依赖多个任务，有runtime和current offset依赖

### Case 7: 时间+依赖任务如何实现？

当前做法是先进行时间检查，再进行依赖检查，但是时间检查如果只通过一个时间标识来表示，貌似有bug，可以看case 2

### Case 8: 不同周期的依赖  
比如C依赖于A和B， A 1小时跑一次，B 3小时跑一次。对于不同周期的依赖如何处理，当前做法是子任务配置调度时间。然后以子任务调度时间为准。

### Case 9: 串行任务如何支持？  
即一个任务执行还要依赖于自己上一次成功。

### Case 10: 当前业务大部分业务是依赖于当天，如果正确选择要依赖于哪几个task?  
比如D依赖于A,B,C  

| 时间    | A   |  B  | C   | 
| ------ | ---- | ----| ---- | 
| 10.10  | a1   |     | c1   |  
| 10.11  | a2   |  b2 |    |
| 10.12  | a3   |  c2 | c3   |

如果收到上面这堆task，如何正确通过依赖检查？

### Case 11: 原地重跑task

原地重跑，taskId不变，会触发后续任务执行

### Case 12: 手动重跑job

输入JobId和起止时间，按照新的依赖关系重跑历史任务。当前有两个方案：1）算出所有task并建立依赖关系，加入TaskGraph，由TaskScheduler触发。2) 新建job和依赖关系，由DAGScheduler触发

### Case 13: 强制执行task

通过父任务成功才触发，将不再有这个功能。

### Case 14: 跑一次性任务

比如ironman提交一次性任务，没有调度时间也没有依赖关系，新加入JobGraph的时候做依赖检查直接通过，则可以直接启动task.

对于一次性任务，有没有必要走调度逻辑？直接提交一个task的好处是不需要新建job，再定期去清理了。

### Case 15: 强制修改task状态(如果改成成功，应该要自动触发后续任务执行)

一个任务失败了，block了后续的任务。通过手工把这个任务跑成功了，之后可以通过强制修改task状态，并触发后续任务接着跑

### Case 16: kill task

kill task不会触发后续任务

### Case 17: 查询job依赖图

可以查询一个job的父亲和孩子

### Case 18: 查询Task执行情况，即task依赖关系

可以查询一个task的父亲，如果孩子已经产生，也可以查看孩子

### Case 19: 新增job

### Case 20: 修改job flag
修改为disable，JobGraph还是维护该job依赖关系，只是不会进行依赖检查和后续触发。前端查询job依赖关系，还是能查看到disable的job，只是显示颜色不一样。
修改为deleted，则从JobGraph中删除。

这里需要注意的是，如果一个job从enable改为其他状态，需要触发后续任务的执行。比如C->A,B，A成功了，B失败了，这个时候把B禁用，C应该被调度起来。

### Case 21：修改job

### Case 21.1: 修改调度时间
需要更新TimeScheduler中该job下一次调度时间

### Case 21.2 增加调度时间
注意任务的依赖关系可能从纯依赖变成时间+依赖，依赖检查的条件不一样了

可能会造成当天任务又跑了一次

### Case 21.3 移除调度时间
注意任务的依赖关系可能从时间+依赖变成了纯依赖，依赖检查条件不一样了

一个任务如果依赖已满足，时间未到，移除调度时间后，需要马上调度起来。

### Case 21.4 增加job依赖关系，父任务已跑完

如果子任务当天已跑过，加入依赖关系后子任务马上再跑一次  

### Case 21.5 增加job依赖关系，父任务尚未开始跑

如果子任务当天已跑过，之后新的父亲跑完后会再次调度子任务。

### Case 21.6 移除job依赖关系，父任务已跑完

如果子任务当天已跑过，移除依赖关系后，子任务会再跑一次

### Case 21.7 移除job依赖关系，父任务尚未开始跑

如果子任务被该父任务block，移除依赖关系后会马上开始跑

### Case 21.8 修改依赖关系，比如移除一个依赖关系，增加另一个依赖关系

修改依赖关系的要事务性，不能添加一个就去做依赖检查

### Case 22:  B依赖于A过去3天，正常逻辑？

### Case 23:  B依赖于A过去3天，且都成功了，这时候成功A过去某一天的数据，应该不需要再重新触发B了

### Case 24:  B依赖于A过去3天，某一天失败了，这时候重跑A，应该要触发对应的B

### Case 25:  B依赖于A过去3天，某一天失败了，过了好几天重跑，这时候可能要触发好几个B

### Case 26:  B依赖于A过去3天，且都成功了，但是中间B修改过调度时间，重跑某一天的a，应该不能触发B调度

### Case 27:  B依赖于A过去3天，某一天失败了，但是中间B修改过调度时间  

| 10.10  | 10.11  | 10.12  | 10.13 | 10.14 |
| ------ | ------ | ----   | ----  | ----  |
| a1     | a2     |   a3   |  a4   |  a5  |
|        |  b2    |   b3   |  b4   |     |

如上图，10.10 a1跑成功了，10.11 b2跑成功了，时间为10.11 1:00，这个时候把B时间从1:00->2:00， 当天重跑a1失败了，之后b3,b4不能执行，10.13重跑a1，应该只重跑b3,b4就可以了

### Case 28:  固定延迟任务
（1）固定延迟任务依赖非固定延迟任务；
（2）非固定延迟任务依赖固定延迟任务；

### Case 29: 创建调度Job后，需要补追历史调度
如2015.12.25创建Job（调度起始时间为2015.12.25，调度结束时间不限），如果要补追2015年完整的历史调度，这个支不支持？还是要把调度起始时间修改为2015.01.01才支持？

### Case 30: 串行任务支持

### Case 30.1: 纯时间任务
A是纯时间任务，每天1:00跑

| 10.10  | 10.11  | 10.12  | 10.13 | 10.14 |
| ------ | ------ | ----   | ----  | ----  |
| a1     | a2     |   a3   |  a4   |  a5  |
| F      |      |      |     |    |

如上表，a1失败，造成后面几天都没跑起来，重跑a1成功后，只能触发a2跑起来，a2跑成功之后再触发a3开始跑...

### Case 30.2 时间+依赖任务
B依赖于A，并且是串行任务，如下图

```  
      A   
      |
 B <- B
```

``` 
    F   F   T
    a2  a3  a4
    |   |   |
b1<-b2<-b3<-b4
T   
```
如上图，a2失败，a3失败，b2,b3,b4都没跑起来。

1） 这时候重跑a2，应该只能触发b2跑起来。b2跑成功后，不会触发b3跑起来，因为a3还失败，再重跑a3成功后，会触发b3跑起来。b3跑成功后会触发b4跑起来。  

2）如果重跑a3成功，不应该触发b3跑起来，因为之前还有个b2没跑。等到重跑a2成功后，b2会跑起来，等b2跑成功后会触发b3再跑起来。