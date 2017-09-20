<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
  <jsp:param name="uname" value="${user.uname}"/>
  <jsp:param name="platform" value="${platform}"/>
  <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<div class="container">

  <div class="row">
    <div class="col-md-4">
      <nav>
        <ol class="cd-breadcrumb triangle">
          <li><a href="${contextPath}/">首页</a></li>
          <li class="current"><em>操作记录</em></li>
        </ol>
      </nav>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
      <div class="row">

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:50%">操作开始日期>=</span>
            <input id="startOperDate" class="form-control"/>
          </div>
        </div>

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:50%">操作结束日期<</span>
            <input id="endOperDate" class="form-control"/>
          </div>
        </div>

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:45%">任务名称</span>
            <select id="title" multiple>
              <c:forEach items="${titles}" var="title" varStatus="status">
                <option value="${title}">${title}</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:45%">操作人</span>
            <select id="operator" multiple>
              <c:forEach items="${operators}" var="operator" varStatus="status">
                <option value="${operator}">${operator}</option>
              </c:forEach>
            </select>
          </div>
        </div>

      </div>

      <div class="row top-buffer">
        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:45%">操作类型</span>
            <select id="operationType">
              <option value="">全部</option>
              <option value="submitJob">提交任务</option>
              <option value="modifyJob">修改任务</option>
              <option value="modifyJobDependency">修改任务依赖</option>
              <option value="modifyJobScheduleExp">修改任务计划表达式</option>
              <option value="modifyJobStatus">修改任务状态</option>
              <option value="manualRerunTask">手动重跑</option>
            </select>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-md-6 col-md-offset-6">
          <div class="row">
            <div class="col-md-6 col-md-offset-6">
              <div class="input-group pull-right">
                <button type="button" class="btn btn-primary" onclick="search()">查询</button>
                <button type="button" class="btn btn-primary" onclick="reset()"
                        style="margin-left: 3px">重置
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>

  <hr>

  <div class="row top-buffer">
    <div class="col-md-12">
      <table id="content" data-toolbar="#toolBar">
      </table>
    </div>
  </div>
</div>

<jsp:include page="../common/footer.jsp">
  <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>
<script type="text/javascript">
  var user = '${user.uname}';
  var appId = '${app.appId}';
</script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/operation/operation.js"></script>
