function showMsg(type, title, text) {
    new PNotify({
        title: title,
        text: text,
        type: type,
        icon: true,
        styling: 'bootstrap3'
    });
}

function tryToNum(val) {
    if (val == '') {
        return null;
    }
    if (val != null && /^[0-9]*$/.test(val)) {
        val = parseInt(val);
    }
    return val;
}

//时间选择器
function createDatetimePickerById(tagId) {
    if (tagId == undefined || tagId == '') {
        return;
    }

    $("#" + tagId).datetimepicker({
        language: 'zh-CN',
        minView: 'month',
        format: 'yyyy-mm-dd',
        autoclose: true
    });
}

//通过后台请求远程rest api,根据请求结果返回flag
function requestRemoteRestApi(url, title, data, async, successShowFlag) {
    var flag = true;
    var result = {};
    if (null == async) {
        async = false;
    }
    if (typeof(successShowFlag) === 'undefined') {
        successShowFlag = false;
    }

    $.ajax({
        url: contextPath + '/remote/request',
        type: 'POST',
        async: async,
        data: {url: url, para: JSON.stringify(data)},
        success: function (data) {
            if (data.code == 0) {
                flag = true;
                if (successShowFlag) {
                    showMsg('success', title, (data.msg == null || data.msg == '') ? '操作成功' : data.msg);
                }
            }
            else {
                flag = false;
                showMsg('warning', title, (data.msg == null || data.msg == '') ? '操作失败' : data.msg);
            }
            result["data"] = data;
        },
        error: function (jqXHR, exception) {
            flag = false;
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', title, msg);
        }
    });

    result["flag"] = flag;
    return result;
}


//时间戳转化成日期
function formatDate(date) {
    if (date == undefined || date == '') {
        return '-';
    }
    var theDate = new Date(date);
    var result = moment(theDate).format("YYYY-MM-DD");
    return result;
}
//时间戳转化成日期时间
function formatDateTime(dateTime) {
    if (dateTime == null || dateTime == '') {
        return '-';
    }
    var theDate = new Date(dateTime);
    var result = moment(theDate).format("YYYY-MM-DD HH:mm:ss");
    return result;
}

//时间戳转化成日期时间
function formatDateTimeWithoutYear(dateTime) {
    if (dateTime == null || dateTime == '') {
        return '-';
    }
    var theDate = new Date(dateTime);
    var result = moment(theDate).format("MM-DD HH:mm:ss");

    result = "<div style='white-space:nowrap;'>" + result + "</div>";

    return result;
}


//秒转化成对应的时间长度
function formatTimeInterval(timeInterval) {
    //console.log(timeInterval);
    if (timeInterval == undefined) {
        return "-";
    }
    if (parseInt(timeInterval) == 0) {
        return "0秒";
    }
    var result = "";

    var day = 24 * 3600;
    var hour = 3600;
    var minute = 60;

    var totalDay = parseInt(timeInterval / day);
    var totalHour = parseInt((timeInterval % day) / hour);
    var totalMinute = parseInt(((timeInterval % day) % hour) / minute);
    var totalSecond = (((timeInterval % day) % hour) % minute);

    if (totalDay > 0) {
        result = totalDay + "天";
    }
    if (totalHour > 0) {
        result += totalHour + "小时";
    }
    if (totalMinute > 0) {
        result += totalMinute + "分";
    }
    if (totalSecond > 0) {
        result += totalSecond + "秒";
    }

    return result;
}

//格式化状态
function formatStatus(dataArr, status) {
    if (dataArr == undefined || dataArr == null || status == undefined || status == null) {
        return "";
    }
    var statusStr = "";
    $(dataArr).each(function (i, c) {
        var id = c["id"];
        if (id == status) {
            statusStr = c["text"];
            return false;
        }
    });
    return statusStr;
}

function arrToString(arr) {
    if (null == arr) {
        return "";
    }
    var result = "";
    for (var i, len = arr.length; i < len; i++) {
        if ("" == result) {
            result = arr[i];
        }
        else {
            result += "," + arr[i];
        }
    }
}

function stringToArr(source) {
    if (null == source || "" == source) {
        return [];
    }
    var arr = source.trim().split(",");
    return arr;
}

var glFuncs = {

    jobStatus: null,

    initJobStatus: function (id, async) {
        $.ajax({
            url: contextPath + "/api/job/getJobStatus",
            async: async != null ? async : true,
            success: function (data) {
                glFuncs.jobStatus = data;
                $("#" + id).select2({
                    data: data,
                    width: '100%'
                });
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '初始化任务状态信息', msg);
            }
        })
    },

    jobTypeJson: null,

    initJobType: function (id, async) {

        $.ajax({
            url: contextPath + "/assets/json/jobType.json",
            async: async != null ? async : true,
            success: function (data) {
                glFuncs.jobTypeJson = data;
                $("#" + id).select2({
                    data: data,
                    width: '100%',
                    tags: true
                });
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '初始化任务类型', msg);
            }
        });
    },
    initExecuteUser: function (id) {
        $.ajax({
            url: contextPath + "/api/common/getExecuteUsers",
            success: function (data) {
                var newData = [];
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
                $("#" + id).select2({
                    data: newData,
                    width: '100%'
                });
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '初始化执行用户列表', msg);
            }
        })
    },

    initTaskId: function (id) {
        $("#" + id).select2({
            width: '100%',
            tags: true
        });
    },


    initJobId: function (id) {

        $("#" + id).select2({
            width: '100%',
            tags: true
        });
    },

    initJobName: function (id, tags) {
        if (typeof tags == 'undefined') {
            tags = true;
        }

        $("#" + id).select2({
            ajax: {
                url: contextPath + "/api/job/getSimilarJobNames",
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
            width: '100%',
            tags: tags
        });
    },
    //获取被选中的执行信息
    getIdSelections: function (id) {
        return $.map($("#" + id).bootstrapTable('getSelections'), function (row) {
            return row;
        });
    }

}

//格式化结果
function formatResult(result) {
    return result.text;
}
//格式化选择框
function formatResultSelection(result) {
    return result.id;
}

function removeTr(tag){
    $(tag).closest("tr").remove();
}

function bootstrapTableRemoveRow(tableId,field,value){
    var value=[value];
    $("#"+tableId).bootstrapTable('remove', {field: field, values: value});
}