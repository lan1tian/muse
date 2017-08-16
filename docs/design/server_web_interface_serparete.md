# Server & Web 接口分离的讨论方案

## 现状
目前，一部分与web管理相关的接口，是通过server来设置的；
为了让Serve只做单纯的调度事情，需要把这部分管理用接口分离出来。

## 接口Web与server相关动作

需要分离的接口名|web功能|与server无关功能|与server有关功能|server通知|是否分离
---|---|---|---|---|---
app相关|查询，增加，修改，删除|app修改[所有者，成员，app类型(是否管理用)]|app增加，app删除，app修改[app名称，app状态（停用/启用），最大任务并行数]|√|
app&workerGroup|查询，增加，删除||app与WorkerGroup的关系，增加，删除|√|
worker|查询，修改||worker状态变更（启用，停止）|√
worker&workerGroup|查询，增加，删除||worker与WorkerGroup的关系，增加，删除|√
alarm报警配置|查询，增加，修改，删除|查询，修改，增加，删除||×

* 备注： 报警alarm方案待定，可能会从jarvis中移除出来，放到其他平台配置。


## 分析与方案
从表格来看，app，worker等的写变更，需要通知server的。

### 方案一
维持现状，写操作走Server接口。

### 方案二
* 写操作通过jarvis-Web来做；
* 然后Server提供通知接口，Web修改后通知Server刷新数据。

### 方案三
* 写操作通过jarvis-web来做。
* server定期刷缓存（比如每隔1分钟自动更新数据），Web修改后不通知server。


### 方案四
* 方案二与方案三混合，一部分提供通知接口，一部分通过缓存有效期刷缓存。