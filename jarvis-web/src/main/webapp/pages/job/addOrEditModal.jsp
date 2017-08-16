<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 选择脚本-弹出框 -->
<div id="searchScriptModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">选择脚本</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <input id="searchScriptInput" class="form-control" type="search"
                           placeholder="回车搜索"/>
                </div>
                <div><b>脚本一览</b><span style="color: gray">&nbsp&nbsp*双击选中*</span></div>
                <div id="searchScriptList" class="list-group"
                     style="height:400px; overflow:auto">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
            </div>
        </div>
    </div>
</div>

<!-- 上传jar包-弹出框 -->
<div id="uploadJarModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">上传jar包</h4>
            </div>
            <div class="modal-body">
                <div class="row top-buffer">
                    <div class="col-md-6 col-md-offset-3">
                        <div class="input-group" style="width:100%">
                            <input id="localFile" type="file" accept=".jar" class="btn btn-xs btn-default"
                                   data-target="">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="confirmUploadJar()">上传</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<!--任务参数——模式框 -->
<div id="paraModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">任务参数</h4>
            </div>
            <div class="modal-body">
                <table id="pattern" style="display: none">
                    <tr>
                        <td>
                            <input name="key" class="form-control" placeholder="请输入属性的key">
                        </td>
                        <td>
                            <input name="value" class="form-control"
                                   placeholder="请输入属性的value">
                        </td>
                        <td>
                            <a class="glyphicon glyphicon-plus" href="javascript:void(0)"
                               onclick="addPara(this)"></a>
                            <a class="glyphicon glyphicon-minus" href="javascript:void(0)"
                               onclick="deletePara(this)"></a>
                        </td>
                    </tr>
                </table>

                <table id="parasTable" class="table table-bordered">
                    <thead>
                    <tr>
                        <th>key</th>
                        <th>value</th>
                        <th>操作</th>
                    </tr>

                    </thead>
                    <tbody>

                    </tbody>
                </table>
                <a class="glyphicon glyphicon-plus" href="javascript:void(0)"
                   onclick="addPara(null)"></a>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="confirmPara()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->


<!--sparkLauncher任务参数——模式框 -->
<div id="sparkLauncherParasModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">SparkLauncher任务参数</h4>
            </div>

            <div id="sparkLauncherParasModalBody" class="modal-body">
                <!-- main函数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">main函数
                            <span class="text-danger" style="vertical-align: middle">*</span></span>
                            <input name="mainClass" class="form-control required" data-desc="main函数" placeholder=""/>
                        </div>
                    </div>
                </div>
                <!-- 文件路径 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">jar文件
                            <span class="text-danger" style="vertical-align: middle">*</span></span>
                            <textarea name="taskJar" class="form-control required" data-desc="jar文件"
                                      rows="3" placeholder=""></textarea>
                        </div>
                    </div>
                </div>
                <!-- 应用参数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">应用参数</span>
                            <input name="applicationArguments" class="form-control" placeholder=""/>
                        </div>
                    </div>
                </div>
                <!-- driver核数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">driver核数</span>
                            <input name="driverCores" class="form-control required" data-defaultValue="1"
                                   data-desc="driver核数" placeholder="不填代表 1"/>
                        </div>
                    </div>
                </div>
                <!-- driver内存 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">driver内存</span>
                            <input name="driverMemory" class="form-control required" data-defaultValue="4g"
                                   data-desc="driver内存" placeholder="不填代表 4g"/>
                        </div>
                    </div>
                </div>
                <!-- executor核数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">executor核数</span>
                            <input name="executorCores" class="form-control required" data-defaultValue="1"
                                   data-desc="executor核数" placeholder="不填代表 1"/>
                        </div>
                    </div>
                </div>
                <!-- executor内存 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">executor内存</span>
                            <input name="executorMemory" class="form-control required" data-defaultValue="4g"
                                   data-desc="executor内存" placeholder="不填代表 4g"/>
                        </div>
                    </div>
                </div>
                <!-- executor数目 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">executor数目</span>
                            <input name="executorNum" class="form-control required" data-defaultValue="6"
                                   data-desc="executor数目" placeholder="不填代表 6"/>
                        </div>
                    </div>
                </div>
                <!-- spark执行参数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">spark执行参数</span>
                            <input name="sparkSubmitProperties" class="form-control" placeholder=""/>
                        </div>
                    </div>
                </div>
                <!-- spark版本 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">spark版本</span>
                            <input name="sparkVersion" class="form-control"
                                   data-defaultValue="spark-1.6.0" data-desc="spark版本" placeholder="不填代表 spark-1.6.0"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="confirmSparkLauncherParas()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<!--java任务参数——模式框 -->
