var taskStatus = null;
var taskStatusColor = null;
var taskOperation = null;
var taskDetailUrl = contextPath + "/task/detail?taskId=";

$(function () {
    createDatetimePickerById("executeDate");
    createDatetimePickerById("scheduleDate");
    createDatetimePickerById("startDate");
    createDatetimePickerById("endDate");

    glFuncs.initJobType("jobType");


    //select采用select2 实现
    $(".input-group select").select2({width: '100%'});

    initTaskStatus();
    initTaskColor();
    initTaskOperation();

    initBatchOperation();

    glFuncs.initTaskId("taskId");

    glFuncs.initJobId("jobId");

    glFuncs.initExecuteUser("executeUser");

    glFuncs.initJobName("jobName");

    initSearchCondition();
    initData();
});

function initTaskStatus() {
    $.ajax({
        url: contextPath + "/api/task/getTaskStatus",
        async: false,
        success: function (data) {
            taskStatus = data;

            var newData = new Array();
            var all = {};
            all["id"] = "all";
            all["text"] = "全部";
            newData.push(all);
            $(data).each(function (i, c) {
                if (0 != c.id && 99 != c.id && 6 != c.id) {
                    var item = {};
                    item["id"] = c["id"];
                    item["text"] = c["text"];
                    newData.push(item);
                }

            });

            $(newData).each(function (index, content) {
                var value = content.id;
                var text = content.text;
                var input = $("<input type='checkbox' name='taskStatus'/>");
                $(input).attr("value", value);

                if (value == 'all') {
                    $(input).click(function () {
                        if (this.checked) {
                            $($("#taskStatus input")).each(function () {
                                this.checked = true;
                            });
                        }
                        else {
                            $($("#taskStatus input")).each(function () {
                                this.checked = false;
                            });
                        }
                    });
                }

                $("#taskStatus").append(input);
                $("#taskStatus").append(text);
                $("#taskStatus").append('  ');
            });
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化执行状态', msg);
        }
    });
}

function initTaskColor() {
    //初始化颜色
    $.ajax({
        url: contextPath + "/assets/json/taskStatusColor.json",
        async: false,
        success: function (data) {
            taskStatusColor = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化执行状态颜色', msg);
        }
    });
}

//初始化操作类型
function initTaskOperation() {
    //当状态为4的时候，也可以原地重跑,目前不支持，后台可能有问题
    $.ajax({
        url: contextPath + "/assets/json/taskOperation.json",
        async: false,
        success: function (data) {
            taskOperation = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化操作类型', msg);
        }
    })
}

function initBatchOperation() {
    if (null == taskOperation) {
        return;
    }
    var result = {};
    for (var key in taskOperation) {
        var arr = taskOperation[key];
        arr.forEach(function (e) {
            if (e.url != '/api/task/retry') {
                if (result[e.url]) {
                    item = result[e.url];
                    item.allowStatus.push(key);
                }
                else {
                    var item = {};
                    item["text"] = e.text;
                    item["allowStatus"] = [key];
                    result[e.url] = item;
                }
            }
        });
    }

    var op = $("<div  style='display: inline-block'></div>");
    for (var key in result) {
        var item = result[key];
        var button = $("<button type='button' class='btn btn-sm btn-default' style='margin: 2px'></button>");
        button.text(item.text);
        button.attr("url", key);
        button.attr("allowStatus", JSON.stringify(item.allowStatus));
        button.on("click", function (e) {
            var self = e.target;
            var arr = JSON.parse($(self).attr("allowstatus"));
            var operationName = $(self).text();
            //允许的状态
            var allowStatus = {};
            arr.forEach(function (e) {
                allowStatus[e] = e;
            });

            var selecteds = glFuncs.getIdSelections("content");
            if (selecteds.length <= 0) {
                showMsg("warning", "批量" + operationName, "请至少选择一条执行记录");
                return;
            }

            var flag = false;

            for (var i = 0; i < selecteds.length; i++) {
                var item = selecteds[i];
                if (allowStatus[item.status]) {
                    flag = true;
                }
                else {
                    flag = false;
                    showMsg("warning", "批量" + operationName, item.jobName + '的当前状态不支持' + operationName + "操作,请重新选择");
                    break;
                }
            }
            //删除
            if (flag) {
                var url = $(self).attr("url");
                var taskIds = [];
                for (var i = 0; i < selecteds.length; i++) {
                    taskIds.push(selecteds[i].taskId);
                }
                ;
                var data = {taskIds: taskIds};
                requestRemoteRestApi(url, '批量' + operationName, data, true, true);
            }


        });
        op.append(button);
    }
    $("#toolBar").append(op);
}


