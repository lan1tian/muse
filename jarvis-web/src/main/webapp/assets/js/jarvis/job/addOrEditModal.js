//------------------------------ 1. 选择脚本  --------------------------------

$(function () {

    initSearchScriptModal();        //初始化搜索Script模式框
    initJobScheduleModal();         //初始化任务计划模式框

    //--针对多个modal显示时,第二个modal会被第一个modal覆盖住.(文件上传modal,java任务参数modal)
    $('.modal').on('show.bs.modal', function (event) {
        var idx = $('.modal:visible').length;
        $(this).css('z-index', 1040 + (10 * idx));
    });
    $('.modal').on('shown.bs.modal', function (event) {
        var idx = ($('.modal:visible').length) - 1; // raise backdrop after animation.
        $('.modal-backdrop').not('.stacked').css('z-index', 1039 + (10 * idx));
        $('.modal-backdrop').not('.stacked').addClass('stacked');
    });
    //--end

});

//初始化——选择脚本-模态框
function initSearchScriptModal() {

    $('#searchScriptList').btsListFilter('#searchScriptInput', {
        resetOnBlur: false,
        minLength: 0,
        sourceTmpl: '<a href="javascript:void(0);" data-id="{id}" data-title="{title}" class="list-group-item">{title} &nbsp|&nbsp{creator}</a>',
        sourceData: function (text, callback) {
            return $.getJSON(contextPath + "/api/script/queryScript?name=" + text, function (json) {
                callback(json.data);
            });
        }
    });

    $("#searchScriptList").on("dblclick", "a", function () {
        $.ajax({
            url: contextPath + "/api/script/getScriptById?id=" + $(this).attr("data-id"),
            async: false,
            success: function (result) {
                if (result.code == 1000 && result.data != null) {
                    var script = result.data;
                    $("#scriptId").val(script.id);
                    $("#scriptTitle").val(script.title);
                    $("#jobContent").val(script.content);
                    $("#searchScriptModal").modal("hide");
                } else {
                    alert(result.msg);
                }
            },
            error: function (jqXHR, exception) {
                var msg = getMsg4ajaxError(jqXHR, exception);
                showMsg('warning', '初始化脚本', msg);
            }
        })
    });

}

//显示-选择脚本-模态框
function showSearchScriptModal() {
    $("#searchScriptInput").val("");
    $("#searchScriptList").empty();
    $("#searchScriptModal").modal("show");
}


//------------------------------ 2 上传jar文件  ------------------------------


//显示-选择脚本-模态框
function showUploadJarModal(target) {
    $("#localFile").val("").attr("data-target", target);
    $("#uploadJarModal").modal("show");
}

function confirmUploadJar() {
    var formData = new FormData();  //必须用new FromData()格式,不能直接{}定义.
    formData.append('file', $('#localFile')[0].files[0]);
    $.ajax({
        url: contextPath + '/api/file/uploadJar',
        type: 'POST',
        async: false,
        data: formData,
        processData: false,  // tell jQuery not to process the data
        contentType: false,  // tell jQuery not to set contentType
        success: function (json) {
            if (json.code == CONST.MSG_CODE.SUCCESS) {
                var uploadUrl = json.data;
                var target = $('#localFile').attr("data-target");
                var targetCtl = $("#" + target);
                if (target == 'javaClasspath') {  //classpath是以,分开的多重文件
                    var oldValue = $(targetCtl).val();
                    if (oldValue.indexOf(uploadUrl) < 0) {
                        var separator = oldValue.length > 0 ? ',' : '';
                        $(targetCtl).val(oldValue + separator + uploadUrl);
                    }
                } else {
                    $(targetCtl).val(uploadUrl)
                }
                $('#uploadJarModal').modal('hide');
            } else {
                showMsg('warning', '上传jia包', json.msg);
            }
        },
        error: function (jqXHR, exception) {
            var msg = getMsg4ajaxError(jqXHR, exception);
            showMsg('warning', '上传jar包', msg);
        }
    });
}

//------------------------------ 2 任务参数  ------------------------------

