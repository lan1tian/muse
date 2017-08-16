$(function () {
    $('#startTime').datetimepicker({
        language: 'zh-CN',
        minView: 'hour',
        format: 'yyyy-mm-dd hh:ii',
        autoclose: true,
        todayBtn:true,
        todayHighlight:true
    });

    $('#endTime').datetimepicker({
        language: 'zh-CN',
        minView: 'hour',
        format: 'yyyy-mm-dd hh:ii',
        autoclose: true,
        todayBtn:true,
        todayHighlight:true
    });
    initValidDate();

    //select采用select2 实现
    $("#originJobId").select2({
        ajax: {
            url: contextPath + "/api/job/getJobBySimilarNames",
            dataType: 'json',
            delay: 1000,
            data: function (params) {
                return {
                    q: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, page) {
                if (data.status) {
                    showMsg('error', '模糊查询任务名', data.status.msg);
                    return {
                        results: []
                    };
                }
                else {
                    return {
                        results: data.items
                    };
                }
            },
            cache: true
        },
        escapeMarkup: function (markup) {
            return markup;
        },
        minimumInputLength: 1,
        templateResult: formatResult,
        templateSelection: formatResultSelection,
        width: '100%'
    });


    $("#originJobId").on("change", function (e) {
        $("#reRunJobs div[name=children]").jstree('destroy');
        $("#reRunJobs").empty();
        var jobIds = $(e.target).val();
        if (jobIds != null && jobIds != '') {
            buildTree(jobIds);
        }
    });
});

//初始化开始日期
function initValidDate() {
    var today = moment((new Date()).getTime()-24*3600*1000).format("YYYY-MM-DD");
    $("#startTime").val(today+" 00:00");
}

//创建子job的关系树
function buildTree(jobIds) {
    $(jobIds).each(function (i, c) {
        var jobId = c;
        var id = "jobId" + jobId;
        var childrenTree = $('<div name="children" id="' + id + '"></div><hr/>');
        $("#reRunJobs").append(childrenTree);


        $("#" + id).jstree({
            'core': {
                data: {
                    'url': function (node) {
                        return node.id === '#' ?
                        contextPath + '/api/job/getRoot' : contextPath + '/api/job/getDirectChildren';
                    },
                    'data': function (node) {
                        if(node.id === '#'){
                            return {'jobId': jobId};
                        }
                        else{
                            return {'jobId': node.li_attr.jobId};
                        }

                    }
                }
            },
            "#" : {
                "valid_children" : ["root"]
            },
            "types": {
                "default": {"icon": "fa fa-users icon-green", "valid_children": []}
            },
            plugins: [
                'checkbox', 'types', 'unique'
            ]
        });

        $("#" + id).bind('check_node.jstree', function (e, data) {
            data.inst.open_all(data.rslt.obj, true);
        });
        $("#" + id).bind('uncheck_node.jstree', function (e, data) {
            data.inst.close_all(data.rslt.obj, true);
        })

    });
}


//重置所有数据
function reset() {
    $("#originJobId").val({}).trigger("change");
    $("#startTime").val('');
    $("#endTime").val('');
    $("#reRunJobs").removeAttr("checked");
}

//提交重跑job
function submit() {
    var originJobId = $("#originJobId").val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if (originJobId == null || originJobId == '') {
        new PNotify({
            title: '重跑任务',
            text: "必须选择Job",
            type: 'warning',
            icon: true,
            styling: 'bootstrap3'
        });
        return;
    }

    if ('' == startTime || '' == endTime) {
        new PNotify({
            title: '重跑任务',
            text: "开始日期与结束日期必须填写",
            type: 'warning',
            icon: true,
            styling: 'bootstrap3'
        });
        return;
    }

    if (startTime != '' && endTime != '' && ((new Date(startTime)) > (new Date(endTime)))) {
        new PNotify({
            title: '重跑任务',
            text: "开始日期必须小于结束日期",
            type: 'warning',
            icon: true,
            styling: 'bootstrap3'
        });
        return;
    }


    var jobIdCache = {};
    var reRunJobs = new Array();
    $(originJobId).each(function (i, c) {
        reRunJobs.push(parseInt(c));
        jobIdCache[c] = c;
    });

    var divTrees = $("#reRunJobs>div");
    $(divTrees).each(function (i, c) {
        var aIds = $(c).jstree().get_checked();
        var modelData = $(c).jstree()._model.data;
        $(aIds).each(function (i, c) {
            var jobId = modelData[c].li_attr.jobId;
            if (jobIdCache[jobId] == null) {
                reRunJobs.push(parseInt(jobId));
                jobIdCache[jobId] = jobId;
            }
        });
    });

    var startDate = (new Date(startTime)).getTime();
    var endDate = (new Date(endTime)).getTime();
    var data = {startDate: startDate, endDate: endDate, jobIdList: reRunJobs};
    requestRemoteRestApi("/api/task/rerun", "重跑任务", data, true,true);
}

//格式化结果
function formatResult(result) {
    return result.text;
}
//格式化选择框
function formatResultSelection(result) {
    return result.text;
}
