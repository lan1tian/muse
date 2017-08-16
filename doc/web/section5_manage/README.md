系统管理
======

## 调度系统管理

        可以启动或关闭调度系统

## 应用管理
    
        每个接入jarvis的应用都需要唯一的应用名称与appKey，添加应用后，第三方应用就可以对接jarvis的RESTful接口。
    
## Worker及WorkerGroup管理
* worker管理
    
        worker需要通过authKey接入jarvis，jarvis管理器将会自动将worker匹配到所属的workerGroup。
            
* workerGroup管理

        根据业务或任务自定义配置分组，在新增的时候将会自动生成一个authKey，每个分组包含多个worker。

## 业务标签管理

        业务标签用于配置任务时的业务描述，方便后期管理。业务标签管理可以维护整体标签列表。

