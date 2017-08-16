function changeUrl(thisTag, key) {
    var aTags = $("#pager a");
    var pageSize = $(thisTag).val();
    var tmpUrl = "";
    $(aTags).each(function (index, content) {
        var url = $(content).attr("href");

        if (url != null && url != '' && url != 'javascript:void(0);') {
            var sourceUrl = getUrl(url);
            var paraJson = getJsonPara(url);
            paraJson[key] = pageSize;
            var paraStr = jsonToPara(paraJson);

            var resultUrl = sourceUrl + paraStr;
            $(content).attr("href", resultUrl);
            console.log(resultUrl);
            if (tmpUrl == '') {
                tmpUrl = resultUrl;
            }
        }
    });


    var refreshUrl = getUrl(tmpUrl);
    var paraJson = getJsonPara(tmpUrl);
    paraJson["pageNo"] = 1;
    var paraStr = jsonToPara(paraJson);

    var refreshUrl = refreshUrl + paraStr;
    window.location.href = refreshUrl;
}

function getUrl(url) {
    var hasPara = url.indexOf("?");
    if (hasPara > 0) {
        var arr = url.split("?");
        return arr[0];
    }
    else {
        return url;
    }
}

function getJsonPara(url) {
    var result = {};
    var hasPara = url.indexOf("?");
    if (hasPara >= 0) {
        var arr = url.split("?");
        var paraString = arr[1];
        var paraArr = paraString.split("&");
        for (var i = 0; i < paraArr.length; i++) {
            var para = paraArr[i];
            var kvArr = para.split("=");
            var key = kvArr[0];
            var value = kvArr[1];
            result[key] = value;
        }
    }
    return result;
}

function jsonToPara(para) {
    var result = "";
    for (var key in para) {
        if (para[key] != null && para[key] != '') {
            if (result == '') {
                result = result + key + "=" + para[key];
            }
            else {
                result = result + "&" + key + "=" + para[key];
            }
        }
    }
    if (result.length > 0) {
        result = "?" + result;
    }

    return result;
}