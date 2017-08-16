//页面变量
var workerPage = {heartbeatInfo: {}};

$(function () {
    //获取心跳信息. 第一步执行.
    getHeartbeatInfo();

    initWorkerData();
    initWorkerGroupData();

    $(".input-group select").select2({width: '100%'});
});

var workerColumns = [{
    field: 'id',
    title: 'WorkerId',
    switchable: true,
    visible: false
}, {
    field: 'workerGroupId',
    title: '所属WorkerGroupId',
    switchable: true,
    visible: false
}, {
    field: 'ip',
    title: 'IP',
    switchable: true
}, {
    field: 'port',
    title: '端口',
    switchable: true
}, {
    field: 'workerGroupName',
    title: '所属WorkerGroup',
    switchable: true
}, {
    field: 'onlineStatus',
    title: '上线状态',
    switchable: true,
    formatter: onlineStatusFormatter
}, {
    field: 'taskNum',
    title: '执行任务数',
    switchable: true,
    formatter: taskNumFormatter
}, {
    field: 'heartbeatStatus',
    title: '心跳状态',
    switchable: true,
    formatter: heartbeatStatusFormatter
}, {
    field: 'status',
    title: '启用状态',
    switchable: true,
    formatter: workerStatusFormatter
}, {
    field: 'createTime',
    title: '创建时间',
    switchable: true,
    formatter: formatDateTime,
    visible: false
}, {
    field: 'updateTime',
    title: '更新时间',
    switchable: true,
    formatter: formatDateTime,
    visible: false
}, {
    field: 'operation',
    title: '操作',
    switchable: true,
    formatter: operateWorkerFormatter
}];

//获取心跳信息
function getHeartbeatInfo() {
    workerPage.heartbeatInfo = {};


    var response = requestRemoteRestApi("/api/worker/heartbeat/get", "获取worker在线状态", {},false,false);
    var list = response.flag == true ? response.data.data.list : [];

    if (list.length == 0) {
        return;
    }
    var i,key;
    for (i = 0; i < list.length; i++) {
        key = list[i].ip + ':' + list[i].port;
        workerPage.heartbeatInfo[key] = {taskNum : list[i].taskNum};
    }
}

//上线状态——格式化
function onlineStatusFormatter(value, row, index) {
    var result = "";
    var heartbeatFlg = isHeartbeat(row);
    var workStatus = row['status'];

    if (workStatus == CONST.WORKER_STATUS.ONLINE && heartbeatFlg) {  //上线中
        result = "<i class='glyphicon glyphicon-ok text-success'>上线中</i>";
    } else if (workStatus == CONST.WORKER_STATUS.OFFLINE || heartbeatFlg == false) { //下线
        result = "<i class='glyphicon glyphicon-remove text-danger'>已下线</i>";
    }
    return result;
}

//任务数——格式化
function taskNumFormatter(value, row, index) {
    var ip = row["ip"];
    var port = row["port"];
    var key = ip + ":" + port;
    var info = workerPage.heartbeatInfo[key];
    if(info !=null){
        return info.taskNum;
    }
    return undefined;
}

function isHeartbeat(row){
    var ip = row["ip"];
    var port = row["port"];
    var key = ip + ":" + port;
    var info = workerPage.heartbeatInfo[key];
    return info != null;
}

//心跳状态——格式化
function heartbeatStatusFormatter(value, row, index) {
    return isHeartbeat(row) ? "有心跳" : undefined;
}

//可用状态——格式化
function workerStatusFormatter(value, row, index) {
    var result = "";
    if (value == CONST.WORKER_STATUS.ONLINE) {
        result = "启用";
    } else if (value == CONST.WORKER_STATUS.OFFLINE) {
        result = "停用";
    }

    return result;
}


//worker操作状态
function operateWorkerFormatter(value, row, index) {

    var operateFlag = row["status"];
    if (operateFlag != CONST.WORKER_STATUS.OFFLINE && operateFlag != CONST.WORKER_STATUS.ONLINE) {
        return "";
    }
    var operation = "";
    var id = row["id"];
    var ip = row["ip"];
    var port = row["port"];
    var style, status, desc;
    if (operateFlag == CONST.WORKER_STATUS.OFFLINE) {
        style = "btn btn-xs btn-success";
        status = CONST.WORKER_STATUS.ONLINE;
        desc = "启用";
    } else if (operateFlag == CONST.WORKER_STATUS.ONLINE) {
        style = "btn btn-xs btn-danger";
        status = CONST.WORKER_STATUS.OFFLINE;
        desc = "停用";
    }
    var item = ' <a class="' + style + '" href="javascript:void(0)" onclick="modifyWorkerStatus('
        + id + ',' + status + ',\'' + ip + '\',' + port + ')" >' + desc + '</a>';
    operation = operation + item;
    return operation;
}

//获取查询参数
function getWorkerQueryPara() {
    var queryPara = {};

    var workerGroupId = $("#workerGroupId").val();
    var ip = $("#ip").val();
    var port = $("#port").val();
    var workerStatus = $("#workerStatus").val();

    workerGroupId = workerGroupId == 'all' ? '' : workerGroupId;
    ip = ip == 'all' ? '' : ip;
    port = port == 'all' ? '' : port;
    workerStatus = workerStatus == 'all' ? '' : workerStatus;

    queryPara["workerGroupId"] = workerGroupId;
    queryPara["ip"] = ip;
    queryPara["port"] = port;
    queryPara["workerStatus"] = workerStatus;

    return queryPara;
}

