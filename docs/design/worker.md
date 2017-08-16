# Worker 实现方案


## 概要



## 待细化问题

* worker状态的变更, 在worker内部, 可以通过observer模式实现? 这样状态变更是否直接写DB, 是否透过代理写DB, 是否写ZK, 是否汇报给Master, 严重错误是否报警,  是否发监控数据, 都可以通过注册不同的listener来实现. 逻辑互相不影响. 就是listener调用链要做好,不能有一个listener block 其它listener的执行. 不知道这一点有什么好的实现办法没有.  或者比如只是调用listener的put方法, 重要的listener实现的时候用同步处理的方式(比如写DB和通知master), 其它listener使用异步处理的方式(比如通知报警,发邮件,监控统计之类). -> java.util.Observable;


## 当前Todo

