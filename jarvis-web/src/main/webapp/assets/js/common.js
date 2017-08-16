function getChineseName(uname) {
    if (uname == null || uname == '') {
        return "请传入花名拼音";
    }

    return usersJson[uname];
}

function getMsg4ajaxError(jqXHR, exception){
        var msg = '';
        if (jqXHR.status === 0) {
            msg = '网络未连接.';
        } else if (jqXHR.status == 404) {
            msg = '地址不存在 [404]';
        } else if (jqXHR.status == 500) {
            msg = '服务器内部错误 [500].';
        } else if (exception === 'parsererror') {
            msg = '返回的json串解析失败.';
        } else if (exception === 'timeout') {
            msg = '请求超时. [timeout]';
        } else if (exception === 'abort') {
            msg = 'Ajax请求人工取消.';
        } else {
            msg = '未知的错误.\n' + jqXHR.responseText;
        }
    return msg;
}

