$(function () {
    $(".input-group select").select2({width: '100%'});
});

//检查worker group名称
function checkWorkerGroupName() {
    var workerGroupId = $("#workerGroupId").val();
    var name = $("#name").val();
    if (name == '') {
        new PNotify({
            title: '保存worker group',
            text: '名称不能为空',
            type: 'warning',
            icon: true,
            styling: 'bootstrap3'
        });
        return false;
    }

    var flag = true;
    $.ajax({
        url: contextPath + '/manage/checkWorkerGroupName',
        type: 'POST',
        data: {id: workerGroupId, name: name},
        success: function (data) {
            if (data.code == 1) {
                new PNotify({
                    title: '保存worker group',
                    text: data.msg,
                    type: 'warning',
                    icon: true,
                    styling: 'bootstrap3'
                });
                flag = false;
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '获取父任务', msg);
        }
    });

    return flag;
}
//增加worker group
function addWorkerGroup() {
    var workerGroupId = $("#workerGroupId").val();
    var name = $("#name").val();
    var flag = checkWorkerGroupName();
    if (flag == false) {
        return;
    }

    var data = {name: name};
    requestRemoteRestApi("/api/workerGroup/add", "新增Worker Group", data,true,true);
}
//更新worker group
function updateWorkerGroup() {
    var workerGroupId = $("#workerGroupId").val();
    var name = $("#name").val();
    var flag = checkWorkerGroupName();
    if (flag == false) {
        return;
    }
    var data = {workerGroupId: workerGroupId, name: name};
    requestRemoteRestApi("/api/workerGroup/edit", "更新Worker Group", data,true,true);
}


