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
                    <li><a href="${contextPath}/manage/bizGroup">业务类型管理</a></li>
                    <li class="current"><em>业务类型管理</em></li>
                </ol>
            </nav>
        </div>
    </div>

    <hr>

    <div class="row top-buffer">
        <div class="col-md-4 col-md-offset-4">
            <div class="input-group" style="width: 100%">
                <span class="input-group-addon" style="width: 30%">标签名</span>
                <input id="name" class="form-control"/>
            </div>
        </div>

        <div class="col-md-4 col-md-offset-4 top-buffer">
            <div class="input-group" style="width: 100%">
                <span class="input-group-addon" style="width: 30%">状态</span>
                <div id="status" class="form-control">

                </div>
            </div>
        </div>

        <div class="col-md-4 col-md-offset-4 top-buffer">
            <div class="input-group" style="width: 100%">
                <span class="input-group-addon" style="width: 30%">维护人</span>
                <select id="owner" multiple></select>
            </div>
        </div>

        <div class="col-md-4 col-md-offset-4 top-buffer text-center">
            <button type="button" class="btn btn-primary" onclick="saveBizGroup()">保存</button>
        </div>
    </div>
</div>


<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script>
    var id = '${id}';
</script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/manage/bizDetail.js"></script>