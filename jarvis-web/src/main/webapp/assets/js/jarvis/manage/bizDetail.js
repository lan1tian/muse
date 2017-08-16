var bizGroup = null;

$(function () {
    initBizGroup();
    initUsers();
    initStatus();
});

//初始化业务标签
function initBizGroup() {
    if (null != id && '' != id) {
        $.ajax({
            url:contextPath + "/api/bizGroup/getById",
            data:{id: id},
            async:false,
            success:function(data){
                if (data.code == 1000) {
                    bizGroup = data.data;
                    $("#name").val(bizGroup.name);
                }
                else {
                    new PNotify({
                        title: '获取业务标签详情',
                        text: data.msg,
                        type: 'warning',
                        icon: true,
                        styling: 'bootstrap3'
                    });
                }
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '获取业务组信息', msg);
            }
        })
    }

}

//初始化内网用户
function initUsers() {
    $.ajax({
        url:contextPath + "/api/common/getAllUser",
        success:function(data){
            if (1000 == data.code) {
                var users = data.rows;
                var newData = new Array();

                $(users).each(function (i, c) {
                    var item = {};
                    item["id"] = c.uname;
                    item["text"] = c.nick;
                    newData.push(item);
                });

                $("#owner").select2({
                    data: newData,
                    width: '100%'
                });
                if (null != bizGroup) {
                    var owner = bizGroup.owner;
                    var arr = owner.trim().split(",");
                    $("#owner").val(arr).trigger("change");
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
//
function initStatus() {
    $.ajax({
        url:contextPath + "/api/bizGroup/getBizGroupStatus",
        success:function(data){
            $(data).each(function (i, c) {
                var input = '<input type="radio" name="status" value="' + c.id + '" />' + c.text + " ";
                $("#status").append(input);
            });

            if (null != bizGroup) {
                $("#status input[value=" + bizGroup.status + "]").click();
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化业务组状态', msg);
        }
    })
}


//获取配置信息
function getData() {
    var name = $("#name").val();
    var ownerArr = $("#owner").val();
    var status = $("#status input:checked").val();
    //检查维护人是否为空
    if (null == ownerArr) {
        new PNotify({
            title: '保存配置',
            text: "维护人不能为空",
            type: 'error',
            icon: true,
            styling: 'bootstrap3'
        });
        return null;
    }
    var hasName = false;

    $.ajax({
        url:contextPath + "/api/bizGroup/getByName",
        data:{name: name},
        async:false,
        success:function(data){
            if (null == data.data) {
                hasName = true;
            }
            else {
                if (null == id || '' == id) {
                    hasName = false;
                }
                else {
                    if (id == data.data.id) {
                        hasName = true;
                    }
                    else {
                        hasName = false;
                    }
                }
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '检查业务名称', msg);
        }
    })

    if (!hasName) {
        new PNotify({
            title: '保存配置',
            text: "业务标签已经存在,不能重复添加",
            type: 'error',
            icon: true,
            styling: 'bootstrap3'
        });
        return null;
    }


    var owner = "";
    $(ownerArr).each(function (i, c) {
        if (owner == "") {
            owner = c;
        }
        else {
            owner += "," + c;
        }
    });

    if(undefined==status){
        new PNotify({
            title: '保存配置',
            text: "业务标签状态必须选择",
            type: 'error',
            icon: true,
            styling: 'bootstrap3'
        });
        return null;
    }

    var result = {};
    if (null != id && '' != id) {
        result["id"] = id;
    }
    result["name"] = name;
    result["owner"] = owner;
    result["status"] = status;

    return result;
}

//保存业务标签
function saveBizGroup() {

    var data = getData();
    if (null == data) {
        return null;
    }

    if (null != id && '' != id) {
        requestRemoteRestApi("/api/bizGroup/edit", "修改业务标签", data,true,true);
    }
    else {
        requestRemoteRestApi("/api/bizGroup/add", "新增业务标签", data,true,true);
    }
}