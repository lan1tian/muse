var appStatus = null;     //app的所有状态
var appType = null;       //app的所有类型

$(function () {
    $.ajax({
        url:contextPath + "/api/app/getAppStatus",
        async:false,
        success:function(data){
            appStatus = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化应用状态', msg);
        }
    })

    $.ajax({
        url:contextPath + "/api/app/getAppType",
        async:false,
        success:function(data){
            appType = data;
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '初始化应用类型', msg);
        }
    })


    $(".input-group select").select2({width: '100%'});
    initType(appType);       //初始化app类型
    initStatus(appStatus);   //初始化app状态
    initData();              //初始化app数据
});
//初始化app的所有类型
function initType(appType) {
    var data = new Array();
    var all = {};
    all["id"] = "all";
    all["text"] = "全部";
    data.push(all);
    $(appType).each(function (i, c) {
        data.push(c);
    });
    $("#appType").select2({
        data: data,
        width: '100%'
    });
    $("#appType").val(1).trigger("change");
}
//初始化app的所有可选状态
function initStatus(appStatus) {
    var data = new Array();
    var all = {};
    all["id"] = "all";
    all["text"] = "全部";
    data.push(all);
    $(appStatus).each(function (i, c) {
        if (c["id"] != 3) {
            data.push(c);
        }
    });
    $("#status").select2({
        data: data,
        width: '100%'
    });

}

//搜索
function search() {
    $("#content").bootstrapTable('destroy', '');
    initData();
}

//重置
function reset() {
    var selects = $(".input-group select");
    $(selects).each(function (i, c) {
        $(c).val("all").trigger("change");
    });
}

//获取查询参数
function getQueryPara() {
    var queryPara = {};

    var appName = $("#appName").val();    //应用名
    var appType = $("#appType").val();    //应用类型
    var status = $("#status").val();      //应用状态

    //如果选择的是全部，则设置为空字符串，在后台查询时将会排除此条件
    appName = appName == 'all' ? '' : appName;
    appType = appType == 'all' ? '' : appType;
    status = status == 'all' ? '' : status;

    queryPara["appName"] = appName;
    queryPara["appType"] = appType;
    queryPara["status"] = status;

    return queryPara;
}

