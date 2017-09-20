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
          <li class="current"><em>部门管理</em></li>
        </ol>
      </nav>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
      <div class="row">

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:45%">部门名称</span>
            <select id="name" multiple>
              <c:forEach items="${names}" var="name" varStatus="status">
                <option value="${name}">${name}</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:45%">产品线名称</span>
            <select id="bizGroup" multiple>
              <c:forEach items="${bizGroups}" var="bizGroup" varStatus="status">
                <option value="${bizGroup}">${bizGroup}</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="col-md-3">
          <div class="input-group" style="width:100%">
            <span class="input-group-addon" style="width:45%">产品线管理员</span>
            <select id="owner" multiple>
              <c:forEach items="${owners}" var="owner" varStatus="status">
                <option value="${owner}">${owner}</option>
              </c:forEach>
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
      <div id="toolbar">
        <a class="btn btn-primary" href="${contextPath}/department/add">新增部门</a>
      </div>
      <table id="content" data-toolbar="#toolbar">

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

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/department/department.js"></script>
