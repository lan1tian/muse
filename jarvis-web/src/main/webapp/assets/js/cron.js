//获取cron表达式的周期类型
function getCircleType(expContent) {

    if (expContent == null || expContent == '') {
        return CONST.SCHEDULE_CIRCLE_TYPE.PER_DAY;
    }

    var circle = CONST.SCHEDULE_CIRCLE_TYPE.NONE;
    var fields = expContent.replace(/(\s)+/g, ' ').split(' ');
    var second = (fields[0] == undefined ? '' : fields[0]),        // seconds
        minute = (fields[1] == undefined ? '' : fields[1]),        // minutes
        hour = (fields[2] == undefined ? '' : fields[2]),          // hours
        day = (fields[3] == undefined ? '' : fields[3]),           // day of month
        month = (fields[4] == undefined ? '' : fields[4]),         // month
        weekDay = (fields[5] == undefined ? '' : fields[5]),       // day of week
        year = (fields[6] == undefined || fields[6] == '' ? '*' : fields[6]);   // year

    if (month == '?' && weekDay == '*') {   //确保 month='*' ,weekDay='?',而不是相反.
        month = '*';
        weekDay = '?';
    }

    for (; ;) {
        if (!$.isNumeric(second)) break;
        if (!$.isNumeric(minute)) break;

        if (hour != '*' && !$.isNumeric(hour)) {
            break;
        } else if (hour == '*') {
            if (day == '*' && month == '*' && weekDay == '?' && year == '*') {
                circle = CONST.SCHEDULE_CIRCLE_TYPE.PER_HOUR;
                break;
            } else {
                break;
            }
        }

        if (day != '?') {
            if (day == '*') {
                if (month == '*' && weekDay == '?' && year == '*') {
                    circle = CONST.SCHEDULE_CIRCLE_TYPE.PER_DAY;
                    break;
                }
            } else {
                if (!isNumberStr(day)) {
                    break;
                }
            }
        }

        if (month == '*') {
            if (weekDay == '?' && year == '*') {
                circle = CONST.SCHEDULE_CIRCLE_TYPE.PER_MONTH;
                break;
            }
        } else {
            if (!isNumberStr(month)) {
                break;
            }
        }

        if (weekDay != '?') {
            if (isNumberStr(weekDay)) {
                if (month = '*' && year == '*') {
                    circle = CONST.SCHEDULE_CIRCLE_TYPE.PER_WEEK;
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        if (year == '*') {
            circle = CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR;
            break;
        } else {
            break;
        }
    }
    return circle;
}

//是否数字(用逗号隔开的数字)
function isNumberStr(field) {
    var subs = field.split(',');
    var okFlg = true;
    for (var key in subs) {
        if (subs[key] != "" && !$.isNumeric(subs[key])) {
            okFlg = false;
            break;
        }
    }
    return okFlg;
}

//获取表达式的描述
function getExpDesc(expType, expContent, circleType) {

    var expDesc = "";
    if (expType == null || expContent == null || expContent == "") {
        return expDesc;
    }

    if (circleType == undefined) {
        if (expType == CONST.SCHEDULE_EXP_TYPE.CRON) {
            circleType = getCircleType(expContent);
        } else {
            circleType = CONST.SCHEDULE_CIRCLE_TYPE.NONE;
        }
    }

    if (expType == CONST.SCHEDULE_EXP_TYPE.CRON) {

        var fields = expContent.replace(/(\s)+/g, ' ').split(' ');
        var second = (fields[0] == undefined ? '' : fields[0]),        // seconds
            minute = (fields[1] == undefined ? '' : fields[1]),        // minutes
            hour = (fields[2] == undefined ? '' : fields[2]),          // hours
            day = (fields[3] == undefined ? '' : fields[3]),           // day of month
            month = (fields[4] == undefined ? '' : fields[4]),         // month
            weekDay = (fields[5] == undefined ? '' : fields[5]),       // day of week
            year = (fields[6] == undefined ? '' : fields[6]);          // year

        if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.NONE) {
            expDesc = "[" + second + "]秒  [" + minute + "]分  [" + hour + ']时  ['
                + day + ']日  [' + month + ']月  [' + weekDay + ']星期';
            if(year != ''){
                expDesc = expDesc + '  [' + year + ']年'
            }
            return expDesc;
        } else {

            if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR) {
                expDesc = "每年 ";
                expDesc = expDesc + month + "月 ";
            }
            if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_MONTH) {
                expDesc = "每月 ";
            }
            if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_YEAR || circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_MONTH) {
                expDesc = expDesc + day + "日 ";
            }
            if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_DAY) {
                expDesc = "每天 ";
            }
            if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_WEEK) {
                expDesc = "每周 " + convertWeekDay(weekDay);
            }
            if (circleType == CONST.SCHEDULE_CIRCLE_TYPE.PER_HOUR) {
                expDesc = "每小时 ";
            } else {
                expDesc = expDesc + "  " + hour + '点';
            }

            expDesc = expDesc + minute + "分" + second + "秒";

            return expDesc;
        }
    }

    return expDesc;

}