//------------------------------ 2.1 常规任务参数  ------------------------------
//显示-任务参数-模态框
function showParaModal() {
    var jobType = $("#jobType").val();
    if (jobType == CONST.JOB_TYPE.SPARK_LAUNCHER) {
        showSparkLauncherParasModal();
    } else if (jobType == CONST.JOB_TYPE.JAVA || jobType == CONST.JOB_TYPE.MAPREDUCE) {
        showJavaParasModal();
    } else {
        showCommonJobParaModal()
    }
}

//显示-任务参数-模态框
function showCommonJobParaModal() {
    var trBody = $("#parasTable tbody");
    var trPattern = $("#pattern tr");
    $(trBody).empty();
    var params = $("#params").val();
    if (params != null && params != '' && params.indexOf("}") > 0) {
        var existParas = JSON.parse(params);
        for (var key in existParas) {
            var value = existParas[key];
            var tr = $(trPattern).clone();
            $(tr).find("input[name=key]").first().val(key);
            $(tr).find("input[name=value]").first().val(value);
            $(trBody).append(tr);
        }
    }
    $("#paraModal").modal("show");
}

//添加参数
function addPara(thisTag) {
    var tr = $("#pattern tr").clone();
    if (thisTag == null) {
        $("#parasTable tbody").append(tr);
    }
    else {
        $(thisTag).parent().parent().after(tr);
    }
}
//删除参数
function deletePara(thisTag) {
    $(thisTag).parent().parent().remove();
}

///确认参数选择
function confirmPara() {
    var paras = {}, val, key;
    $("#parasTable tbody tr").each(function (i, c) {
        key = $(c).find("input[name=key]").first().val().trim();
        val = $(c).find("input[name=value]").first().val().trim();
        if (key == null || key == '') {
            return;
        }
        paras[key] = val;
    });
    var parasStr = JSON.stringify(paras);
    if (!validPara(parasStr)) {
        return;
    }
    $("#params").val(parasStr);
    $("#paraModal").modal("hide");
}

function validPara(parasStr) {
    var paras = JSON.parse(parasStr);
    var val, flg = true;
    for (var key in paras) {
        val = paras[key];
        if (testChinese.test(key)) {
            showMsg('warning', '任务参数', "key[" + key + "]不能为中文.");
            flg = false;
        }
    }
    return flg;
}

//------------------------------ 2.2 sparkLauncher任务参数  ------------------------------

//显示-SparkLauncher任务参数-模态框
function showSparkLauncherParasModal() {
    var existParas = {};
    var params = $("#params").val();
    if (params != null && params != '' && params.indexOf("}") > 0) {
        existParas = JSON.parse(params);
    }

    $("#sparkLauncherParasModalBody input, #sparkLauncherParasModalBody textarea").each(function (i, c) {
        var key = $(this).attr("name");
        if (key in existParas) {
            $(this).val(existParas[key]);
        } else {
            $(this).val("");
        }
    });

    $("#sparkLauncherParasModal").modal("show");
}

///确认参数选择
function confirmSparkLauncherParas() {

    var paras = {}, val, key;
    $("#sparkLauncherParasModalBody input, #sparkLauncherParasModalBody textarea").each(function (i, c) {
        key = $(this).attr("name");
        val = $(this).val().trim();
        if (val == null || val == "") {
            return;
        }
        paras[key] = val;

    });

    var parasStr = JSON.stringify(paras);
    if (!validSparkLauncherParas(parasStr)) {
        return;
    }
    $("#params").val(parasStr);
    $("#jobContent").val("sparkLauncher.sh " + parasStr);
    $("#sparkLauncherParasModal").modal("hide");
}


