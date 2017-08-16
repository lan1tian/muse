var dependencyUrl = contextPath + "/api/job/getDepend?jobId=";
var url = dependencyUrl + jobVo.jobId;
var detailUrl = contextPath + "/job/detail?jobId=";
var taskDetailUrl = contextPath + "/task/detail?taskId=";
var taskStatusColor = null;

$(function () {
    if (undefined != jobVo.jobId) {
        initJobData();     //初始化job详细信息
        initJobStatusColor();
        initGraph();

        initTaskStatusColor();
        initData(jobVo.jobId);

    }
});

function initTaskStatusColor() {
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

//字段配置
var columns = [{
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
    field: 'executeUser',
    title: '执行用户',
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
    field: 'executeStartTime',
    title: '开始执行时间',
    sortable: true,
    switchable: true,
    visible: true,
    formatter: formatDateTimeWithoutYear
}, {
    field: 'executeEndTime',
    title: '执行结束时间',
    sortable: true,
    switchable: true,
    visible: true,
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
}];

function initData(jobId) {
    $("#taskList").bootstrapTable({
        columns: columns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/task/getTasks',
        queryParams: function (params) {
            var queryParams = {};
            queryParams["jobIdList"] = JSON.stringify([jobId]);
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
        showColumns: false,
        showHeader: true,
        showToggle: false,
        sortable: true,
        pageSize: 5,
        pageList: [10, 20, 50, 100, 200, 500, 1000],
        paginationFirstText: '首页',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLastText: '末页',
        showExport: false,
        exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'doc', 'excel'],
        exportDataType: 'basic'
    });
}
//百分比格式化
function progressFormatter(value, row, index) {
    var result = value * 100 + "%";
    return result;
}
//执行状态格式化
function taskStatusFormatter(value, row, index) {
    var color = taskStatusColor[value].color;
    var text = taskStatusColor[value].text;
    var result = '<i class="fa fa-circle fa-2x" style="color: ' + color + '"></i>' + text;

    result = "<div style='white-space:nowrap;'>" + result + "</div?";

    return result;
}
function taskDetailFormatter(value, row, index) {

    var result = "<a href='" + taskDetailUrl + value + "'>" + value + "</a>";

    return result;
}


function initJobStatusColor() {
    $.ajax({
        url: contextPath + "/assets/json/jobStatusColor.json",
        async: false,
        success: function (data) {
            stautsColor = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化状态颜色', msg);
        }
    })
}

function initGraph() {
    var level = 1;
    var size = 5;
    var parentLevel = 0;
    var childLevel = 0;
    $.ajax({
        url: url,
        async: false,
        success: function (data) {
            if(data.status.code){
                showMsg('warning', '获取依赖', data.result);
            }
            else{
                var children = data.children;
                var parents = data.parents;

                if (null == parents) {
                    parentLevel = 0;
                }
                else {
                    parentLevel = parents.length % size == 0 ? parents.length / size : parseInt(parents.length / size) + 1;
                }

                if (null == children) {
                    childLevel = 0;
                }
                else {
                    childLevel = children.length % size == 0 ? children.length / size : parseInt(children.length / size) + 1;
                }

                level = level + parentLevel + childLevel;
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化执行用户', msg);
        }
    });

    var y = 0;
    var width = 1140;
    var height = 100;
    if (level > 1) {
        height = (level - 1) * 120 + 200;
    }
    else {
        height = 140;
    }

    if (parentLevel == 0 && childLevel != 0) {
        y = 50;
    }
    else if (parentLevel != 0 && childLevel == 0) {
        y = height - 50;
    }
    else if (parentLevel == 0 && childLevel == 0) {
        y = height / 2;
    }
    else {
        y = height * parentLevel / (level - 1) + 20;
    }
    //console.log(y);
    //console.log(height);


    var tree = CollapsibleTree("#dependTree", width, height, y);
    tree.init(url);
}


function jumpToNode(d) {
    if (!d.rootFlag) {
        window.location.href = detailUrl + d.jobId;
    }
}
//初始化job基本信息
function initJobData() {
    if (undefined != jobVo.jobId) {
        $.ajax({
            url: contextPath + "/api/job/getById",
            data: {jobId: jobVo.jobId},
            success: function (data) {
                var newData = data.data;
                for (var key in newData) {
                    var value = generateValue(key, newData[key]);
                    $("#" + key).text(value);
                }
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '初始化任务信息', msg);
            }
        })

    }
}

//格式化显示数据
function generateValue(key, source) {
    var formatter = {
        "status": function (sourceData) {
            var result = "";

            $.ajax({
                url: contextPath + "/api/job/getJobStatus",
                async: false,
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].id == sourceData) {
                            result = data[i].text;
                            break;
                        }
                    }
                },
                error: function (jqXHR, exception) {
                    var msg = getMsg4ajaxError(jqXHR, exception);
                    showMsg('warning', '初始化显示颜色信息', msg);
                }
            })
            return result;
        },
        "appId": function (sourceData) {
            var result = "";
            $.ajax({
                url: contextPath + "/api/app/getByAppId",
                data: {appId: sourceData},
                async: false,
                success: function (data) {
                    if (null != data.appId) {
                        result = data.appName;
                    }
                },
                error: function (jqXHR, exception) {
                    var msg = getMsg4ajaxError(jqXHR, exception);
                    showMsg('warning', '获取应用信息', msg);
                }
            })
            return result;
        },
        "workerGroupId": function (sourceData) {
            var result = "";

            $.ajax({
                url: contextPath + "/api/workerGroup/getById",
                async: false,
                data: {id: sourceData},
                success: function (data) {
                    if (null != data.id) {
                        result = data.name;
                    }
                },
                error: function (jqXHR, exception) {
                    var msg = getMsg4ajaxError(jqXHR, exception);
                    showMsg('warning', '获取workerGroup信息', msg);
                }
            })
            return result;
        },
        "bizGroupId": function (sourceData) {
            var result = "";
            $.ajax({
                url: contextPath + "/api/bizGroup/getById",
                data: {id: sourceData},
                async: false,
                success: function (data) {
                    if (null != data.id) {
                        result = data.name;
                    }
                },
                error: function (jqXHR, exception) {
                    var msg = getMsg4ajaxError(jqXHR, exception);
                    showMsg('warning', '获取业务组信息', msg);
                }
            })

            return result;
        },
        "activeStartDate": function (sourceData) {
            return moment(sourceData).format("YYYY-MM-DD");
        },
        "activeEndDate": function (sourceData) {
            return moment(sourceData).format("YYYY-MM-DD");
        },
        "expiredTime": function (sourceData) {
            return sourceData + "秒";
        }
    };
    if (key in formatter) {
        var result = formatter[key](source);
        return result;
    }
    else {
        return source;
    }
}