//初始化数据及分页
function initData() {
    var queryParams = getQueryPara();     //获取查询参数
    $("#content").bootstrapTable({
        columns: columns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/app/getApps',
        queryParams: function (params) {
            for (var key in queryParams) {
                var value = queryParams[key];
                params[key] = value;
            }
            return params;
        },
        responseHandler:function(res){
            if(res.status){
                showMsg("error","初始化应用列表",res.status.msg);
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

//修改app的状态
function modifyAppStatus(appId, status, appName, maxConcurrency) {
    var data = {appId: appId, applicationName: appName, status: status, maxConcurrency: maxConcurrency};
    //删除需要提示
    if (3 == status) {
        (new PNotify({
            title: '删除App',
            text: '确定删除App?',
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
                requestRemoteRestApi("/api/app/edit", "修改应用状态", data,false,true);
                search();
            }).on('pnotify.cancel', function () {
            });
    }
    //启用或停用
    else {
        requestRemoteRestApi("/api/app/edit", "修改应用状态", data,false,true);
        search();
    }
}

//app的column配置
var columns = [{
    field: 'appId',
    title: '应用id',
    switchable: true,
    visible: false
}, {
    field: 'appName',
    title: '应用名称',
    switchable: true
}, {
    field: 'appKey',
    title: 'appkey',
    switchable: true
}, {
    field: 'appType',
    title: '应用类型',
    switchable: true,
    formatter: appTypeFormatter,
    visible: false
}, {
    field: 'status',
    title: '应用状态',
    switchable: true,
    formatter: appStatusFormatter
}, {
    field: 'workerGroup',
    title: 'Worker组',
    switchable: true,
    formatter: workerGroupFormatter
}, {
    field: 'maxConcurrency',
    title: '最大并发数',
    switchable: true
}, {
    field: 'owner',
    title: '维护人',
    switchable: true
}, {
    field: 'updateUser',
    title: '最后更新人',
    switchable: true,
    visible: false
}, {
    field: 'createTime',
    title: '创建时间',
    switchable: true,
    formatter: formatDate,
    visible: false
}, {
    field: 'updateTime',
    title: '更新时间',
    switchable: true,
    formatter: formatDate,
    visible: false
}, {
    field: 'operation',
    title: '操作',
    switchable: true,
    formatter: operateFormatter
}];

//app状态格式化
function appStatusFormatter(value, row, index) {
    var result = "";
    //停用
    if (2 == value) {
        result = "<i class='glyphicon glyphicon-pause text-danger'></i>";
    }
    //启用
    else if (1 == value) {
        result = "<i class='glyphicon glyphicon-ok text-success'></i>";
    }

    return result;
}

//操作按钮格式化
function operateFormatter(value, row, index) {
    var appId = row["appId"];
    var appName = row["appName"];
    var maxConcurrency = row["maxConcurrency"];
    var status = row["status"];
    var result = [
        '<a class="edit" href="' + contextPath + '/manage/appDetail?appId=' + appId + '" title="编辑应用信息" target="_blank">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>  '
    ].join('');

    var operation = '';

    $(appStatus).each(function (i, c) {
        if (c["id"] != 'all' && c["id"] != status) {
            var style = "";
            if (1 == c["id"]) {
                style = "btn btn-xs btn-success";
            }
            else if (2 == c["id"]) {
                style = "btn btn-xs btn-danger";
            }
            operation += '&nbsp;<a class="' + style + '" href="javascript:void(0)" onclick="modifyAppStatus(' + appId + ',' + c["id"] + ',\'' + appName + '\',' + maxConcurrency + ')" >' + c["text"] + '</a>';
        }
    });
    return result + "&nbsp" + operation;
}

//app类型格式化
function appTypeFormatter(value, row, index) {
    var result = '';
    if (value == 1) {
        result = "普通";
    }
    else if (value == 2) {
        result = "管理";
    }
    else {
        result = "未定义类型:" + value;
    }
    return result;
}
//workerGroup格式化器
function workerGroupFormatter(value, row, index) {
    var appId = row["appId"];
    var result = '<a href="javascript:void(0)" onclick="showWorkerGroup(' + appId + ')">查看</a>';
    return result;
}

//获取workerGroup列表并显示模态框
function showWorkerGroup(appId) {
    $("#workerGroupList").bootstrapTable("destroy");
    var queryParams = {};
    queryParams["appId"] = appId;
    $.ajaxSettings.async = false;          //禁用异步加载，数据完成后才显示模态框
    $("#workerGroupList").bootstrapTable({
        columns: workerGroupColumn,
        pagination: false,
        sidePagination: 'server',
        search: false,
        url: contextPath + "/api/workerGroup/getByAppId",
        queryParams: function (params) {
            for (var key in queryParams) {
                var value = queryParams[key];
                params[key] = value;
            }
            return params;
        },
        responseHandler:function(res){
            if(res.status){
                showMsg("error","初始化workerGroup列表",res.status.msg);
                return res;
            }
            else{
                return res;
            }
        },
        showColumns: true,
        showHeader: true,
        showToggle: true
    });
    $.ajaxSettings.async = true;
    $("#workerGroupModal").modal("show");
}

//WorkerGroup列表的字段信息
var workerGroupColumn = [{
    field: 'id',
    title: 'WorkerGroupId',
    switchable: true,
    visible: false
}, {
    field: 'name',
    title: 'WorkerGroup名',
    switchable: true,
    visible: true
}, {
    field: 'status',
    title: '状态',
    switchable: true,
    visible: true,
    formatter: workerGroupStatusFormatter
}, {
    field: 'owner',
    title: '维护人',
    switchable: true,
    visible: true
}];

function workerGroupStatusFormatter(value, row, index) {
    var result = "";
    //禁用
    if (2 == value) {
        result = "<i class='glyphicon glyphicon-remove text-danger'></i>";
    }
    //启用
    else if (1 == value) {
        result = "<i class='glyphicon glyphicon-ok text-success'></i>";
    }
    else {
        result = "<i class='glyphicon glyphicon-question-sign text-info'></i>";
    }

    return result;
}