$(function () {
    initSystemStatus();
});

//初始化系统状态：初始状态、switch、事件
function initSystemStatus() {
    //详细处理方式等待rest接口完成
    var response = requestRemoteRestApi("/api/system/status/get", "获取系统当前状态", {},false,false);


    if (response.flag == false) {
        return;
    }
    var status = response.data.data.status;

    status = status == 1 ? true : false;

    $("#systemStatus").bootstrapSwitch({
        onText: "启用",
        offText: "关闭",
        state: status,
        labelText: "当前状态",
        offColor: 'danger'
    });
    $("#systemStatus").on("switchChange.bootstrapSwitch", function (e) {
        var input = e.target;
        var value = input.checked;
        var status;
        switch (value) {
            case true:
                status = 1;
                break;
            case false:
                status = 2;
                break;
            default:
                status = 1;
        }
        //等rest接口完成再取消注释
        updateSystemStatus(status);
    });
}

//初始化是否自动调度状态：初始状态、switch、事件
function initSystemAutoStatus() {
    //详细处理方式等待rest接口完成
    //var response = requestRemoteRestApi("/api/system/autoStatus", "获取系统当前状态", {});
    //创建bootstrapSwitch
    $("#systemAutoStatus").bootstrapSwitch({
        onText: "启用",
        offText: "关闭",
        state: false,
        labelText: "自动调度",
        offColor: 'danger'
    });

    $("#systemAutoStatus").on("switchChange.bootstrapSwitch", function (e) {
        var input = e.target;
        var value = input.checked;
        var status;
        switch (value) {
            case true:
                status = 1;
                break;
            case false:
                status = 2;
                break;
            default:
                status = 1;
        }
        //等rest接口完成再取消注释
        //updateSystemAutoStatus(status);
    });
}

//更新系统是否可用状态
function updateSystemStatus(status) {
    var data = {status: status};
    requestRemoteRestApi("/api/system/status", "修改系统状态", data,true,true);
}
//更新系统是否可以自动调度状态
function updateSystemAutoStatus(status) {
    var data = {status: status};
    requestRemoteRestApi("/api/system/autoSchedule", "修改系统是否自动调度", data,true,true);

}
