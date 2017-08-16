$(function () {
    initLogMore();
    initChart(echarts);
    $('#tabNav a[href="#log"]').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
        initLog(true, 0, 10000);
    });
});


function initChart(echarts) {

    var xAxis = new Array();
    var data = new Array();

    if (taskVoList.length == 0) {

        document.getElementById('container').innerHTML = "暂无数据";

        return;
    }
    for (var i = 0; i < taskVoList.length; i++) {
        var task = taskVoList[i];
        var theDate = new Date(task["executeStartTime"]);
        var result = moment(theDate).format("YYYY-MM-DD HH:mm:ss");
        xAxis.push(result);
        data.push(task["executeTime"]);
    }
    if (data.length < 1) {
        return;
    }


    var myChart = echarts.init(document.getElementById('container'));
    var option = {
        title: {
            text: '最近30次成功执行所用时间',
            subtext: '单位(秒)'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['执行用时']
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: xAxis
            }
        ],
        yAxis: [
            {
                type: 'value',
                axisLabel: {
                    formatter: '{value} 秒'
                }
            }
        ],
        series: [
            {
                name: '执行用时',
                type: 'line',
                data: data,
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };

    // 为echarts对象加载数据
    myChart.setOption(option);
}


function initLog(isInit, offset, size) {
    var logContent = $("#log").html();
    if (isInit && null != logContent && logContent.length > 0) {
        return
    }


    var data = {};
    data["taskId"] = taskId;
    data["jobId"] = jobId;
    data["attemptId"] = attemptId;
    data["offset"] = offset;
    data["size"] = size;

    var url;
    url = "/api/log/readExecuteLog";
    var result = requestRemoteRestApi(url, "读取执行日志", data, false);
    if (result.flag == true) {
        var log = result.data.data.log;
        $("#log").append(log);
        $("#errorNotifyMsg").html(result.data.errorNotify);

        $("#moreLog").off("click");
        if (result.data.data.end) {
            $("#moreLog").hide();
        }
        else {
            if(result.data.data.offset < size){
                $("#moreLog").hide();
            }
            else{
                $("#moreLog").show();
                $("#moreLog").on("click", function () {
                    initLog(false, offset + result.data.data.offset, size);
                })
            }
        }
    }
}


//显示更多的日志信息(标准输出)
function initLogMore() {

    //标准输出div不存在,则推出
    if ($("#logMore").length == 0) {
        return;
    }

    var data = {};
    data["taskId"] = taskId;
    data["jobId"] = jobId;
    data["attemptId"] = attemptId;
    data["offset"] = 0;
    data["size"] = 10000;
    var url;
    url = "/api/log/readResult";
    var result = requestRemoteRestApi(url, "读取执行日志", data, false);
    if (result.flag == true) {
        var log = result.data.data.log;
        $("#logMore").html(log);
    }
}

//获取taskHistory并用模态框显示
function showTaskHistory(taskId) {
    $("#taskHistory").bootstrapTable("destroy");

    var queryParams = {};
    queryParams["taskId"] = taskId;
    $.ajaxSettings.async = false;
    $("#taskHistory").bootstrapTable({
        columns: taskHistoryColumn,
        pagination: false,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/taskHistory/getByTaskId',
        queryParams: function (params) {
            for (var key in queryParams) {
                var value = queryParams[key];
                params[key] = value;
            }
            return params;
        }, responseHandler: function (res) {
            if (res.status) {
                showMsg("error", "初始化执行历史列表", res.status.msg);
                return res;
            }
            else {
                return res;
            }
        },
        showColumns: true,
        showHeader: true,
        showToggle: true,
        pageSize: 20,
        pageList: [10, 20, 50, 100, 200, 500, 1000],
        paginationFirstText: '首页',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLastText: '末页'
    });
    $.ajaxSettings.async = true;
    $("#taskHistoryModal").modal("show");
}

var taskHistoryColumn = [{
    field: 'executeStartTime',
    title: '开始执行时间',
    switchable: true,
    formatter: formatDateTime
}, {
    field: 'executeEndTime',
    title: '执行结束时间',
    switchable: true,
    formatter: formatDateTime
}, {
    field: 'dataTime',
    title: '数据时间',
    switchable: true,
    formatter: formatDateTime
}, {
    field: 'executeUser',
    title: '执行者',
    switchable: true
}, {
    field: 'finishReason',
    title: '结束原因',
    switchable: true
}];