function validSparkLauncherParas(parasStr) {

    var paras = JSON.parse(parasStr);

    var val, flg = true;
    for (var key in CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY) {
        val = paras[key];
        switch (key) {
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.mainClass:    //mainClass
                if (val == null || val == "") {
                    showMsg('warning', '任务参数', "'mainClass'不能为空");
                    flg = false;
                }
                break;
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.taskJar:    //taskJar
                if (val == null || val == "") {
                    showMsg('warning', '任务参数', "'jar文件'不能为空");
                    flg = false;
                }
                break;
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.driverCores:    //driver核数
                if (val == null || val == "") break;
                if (!$.isNumeric(val) || val < 1 || val > 4) {
                    showMsg('warning', '任务参数', "'driver核数'不对,请输入1-4之间数字.");
                    flg = false;
                }
                break;
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.driverMemory :  //driver内存
                if (val == null || val == "") break;
                if (!/^\d?g$/i.test(val)) {
                    showMsg('warning', '任务参数', "'driver内存'不对,请输入'数字+G',比如'4G'.");
                    flg = false;
                }
                break;
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.executorCores:  //executor核数
                if (val == null || val == "") break;
                if (!$.isNumeric(val) || val < 1 || val > 4) {
                    showMsg('warning', '任务参数', "'executor核数'不对,请输入1-4之间数字.");
                    flg = false;
                }
                break;
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.executorMemory :    //executor内存
                if (val == null || val == "") break;
                if (!/^\d?g$/i.test(val)) {
                    showMsg('warning', '任务参数', "'executor内存'不对,请输入'数字+G',比如'4G'.")
                    flg = false;
                }
                break;
            case CONST.SPARK_LAUNCHER_JOB.PARAMS_KEY.executorNum:    //executor数目
                if (val == null || val == "") break;
                if (!$.isNumeric(val) || val < 0) {
                    showMsg('warning', '任务参数', "'executor数目'不对,请大于0的数字.")
                    flg = false;
                }
                break;
        }
    }
    return flg;
}


//------------------------------ 2.3 java任务参数  ------------------------------
//显示-java任务参数-模态框
function showJavaParasModal() {
    var existParas = {};
    var params = $("#params").val();
    if (params != null && params != '' && params.indexOf("}") > 0) {
        existParas = JSON.parse(params);
    }

    $("#javaParasModalBody input, #javaParasModalBody textarea").each(function (i, c) {
        var key = $(this).attr("name");
        if (key in existParas) {
            $(this).val(existParas[key]);
        } else {
            $(this).val("");
        }
    });

    $("#javaParasModal").modal("show");
}

///确认参数选择
function confirmJavaParas() {
    var paras = {}, val, key;
    $("#javaParasModalBody input, #javaParasModalBody textarea").each(function (i, c) {
        key = $(this).attr("name");
        val = $(this).val().trim();
        if (val == null || val == "") {
            return;
        }
        paras[key] = val;
    });
    var paramsStr = JSON.stringify(paras);
    if (!validJavaParas(paramsStr)) {
        return;
    }
    $("#params").val(paramsStr);

    var content, jobType = $('#jobType').val();
    if (jobType == CONST.JOB_TYPE.JAVA) {
        content = "java -cp ";
    } else if (jobType == CONST.JOB_TYPE.MAPREDUCE) {
        content = "yarn jar ";
    }

    content = content + $("#javaJar").val() + " " + $("#javaClasspath").val() + " "
        + $("#javaMainClass").val() + " " + $("#javaArguments").val();

    $("#jobContent").val(content);
    $("#javaParasModal").modal("hide");
}

function validJavaParas(parasStr) {
    var paras = JSON.parse(parasStr);
    var val, flg = true;
    for (var key in CONST.JAVA_JOB.PARAMS_KEY) {
        val = paras[key];
        switch (key) {
            case CONST.JAVA_JOB.PARAMS_KEY.mainClass:    //mainClass
                if (val == null || val == "") {
                    showMsg('warning', '任务参数', "'mainClass'不能为空");
                    flg = false;
                }
                break;
            case CONST.JAVA_JOB.PARAMS_KEY.jar:    //jar
                if (val == null || val == "") {
                    showMsg('warning', '任务参数', "'jar文件'不能为空");
                    flg = false;
                }
                break;
        }
    }
    return flg;
}


