var app = null;
var page = {appWorkerGroup: []};
$(function () {
    getAppDetail();
    initAppStatus();
    initAppOwner();
    initAppWorkerGroup();
});
//获取app详细信息
function getAppDetail() {
    if (null != appId && '' != appId) {
        $.ajax({
            url: contextPath + '/api/app/getByAppId',
            type: 'POST',
            async: false,
            data: {appId: appId},
            success: function (data) {
                if (data.code == 1000) {
                    app = data.data;

                    var appName = app.appName;
                    var maxConcurrency = app.maxConcurrency;
                    $("#appName").val(appName);
                    $("#maxConcurrency").val(maxConcurrency);
                }
                else {
                    new PNotify({
                        title: '获取app信息',
                        text: data.msg,
                        type: 'error',
                        icon: true,
                        styling: 'bootstrap3'
                    });
                }
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '获取报警详情', msg);
            }
        });
    }

}

function initAppStatus() {

    $.ajax({
        url: contextPath + "/api/app/getAppStatus",
        success: function (data) {
            var newData = new Array();
            $(data).each(function (i, c) {
                if (c["id"] != 3) {
                    var item = {};
                    item["id"] = c["id"];
                    item["text"] = c["text"];
                    newData.push(item);
                }
            });

            $("#status").select2({
                data: newData,
                width: '100%'
            });

            if (app != undefined) {
                $("#status").val(app.status).trigger("change");
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化应用状态', msg);
        }
    })
}

//初始化维护人
function initAppOwner() {
    $.ajax({
        url: contextPath + "/api/common/getAllUser",
        success: function (data) {
            if (data.code == 1000) {
                var userData = new Array();
                $(data.rows).each(function (i, c) {
                    var item = {};
                    item["id"] = c.uname;
                    item["text"] = c.nick;
                    userData.push(item);
                });

                $("#owner").select2({
                    data: userData,
                    width: '100%'
                });

                if (null != app && '' != app.owner) {
                    var owner = app.owner.trim().split(",");
                    var newData = new Array();
                    $(owner).each(function (i, c) {
                        newData.push(c);
                    });
                    $("#owner").val(newData).trigger("change");
                }
            }
            else {
                new PNotify({
                    title: '获取内网用户信息',
                    text: data.msg,
                    type: 'error',
                    icon: true,
                    styling: 'bootstrap3'
                });
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化用户列表', msg);
        }
    })
}

//初始化workerGroup
function initAppWorkerGroup() {
    $.ajax({
        url: contextPath + "/api/workerGroup/getAllWorkerGroup",
        success: function (data) {
            var newData = new Array();
            $(data).each(function (i, c) {
                var item = {};
                item["id"] = c.id;
                item["text"] = c.name;
                newData.push(item);
            });

            $("#workerGroup").select2({
                data: newData,
                width: '100%'
            });
            resetWorkGroupData(true);
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化workerGroup列表', msg);
        }
    });
}

function resetWorkGroupData(isSetControl) {
    if (isSetControl == null) {
        isSetControl = false;
    }
    if (null != appId && '' != appId) {
        $.getJSON(contextPath + "/api/workerGroup/getByAppId", {appId: appId}, function (data) {
            $(data.rows).each(function (i, c) {
                page.appWorkerGroup.push(c.id);
            });
            if (isSetControl) {
                $("#workerGroup").val(page.appWorkerGroup).trigger("change");
            }
        });
    }
}


function ArrToStr(arr) {
    var result = "";
    $(arr).each(function (i, c) {
        if ('' == result) {
            result = c;
        }
        else {
            result += "," + c;
        }
    });
    return result;
}

function getData() {
    var result = {};
    var appName = $("#appName").val();
    var maxConcurrency = $("#maxConcurrency").val();
    var status = $("#status").val();
    var ownerArr = $("#owner").val();

    var owner = "";
    if (null != ownerArr) {
        owner = ArrToStr(ownerArr);
    }

    result["appId"] = appId;
    result["applicationName"] = appName;
    result["maxConcurrency"] = maxConcurrency;
    result["status"] = status;
    result["owner"] = owner;

    return result;
}

//获取app与workerGroup组的操作数据
function getAppWorkerGroupData() {
    var allWorkerGroup = {};    //暂存所有workerGroup
    var oldWorkerGroupJson = {};
    var currentWorkerGroupJson = {};

    $(page.appWorkerGroup).each(function (i, c) {
        allWorkerGroup[c] = c;
        oldWorkerGroupJson[c] = c;
    });

    var currentWorkerGroup = $("#workerGroup").val();
    $(currentWorkerGroup).each(function (i, c) {
        allWorkerGroup[c] = c;
        currentWorkerGroupJson[c] = c;
    });

    var result = {};
    var add = [];
    var remove = [];
    for (var key in allWorkerGroup) {
        if (oldWorkerGroupJson[key] != null && currentWorkerGroupJson[key] == null) {
            var item = {};
            item["appId"] = appId;
            item["workerGroupId"] = key;
            remove.push(item);
        }
        else if (oldWorkerGroupJson[key] == null && currentWorkerGroupJson[key] != null) {
            var item = {};
            item["appId"] = appId;
            item["workerGroupId"] = key;
            add.push(item);
        }
    }
    result["add"] = add;
    result["remove"] = remove;
    return result;
}

//保存app
function saveApp() {
    if (!checkAppName()) {
        return;
    }
    var data = getData();   //获取应用数据
    var response, title;
    var flag1 = false, flag2 = false;
    if (null == appId || '' == appId) { //新增
        title = "新增app";
        response = requestRemoteRestApi("/api/app/add", title, data,false,true);
        flag1 = response.flag;
        if (true == flag1) {
            appId = response.data.data.appId;
            flag2 = modifyAppWorkerGroup();
        }
    } else {  //编辑
        title = "修改app";
        response = requestRemoteRestApi("/api/app/edit", title, data,false,true);
        flag1 = response.flag;
        if (true == flag1) {    //修改成功
            flag2 = modifyAppWorkerGroup();
        }
    }
    if (flag1 && flag2) {
        showMsg('success', title, "操作成功");
    }

    //重新刷一次 workGroup的数据.
    if (flag1) {
        resetWorkGroupData(false);
    }

}
//绑定workerGroup(新增或删除)
function modifyAppWorkerGroup() {
    var workerGroupData = getAppWorkerGroupData();
    var response;
    var flag = true;
    if (workerGroupData["add"].length > 0) {
        response = requestRemoteRestApi("/api/app/workerGroup/add", "app加入WorkerGroup", workerGroupData["add"],false,true);
        flag = response.flag;
    }
    if (workerGroupData["remove"].length > 0) {
        response = requestRemoteRestApi("/api/app/workerGroup/delete", "app移除WorkerGroup", workerGroupData["remove"],false,true);
        flag = response.flag;
    }
    return flag;
}

//检查app名是否重复
function checkAppName() {
    var appName = $("#appName").val();
    if (null == appName || "" == appName) {
        return false;
    }
    var flag = true;
    $.ajax({
        url: contextPath + '/manage/checkAppName',
        type: 'POST',
        async: false,
        data: {appId: appId, appName: appName},
        success: function (data) {
            if (data.code == 1) {
                new PNotify({
                    title: '保存应用',
                    text: data.msg,
                    type: 'warning',
                    icon: true,
                    styling: 'bootstrap3'
                });
                flag = false;
                $("#appName").focus();
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '检查应用名称', msg);
        }
    });

    return flag;
}