//查找
function search() {
    $("#content").bootstrapTable("refresh");
}

function initSearchCondition() {
    if (taskQo != null) {
        if (taskQo.jobIdList != null) {
            //var data = [];
            $("#jobId").select2({data: [{id: -1, text: "撑住单元格,请@admin"}]});
            for (var i = 0; i < taskQo.jobIdList.length; i++) {
                var jobId = taskQo.jobIdList[i];
                //data.push();
                $("#jobId").select2({data: [{id: jobId, text: jobId}]})
                    .val(jobId).trigger("change");
            }
        }
        if (taskQo.scheduleDate != null && taskQo.scheduleDate != "") {
            $("#scheduleDate").val(taskQo.scheduleDate);
        }
    }
}

//重置参数
function reset() {
    $("#scheduleDate").val("");
    $("#executeDate").val("");
    $("#startDate").val("");
    $("#endDate").val("");
    $("#jobId").val("all").trigger("change");
    $("#jobName").val("all").trigger("change");
    $("#jobType").val("all").trigger("change");
    $("#executeUser").val("all").trigger("change");
    $("#taskStatus input").each(function (i, c) {
        this.checked = false;
    });
}

//获取查询参数
function getQueryPara() {
    var queryPara = {};

    var taskIdList = $("#taskId").val();
    var jobIdList = $("#jobId").val();
    var jobNameList = $("#jobName").val();
    var jobTypeList = $("#jobType").val();
    var executeUserList = $("#executeUser").val();

    var taskStatus = new Array();
    var inputs = $("#taskStatus").find("input:checked[value!=all]");
    if (inputs.length == 0) {
        inputs = $("#taskStatus").find("input[value!=all]");
    }
    $(inputs).each(function (i, c) {
        var value = $(c).val();
        taskStatus.push(value);
    });

    var isTempInputs = $("#isTemp").find("input:checked");
    var isTemp = [];
    $(isTempInputs).each(function (i, c) {
        isTemp.push($(c).val());
    });

    var scheduleDate = $("#scheduleDate").val();
    var executeDate = $("#executeDate").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();


    taskIdList = taskIdList == null ? undefined : taskIdList;
    jobIdList = jobIdList == null ? undefined : jobIdList;
    jobNameList = jobNameList == null ? undefined : jobNameList;
    jobTypeList = jobTypeList == 'all' ? undefined : jobTypeList;
    jobTypeList = jobTypeList == null ? undefined : jobTypeList;
    executeUserList = executeUserList == "all" ? undefined : executeUserList;
    executeUserList = executeUserList == null ? undefined : executeUserList;

    queryPara["taskIdList"] = JSON.stringify(taskIdList);
    queryPara["jobIdList"] = JSON.stringify(jobIdList);
    queryPara["jobNameList"] = JSON.stringify(jobNameList);
    queryPara["jobTypeList"] = JSON.stringify(jobTypeList);
    queryPara["executeUserList"] = JSON.stringify(executeUserList);
    queryPara["taskStatusArrStr"] = JSON.stringify(taskStatus);
    queryPara["isTemp"] = JSON.stringify(isTemp);
    queryPara["scheduleDate"] = scheduleDate;
    queryPara["executeDate"] = executeDate;
    queryPara["startDate"] = startDate;
    queryPara["endDate"] = endDate;

    return queryPara;
}

//初始化数据及分页
function initData() {
    $("#content").bootstrapTable({
        columns: columns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/task/getTasks',
        queryParams: function (params) {
            var queryParams = getQueryPara();
            for (var key in queryParams) {
                var value = queryParams[key];
                params[key] = value;
            }
            return params;
        }, responseHandler: function (res) {
            if (res.status) {
                showMsg("error", "初始化执行列表", res.status.msg);
                return res;
            }
            else {
                return res;
            }
        },
        showColumns: true,
        showHeader: true,
        showToggle: true,
        sortable: true,
        pageSize: 20,
        pageList: [10, 20, 50, 100, 200, 500, 1000],
        paginationFirstText: '首页',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLastText: '末页',
        showExport: true,
        exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'doc', 'excel'],
        exportDataType: 'basic'
    });
}