//------------------------------ 3 调度时间  ------------------------------
$.fn.restrict = function (chars) {
    return this.keypress(function (e) {
        var found = false, i = -1;
        while (chars[++i] != null && !found) {
            found = chars[i] == String.fromCharCode(e.keyCode).toLowerCase() ||
                chars[i] == e.which;
        }
        found || e.preventDefault();
    });
};

function initJobScheduleModal() {
    initPerMonthSelect();
    initPerDaySelect();
    initPerWeekSelect();

    $("#perHour,#perMinute,#perSecond").change(function () {
        var max = parseInt($(this).attr('max'));
        var min = parseInt($(this).attr('min'));
        if ($(this).val() > max) {
            $(this).val(max);
        }
        else if ($(this).val() < min) {
            $(this).val(min);
        }
    });

    $("input[name='circleType']").change(function (e) {
        changeCircleType($(this));
    });

    $('#cronSecond').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/']);
    $('#cronMinute').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/']);
    $('#cronHour').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/']);
    $('#cronDay').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/', '?']);
    $('#cronMonth').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/']);
    $('#cronWeekDay').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/', '?']);
    $('#cronYear').restrict(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ',', '*', '/', '?']);

}

//初始化-perDay
function initPerMonthSelect() {
    var newData = [];
    for (var i = 1; i <= 12; i++) {
        newData.push({id: i, text: i + "月"});
    }
    var selector = $("#perMonth");
    $(selector).select2({
        data: newData,
        width: '100%'
    });
}

//初始化-perDay
function initPerDaySelect() {
    var newData = [];
    for (var i = 1; i <= 31; i++) {
        newData.push({id: i, text: i + "日"});
    }
    var selector = $("#perDay");
    $(selector).select2({
        data: newData,
        width: '100%'
    });
}

//初始化-perWeek
function initPerWeekSelect() {
    var newData = [{id: 1, text: "星期一"}, {id: 2, text: "星期二"}, {id: 3, text: "星期三"}
        , {id: 4, text: "星期四"}, {id: 5, text: "星期五"}, {id: 6, text: "星期六"}, {id: 7, text: "星期天"}];
    var selector = $("#perWeekDay");
    $(selector).select2({
        data: newData,
        width: '100%'
    });
}

(function ($) {
    $.fn.hourEnable = function (enable) {
        if (!$(this).hasClass("disabled") == enable) {    //判断状态是否改变
            return;
        }
        if (enable) {
            $(this).removeAttr("disabled").removeClass("disabled").val(pageJobEdit.curPerHour);
        } else {
            pageJobEdit.curPerHour = $(this).val();
            $(this).attr("disabled", true).addClass("disabled").val("");
        }
    };
})(jQuery);

//改变-计划类型
function changeCircleType(curRadio) {

    var curValue = $(curRadio).val();
    if (curValue == CONST.SCHEDULE_CIRCLE_TYPE.PER_DAY) {    //每天
        $("#perHour").hourEnable(true);
        $("#perMonthDiv").hide();
        $("#perDayDiv").hide();
        $("#perWeekDiv").hide();
    } else if (curValue == CONST.SCHEDULE_CIRCLE_TYPE.PER_HOUR) {  //每小时
        $("#perHour").hourEnable(false);
        $("#perMonthDiv").hide();
        $("#perDayDiv").hide();
        $("#perWeekDiv").hide();
    } else if (curValue == CONST.SCHEDULE_CIRCLE_TYPE.PER_WEEK) {  //每周
        $("#perHour").hourEnable(true);
        $("#perMonthDiv").hide();
        $("#perDayDiv").hide();
        $("#perWeekDiv").show();
    } else if (curValue == CONST.SCHEDULE_CIRCLE_TYPE.PER_MONTH) {  //每月
        $("#perHour").hourEnable(true);
        $("#perMonthDiv").hide();
        $("#perDayDiv").show();
        $("#perWeekDiv").hide();
    } else if (curValue == CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR) {  //每年
        $("#perHour").hourEnable(true);
        $("#perMonthDiv").show();
        $("#perDayDiv").show();
        $("#perWeekDiv").hide();
    }
}

