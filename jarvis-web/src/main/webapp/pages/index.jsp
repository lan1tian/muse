<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="./common/header.jsp">
    <jsp:param name="uname" value="${user.uname}"/>
    <jsp:param name="platform" value="${platform}"/>
    <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<%--<link rel="stylesheet" href="${contextPath}/assets/css/style.css" type="text/css" charset="utf-8">--%>


<div class="container">
    <div class="row">
        <div class="col-md-4">
            <nav>
                <%--20170914注释 style.css 73行--%>
                <ol class="cd-breadcrumb triangle">
                    <li><a href="/">首页</a></li>
                    <li class="current"><em>平台管理</em></li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row top-buffer">
        <div class="col-md-12">
            欢迎使用调度系统
        </div>
    </div>
</div>



<jsp:include page="./common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>
