var apiUrl = contextPath + "/api/job/getDepend?jobId=";
var dependUrl = contextPath + "/job/dependency?jobId=";

$(function () {
    $.ajax({
        url:contextPath + "/assets/json/taskStatusColor.json",
        async:false,
        success:function(data){
            stautsColor = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化状态颜色', msg);
        }
    })

    var tree = CollapsibleTree("#dependTree");
    tree.init(apiUrl + query.jobId);
});

function jumpToNode(d) {
    if (!d.rootFlag) {
        window.location.href = dependUrl + d.jobId;
    }
}

//计算填充颜色
function getColor(d) {
    //不是有效场合,直接显示
    if (d.status != 1) {
        return stautsColor[d.status];
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
            //
            if (statusArr.length == 1) {
                return stautsColor[statusArr[0]];
            }
            else {
                return "gray";
            }
        }
    }
}


function showNoteInfo(thisTag, d) {
    var options = {};

    var content = getContent(d);


    options["title"] = d.jobName;
    options["content"] = content;
    options["template"] = '<div class="popover" role="tooltip" style="width:100%"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>';
    options["animation"] = true;
    options["placement"] = "bottom";
    options["container"] = $("#popoverContainer");

    //console.log(options);

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
    $(single).find("[name=activeStartDate]").text(formatDateTime(d.activeStartDate));
    $(single).find("[name=activeEndDate]").text(formatDateTime(d.activeEndDate));

    content.append(single);

    //var taskList = d.taskList;
    //for (var i = 0, len = taskList.length; i < len; i++) {
    //    var task = taskList[i];
    //    var jobId = task.jobId;
    //    var taskId = task.taskId;
    //    var executeUser = task.executeUser;
    //    var scheduleTime = formatDateTime(task.scheduleTime);
    //    var executeStartTime = formatDateTime(task.executeStartTime);
    //    var executeEndTime = formatDateTime(task.executeEndTime);
    //    var executeTime = formatTimeInterval(task.executeTime);
    //    var status = task.status;
    //
    //    var color = stautsColor[task.status];
    //    var single = $("#pattern").children().clone();
    //    var newUrl = dependencyUrl + taskId;
    //
    //    $(single).find("[name=status]").attr("href", newUrl);
    //    $(single).find("[name=status] i").css("color", color);
    //    $(single).find("[name=jobId]").text(jobId);
    //    $(single).find("[name=taskId]").text(taskId);
    //    $(single).find("[name=executeUser]").text(executeUser);
    //    $(single).find("[name=executeStartTime]").text(executeStartTime);
    //    $(single).find("[name=executeEndTime]").text(executeEndTime);
    //    $(single).find("[name=executeTime]").text(executeTime);
    //    $(single).find("[name=scheduleTime]").text(scheduleTime);
    //
    //    content.append(single);
    //}

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