//显示-任务计划-模态框
function showJobScheduleModal() {

    var expType = tryToNum($("#expType").val());
    if (expType == null || expType == "") {
        expType = CONST.SCHEDULE_EXP_TYPE.CRON;
    }

    var expContent = $('#expContent').val();
    switch (expType) {
        case CONST.SCHEDULE_EXP_TYPE.CRON  :
            var circleType = getCircleType(expContent);
            if (circleType != CONST.SCHEDULE_CIRCLE_TYPE.NONE) {
                $('.nav-tabs a[href="#circleTab"]').tab('show');
                loadCircleData(expContent, circleType);
            } else {
                $('.nav-tabs a[href="#cronTab"]').tab('show');
                loadCronData(expContent);
            }
            break;
        //case CONST.SCHEDULE_EXP_TYPE.FIXED_DELAY  :
        //    $('#fixedDelayTab').addClass("active");
        //    break;
        //case CONST.SCHEDULE_EXP_TYPE.FIXED_RATE  :
        //    $('#fixedRateTab').addClass("active");
        //    break;
        //case CONST.SCHEDULE_EXP_TYPE.ISO8601  :
        //    $('#iso8601Tab').addClass("active");
        //    break;
    }
    $("#jobScheduleModal").modal("show");
}

function loadCircleData(expContent, cirCleType) {
    var radio;
    if (cirCleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_DAY) {
        radio = $("#circleDay");
    } else if (cirCleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_HOUR) {
        radio = $("#circleHour");
    } else if (cirCleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_MONTH) {
        radio = $("#circleMonth")
    } else if (cirCleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_WEEK) {
        radio = $("#circleWeek")
    } else if (cirCleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR) {
        radio = $("#circleYear")
    } else {
        return;
    }

    radio.prop('checked', true).trigger("change");

    var fields = expContent.replace(/(\s)+/g, ' ').split(' ');
    var second = (fields[0] == undefined ? '' : fields[0]),        // seconds
        minute = (fields[1] == undefined ? '' : fields[1]),        // minutes
        hour = (fields[2] == undefined ? '' : fields[2]),          // hours
        day = (fields[3] == undefined ? '' : fields[3]),           // day of month
        month = (fields[4] == undefined ? '' : fields[4]),         // month
        weekDay = (fields[5] == undefined ? '' : fields[5]),       // day of week
        year = (fields[6] == undefined ? '' : fields[6]);          // year

    $('#perSecond').val(second);
    $('#perMinute').val(minute);
    if (cirCleType != CONST.CONTENT_TYPE.PER_HOUR) {
        $('#perHour').val(hour);
    }
    if (isNumberStr(day)) {
        $("#perDay").val(day.split(",")).trigger("change");
    }
    if (isNumberStr(month)) {
        $("#perMonth").val(month.split(",")).trigger("change");
    }
    if (isNumberStr(weekDay)) {
        $("#perWeekDay").val(weekDay.split(",")).trigger("change");
    }
}

function loadCronData(expContent) {
    var fields = expContent.replace(/(\s)+/g, ' ').split(' ');
    var second = (fields[0] == undefined ? '' : fields[0]),        // seconds
        minute = (fields[1] == undefined ? '' : fields[1]),        // minutes
        hour = (fields[2] == undefined ? '' : fields[2]),          // hours
        day = (fields[3] == undefined ? '' : fields[3]),           // day of month
        month = (fields[4] == undefined ? '' : fields[4]),         // month
        weekDay = (fields[5] == undefined ? '' : fields[5]),       // day of week
        year = (fields[6] == undefined ? '' : fields[6]);          // year

    $('#cronSecond').val(second);
    $('#cronMinute').val(minute);
    $('#cronHour').val(hour);
    $('#cronDay').val(day);
    $('#cronMonth').val(month);
    $('#cronWeekDay').val(weekDay);
    $('#cronYear').val(year);
}


