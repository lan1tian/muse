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
                    <li class="current"><em>调度系统管理</em></li>
                </ol>
            </nav>
        </div>
    </div>
    <div class="row top-buffer">
        <div class="col-md-12">
            <div class="row top-buffer">
                <div class="col-md-2 col-md-offset-4">
                    <span class="pull-right btn text-info">调度系统状态</span>
                </div>

                <div class="col-md-2 text-center">
                    <input id="systemStatus" type="checkbox" checked="">
                </div>
            </div>
            <!--
            <div class="row top-buffer">
                <div class="col-md-2 col-md-offset-4">
                    <span class="pull-right btn text-info">是否自动调度</span>
                </div>
                <div class="col-md-2 text-center">
                    <input id="systemAutoStatus" type="checkbox" checked="">
                </div>
            </div>
            -->
        </div>
    </div>

</div>

<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/manage/system.js"></script>