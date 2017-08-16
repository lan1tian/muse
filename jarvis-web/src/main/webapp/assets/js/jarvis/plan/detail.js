var dependencyAPIUrl = contextPath + "/api/plan/getDependDetail?jobId=" + jobId;
var dependencyUrl = contextPath + "/plan/detail?jobId=";
var taskStatusColor = null;

$(function () {
    initJobDetail();
    initColor();
    planReason();
    initGraph();
});


function initJobDetail() {
    $.ajax({
        url: contextPath + "/api/job/getById",
        data: {jobId: jobId},
        success: function (data) {
            if (data.code == 1000) {
                var job = data.data;
                $("#jobDetail").attr("href", contextPath + "/job/detail?jobId=" + job.jobId);
                $("#jobDetail").text(job.jobName);
            }
            else {
                showMsg('warning', '初始化任务信息', data.msg);
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化任务信息', msg);
        }
    })
}

function initColor() {
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
function planReason() {
    $.ajax({
        url: contextPath + "/api/plan/planReason",
        data: {jobId: jobId, scheduleTime: scheduleTime},
        success: function (data) {
            if (data.status) {
                showMsg('warning', '推测未执行原有', data.status.msg);
            }
            else {
                $("#reason").text(data.retCont);
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '推测未执行原有', msg);
        }
    })
}

function initGraph() {
    var level = 1;
    var size = 5;
    var parentLevel = 0;
    var childLevel = 0;
    $.ajax({
        url: dependencyAPIUrl,
        async: false,
        success: function (data) {
            if (data.status.code) {
                showMsg('warning', '获取依赖', data.result);
            }
            else {
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
    tree.init(dependencyAPIUrl);
}

//计算填充颜色
function getColor(d) {
    if (d.status == null || d.status != 1) {
        return "#B10DC9";
    }
    //代表是job，需要根据task计算显示颜色
    else {
        var taskList = d.taskList;
        //没有对应task，显示白色
        if (taskList.length <= 0) {
            return "white";
        }
        //有对应task，根据task状态计算显示颜色
        else {
            var totalStatus = {};
            var statusArr = new Array();
            //获取所有task状态
            $(taskList).each(function (i, c) {
                var status = c.status;
                if (totalStatus[status] == null) {
                    totalStatus[status] = status;
                    statusArr.push(status);
                }
            });
            //统计状态
            if (statusArr.length == 1) {
                return taskStatusColor[statusArr[0]].color;
            }
            else {
                return "gray";
            }
        }
    }
}


function showTasks(thisTag, d) {
    var options = {};

    var content = getContent(d);


    options["title"] = "任务ID:" + d.jobId + " 任务名:" + d.jobName;
    options["content"] = content;
    options["template"] = '<div class="popover" role="tooltip" style="width:100%"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>';
    options["animation"] = true;
    options["placement"] = "bottom";
    options["container"] = $("#popoverContainer");

    //console.log(options);

    $(thisTag).popover(options);
    var result = $(thisTag).popover('show');
    var popId = $(result).attr("aria-describedby");
    $("#" + popId).find("h3").append($('<a class="close" onclick="clickHideTasks(\'' + popId + '\')"><span aria-hidden="true">&times;</span></a>'));
    $("#" + popId).find(".popover-content").html(content);
}
function getContent(d) {
    var taskList = d.taskList;
    var content = $("<div></div>");

    taskList=taskList==null?[]:taskList;

    for (var i = 0, len = taskList.length; i < len; i++) {
        var task = taskList[i];
        var jobId = task.jobId;
        var taskId = task.taskId;
        var executeUser = task.executeUser;
        var scheduleTime = formatDateTime(task.scheduleTime);
        var executeStartTime = formatDateTime(task.executeStartTime);
        var executeEndTime = formatDateTime(task.executeEndTime);
        var executeTime = formatTimeInterval(task.executeTime);
        var status = task.status;

        var color = taskStatusColor[task.status].color;
        var single = $("#pattern").children().clone();
        var newUrl = dependencyUrl + taskId;

        //executeTime="1天23小时59分59秒";
        //jobId="1000000";
        //taskId="1000000";
        //executeUser="qqqqqqqqqq";

        $(single).find("[name=status]").attr("href", newUrl);
        $(single).find("[name=status] i").css("color", color);
        $(single).find("[name=jobId]").text(jobId);
        $(single).find("[name=taskId]").text(taskId);
        $(single).find("[name=executeUser]").text(executeUser);
        $(single).find("[name=executeStartTime]").text(executeStartTime);
        $(single).find("[name=executeEndTime]").text(executeEndTime);
        $(single).find("[name=executeTime]").text(executeTime);
        $(single).find("[name=scheduleTime]").text(scheduleTime);

        content.append(single);
    }

    return content;
}

function hideTasks(thisTag) {
    $(thisTag).popover('hide');
}

function clickHideTasks(tagId) {
    $("#" + tagId).popover('hide');
}

function chooseTask(d) {
    var content = getContent(d);
    $("#toTaskModal .modal-body").html(content);
    $("#toTaskModal").modal("show");
}


