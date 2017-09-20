<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
  <jsp:param name="uname" value="${user.uname}"/>
  <jsp:param name="platform" value="${platform}"/>
  <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<style>
  .nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover {
    background-color: #96c03d;
    color: #fff;
  }

  label.disabled {
    color: darkgrey;
  }
</style>


<div class="container">

  <div class="row">
    <div class="col-md-4">
      <nav>
        <ol class="cd-breadcrumb triangle">
          <li><a href="${contextPath}/">首页</a></li>
          <li><a href="${contextPath}/department">部门管理</a></li>
          <li class="current"><em>新增部门</em></li>
        </ol>
      </nav>
    </div>
  </div>


  <div class="row">
    <div class="col-md-12">

      <ul class="nav nav-tabs cd-multi-steps text-center custom-icons">
        <li class="active" id="departmentInfoDiv">
          <a href="#departmentInfo" data-toggle="tab">部门信息</a>
        </li>
        <li id="bizGroupInfoDiv">
          <a href="#bizGroupInfo" data-toggle="tab" >产品线信息</a>
        </li>
        <li id="ralationInfoDiv">
          <a href="#ralationInfo" data-toggle="tab">部门产品线关联信息</a>
        </li>
      </ul>

      <!-- Tab panes -->
      <div class="tab-content">
        <div class="tab-pane active" id="departmentInfo">
          <!--部门名称 -->
          <div class="col-md-12">
            <div class="row top-buffer">
              <div class="col-md-6 col-md-offset-3">
                <div class="input-group" style="width:100%">
                  <span class="input-group-addon" style="width:35%">部门名称
                  <span class="text-danger" style="vertical-align: middle">*</span></span>
                  <input id="departmentName" class="form-control" desc="部门名称"/>
                </div>
              </div>
            </div>
          </div>

          <div class="row"></div>
          <div class="row top-buffer">
            <div class="col-md-6 col-md-offset-3 text-center">
              <button type="button" class="btn btn-primary" onclick="saveDepartment()">保存</button>
              <button type="button" class="btn btn-primary" onclick="resetDepartment()">重置</button>
            </div>
          </div>
        </div>

        <!-----------产品线管理----------->
        <div class="tab-pane" id="bizGroupInfo">
          <!--产品线名称 -->
          <div class="col-md-12">
            <div class="row top-buffer">
              <div class="col-md-6 col-md-offset-3">
                <div class="input-group" style="width:100%">
                    <span class="input-group-addon" style="width:35%">产品线名称
                      <span class="text-danger" style="vertical-align: middle">*</span>
                    </span>
                  <input id="bizGroupName" class="form-control" desc="产品线名称"/>
                </div>
              </div>
            </div>
          </div>

          <!--产品线管理员 -->
          <div class="col-md-12">
            <div class="row top-buffer">
              <div class="col-md-6 col-md-offset-3">
                <div class="input-group" style="width:100%">
                    <span class="input-group-addon" style="width:35%">产品线管理员
                      <span class="text-danger" style="vertical-align: middle">*</span>
                    </span>
                  <select id="owner" multiple></select>
                </div>
              </div>
            </div>
          </div>

          <!-----状态------->
          <div class="col-md-12">
            <div class="row top-buffer">
              <div class="col-md-6 col-md-offset-3">
                <div class="input-group" style="width:100%">
                  <span class="input-group-addon" style="width:35%">启用/禁用</span>

                  <div id="status" class="form-control">
                    <input name="status" type="radio" value="1" checked="checked">启用
                    <input name="status" type="radio" value="2">禁用
                  </div>

                </div>
              </div>
            </div>
          </div>

          <div class="row"></div>
          <div class="row top-buffer">
            <div class="col-md-6 col-md-offset-3 text-center">
              <button type="button" class="btn btn-primary" onclick="saveBzGroup()">保存</button>
              <button type="button" class="btn btn-primary" onclick="resetBizGroup()">重置</button>
            </div>
          </div>
        </div>

        <!------------依赖关系------------------->
        <div class="tab-pane" id="ralationInfo">
          <!--部门名称 -->
          <div class="col-md-12">
            <div class="row top-buffer">
              <div class="col-md-6 col-md-offset-3">
                <div class="input-group" style="width:100%">
                  <span class="input-group-addon" style="width:35%">部门名称
                  <span class="text-danger" style="vertical-align: middle">*</span></span>
                  <select id="departmentNameR" desc="部门名称">
                    <option></option>
                  </select>
                </div>
              </div>
            </div>
          </div>

          <!--产品线名称 -->
          <div class="col-md-12">
            <div class="row top-buffer">
              <div class="col-md-6 col-md-offset-3">
                <div class="input-group" style="width:100%">
                    <span class="input-group-addon" style="width:35%">产品线名称
                      <span class="text-danger" style="vertical-align: middle">*</span>
                    </span>
                  <select id="bizGroupNameR" multiple desc="产品线名称">
                  </select>
                </div>
              </div>
            </div>
          </div>

          <div class="row"></div>
          <div class="row top-buffer">
            <div class="col-md-6 col-md-offset-3 text-center">
              <button type="button" class="btn btn-primary" onclick="saveDepartBizMap()">保存</button>
              <button type="button" class="btn btn-primary" onclick="resetMap()">重置</button>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</div>


</div>

<%--<%@include file="addOrEditModal.jsp" %>--%>


<jsp:include page="../common/footer.jsp">
  <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script>
</script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/department/add.js"></script>


