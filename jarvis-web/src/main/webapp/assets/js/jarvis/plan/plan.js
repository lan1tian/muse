var priority = {};
var taskStatusColor = null;

$(function () {


    glFuncs.initJobType("jobType");
    initJobPriority();


    glFuncs.initExecuteUser("executeUser");

    glFuncs.initJobId("jobId");

    glFuncs.initJobName("jobName");

    initTaskStatusColor();

    initData();
});

function initJobPriority() {
    //初始化权重
    $.ajax({
        url:contextPath + "/assets/json/jobPriority.json",
        success:function(data){
            $(data).each(function (i, c) {
                var key = c.id;
                var value = c.text;
                if (key != 'all') {
                    priority[key] = value;
                }
            });
            $("#priority").select2({
                data: data,
                width: '100%'
            });
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化任务权重', msg);
        }
    })

}

function initTaskStatusColor() {
    $.ajax({
        url: contextPath + "/assets/json/taskStatusColor.json",
        async: false,
        success: function (data) {
            taskStatusColor = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化任务颜色', msg);
        }
    });
}




function chooseStatus(tag) {
    if ($(tag).val() == 'all') {
        if (tag.checked) {
            $($("#taskStatus input")).each(function () {
                this.checked = true;
            });
        }
        else {
            $($("#taskStatus input")).each(function () {
                this.checked = false;
            });
        }
    }
}


function formatResult(result) {
    return result.text;
}
function formatResultSelection(result) {
    return result.id;
}

function search() {
    $("#content").bootstrapTable('refresh');
}
//重置参数
function reset() {
    $("#jobName").val("").trigger("change");
    $("#jobType").val("all").trigger("change");
    $("#priority").val("all").trigger("change");
    $("#executeUser").val("all").trigger("change");
    $("#dataTime").val("");
}

//获取查询参数
function getQueryPara() {
    var queryPara = {};

    var jobIdList = $("#jobId").val();
    var jobNameList = $("#jobName").val();
    var jobTypeList = $("#jobType").val();
    var priorityList = $("#priority").val();
    var executeUserList = $("#executeUser").val();
    var dataTime = $("#dataTime").val();
    var taskStatusList = [];

    var inputs = $("#taskStatus input[value!=all]:checked");
    //console.log(inputs);
    if (inputs.length == 0) {
        inputs = $("#taskStatus input[value!=all]");
    }
    $(inputs).each(function (i, c) {
        taskStatusList.push($(c).val());
    });
    jobIdList = jobIdList == null ? undefined : jobIdList;
    jobNameList = jobNameList == 'all' ? undefined : jobNameList;
    jobNameList = jobNameList == null ? undefined : jobNameList;
    jobTypeList = jobTypeList == 'all' ? undefined : jobTypeList;
    jobTypeList = jobTypeList == null ? undefined : jobTypeList;
    executeUserList = executeUserList == "all" ? undefined : executeUserList;
    executeUserList = executeUserList == null ? undefined : executeUserList;
    priorityList = priorityList == "all" ? undefined : priorityList;
    priorityList = priorityList == null ? undefined : priorityList;

    queryPara["jobIdList"] = JSON.stringify(jobIdList);
    queryPara["jobNameList"] = JSON.stringify(jobNameList);
    queryPara["jobTypeList"] = JSON.stringify(jobTypeList);
    queryPara["executeUserList"] = JSON.stringify(executeUserList);
    queryPara["priorityList"] = JSON.stringify(priorityList);
    //queryPara["taskStatusList"] = JSON.stringify(taskStatusList);

    return queryPara;
}

//初始化数据及分页
function initData() {
    $("#content").bootstrapTable({
        columns: columns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/plan/getPlans',
        queryParams: function (params) {
            var queryParams = getQueryPara();
            for (var key in queryParams) {
                var value = queryParams[key];
                params[key] = value;
            }
            return params;
        },
        responseHandler:function(res){
            if(res.status){
                showMsg("error","初始化计划列表",res.status.msg);
                return res;
            }
            else{
                return res;
            }
        },
        showColumns: true,
        showHeader: true,
        showToggle: true,
        pageSize: 20,
        sortable: true,
        pageList: [10, 20, 50, 100, 200, 500, 1000],
        paginationFirstText: '首页',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLastText: '末页',
        showExport: true,
        exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'doc', 'excel'],
        exportDataType: 'basic',
        exportOptions: {}
    });
}

