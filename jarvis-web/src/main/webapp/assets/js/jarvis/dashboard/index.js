$(function () {

});

$.fn.extend({
    animateCss: function (animationName, hideFlag) {
        var animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
        $(this).addClass('animated ' + animationName).one(animationEnd, function () {
            $(this).removeClass('animated ' + animationName);
            if (true == hideFlag) {
                $(this).hide();
            }
        });
    }
});

var dashBoard = {
    refreshTasks: function (status) {
        var queryParams = {};
        $("#tasks").bootstrapTable({
            columns: columns,
            pagination: true,
            sidePagination: 'server',
            search: false,
            url: contextPath + '/api/plan/getPlans',
            queryParams: function (params) {
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
            pageSize: 10,
            sortable: true,
            pageList: [10, 20, 50, 100, 200, 500, 1000],
            paginationFirstText: '首页',
            paginationPreText: '上一页',
            paginationNextText: '下一页',
            paginationLastText: '末页',
            showExport: true,
            exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'doc', 'excel'],
            exportDataType: 'basic',
            exportOptions: {},
            detailView: true,
            onExpandRow: function (index, row, $detail) {
                var jobId = row["jobId"];
                expandTable($detail, jobId);
            }
        });

        $("#statusTaskList").show();
        $("#statusTaskList").animateCss("zoomInRight");

    },
    hideTaskList: function () {
        $("#statusTaskList").animateCss("fadeOutUp",true);
    }

}

function expandTable($detail, jobId) {
    var detailTable = $detail.html('<table></table>').find('table');
    var queryParams = {};
    queryParams["jobId"] = jobId;
    $(detailTable).bootstrapTable({
        columns: columns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/plan/getPlans',
        queryParams: function (params) {
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
        pageSize: 10,
        sortable: true,
        pageList: [10, 20, 50, 100, 200, 500, 1000],
        paginationFirstText: '首页',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLastText: '末页'
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
    title: 'APP名',
    sortable: true,
    switchable: true,
    visible: false
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
    title: '任务优先级',
    sortable: true,
    switchable: true,
    visible: true
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
    field: 'scheduleTimeFirst',
    title: '首个调度时间',
    switchable: true,
    sortable: true,
    formatter: formatDateTime,
    visible: false
}, {
    field: 'taskStatus',
    title: '执行状态一览',
    switchable: true,
    sortable: true,
    visible: true,
    formatter: taskStatusListFormatter
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
    var result = '<a target="_blank" href="' + contextPath + "/job/detail?jobId=" + row["jobId"] + '">' + value + '</a>';

    return result;
}

function operateFormatter(value, row, index) {
    var jobId = row["jobId"];
    var dependUrl = contextPath + '/job/dependency?jobId=' + jobId;
    var taskUrl = contextPath + '/task?jobIdList=' + JSON.stringify([jobId]) + '&scheduleDate=' ;
    var result = [
        '<a class="edit" href="' + dependUrl + '" title="查看任务依赖" target="_blank">',
        '<i class="glyphicon glyphicon-object-align-vertical"></i>任务依赖',
        '</a>  ',
        '<a class="edit" href="' + taskUrl + '" title="查看当前执行" target="_blank">',
        '<i class="glyphicon glyphicon-list"></i>执行详情',
        '</a>  ',
    ].join('');
    return result;
}
function taskStatusFormatter(value, row, index) {
    var color = taskStatusColor[value].color;

    var result = '<i class="fa fa-circle fa-2x" style="color: ' + color + '"></i>';
    return result;
}
function taskStatusListFormatter(value, row, index) {
    var result = "";
    $(value).each(function (i, c) {
        var taskId = c.taskId;
        var status = c.status;
        var color = taskStatusColor[status].color;
        var text = taskStatusColor[status].text;

        var item = '<a target="_blank" href="' + contextPath + "/task/detail?taskId=" + taskId + '"><i class="fa fa-circle fa-2x" style="color: ' + color + '"></i>' + text + '</a>';

        result = result + item;
    });

    if ("" == result) {
        result = "等待生成执行";
    }


    return result;
}