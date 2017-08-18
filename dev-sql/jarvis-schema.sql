-- Create syntax for TABLE 'alarm'
CREATE TABLE `alarm` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `jobId` bigint(11) unsigned NOT NULL,
  `alarmType` varchar(32) NOT NULL DEFAULT '' COMMENT '报警类型，有多个逗号间隔:1-短信，2-TT，3-邮件，4-微信',
  `receiver` varchar(256) NOT NULL DEFAULT '' COMMENT '接受者，有多个逗号间隔。',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1-启用；2-禁用；',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '最后更新用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_jobId` (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=9012 DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'app'
CREATE TABLE `app` (
  `appId` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'appId',
  `appName` varchar(64) NOT NULL DEFAULT '' COMMENT 'app名称',
  `appKey` varchar(32) NOT NULL DEFAULT '' COMMENT 'appKey',
  `appType` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '类型：1：普通；2：管理',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1：启用；2：停用；3：删除',
  `maxConcurrency` int(11) unsigned NOT NULL DEFAULT '10' COMMENT '最大任务并行度',
  `owner` varchar(32) NOT NULL DEFAULT '' COMMENT '所有者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '最后更新用户',
  PRIMARY KEY (`appId`),
  UNIQUE KEY `idx_appName` (`appName`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='app表';

-- Create syntax for TABLE 'app_worker_group'
CREATE TABLE `app_worker_group` (
  `appId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'appId',
  `workerGroupId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'workerGroupID',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '最后更新用户',
  PRIMARY KEY (`appId`,`workerGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app_workgroup表';

-- Create syntax for TABLE 'biz_group'
CREATE TABLE `biz_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'bizGroupID',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'bizGroup名称',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1：启用；2：禁用；',
  `owner` varchar(32) NOT NULL DEFAULT '' COMMENT '所有者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '最后更新用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='biz_group表';

-- Create syntax for TABLE 'job'
CREATE TABLE `job` (
  `jobId` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'jobID',
  `jobName` varchar(256) NOT NULL DEFAULT '' COMMENT '任务名称',
  `jobType` varchar(64) NOT NULL DEFAULT '0' COMMENT '任务类型 hive; shell; java; MR',
  `status` int(3) NOT NULL DEFAULT '1' COMMENT '任务状态 1:有效； 2:无效；3：过期；4：垃圾箱；5:暂停',
  `contentType` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '执行内容类型 1: 文本，2:脚本，3: jar包',
  `content` mediumtext NOT NULL COMMENT '任务内容,脚本内容或脚本名',
  `params` varchar(2048) NOT NULL DEFAULT '{}' COMMENT '任务参数，json格式',
  `submitUser` varchar(32) NOT NULL DEFAULT '' COMMENT '提交用户',
  `priority` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '优先级,1:low,2:normal,3:high,4:verg high',
  `isSerial` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否串行。0：并行; 1：串行',
  `isTemp` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为临时任务',
  `appId` int(11) unsigned NOT NULL COMMENT '应用ID',
  `workerGroupId` int(11) unsigned NOT NULL DEFAULT '1' COMMENT 'worker组ID',
  `department` varchar(256) NOT NULL DEFAULT '' COMMENT '部门名称',
  `bizGroups` varchar(256) NOT NULL DEFAULT '' COMMENT '业务组名称，多个的话逗号分隔',
  `activeStartDate` datetime NOT NULL COMMENT '有效开始日期',
  `activeEndDate` datetime NOT NULL COMMENT '有效结束日期',
  `expiredTime` int(11) unsigned NOT NULL DEFAULT '86400' COMMENT '失效时间(s)，默认24小时',
  `failedAttempts` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '任务运行失败时的重试次数',
  `failedInterval` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '任务运行失败时重试的间隔(秒)',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '更新用户',
  PRIMARY KEY (`jobId`),
  KEY `index_submitUser` (`submitUser`),
  KEY `index_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=9136 DEFAULT CHARSET=utf8 COMMENT='job表';

