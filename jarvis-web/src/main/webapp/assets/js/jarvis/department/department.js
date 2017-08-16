//查找
function search() {
  $("#content").bootstrapTable("destroy");
  initData();
}

//重置参数
function reset() {
  $("#name").val("");
  $("#bizGroup").val("");
  $("#owner").val("");
}

$(function () {
  //select采用select2 实现
  $(".input-group select").select2({width: '100%'});
  // 部门名称
  getSimilarString("name", contextPath + "/api/department/getSimilarDepartmentName");
  // 产品线名称
  getSimilarString("bizGroup", contextPath + "/api/department/getSimilarBizGroupName");
  // 产品线管理员
  getSimilarString("owner", contextPath + "/api/department/getSimilarBizGroupOwner");

  initData();
});



function getSimilarString(name, url) {
  $("#" + name).select2({
    ajax: {
      url: url,
      dataType: 'json',
      delay: 1000,
      data: function (params) {
        return {
          q: params.term, // search term
          page: params.page
        };
      },
      processResults: function (data, page) {
        if(data.status){
          showMsg('error','模糊查询操作信息',data.status.msg);
          return {
            results: []
          };
        }
        else{
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
}



//获取查询参数
function getQueryPara() {
  var queryPara = {};

  var nameList = $("#name").val();
  var bizGroupList = $("#bizGroup").val();
  var ownerList = $("#owner").val();

  nameList = nameList == "all" ? undefined : nameList;
  nameList = nameList == null ? undefined : nameList;
  bizGroupList = bizGroupList == "all" ? undefined : bizGroupList;
  bizGroupList = bizGroupList == null ? undefined : bizGroupList;
  ownerList = ownerList == "all" ? undefined : ownerList;
  ownerList = ownerList == null ? undefined : ownerList;
  queryPara["nameList"] = JSON.stringify(nameList);
  queryPara["bizGroupList"] = JSON.stringify(bizGroupList);
  queryPara["ownerList"] = JSON.stringify(ownerList);

  return queryPara;
}


//字段配置
var columns = [{
  field: 'departmentName',
  title: '部门名称',
  switchable: true,
  visible: true
}, {
  field: 'bizGroupName',
  title: '产品线名称',
  switchable: true,
  visible: true
}, {
  field: 'bizGroupOwner',
  title: '产品线管理员',
  switchable: true,
  visible: true
},{
  field: 'updateTime',
  title: '时间',
  switchable:true,
  visible:true

},{
  field: 'operation',
  title: '操作',
  switchable: true,
  visible: true,
  formatter: operationFormatter
}];

//初始化数据及分页
function initData() {
  var queryParams = getQueryPara();
  $("#content").bootstrapTable({
    columns: columns,
    pagination: true,
    sidePagination: 'server',
    search: false,
    url: contextPath + '/api/department/getDepartments',
    queryParams: function (params) {
      for (var key in queryParams) {
        var value = queryParams[key];
        params[key] = value;
      }
      return params;
    },
    responseHandler:function(res){
      if(res.status){
        showMsg("error","初始化部门列表",res.status.msg);
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

//格式化结果
function formatResult(result) {
  return result.text;
}
//格式化结果选择框
function formatResultSelection(result) {
  return result.id;
}

function operationFormatter(value, row, index) {
  var result = '<a href="' + contextPath + '/department/departmentDetail?id=' + row["id"] + '" target="_blank" title="编辑部门"><i class="glyphicon glyphicon-pencil">编辑部门</i></a>';

  result += '<a href="' + contextPath + '/manage/bizGroup' + '"target="_blank" title="编辑产品线"><i class="glyphicon glyphicon-edit">编辑产品线</i></a>';

  result += ' <a href="javascript:void(0)" onclick="deleteDepartment(' + row["id"] + ')" title="删除部门"><i class="glyphicon glyphicon-remove text-danger">删除部门</i></a>';

  return result;
}

// 删除部门
function deleteDepartment(id) {
  (new PNotify({
    title: '部门名称操作',
    text: '确定删除此部门?',
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
    var data = {};
    data["departmentNameR"] = id;
    requestRemoteRestApi("/api/department/deleteDepartment", "删除业务标签", data,false,true);
    window.setTimeout(function(){
      search();
    },1000);
  }).on('pnotify.cancel', function () {
  });
}