//高级参数-显示或隐藏
function toggleCronHelpDiv(thisTag) {
    var oldState = $(thisTag).find("i").attr("data-state");
    var newState = oldState == "up" ? "down" : "up";
    $(thisTag).find("i").removeClass("glyphicon-chevron-" + oldState)
        .addClass("glyphicon-chevron-" + newState)
        .attr("data-state", newState);
    $("#cronHelpDiv").toggle();
}

//检查-简单设定
function validCircleTab() {
    var val, flg, expContent = "";
    var circleType = $("input[name='circleType']:checked").val();

    if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_WEEK) {
        val = $('#perWeekDay').val();
        if (val == null) {
            showMsg('warning', '调度时间', "'星期'不能为空");
            flg = false;
        } else {
            expContent = val.join(',');
        }
    } else {
        expContent = '?';
    }

    if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR) {
        val = $('#perMonth').val();
        if (val == null) {
            showMsg('warning', '调度时间', "'月'不能为空");
            flg = false;
        } else {
            expContent = val.join(',') + ' ' + expContent;
        }
    } else {
        expContent = '*' + ' ' + expContent;
    }

    if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR || circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_MONTH) {
        val = $('#perDay').val();
        if (val == null) {
            showMsg('warning', '调度时间', "'日'不能为空");
            flg = false;
        } else {
            expContent = val.join(',') + ' ' + expContent;
        }
    } else if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_WEEK) {
        expContent = '?' + ' ' + expContent;
    } else {
        expContent = '*' + ' ' + expContent;
    }

    if (circleType != CONST.SCHEDULE_CIRCLE_TYPE.PER_HOUR) {
        val = $('#perHour').val();
        if (val == null || val == "") {
            showMsg('warning', '调度时间', "'小时'不能为空");
            flg = false;
        } else if (!$.isNumeric(val) || val < 0 || val > 23) {
            showMsg('warning', '调度时间', "'小时'不对,请输入0-23之间数字.");
            flg = false;
        }
        expContent = val + ' ' + expContent;
    } else {
        expContent = '*' + ' ' + expContent;
    }

    val = $('#perMinute').val();
    if (val == null || val == "") {
        showMsg('warning', '调度时间', "'分钟'不能为空");
        flg = false;
    } else if (!$.isNumeric(val) || val < 0 || val > 59) {
        showMsg('warning', '调度时间', "'分钟'不对,请输入0-59之间数字.");
        flg = false;
    }
    expContent = val + ' ' + expContent;

    val = $('#perSecond').val();
    if (val == null || val == "") {
        showMsg('warning', '调度时间', "'秒'不能为空");
        flg = false;
    } else if (!$.isNumeric(val) || val < 0 || val > 59) {
        showMsg('warning', '调度时间', "'秒'不对,请输入0-59之间数字.");
        flg = false;
    }
    expContent = val + ' ' + expContent;

    return {
        flg: flg,
        expType: CONST.SCHEDULE_EXP_TYPE.CRON,
        expContent: expContent,
        circleType: circleType
    };

}

//检查高级设定
function validCronTab() {
    var flg, expArray = [];
    $('#cronTable .cronInput').each(function (i, c) {
        var val = $(c).val();
        expArray.push(val);
    });

    //如果year为空,则移除
    if (expArray[6] == null || expArray == "") {
        expArray.a.splice(6, 1);
    }
    var expContent = expArray.join(' ');

    flg = validCronByArray(expArray);
    return {
        flg: flg,
        expType: CONST.SCHEDULE_EXP_TYPE.CRON,
        expContent: expContent,
        circleType: CONST.SCHEDULE_CIRCLE_TYPE.NONE
    };
}

///确认任务计划
function confirmJobSchedule() {

    var result;
    if ($('#circleTab').hasClass('active')) {
        result = validCircleTab();
    } else if ($('#cronTab').hasClass('active')) {
        result = validCronTab();
    }

    if (result.flg == false) {
        return;
    }
    var expDesc = getExpDesc(result.expType, result.expContent);

    $("#expType").val(result.expType);
    $("#expContent").val(result.expContent);
    $("#expDesc").val(expDesc);
    $("#jobScheduleModal").modal("hide");
}