<div id="javaParasModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">java任务参数</h4>
            </div>

            <div id="javaParasModalBody" class="modal-body">
                <!-- main函数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-0">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">main函数
                            <span class="text-danger" style="vertical-align: middle">*</span></span>
                            <input id="javaMainClass" name="mainClass" class="form-control required" data-desc="main函数"
                                   placeholder=""/>
                        </div>
                    </div>
                </div>
                <!-- jar -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-0">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">jar文件
                            <span class="text-danger" style="vertical-align: middle">*</span></span>
                            <textarea id="javaJar" name="jar" class="form-control required" data-desc="jar"
                                      rows="3" placeholder="请输入jar文件HDFS路径"></textarea>
                        </div>
                    </div>
                    <button type="button" style="float:right;margin-right: 15px;" class="btn btn-default"
                            onclick="showUploadJarModal('javaJar')">上传
                    </button>
                </div>
                <!-- classpath -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-0">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">classpath</span>
                            <textarea id="javaClasspath" name="classpath" class="form-control" data-desc="classpath"
                                      rows="3" placeholder="请输入依赖文件的HDFS路径,多个依赖用逗号','隔开"></textarea>
                        </div>
                    </div>
                    <button type="button" style="float:right;margin-right: 15px;" class="btn btn-default"
                            onclick="showUploadJarModal('javaClasspath')">上传
                    </button>
                </div>

                <!-- 执行参数 -->
                <div class="row top-buffer">
                    <div class="col-md-10 col-md-offset-0">
                        <div class="input-group" style="width:100%">
                            <span class="input-group-addon" style="width:30%">执行参数</span>
                            <textarea id="javaArguments" name="arguments" class="form-control" data-desc="执行参数"
                                      rows="3" placeholder=""></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="confirmJavaParas()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->


<style>
    #jobScheduleModal table tr a {
        border: 1px transparent solid;
        width: 100%;
        display: inline-block;
        margin: 0;
        padding: 8px 0;
        outline: 0;
        color: #333;
    }

    #jobScheduleModal table td span {
        width: 100%;
    }

    #jobScheduleModal table td input {
        margin: 0;
        text-align: center;
    }

    .cronInput {
        width: 70px;
    }

    #cronTable tr td {
        text-align-last: center;
    }

    #jobScheduleModal .tab-pane {
        width: 600px;
        height: 400px;
    }

    #jobScheduleModal .modal-dialog{
        width: 600px;
    }

</style>