//字段配置
var columns = [{
    field: 'choose',
    checkbox: true,
    visible: true
}, {
    field: 'taskId',
    title: '执行ID',
    switchable: true,
    sortable: true,
    visible: true,
    formatter: taskDetailFormatter
}, {
    field: 'attemptId',
    title: '最后尝试ID',
    switchable: true,
    sortable: true,
    visible: false
}, {
    field: 'jobId',
    title: '任务ID',
    sortable: true,
    switchable: true
}, {
    field: 'jobName',
    title: '任务名',
    switchable: true,
    sortable: true,
    formatter: jobNameFormatter
}, {
    field: 'jobType',
    title: '任务类型',
    sortable: true,
    switchable: true
}, {
    field: 'content',
    title: '任务内容',
    switchable: true,
    visible: false
}, {
    field: 'params',
    title: '任务参数',
    switchable: true,
    visible: false
}, {
    field: 'executeUser',
    title: '执行人',
    sortable: true,
    switchable: true
}, {
    field: 'scheduleTime',
    title: '调度时间',
    switchable: true,
    sortable: true,
    formatter: formatDateTimeWithoutYear
}, {
    field: 'dataTime',
    title: '数据时间',
    switchable: true,
    sortable: true,
    formatter: formatDateTimeWithoutYear
}, {
    field: 'progress',
    title: '进度',
    switchable: true,
    sortable: true,
    visible: false,
    formatter: progressFormatter
}, {
    field: 'workerGroupId',
    title: 'workerGroupId',
    switchable: true,
    sortable: true,
    visible: false
}, {
    field: 'ip',
    title: 'IP',
    sortable: true,
    switchable: true,
    visible: false
}, {
    field: 'executeStartTime',
    title: '开始执行时间',
    sortable: true,
    switchable: true,
    visible: false,
    formatter: formatDateTimeWithoutYear
}, {
    field: 'executeEndTime',
    title: '执行结束时间',
    sortable: true,
    switchable: true,
    visible: false,
    formatter: formatDateTimeWithoutYear
}, {
    field: 'executeTime',
    title: '执行时长',
    sortable: true,
    switchable: false,
    visible: true,
    formatter: formatTimeInterval
}, {
    field: 'status',
    title: '状态',
    switchable: true,
    sortable: true,
    width: '7%',
    formatter: taskStatusFormatter
}, {
    field: 'createTime',
    title: '执行创建时间',
    switchable: false,
    sortable: true,
    visible: false,
    formatter: formatDateTime
}, {
    field: 'updateTime',
    title: '执行更新时间',
    switchable: true,
    sortable: true,
    visible: false,
    formatter: formatDateTime
}, {
    field: 'submitUser',
    title: '任务创建者',
    sortable: true,
    switchable: true,
    visible: false
}, {
    field: 'appName',
    sortable: true,
    title: '应用名',
    switchable: true,
    visible: true,
    formatter: appNameFormatter
}, {
    field: 'priority',
    title: '任务优先级',
    sortable: true,
    switchable: true,
    visible: false
}, {
    field: 'operation',
    title: '操作',
    switchable: true,
    width: '12%',
    formatter: operateFormatter
}];


function taskDetailFormatter(value, row, index) {
    var result = "<a href='" + taskDetailUrl + value + "'>" + value + "</a>";

    return result;
}

//重试还是kill，type与rest接口一一对应
function TaskOperate(jobId, taskId, attemptId, url, text) {
    (new PNotify({
        title: '任务操作',
        text: '确定' + text + "?",
        icon: 'glyphicon glyphicon-question-sign',
        hide: false,
        confirm: {
            confirm: true
        },
        buttons: {
            closer: false,
            sticker: false
        },
        history: {
            history: false
        }
    })).get().on('pnotify.confirm', function () {
            var data = {};
            data["jobId"] = jobId;
            data["taskId"] = taskId;
            data["taskIds"] = [taskId];
            data["attemptId"] = attemptId;
            requestRemoteRestApi(url, text, data, true, true);
        }).on('pnotify.cancel', function () {

        });
}