-- Create syntax for TABLE 'job_depend'
CREATE TABLE `job_depend` (
  `jobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT 'jobId',
  `preJobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '前置JobId',
  `commonStrategy` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '通用依赖策略。1:ALL, 2:LASTONE, 3:ANYONE',
  `offsetStrategy` varchar(1024) NOT NULL DEFAULT '' COMMENT '偏移依赖策略',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '更新用户',
  PRIMARY KEY (`jobId`,`preJobId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='job依赖表';

-- Create syntax for TABLE 'job_schedule_expression'
CREATE TABLE `job_schedule_expression` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `jobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT 'JobId',
  `expressionType` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '1:cron; 2:rate; 3:delay; 4:ISO8601',
  `expression` varchar(64) NOT NULL,
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `index_jobId_type` (`jobId`,`expressionType`)
) ENGINE=InnoDB AUTO_INCREMENT=9021 DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'operation_log'
CREATE TABLE `operation_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `operator` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refer` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `detail` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '任务内容',
  `opeDate` datetime NOT NULL COMMENT '创建时间',
  `type` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型，job task',
  operationType varchar(2000),
  preOperationContent varchar(2000),
  afterOperationContent varchar(2000),
  PRIMARY KEY (`id`),
  KEY `index_title` (`title`(191)),
  KEY `index_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=295 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Create syntax for TABLE 'plan'
CREATE TABLE `plan` (
  `jobId` bigint(11) unsigned NOT NULL COMMENT 'jobID',
  `planTime` datetime NOT NULL COMMENT '计划调度时间',
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`jobId`,`planTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='plan表';

-- Create syntax for TABLE 'task'
CREATE TABLE `task` (
  `taskId` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'taskId',
  `attemptId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后的尝试ID',
  `jobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属JobID',
  `content` mediumtext NOT NULL COMMENT '任务内容',
  `params` varchar(2048) NOT NULL DEFAULT '' COMMENT '任务参数',
  `scheduleTime` datetime NOT NULL COMMENT '调度时间',
  `dataTime` datetime NOT NULL COMMENT '数据时间',
  `progress` float NOT NULL DEFAULT '0' COMMENT '执行进度',
  `type` int(3) unsigned NOT NULL DEFAULT '1' COMMENT 'task类型：1:调度的task; 2:手动重跑的task; 3:一次性任务',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT 'task状态： 1:waiting；2:ready；3:running；4:success；5:failed；6:killed；99:removed',
  `finishReason` varchar(1024) NOT NULL DEFAULT '' COMMENT 'task结束的原因',
  `appId` int(11) NOT NULL DEFAULT '0' COMMENT 'appId',
  `workerId` int(11) NOT NULL DEFAULT '0' COMMENT 'workerId',
  `executeUser` varchar(32) NOT NULL DEFAULT '' COMMENT '执行用户',
  `executeStartTime` datetime DEFAULT NULL COMMENT '执行开始时间',
  `executeEndTime` datetime DEFAULT NULL COMMENT '执行结束时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`taskId`),
  KEY `index_jobId` (`jobId`),
  KEY `index_dataYmd` (`scheduleTime`),
  KEY `index_executeStartTime` (`executeStartTime`),
  KEY `index_executeUser` (`executeUser`) KEY_BLOCK_SIZE=4
) ENGINE=InnoDB AUTO_INCREMENT=41292 DEFAULT CHARSET=utf8 COMMENT='task表';

-- Create syntax for TABLE 'task_depend'
CREATE TABLE `task_depend` (
  `taskId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT 'taskId',
  `dependTaskIds` text NOT NULL COMMENT '依赖task信息',
  `childTaskIds` text NOT NULL COMMENT '子task信息',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='task依赖表';

-- Create syntax for TABLE 'task_history'
CREATE TABLE `task_history` (
  `taskId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT 'taskId',
  `attemptId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '尝试ID',
  `jobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属JobID',
  `content` mediumtext NOT NULL COMMENT '任务内容',
  `params` varchar(2048) NOT NULL DEFAULT '' COMMENT '任务参数',
  `scheduleTime` datetime NOT NULL COMMENT '调度时间',
  `dataTime` datetime NOT NULL COMMENT '数据时间',
  `progress` float NOT NULL DEFAULT '0' COMMENT '执行进度',
  `type` int(3) unsigned NOT NULL DEFAULT '1' COMMENT 'task类型：1:调度的task; 2:手动重跑的task; 3:一次性任务',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT 'task状态： 1:waiting；2:ready；3:running；4:success；5:failed；6:killed；99:removed',
  `finishReason` varchar(1024) NOT NULL COMMENT 'task结束的原因',
  `appId` int(11) NOT NULL DEFAULT '0' COMMENT 'appId',
  `workerId` int(11) NOT NULL DEFAULT '0' COMMENT 'workerId',
  `executeUser` varchar(32) NOT NULL DEFAULT '' COMMENT '执行用户',
  `executeStartTime` datetime DEFAULT NULL COMMENT '执行开始时间',
  `executeEndTime` datetime DEFAULT NULL COMMENT '执行结束时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`taskId`,`attemptId`),
  KEY `index_jobId` (`jobId`),
  KEY `index_scheduleTime` (`scheduleTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='task_execute_records表';

-- Create syntax for TABLE 'worker'
CREATE TABLE `worker` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'workerId',
  `ip` char(16) NOT NULL DEFAULT '' COMMENT 'ip地址',
  `port` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '端口号',
  `workerGroupId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'workerGroupID',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1：启用；2：停用；',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `index_ip_port` (`ip`,`port`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='worker表';

-- Create syntax for TABLE 'worker_group'
CREATE TABLE `worker_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'workerGroupID',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'workerGroup名称',
  `authKey` varchar(32) NOT NULL DEFAULT '' COMMENT '认证key',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1：启用；2：禁用；',
  `owner` varchar(32) NOT NULL DEFAULT '' COMMENT '所有者',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '最后更新用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='workerGroup表';


--------20170728 new----

CREATE TABLE `job` (
  `jobId` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'jobID',
  `jobName` varchar(256) NOT NULL DEFAULT '' COMMENT '任务名称',
  `jobType` varchar(64) NOT NULL DEFAULT '0' COMMENT '任务类型 hive; shell; java; MR',
  `status` int(3) NOT NULL DEFAULT '1' COMMENT '任务状态 1:有效； 2:无效；3：过期；4：垃圾箱；5:暂停',
  `contentType` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '执行内容类型 1: 文本，2:脚本，3: jar包',
  `content` mediumtext NOT NULL COMMENT '任务内容,脚本内容或脚本名',
  `params` varchar(2048) NOT NULL DEFAULT '{}' COMMENT '任务参数，json格式',
  `submitUser` varchar(32) NOT NULL DEFAULT '' COMMENT '提交用户',
  `priority` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '优先级,1:low,2:normal,3:high,4:verg high',
  `isSerial` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否串行。0：并行; 1：串行',
  `isTemp` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为临时任务',
  `appId` int(11) unsigned NOT NULL COMMENT '应用ID',
  `workerGroupId` int(11) unsigned NOT NULL DEFAULT '1' COMMENT 'worker组ID',
  `departmentId` varchar(256) NOT NULL DEFAULT '' COMMENT '部门名称',
  `bizGroups` varchar(256) NOT NULL DEFAULT '' COMMENT '业务组名称，多个的话逗号分隔',
  `activeStartDate` datetime NOT NULL COMMENT '有效开始日期',
  `activeEndDate` datetime NOT NULL COMMENT '有效结束日期',
  `expiredTime` int(11) unsigned NOT NULL DEFAULT '86400' COMMENT '失效时间(s)，默认24小时',
  `failedAttempts` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '任务运行失败时的重试次数',
  `failedInterval` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '任务运行失败时重试的间隔(秒)',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
  `updateUser` varchar(32) NOT NULL DEFAULT '' COMMENT '更新用户',
  PRIMARY KEY (`jobId`),
  KEY `index_submitUser` (`submitUser`),
  KEY `index_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=9136 DEFAULT CHARSET=utf8 COMMENT='job表';

CREATE TABLE `task` (
  `taskId` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'taskId',
  `attemptId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后的尝试ID',
  `jobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属JobID',
  `content` mediumtext NOT NULL COMMENT '任务内容',
  `params` varchar(2048) NOT NULL DEFAULT '' COMMENT '任务参数',
  `scheduleTime` datetime NOT NULL COMMENT '调度时间',
  `dataTime` datetime NOT NULL COMMENT '数据时间',
  `progress` float NOT NULL DEFAULT '0' COMMENT '执行进度',
  `type` int(3) unsigned NOT NULL DEFAULT '1' COMMENT 'task类型：1:调度的task; 2:手动重跑的task; 3:一次性任务',
  `status` int(3) unsigned NOT NULL DEFAULT '1' COMMENT 'task状态： 1:waiting；2:ready；3:running；4:success；5:failed；6:killed；99:removed',
  `finishReason` varchar(1024) NOT NULL DEFAULT '' COMMENT 'task结束的原因',
  `appId` int(11) NOT NULL DEFAULT '0' COMMENT 'appId',
  `workerId` int(11) NOT NULL DEFAULT '0' COMMENT 'workerId',
  `executeUser` varchar(32) NOT NULL DEFAULT '' COMMENT '执行用户',
  `executeStartTime` datetime DEFAULT NULL COMMENT '执行开始时间',
  `executeEndTime` datetime DEFAULT NULL COMMENT '执行结束时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    `alarmEnable` int(11) COMMENT 'alarm',

  PRIMARY KEY (`taskId`),
  KEY `index_jobId` (`jobId`),
  KEY `index_dataYmd` (`scheduleTime`),
  KEY `index_executeStartTime` (`executeStartTime`),
  KEY `index_executeUser` (`executeUser`) KEY_BLOCK_SIZE=4
) ENGINE=InnoDB AUTO_INCREMENT=41292 DEFAULT CHARSET=utf8 COMMENT='task表';

CREATE TABLE `operation_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8_general_ci DEFAULT NULL,
  `operator` varchar(200) COLLATE utf8_general_ci DEFAULT NULL,
  `refer` varchar(11) COLLATE utf8_general_ci DEFAULT NULL,
  `detail` mediumtext COLLATE utf8_general_ci  COMMENT '任务内容',
  `opeDate` datetime NOT NULL COMMENT '创建时间',
  `type` varchar(40) COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型，job task',
    `operationType` varchar(200) COLLATE utf8_general_ci  COMMENT '任务内容',
  `preOperationContent` mediumtext COLLATE utf8_general_ci  COMMENT '任务内容',
  `afterOperationContent` mediumtext COLLATE utf8_general_ci  COMMENT '任务内容',
  PRIMARY KEY (`id`),
  KEY `index_title` (`title`(191)),
  KEY `index_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=295 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

create table ironman.script (
   `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(200)   COMMENT ' ',
  `type` varchar(200)   COMMENT ' ',
  `creator` varchar(200)  COMMENT '任务内容',
    `createTime` datetime   COMMENT '创建时间',
  `updateTime` datetime  COMMENT '最后更新时间',
    `last_editor` varchar(200)   COMMENT ' ',
    `status` int   COMMENT ' ',
    `content` mediumtext   COMMENT ' ',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ironman.script';

insert into worker_group values(1,'a','ec80df2716a547b89d99a3d135dea1d3',1,'lisi',now(),now(),'lisi');
insert into app_worker_group values(1,1,now(),now(),'lisi');
insert into app values(1, 'jarvis-web', '11111',1,1,10, 'lisi', now(), now(), 'lisi');

alter table job change  department  departmentId varchar(256);
 alter table task add column(alarmEnable int);


