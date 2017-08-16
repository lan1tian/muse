- MyBatis transactions
  
  ![transaction](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/images/tx.png)
  
- MyBatis @Transactional annotation
  
  | Property            | Default             | Description                              |
  | ------------------- | ------------------- | ---------------------------------------- |
  | executorType        | ExecutorType.SIMPLE | the MyBatis executor type                |
  | isolation           | Isolation.DEFAULT   | the transaction isolation level. The default value will cause MyBatis to use the default isolation level from the data source. |
  | force               | false               | Flag to indicate that MyBatis has to force the transaction commit() |
  | rethrowExceptionsAs | Exception.class     | rethrow caught exceptions as new Exception (maybe a proper layer exception) |
  | exceptionMessage    | empty string        | A custom error message when throwing the custom exception; it supports java.util.Formatter place holders, intercepted method arguments will be used as message format arguments. |
  | rollbackOnly        | false               | If true, the transaction will never committed, but rather the rollback will be forced. That configuration is useful for testing purposes. |
  
- MyBatis @Transactional VS Spring @Transactional 
  
  Spring的AOP事务管理默认针对unchecked exception回滚(RuntimeErrorException，RuntimeException)，checked exception不回滚。
  
  MyBatis默认针对所有异常都回滚。
  
- DAO与内存操作 @Transactional 回滚
  
  D：数据库操作
  
  M：内存操作
  
  *：表示一个或多个操作
  
  | Case（同一方法内执行） | 回滚处理                             |
  | ------------- | -------------------------------- |
  | D\*           | 无需处理                             |
  | D\*M\*        | D异常：无需处理  M异常：捕获异常，内存回滚，然后重新抛出异常 |
  | M\*D\*        | 捕获异常，内存回滚，然后重新抛出异常               |
  
- 内存事务
  
  STM、CAS？
  
  开源STM框架
  
  https://github.com/pveentjer/Multiverse
  
  http://www.javacreed.com/software-transactional-memory-example-using-multiverse/



- 当Worker与Server失联后，如何异常处理？

Worker可自身维护任务的状态数据，并标记汇报状态（是否向Server汇报成功）。

如因网络问题导致Worker无法向Server进行心跳与任务状态汇报超过一定时间时，Server将所有发送到该Worker的任务状态置为Unknow（或者Failed，但无法区分失败原因）。在Worker与Server失联期间，Worker将任务状态持久化，并定期重试向Server汇报任务状态。

如因Worker crash导致的与Server失联，重启时从状态数据中进行状态恢复，将Success或Failed的任务正常向Server汇报，将Running的任务汇报为Failed。

PS：为使Worker无状态化，可考虑将状态数据存储 StateStore 接口化，提供多种Store方式，如：LocalFileSystemStateStore、DistributionFileSystemStateStore、ZooKeeperStateStore等，支持自定义配置。也可以考虑把任务状态恢复的逻辑也接口化，各个任务类型自己来实现，比如：shell任务可以保持之前的做法把exitcode存到本地文件中，hive、mr任务向rm查询。