//初始化数据及分页
function initWorkerData() {
    var queryParams = getWorkerQueryPara();
    $("#workerContent").bootstrapTable({
        columns: workerColumns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/worker/getWorkers',
        queryParams: function (params) {
            for (var key in queryParams) {
                var value = queryParams[key];
                if(key == 'workerStatus'){
                    key = 'status';
                }
                params[key] = value;
            }
            return params;
        },
        responseHandler:function(res){
            if(res.status){
                showMsg("error","初始化Worker列表",res.status.msg);
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

function searchWorker() {
    $("#workerContent").bootstrapTable('destroy', '');
    getHeartbeatInfo();
    initWorkerData();
}

function resetWorker() {
    $("#workerGroupId").val("all").trigger("change");
    $("#ip").val("all").trigger("change");
    $("#port").val("all").trigger("change");
    $("#workerStatus").val("all").trigger("change");
}

//修改worker
function modifyWorkerStatus(workerId, status, ip, port) {
    var data = {workerId: workerId, status: status, ip: ip, port: port};
    requestRemoteRestApi("/api/worker/status/set", "修改Worker Group状态", data,false,false);
    $("#workerContent").bootstrapTable("destroy");
    initWorkerData();

}


//---------------------  worker Group代码 ----------------
var workerGroupColumns = [{
    field: 'id',
    title: 'Worker Group id',
    switchable: true,
    visible: false
}, {
    field: 'name',
    title: '名称',
    switchable: true
}, {
    field: 'authKey',
    title: 'authKey',
    switchable: true
}, {
    field: 'status',
    title: '状态',
    switchable: true,
    formatter: workerGroupStatusFormatter
}, {
    field: 'createTime',
    title: '创建时间',
    switchable: true,
    formatter: formatDateTime,
    visible: false
}, {
    field: 'updateTime',
    title: '更新时间',
    switchable: true,
    formatter: formatDateTime,
    visible: false
}, {
    field: 'updateUser',
    title: '更新人',
    switchable: true,
    visible: false
}, {
    field: 'operation',
    title: '操作',
    switchable: true,
    formatter: operateWorkerGroupFormatter
}];

//初始化数据及分页
function initWorkerGroupData() {
    var queryParams = getWorkerGroupQueryPara();
    $("#workerGroupContent").bootstrapTable({
        columns: workerGroupColumns,
        pagination: true,
        sidePagination: 'server',
        search: false,
        url: contextPath + '/api/workerGroup/getWorkerGroups',
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

//获取查询参数
function getWorkerGroupQueryPara() {
    var queryPara = {};

    var name = $("#name").val();

    name = name == 'all' ? '' : name;

    queryPara["name"] = name;
    return queryPara;
}

//worker group操作状态
function operateWorkerGroupFormatter(value, row, index) {
    //console.log(row);
    var id = row["id"];
    var operateFlag = row["status"];
    var authKey = row["authKey"];
    //console.log(jobId);
    var result = [
        '<a class="edit" href="' + contextPath + '/manage/workerGroupAddOrEdit?id=' + id + '" title="编辑WorkerGroup信息" target="_blank">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>  '

    ].join('');

    var operation = '';

    var workerGroupStatus = [{id: 1, text: "启用"}, {id: 2, text: "禁用"}, {id: 3, text: "删除"}];
    $(workerGroupStatus).each(function (i, c) {
        if (c["id"] != 'all' && c["id"] != operateFlag) {
            var style = "";
            //禁用
            if (2 == c["id"]) {
                style = "btn btn-xs btn-danger";
            }
            //启用
            else if (1 == c["id"]) {
                style = "btn btn-xs btn-success";
            }

            var item = ' <a class="' + style + '" href="javascript:void(0)" onclick="modifyWorkerGroupStatus(' + id + ',\'' + authKey + '\',' + c["id"] + ')" >' + c["text"] + '</a>';
            operation = operation + item;
        }
    });

    //console.log(result);

    return result + operation;
}

function workerGroupStatusFormatter(value, row, index) {
    var result = "";
    //禁用
    if (value == CONST.WORKER_GROUP_STATUS.DISABLE) {
        result = "<i class='glyphicon glyphicon-remove text-danger'></i>";
    }
    //启用
    else if (value == CONST.WORKER_GROUP_STATUS.ENABLE) {
        result = "<i class='glyphicon glyphicon-ok text-success'></i>";
    }
    else {
        result = "<i class='glyphicon glyphicon-question-sign text-info'></i>";
    }

    return result;
}

function searchWorkerGroup() {
    $("#workerGroupContent").bootstrapTable('destroy', '');
    initWorkerGroupData();
}

function resetWorkerGroup() {
    $("#name").val("all").trigger("change");
    $("#creator").val("all").trigger("change");
}

//修改worker group状态
function modifyWorkerGroupStatus(workerGroupId, authKey, status) {
    var data = {workerGroupId: workerGroupId, status: status};
    requestRemoteRestApi("/api/workerGroup/status/set", "修改Worker Group状态", data,false,true);
    $("#workerGroupContent").bootstrapTable("destroy");
    initWorkerGroupData();
}