var columns = [{
    field: 'jobId',
    title: '任务ID',
    switchable: true,
    sortable: true,
    visible: true
}, {
    field: 'jobName',
    title: '任务名称',
    switchable: true,
    sortable: true,
    formatter: jobNameFormatter
}, {
    field: 'appId',
    title: 'APP ID',
    sortable: true,
    switchable: true,
    visible: false
}, {
    field: 'appName',
    title: '应用名',
    sortable: true,
    switchable: true,
    visible: true,
    formatter:appFormatter
}, {
    field: 'jobType',
    title: '任务类型',
    sortable: true,
    switchable: true,
    visible: true
}, {
    field: 'bizGroups',
    title: '业务组ID',
    sortable: true,
    switchable: true,
    visible: false
}, {
    field: 'bizGroupName',
    title: '业务组名',
    sortable: true,
    switchable: true,
    visible: true
}, {
    field: 'priority',
    title: '优先级',
    sortable: true,
    switchable: true,
    visible: true,
    formatter: priorityFormatter
}, {
    field: 'submitUser',
    title: '提交用户',
    sortable: true,
    switchable: true,
    visible: true
}, {
    field: 'workerGroupId',
    title: 'workerGroupId',
    switchable: true,
    sortable: true,
    visible: false
}, {
    field: 'workerGroupName',
    title: 'worker组名',
    sortable: true,
    switchable: true,
    visible: false
}, {
    field: 'scheduleTime',
    title: '计划调度时间',
    switchable: true,
    sortable: true,
    formatter: formatDateTimeWithoutYear,
    visible: true
}, {
    field: 'averageExecuteTime',
    title: '预计执行时长',
    switchable: true,
    formatter: formatTimeInterval,
    visible: true
}, {
    field: 'createTime',
    title: '创建时间',
    sortable: true,
    switchable: true,
    visible: false,
    formatter: formatDateTime
}, {
    field: 'updateTime',
    title: '最后更新时间',
    switchable: true,
    sortable: true,
    visible: false,
    formatter: formatDateTime
}, {
    field: 'operation',
    title: '操作',
    width: '15%',
    switchable: true,
    formatter: operateFormatter
}];

function jobNameFormatter(value, row, index) {
    // var result = '<a target="_blank" href="' + contextPath + "/job/detail?jobId=" + row["jobId"] + '">' + value + '</a>';
    var result = '<a href="' + contextPath + "/job/detail?jobId=" + row["jobId"] + '">' + value + '</a>';

    return result;
}

function appFormatter(value, row, index){
    var result="<div style='white-space: nowrap'>"+value+"</div>";

    return result;
}

function priorityFormatter(value, row, index) {
    var text = priority[value];
    if (null == text) {
        $.ajax({
            url:contextPath + "/assets/json/jobPriority.json",
            async:false,
            success:function(data){
                $(data).each(function (i, c) {
                    if (c.key == value) {
                        text = c.text;
                        return false;
                    }
                });
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '格式化优先级信息', msg);
            }
        })
    }
    return text;
}

function taskStatusFormatter(value, row, index) {
    var taskId = row.taskId;
    var status = row.status;
    if (status == undefined) {
        status = 0;
    }
    var color = taskStatusColor[status].color;
    var text = taskStatusColor[status].text;

    var item = '';
    if (undefined !== taskId) {
        item = '<a target="_blank" href="' + contextPath + "/task/detail?taskId=" + taskId + '"><i class="fa fa-circle fa-2x" style="color: ' + color + '"></i>' + text + '</a>';
    }
    else {
        item = '<i class="fa fa-circle fa-2x" style="color: ' + color + '"></i>' + text;
    }
    ;

    item = "<div style='white-space:nowrap;'>" + item + "</div>";
    return item;
}

function operateFormatter(value, row, index) {
    var jobId = row["jobId"];
    var scheduleTime=row["scheduleTime"];
    var dependUrl = contextPath + '/plan/detail?jobId=' + jobId+"&scheduleTime="+scheduleTime;
    var result = [
        '<a style="white-space:nowrap;" class="edit" href="' + dependUrl + '" title="查看任务依赖" >',
        '<i class="glyphicon glyphicon-object-align-vertical"></i>计划详情',
        '</a>'
    ].join('');
    return result;
}


function StringFormatter(value, row, index) {
    return value;
}

