var testNum = /^[0-9]*$/;

$(function () {
  if (departmentAndBizGroupVoList.length > 0 && undefined != departmentAndBizGroupVoList[0].departmentId) {
    $("#departmentName").val(departmentAndBizGroupVoList[0].name);

    initData(contextPath + "/api/bizGroup/getAllByCondition", "bizGroupNameR");
    initData(contextPath + "/api/department/getAllNames", "departmentNameR");

    // set detail page value
    var bizGroupData=[];
    for(var i=0; i<departmentAndBizGroupVoList.length; i++) {
      bizGroupData.push(departmentAndBizGroupVoList[i].bizGroupId);
    }
    $("#bizGroupNameR").val(bizGroupData).trigger('change');

    var departmentData = [];
    departmentData.push(departmentAndBizGroupVoList[0].departmentId)
    $("#departmentNameR").val(departmentData).trigger('change');

  }
});


function initData(url, id) {
  $.ajax({
    url: url, async : false, data: {status: 1}, success: function (data) {
      if (data.code == 1000) {
        var newData = new Array();
        $(data.data).each(function (i, c) {
          var item = {};
          item["id"] = c.id;
          item["text"] = c.name;
          newData.push(item);
        });

        $("#" + id).select2({
          data: newData, width: '100%'
        });
      } else {
        new PNotify({
          title: '获取业务标签类型', text: data.msg, type: 'error', icon: true, styling: 'bootstrap3'
        });
      }
    }, error: function (jqXHR, exception) {
      var msg = getMsg4ajaxError(jqXHR, exception);
      showMsg('warning', '初始化业务组信息', msg);
    }
  });
}

// 编辑部门名字
function editDepartment() {
  var ids = ["departmentName"];
  if (!checkEmptyByIds(ids, "编辑部门")) {
    return;
  }

  var data = getDepartDataFromPage();

  var response = requestRemoteRestApi("/api/department/editDepartment", "编辑部门", data);
  if (response.flag == true) {
    window.setTimeout(function () {
      showMsg('info', '部门', '编辑成功');
    }, 1000);
  }
}

// 映射关系
function editDepartmentBizMap() {
  var ids = ["departmentNameR", "bizGroupNameR"];
  if (!checkSelectIsEmpty(ids, "编辑部门产品线映射关系")) {
    return;
  }

  var data = getDepartDataFromPage();

  var response = requestRemoteRestApi("/api/department/editDepartmentBizGroupMap", "编辑部门产品线映射关系", data);
  if (response.flag == true) {
    window.setTimeout(function () {
      showMsg('info', '部门产品线映射关系', '编辑成功');
    }, 1000);
  }
}


//获取数据
function getDepartDataFromPage() {
  var result = {};
  var inputs = $(
      "#departmentInfo .input-group>input,#departmentInfo .input-group>textarea,#ralationInfo .input-group>input,#ralationInfo .input-group>textarea");
  var status = $("#status input:checked").val();
  var selects = $("#ralationInfo .input-group>select");
  $(inputs).each(function (i, c) {
    var id = $(c).prop("id");
    if (id == null || id == '') {
      return;
    }
    var value = $(c).val();
    if (typeof value == 'string') {
      value = value.trim();
    }

    if (value != '' && testNum.test(value)) {
      value = parseInt(value);
    }

    result[id] = value;
    result["status"] = status;

  });

  $(selects).each(function (i, c) {
    var id = $(c).prop("id");
    var value = $(c).val();

    if ($(this).prop("multiple")) { //多选框
      result[id] = value == null ? "" : value.join(",");
      if (id == "bizGroups") {
        result[id] = result[id] != "" ? result[id] : "," + result[id] + ",";
      }
    } else {
      if (value != '' && testNum.test(value)) {
        value = parseInt(value);
      }
      result[id] = value;
    }
  });

  return result;
}


//校验某些属性是否为空
function checkEmptyByIds(ids, desc) {
  var flag = true;
  $(ids).each(function (i, c) {
    var value = $("#" + c).val();
    if (value != null) {
      value = value.trim();
    }
    if (value == undefined || value == '' || value == null) {
      flag = false;
      var desc = $("#" + c).attr("desc");
      showMsg('warning', desc, desc + '不能为空');
    }
  });
  return flag;
}

// 检验select是否设置
function checkSelectIsEmpty(ids, desc) {
  var flag = true;
  $(ids).each(function (i, c) {
    var value = $("#" + c).val();
    if (value == undefined || value == '' || value == null) {
      flag = false;
      var desc = $("#" + c).attr("desc");
      showMsg('warning', desc, desc + '不能为空');
    }
  });
  return flag;
}