//转换星期
function convertWeekDay(weekDay) {

    if (weekDay == null || weekDay == "") {
        return "";
    }
    return weekDay.replace(/(1)/g, '周一')
        .replace(/(2)/g, '周二')
        .replace(/(3)/g, '周三')
        .replace(/(4)/g, '周四')
        .replace(/(5)/g, '周五')
        .replace(/(6)/g, '周六')
        .replace(/(7)/g, '周日');
}


//校验cron表达式
function validCronByStr(expContent) {
    var fields = expContent.replace(/(\s)+/g, ' ').split(' ');
    return validCronByArray(fields);
}

//校验cron表达式--数组方式
function validCronByArray(expArray) {
    var FIELDS = {
        second: [0, 0, 59, '秒'],        // seconds
        minute: [1, 0, 59, '分'],        // minutes
        hour: [2, 0, 23, '小时'],         // hours
        day: [3, 1, 31, '日'],               // day of month
        month: [4, 1, 12, '月'],             // month
        weekDay: [5, 1, 7, 1, '星期'],       // day of week
        year: [6, 1970, 2099, '年']          // year
    };
    var key, item, val, flg = true;
    for (key in FIELDS) {
        item = FIELDS[key];
        val = expArray[item[0]];
        if (val == null || val.trim() == '') {
            if (key == 'year') {  //年份允许为空
                continue;
            }
            showMsg("warning", "调度时间", item[3] + "不能为空");
            flg = false;
        } else if (!/^[\d\-\*\/,\?#L]+$/i.test(val)) {
            showMsg("warning", "调度时间", item[3] + "输入字符不对,请输入 0-9数字,以及特殊字符'-,*?/#L'");
            flg = false;
        }
    }
    if (expArray[3] != null && expArray[3] != '' && expArray[5] != null && expArray[5] != '') {
        if (expArray[3] != '?' && expArray[5] != '?') {
            showMsg("warning", "调度时间", "'日'与'星期'有一个字段必须为'?'");
            flg = false;
        }
    }

    //if (flg) {
    //    $.ajax({
    //        url: contextPath + '/remote/',
    //        type: 'POST',
    //        async: false,
    //        data: {cronExp: cronExp},
    //        success: function (data) {
    //            if (data.code == 0) {
    //            } else {
    //                flg = false;
    //                showMsg('warning', title, (data.msg == null || data.msg == '') ? '操作失败' : data.msg);
    //            }
    //        },
    //        error: function (jqXHR, exception) {
    //            flg = false;
    //            var msg = getMsg4ajaxError(jqXHR, exception);
    //            showMsg('warning', title, msg);
    //        }
    //    });
    //}

    return flg;

}