//计算填充颜色
function getColor(d) {
    return stautsColor[d.status];
}


function showNoteInfo(thisTag, d) {
    var options = {};

    var content = getContent(d);

    options["title"] = d.jobName;
    options["content"] = content;
    options["template"] = '<div class="popover" role="tooltip" style="max-width: 600px"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>';
    options["animation"] = true;
    options["placement"] = "bottom";
    options["container"] = $("#popoverContainer");

    $(thisTag).popover(options);
    var result = $(thisTag).popover('show');
    var popId = $(result).attr("aria-describedby");
    $("#" + popId).find("h3").append($('<a class="close" onclick="clickHideNoteInfo(\'' + popId + '\')"><span aria-hidden="true">&times;</span></a>'));
    $("#" + popId).find(".popover-content").html(content);

}

function getContent(d) {

    var content = $("<div></div>");
    var single = $("#pattern").children().clone();

    $(single).find("[name=jobId]").text(d.jobId);
    $(single).find("[name=submitUser]").text(d.submitUser);
    $(single).find("[name=jobType]").text(d.jobType);
    $(single).find("[name=status]").text(d.status);
    $(single).find("[name=priority]").text(d.priority);
    $(single).find("[name=bizGroupName]").text(d.bizGroupName);

    content.append(single);

    return content;
}

function hideNoteInfo(thisTag) {
    $(thisTag).popover('hide');
}

function clickHideNoteInfo(tagId) {
    $("#" + tagId).popover('hide');
}

function chooseTask(d) {
    var content = getContent(d);
    $("#toTaskModal .modal-body").html(content);
    $("#toTaskModal").modal("show");
}
