var testNum = /^[0-9]*$/;
var bizGroup = null;

$(function () {
  initData(contextPath + "/api/bizGroup/getAllByCondition", "bizGroupNameR");
  initData(contextPath + "/api/department/getAllNames", "departmentNameR");
  initUsers();
});

function resetMap() {
  $("#departmentNameR").val("all").trigger("change");
  $("#bizGroupNameR").val("all").trigger("change");
}

function resetBizGroup() {
  $("#bizGroupName").val("");
  $("#owner").val("all").trigger("change");
  $("#status").val("all").trigger("change");
}

function resetDepartment() {
  $("#departmentName").val("");
}

//保存department
function saveDepartment() {
  var ids = ["departmentName"];
  if (!checkEmptyByIds(ids, "保存部门")) {
    return;
  }

  var data = getDepartDataFromPage();
  // console.log("saveDepartment:"+JSON.stringify(data))

  var response = requestRemoteRestApi("/api/department/submit", "新增任务", data);
  if (response.flag == true) {
    window.setTimeout(function () {
      showMsg('info', '部门', '保存成功');
    }, 1000);
    $("#departmentInfoDiv").removeClass("active");
    $("#ralationInfoDiv").removeClass("active");
    $("#bizGroupInfoDiv").addClass("active");
    $("#departmentInfo").hide();
    $("#ralationInfo").hide();
    $("#bizGroupInfo").show();
  }
}

// 保存产品线
function saveBzGroup() {
  var ids = ["bizGroupName", "owner"];
  if (!checkEmptyByIds(ids, "保存产品线")) {
    return;
  }

  var data = getDepartDataFromPage();
  // console.log("saveBzGroup:"+JSON.stringify(data))
  var response = requestRemoteRestApi("/api/bizGroup/add", "新增产品线", data);
  if (response.flag == true) {
    window.setTimeout(function () {
      showMsg('info', '产品线', '保存成功');
    }, 1000);

    window.location.reload();
    window.setTimeout(function () {
      console.log("reload..")
      $("#ralationInfoDiv").trigger('click');
    }, 1000)
    $("#bizGroupInfoDiv").trigger('click');
    $("#departmentInfoDiv").removeClass("active");
    $("#bizGroupInfoDiv").removeClass("active");
    $("#ralationInfoDiv").addClass("active");
    $("#departmentInfo").hide();
    $("#bizGroupInfo").hide();
    $("#ralationInfo").show();
  }

}

// 保存部门和产品线的映射关系
function saveDepartBizMap() {
  var ids = ["departmentNameR", "bizGroupNameR"]
  if (!checkSelectIsEmpty(ids, "保存部门和产品线映射关系")) {
    return;
  }

  var data = getDepartDataFromPage();
  var response = requestRemoteRestApi("/api/department/addDepartBizMap", "新增部门和产品线映射关系", data);
  if (response.flag == true) {
    window.setTimeout(function () {
      showMsg('info', '产品线', '保存成功');
    }, 1000);

    window.location.href = contextPath + '/department';

  }
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


//校验某些属性是否为空
function checkEmptyByIds(ids, desc) {
  var flag = true;
  $(ids).each(function (i, c) {
    var value = $("#" + c).val();
    if (value != null) {
        value = (value+"").trim()
    }
    if (value == undefined || value == '' || value == null) {
      flag = false;
      var desc = $("#" + c).attr("desc");
      showMsg('warning', desc, desc + '不能为空');
    }
  });
  return flag;
}

//获取数据
function getDepartDataFromPage() {
  var result = {};
  var inputs = $(
      "#departmentInfo .input-group>input,#departmentInfo .input-group>textarea,#bizGroupInfo .input-group>input,#bizGroupInfo .input-group>textarea,#ralationInfo .input-group>input,#ralationInfo .input-group>textarea");
  var status = $("#status input:checked").val();
  var selects = $("#ralationInfo .input-group>select");

  var owner = $("#owner").text();
  // console.log("inputs:"+JSON.stringify(inputs));
  console.log("owner:"+owner);
  // console.log("selects:"+JSON.stringify(selects));

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
  result["owner"] = owner;
  // console.log("result2:"+JSON.stringify(result));
  return result;
}

function initData(url, id) {
  $.ajax({
    url: url,
    data: {status: 1},
    success: function (data) {
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

//格式化结果
function formatResult(result) {
  return result.text;
}
//格式化结果选择框
function formatResultSelection(result) {
  return result.id;
}

//初始化内网用户
function initUsers() {
  $.ajax({
    url:contextPath + "/api/common/getAllUser",
    success:function(data){
      if (1000 == data.code) {
        var users = data.rows;
        var newData = new Array();

        $(users).each(function (i, c) {
          var item = {};
          item["id"] = c.uname;
          item["text"] = c.nick;
          newData.push(item);
        });

        $("#owner").select2({
          data: newData,
          width: '100%'
        });
        if (null != bizGroup) {
          var owner = bizGroup.owner;
          var arr = owner.trim().split(",");
          $("#owner").val(arr).trigger("change");
        }
      }
      else {
        new PNotify({
          title: '获取内网用户信息',
          text: data.msg,
          type: 'error',
          icon: true,
          styling: 'bootstrap3'
        });
      }
    },
    error: function (jqXHR, exception) {
      var msg = getMsg4ajaxError(jqXHR, exception);
      showMsg('warning', '初始化用户列表', msg);
    }
  })

}
