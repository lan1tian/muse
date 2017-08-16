var bizGroupStatus = {};
$(function () {
    initName();
    initOwner();
    initStatus();

    initData();
});

//初始化标签名
function initName() {
    $.ajax({
        url:contextPath + "/api/bizGroup/getBizGroupName",
        success:function(data){
            var newData = new Array();

            var all = {};
            all["id"] = "all";
            all["text"] = "全部";
            newData.push(all);

            $(data).each(function (i, c) {
                var item = {};
                item["id"] = c;
                item["text"] = c;
                newData.push(item);
            });

            $("#name").select2({
                data: newData,
                width: '100%'
            });
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化业务组信息', msg);
        }
    })
}
//初始化标签状态
function initStatus() {
    $.ajax({
        url:contextPath + "/api/bizGroup/getBizGroupStatus",
        success:function(data){
            var newData = new Array();
            var all = {};
            all["id"] = "all";
            all["text"] = "全部";
            newData.push(all);

            $(data).each(function (i, c) {
                var item = {};
                item["id"] = c.id;
                item["text"] = c.text;
                newData.push(item);
                bizGroupStatus[c.id] = c.text;
            });

            $("#status").select2({
                data: newData,
                width: '100%'
            });
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化业务组状态', msg);
        }
    })
}
//初始化维护人
function initOwner() {
    $.ajax({
        url:contextPath + "/api/bizGroup/getBizGroupOwner",
        success:function(data){
            var newData = new Array();
            var all = {};
            all["id"] = "all";
            all["text"] = "全部";
            newData.push(all);

            $(data).each(function (i, c) {
                var item = {};
                item["id"] = c;
                item["text"] = c;
                newData.push(item);
            });

            $("#owner").select2({
                data: newData,
                width: '100%'
            });
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化维护人', msg);
        }
    })
}
//搜索
function search() {
    $("#content").bootstrapTable("destroy");
    initData();
}
//重置
function reset() {
    $("#name").val("all").trigger("change");
    $("#status").val("all").trigger("change");
    $("#owner").val("all").trigger("change");
}

//获取查询参数
function getQueryPara() {
    var queryPara = {};

    var name = $("#name").val();
    var status = $("#status").val();
    var owner = $("#owner").val();

    name = name == "all" ? "" : name;
    status = status == "all" ? "" : status;
    owner = owner == "all" ? "" : owner;

    queryPara["name"] = name;
    queryPara["status"] = status;
    queryPara["owner"] = owner;

    return queryPara;
}

//初始化数据及分页
function initData() {
    var queryParams = getQueryPara();
    $("#content").bootstrapTable({
        columns: columns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/bizGroup/getPaginationByCondition',
        queryParams: function (params) {
            for (var key in queryParams) {
                var value = queryParams[key];
                params[key] = value;
            }
            return params;
        },
        responseHandler:function(res){
            if(res.status){
                showMsg("error","初始化业务组列表",res.status.msg);
                return res;
            }
            else{
                return res;
            }
        },
        showColumns: true,
        showHeader: true,
        showToggle: true,
        pageSize: 20,
        pageList: [10, 20, 50, 100, 200, 500],
        paginationFirstText: '首页',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLastText: '末页',
        showExport: true,
        exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'doc', 'excel'],
        exportDataType: 'all'
    });
}


var columns = [{
    field: 'id',
    title: 'id',
    switchable: true,
    visible: false
}, {
    field: 'name',
    title: '业务标签名',
    switchable: true,
    visible: true
}, {
    field: 'status',
    title: '状态',
    switchable: true,
    visible: true,
    formatter: bizGroupStatusFormatter
}, {
    field: 'owner',
    title: '维护人',
    switchable: true,
    visible: true
}, {
    field: 'createTime',
    title: '创建时间',
    switchable: true,
    visible: false,
    formatter: formatDateTime
}, {
    field: 'updateTime',
    title: '更新时间',
    switchable: true,
    visible: false,
    formatter: formatDateTime
}, {
    field: 'updateUser',
    title: '更新人',
    switchable: true,
    visible: false
}, {
    field: 'operation',
    title: '操作',
    switchable: true,
    visible: true,
    formatter: operationFormatter
}];

var statusClassStyle = {"1": "glyphicon glyphicon-ok text-success", "2": "glyphicon glyphicon-pause text-danger"};
//格式化bizType状态
function bizGroupStatusFormatter(value, row, index) {
    var result = '<i class="' + statusClassStyle[value] + '"></i>';
    return result;
}

function operationFormatter(value, row, index) {
    var result = '<a href="' + contextPath + '/manage/bizDetail?id=' + row["id"] + '" target="_blank"><i class="glyphicon glyphicon-pencil"></i></a>';

    result += ' <a href="javascript:void(0)" onclick="removeBizGroup(' + row["id"] + ')" title="删除部门"><i class="glyphicon glyphicon-remove text-danger"></i></a>';

    return result;
}

function removeBizGroup(id) {


    (new PNotify({
        title: '业务标签操作',
        text: '确定删除此标签?',
        icon: 'glyphicon glyphicon-question-sign',
        hide: false,
        confirm: {
            confirm: true
        },
        buttons: {
            closer: false,
            sticker: false
        },
        history: {
            history: false
        }
    })).get().on('pnotify.confirm', function () {
            var data = {};
            data["id"] = id;
            requestRemoteRestApi("/api/bizGroup/delete", "删除业务标签", data,false,true);
            window.setTimeout(function(){
                search();
            },1000);
        }).on('pnotify.cancel', function () {
        });


}