function modifyStatus(taskId, status, text,tag) {
    var url = '/api/task/modify/status';

    (new PNotify({
        title: '任务操作',
        text: '确定' + text + "?",
        icon: 'glyphicon glyphicon-question-sign',
        hide: false,
        confirm: {
            confirm: true
        },
        buttons: {
            closer: false,
            sticker: false
        },
        history: {
            history: false
        }
    })).get().on('pnotify.confirm', function () {
            var data = {};
            data["taskId"] = taskId;
            data["status"] = status;
            var result = requestRemoteRestApi(url, text, data, false, true);
            if(result.flag&&result.data.code==0){
                var color='';
                var desc='';
                for(var key in taskStatusColor){
                    if(key == status){
                        color=taskStatusColor[key].color;
                        desc=taskStatusColor[key].text;
                    }
                }
                $(tag).closest("tr").find("div[name=status] i").css("color",color);
                $(tag).closest("tr").find("div[name=status] span").text(desc);
            }
        }).on('pnotify.cancel', function () {

        });
}

function operateFormatter(value, row, index) {
    var jobId = row['jobId'];
    var attemptId = row['attemptId'];
    var taskId = row["taskId"];
    var status = row["status"];
    var operations = taskOperation[status];
    var operationStr = "";
    $(operations).each(function (i, c) {
        if(row["isTemp"]==0||(row["isTemp"]==1&& c.url!='/api/task/retry')){
            operationStr += '<li><a href="javascript:void(0)" onclick="TaskOperate(\'' + jobId + '\',\'' + taskId + '\',\'' + attemptId + '\',\'' + c.url + '\',\'' + c.text + '\')">' + c.text + '</a></li>';
        }
    });

    var result='';
    //原地重跑
    if(operations.length>0) {
        result = [
            '<div class="btn-group"> <button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">操作 <span class="caret"></span> </button>',
            '<ul class="dropdown-menu">',
            operationStr,
            '</ul>',
            '</div>'
        ].join('');
    }


    var statusResult='';
    var modifyStatus = generateModifyStatus(status);
    if (modifyStatus.length > 0) {
        var modifyTaskStatusOperation = '';
        $(modifyStatus).each(function (i, c) {
            modifyTaskStatusOperation += '<li><a href="javascript:void(0)" onclick="modifyStatus(\'' + taskId + '\',\'' + c.status + '\',\'' + '标为' + c.name + '\',this)">' + '标为' + c.name + '</a></li>';
        });

        statusResult = [
            ' <div class="btn-group"> <button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">修改状态 <span class="caret"></span> </button>',
            '<ul class="dropdown-menu">',
            modifyTaskStatusOperation,
            '</ul>',
            '</div>'
        ].join('');
    }

    result = "<div style='white-space: nowrap'>" + result + statusResult + "</div";

    return result;
}


function jobNameFormatter(value, row, index) {
    var result = '<a href="' + contextPath + "/job/detail?jobId=" + row["jobId"] + '">' + value + '</a>';

    return result;
}

//执行状态格式化
function taskStatusFormatter(value, row, index) {
    var color = taskStatusColor[value].color;
    var text = taskStatusColor[value].text;
    var result = '<i class="fa fa-circle fa-2x" style="color: ' + color + '"></i>' +'<span>' +text+'</span>';

    result = "<div name='status' style='white-space:nowrap;'>" + result + "</div?";

    return result;
}

//百分比格式化
function progressFormatter(value, row, index) {
    var result = value * 100 + "%";
    return result;
}

//格式化结果
function formatResult(result) {
    return result.text;
}

//格式化结果选择框
function formatResultSelection(result) {
    return result.id;
}

function appNameFormatter(value, row, index) {

    var result = "<div style='white-space: nowrap'>" + value + "</div>";

    return result;
}

function generateModifyStatus(status) {
    var result = [];
    if (status == 4) {
        return result;
    }
    if (typeof taskStatus != 'undefined') {
        for (var i = 0; i < taskStatus.length; i++) {
            var item = taskStatus[i];
            if (item.id == 0 ||item.id == 1 ||item.id == 2 ||item.id == 3 || item.id == 6 || item.id == 99 || item.id == status) {
                continue;
            }
            var object = {};
            object.status = item.id;
            object.name = item.text;
            result.push(object);
        }
    }
    return result;
}