<!-- 计划表达式-弹出框 -->
<div id="jobScheduleModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">调度时间</h4>
            </div>
            <div class="modal-body">

                <ul class="nav nav-tabs">
                    <li role="presentation" class="active">
                        <a href="#circleTab" data-toggle="tab">简单设定</a>
                    </li>
                    <li role="presentation">
                        <a href="#cronTab" data-toggle="tab">高级设定</a>
                    </li>
                    <%--<li role="presentation">--%>
                    <%--<a href="#fixedDelayTab" data-toggle="tab">固定延迟</a>--%>
                    <%--</li>--%>
                    <%--<li role="presentation">--%>
                    <%--<a href="#fixedRateTab" data-toggle="tab">固定频率</a>--%>
                    <%--</li>--%>
                    <%--<li role="presentation">--%>
                    <%--<a href="#iso8601Tab" data-toggle="tab">ISO8601</a>--%>
                    <%--</li>--%>

                </ul>

                <div class="tab-content">

                    <div id="circleTab" class="tab-pane active">

                        <div class="row top-buffer">
                            <div class="col-md-10 col-md-offset-1">
                                <div class="input-group" style="width:100%">
                                    <span class="input-group-addon" style="width:25%">周期类型
                                        <span class="text-danger" style="vertical-align: middle">*</span>
                                    </span>

                                    <div class="form-control">
                                        <label class="radio-inline">
                                            <input name="circleType" id="circleDay" value="1" type="radio">每天
                                        </label>
                                        <label class="radio-inline">
                                            <input name="circleType" id="circleHour" value="2" type="radio"> 每小时
                                        </label>
                                        <label class="radio-inline">
                                            <input name="circleType" id="circleWeek" value="3" type="radio"> 每周
                                        </label>
                                        <label class="radio-inline">
                                            <input name="circleType" id="circleMonth" value="4" type="radio"> 每月
                                        </label>
                                        <label class="radio-inline">
                                            <input name="circleType" id="circleYear" value="5" type="radio"> 每年
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row top-buffer" id="perMonthDiv">
                            <div class="col-md-10 col-md-offset-1">
                                <div class="input-group" style="width:100%">
                                    <span class="input-group-addon" style="width:25%">月
                                        <span class="text-danger" style="vertical-align: middle">*</span>
                                    </span>
                                    <select id="perMonth" desc="" multiple="multiple"></select>
                                    <span class="input-group-addon">可多选</span>
                                </div>
                            </div>
                        </div>

                        <div class="row top-buffer" id="perDayDiv">
                            <div class="col-md-10 col-md-offset-1">
                                <div class="input-group" style="width:100%">
                                <span class="input-group-addon" style="width:25%">日
                                    <span class="text-danger" style="vertical-align: middle">*</span>
                                </span>
                                    <select id="perDay" desc="" multiple="multiple"></select>
                                    <span class="input-group-addon">可多选</span>
                                </div>
                            </div>
                        </div>

                        <div class="row top-buffer" id="perWeekDiv">
                            <div class="col-md-10 col-md-offset-1">
                                <div class="input-group" style="width:100%">
                                    <span class="input-group-addon" style="width:25%">星期
                                        <span class="text-danger" style="vertical-align: middle">*</span>
                                    </span>
                                    <select id="perWeekDay" desc="" multiple="multiple"></select>
                                    <span class="input-group-addon">可多选</span>
                                </div>
                            </div>
                        </div>

                        <div class="row top-buffer" id="perTimeDiv">
                            <div class="col-md-10 col-md-offset-1">
                                <div class="input-group" style="width:100%">
                                    <span class="input-group-addon" style="width:25%">时刻
                                        <span class="text-danger" style="vertical-align: middle">*</span>
                                    </span>

                                    <div class="form-control" style="float: inherit">
                                        <table id="timeTable">
                                            <tbody>
                                            <tr>
                                                <td><input id="perHour" type="number" min="0" max="23" step="1"></td>
                                                <td class="separator">时&nbsp;</td>
                                                <td><input id="perMinute" type="number" min="0" max="59" step="15"></td>
                                                <td class="separator">分&nbsp;</td>
                                                <td><input id="perSecond" type="number" min="0" max="59" step="15"></td>
                                                <td class="separator">秒&nbsp;</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="cronTab" class="tab-pane">

                        <%--<span class="input-group-addon" style="width:25%">cron表达式--%>
                        <%--<span class="text-danger" style="vertical-align: middle">*</span>--%>
                        <%--</span>--%>

                        <div class="row top-buffer">
                            <div class="col-md-11 col-md-offset-0">
                                <div class="input-group" style="width:100%">
                                    <div class="form-control" style="float: inherit">
                                        <table id="cronTable">
                                            <tbody>
                                            <tr>
                                                <td>秒<span class="text-danger">*</span></td>
                                                <td class="separator">&nbsp</td>
                                                <td>分<span class="text-danger">*</span></td>
                                                <td class="separator">&nbsp</td>
                                                <td>时<span class="text-danger">*</span></td>
                                                <td class="separator">&nbsp</td>
                                                <td>日<span class="text-danger">*</span></td>
                                                <td class="separator">&nbsp</td>
                                                <td>月<span class="text-danger">*</span></td>
                                                <td class="separator">&nbsp</td>
                                                <td>星期<span class="text-danger">*</span></td>
                                                <td class="separator">&nbsp</td>
                                                <td>年份</td>
                                            </tr>
                                            <tr>
                                                <td><input id="cronSecond" type="text" class="cronInput" data-desc="秒">
                                                </td>
                                                <td class="separator">&nbsp</td>
                                                <td><input id="cronMinute" type="text" class="cronInput" data-desc="分钟">
                                                </td>
                                                <td class="separator">&nbsp</td>
                                                <td><input id="cronHour" type="text" class="cronInput" data-desc="小时">
                                                </td>
                                                <td class="separator">&nbsp</td>
                                                <td><input id="cronDay" type="text" class="cronInput" data-desc="日期">
                                                </td>
                                                <td class="separator">&nbsp</td>
                                                <td><input id="cronMonth" type="text" class="cronInput" data-desc="月份">
                                                </td>
                                                <td class="separator">&nbsp</td>
                                                <td><input id="cronWeekDay" type="text" class="cronInput"
                                                           data-desc="星期">
                                                </td>
                                                <td class="separator">&nbsp</td>
                                                <td><input id="cronYear" type="text" class="cronInput" data-desc="年份">
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--输入帮助 -->
                        <div class="row top-buffer">
                            <div class="col-md-11 col-md-offset-0">
                                <a href="javascript:void(0)" onclick="toggleCronHelpDiv(this)">
                                    <i class="pull-right text-primary glyphicon glyphicon-chevron-down"
                                       data-state="down"></i>
                                    <span class="pull-right text-info" style="margin-right:20px">更多帮助</span>
                                </a>
                            </div>

                        </div>


                        <div id="cronHelpDiv" style="display: none;">
                            <table>
                                <tbody>
                                <tr>
                                    <th>时间域名&nbsp;</th>
                                    <th>允许值&nbsp;&nbsp;</th>
                                    <th>允许的特殊字符&nbsp;</th>
                                </tr>
                                <tr>
                                    <td>秒</td>
                                    <td>0-59</td>
                                    <td>, - * /</td>
                                </tr>
                                <tr>
                                    <td>分钟</td>
                                    <td>0-59</td>
                                    <td>, - * /</td>
                                </tr>
                                <tr>
                                    <td>小时</td>
                                    <td>0-23</td>
                                    <td>, - * /</td>
                                </tr>
                                <tr>
                                    <td>日期</td>
                                    <td>1-31</td>
                                    <td>, - * / ?</td>
                                </tr>
                                <tr>
                                    <td>月份</td>
                                    <td>1-12</td>
                                    <td>, - * /</td>
                                </tr>
                                <tr>
                                    <td>星期</td>
                                    <td>1-7</td>
                                    <td>, - * / ?</td>
                                </tr>
                                </tbody>
                            </table>
                            <dvi>逗号(,)：表示一个列表值；如'小时'域使用“10,15,20”，表示10点，15点和20点。</dvi>
                            <div>减号(-)：表示一个范围；如'小时'域使用“10-12”，表示从10点到12点。</div>
                            <div>星号(*)：表示每一个时刻；如'小时'域使用"*",就表示每小时。</div>
                            <div>斜杠(/)：x/y表示一个等步序列，x为起始值，y为增量值。</div>
                            <div>问号(?)：'日期'和'星期'域中使用，指“无意义的值”，相当于占位符。</div>
                            <div style="color:red">注意：日期和星期字段互斥，即两个字段必须有一个是"?"</div>
                            <%--<div>0 1 * * ?:每天凌晨1点运行</div>--%>
                            <%--<div>0 * * * ?:每个整点运行</div>--%>
                            <%--<div>0-5 14 * * ?:每天14点到14点5分，每分钟执行一次</div>--%>
                        </div>
                    </div>

                    <%--<div id="fixedDelayTab" class="tab-pane" >--%>
                    <%--</div>--%>

                    <%--<div id="fixedRateTab" class="tab-pane" >--%>
                    <%--</div>--%>

                    <%--<div id="iso8601Tab" class="tab-pane">--%>
                    <%--</div>--%>

                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="confirmJobSchedule()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<style>
    #offsetStrategy .select2-container--default .select2-selection--single{
        border: 1px solid #ccc;
        border-bottom-left-radius:4px;
        border-top-left-radius:4px;
    }
</style>

<div id="offsetStrategy" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">设置偏移策略</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <select name="offset">
                            <option value="c">当前</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <select name="time">
                            <option value="d">天</option>
                            <option value="w">周</option>
                            <option value="M">月</option>
                            <option value="m">分钟</option>
                            <option value="h">小时</option>
                            <option value="y">年</option>
                        </select>

                    </div>
                    <div class="col-md-4" name="text" style="display: none;margin-left: -15px;padding:3px;">
                        <span class="text-info " style="font-size: 18px;">
                            内
                        </span>
                    </div>

                </div>



            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default" onclick="hideOffsetStrategy()">确定</button>
            </div>
        </div>
    </div>
</div>





