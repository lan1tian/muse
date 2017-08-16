var apiUrl = contextPath + "/api/task/getDepend?taskId=";
var taskUrl = contextPath + "/task/detail?taskId=";

$(function () {
    $.ajax({
        url: contextPath + "/assets/json/taskStatusColor.json",
        async: false,
        success: function (data) {
            stautsColor = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化执行状态颜色', msg);
        }
    })

    initGraph();

});

function initGraph(){
    var level = 1;
    var size = 5;
    var parentLevel = 0;
    var childLevel = 0;
    $.ajax({
        url: apiUrl+taskId,
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
    tree.init(apiUrl + taskId);
}

function jumpToNode(d) {
    if (!d.rootFlag) {
        window.location.href = taskUrl + d.taskId;
    }
}

//计算填充颜色
function getColor(d) {

    if (d.status != null) {
        var color = stautsColor[d.status].color;

        return color;
    }
}

function getNodeName(d) {
    return d.jobName + "_" + d.taskId;
}


function showTaskInfo(thisTag, d) {
    var options = {};

    var content = getContent(d);
    options["title"] = d.jobName + "_" + d.taskId;
    options["content"] = content;
    options["template"] = '<div class="popover" role="tooltip" style="max-width:600px"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>';
    options["animation"] = true;
    options["placement"] = "bottom";
    options["container"] = $("#popoverContainer");

    //console.log(options);

    $(thisTag).popover(options);
    var result = $(thisTag).popover('show');
    var popId = $(result).attr("aria-describedby");
    $("#" + popId).find("h3").append($('<a class="close" onclick="clickHideTaskInfo(\'' + popId + '\')"><span aria-hidden="true">&times;</span></a>'));
    $("#" + popId).find(".popover-content").html(content);
}
function getContent(d) {
    var content = $("<div></div>");
    var single = $("#pattern").children().clone();
    $(single).find("[name=jobId]").text(d.jobId);
    $(single).find("[name=taskId]").text(d.taskId);
    $(single).find("[name=executeUser]").text(d.executeUser);
    $(single).find("[name=executeStartTime]").text(formatDateTime(d.executeStartTime));
    $(single).find("[name=executeEndTime]").text(formatDateTime(d.executeEndTime));
    $(single).find("[name=executeTime]").text(formatTimeInterval(d.executeTime));
    $(single).find("[name=scheduleTime]").text(formatDateTime(d.scheduleTime));
    content.append(single);
    return content;
}

function hideTaskInfo(thisTag) {
    $(thisTag).popover('hide');
}

function clickHideTaskInfo(tagId) {
    $("#" + tagId).popover('hide');
}

function chooseTask(d) {
    var content = getContent(d);
    $("#toTaskModal .modal-body").html(content);
    $("#toTaskModal").modal("show");
}


