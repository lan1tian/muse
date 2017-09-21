<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--<%@ taglib prefix="admin" uri="http://bda.mogujie.org/admin" %>--%>
<c:set var="contextPath" scope="application" value="<%=request.getContextPath()%>" />

<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="${contextPath}/assets/img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="${contextPath}/assets/css/reset.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/css/style.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/bootstrap-3.3.5/css/bootstrap.min.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/bootstrap-3.3.5/css/bootstrap-theme.min.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/font-awesome/css/font-awesome.min.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/css/login.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/bootstrap-switch/css/bootstrap3/bootstrap-switch.min.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/back-to-top/css/style.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/css/cart.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/breadcrumbs/css/style.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/css/main.css" type="text/css" charset="utf-8">
    <link rel="stylesheet" href="${contextPath}/assets/plugins/select2/4.0.1/css/select2.min.css" type="text/css" charset="utf-8">
    <link type="text/css" rel="stylesheet" href="${contextPath}/assets/plugins/qtip2/jquery.qtip.min.css" />
    <link type="text/css" rel="stylesheet" href="${contextPath}/assets/plugins/pnotify/pnotify.custom.min.css" />
    <link type="text/css" rel="stylesheet" href="${contextPath}/assets/plugins/animate/animate.css" />

    <link type="text/css" rel="stylesheet" href="${contextPath}/assets/plugins/bootstrap-table/bootstrap-table.min.css" />
    <link type="text/css" rel="stylesheet" href="${contextPath}/assets/css/select2_special.css" />
    <link type="text/css" rel="stylesheet" href="${contextPath}/assets/plugins/jstree/3.2.1/themes/default/style.min.css" />
    <script src="${contextPath}/assets/plugins/modernizr/modernizr.js" type="application/javascript" charset="utf-8"></script>
    <script src="${contextPath}/assets/plugins/jquery/jquery-2.1.4.min.js" type="application/javascript" charset="utf-8"></script>


    <script>
        var contextPath = "${contextPath}";
        function skip() {
            var v = $("#manager").val();
            window.location.href = "${contextPath}/pages/manage/"+v;
        }
    </script>
    <title>muse</title>
</head>
<body>
<header>

  <%--<div id="cd-logo" class="bdmenu">--%>
    <%--<ul>--%>
      <%--<li class="active">--%>
        <%--<a  href="${contextPath}/">${platformName}</a>--%>
      <%--</li>--%>
    <%--</ul>--%>
  <%--</div>--%>

    <nav id="cd-top-nav" class="navbar">
        <%--${admin:menus(__menus, contextPath)}--%>
            <ul>
                <li><a href="${contextPath}/pages/dashboard/index.jsp">我的任务</a></li>
                <li><a href="${contextPath}/pages/job/index.jsp">任务管理</a></li>
                <li><a href="${contextPath}/pages/task/index.jsp">执行流水</a></li>
                <li><a href="${contextPath}/pages/plan/index.jsp">执行计划</a></li>
                <li><a href="${contextPath}/pages/trigger/index.jsp">重跑任务</a></li>
                <li><a href="${contextPath}/pages/operation/index.jsp">操作记录</a></li>
                <li><a href="${contextPath}/pages/department/index.jsp">部门管理</a></li>
                <%--<li><a href="${contextPath}/pages/manage/bizGroup.jsp">业务类型管理</a></li>--%>
                <%--<li><a href="${contextPath}/pages/manage/app.jsp">应用管理</a></li>--%>
                <%--<li><a href="${contextPath}/pages/manage/worker.jsp">Worker管理</a></li>--%>
                <%--<li><a href="${contextPath}/pages/manage/system.jsp">调度系统管理</a></li>--%>
                <li>
                    <select onchange="skip()" id="manager" style="background-color: rgb(49, 53, 61);color: white;border: hidden;">
                        <option value="app.jsp" style="color:white; background-color: rgb(49, 53, 61)">后台管理</option>
                        <option value="app.jsp" style="color:white; background-color: rgb(49, 53, 61)">应用管理</option>
                        <option value="bizGroup.jsp" style="color:white;background-color: rgb(49, 53, 61)">业务类型管理</option>
                        <option value="worker.jsp" style="color:white;background-color: rgb(49, 53, 61)">Worker管理</option>
                        <option value="system.jsp" style="color:white;background-color: rgb(49, 53, 61)">调度系统管理</option>

                    </select>
                </li>
            </ul>
    </nav>

    <c:choose>
        <c:when test="${null != user && user.uname != ''}">
            <a id="cd-menu-trigger" href="javascript:void(0);">
                <span class="cd-menu-text">${user.nick}</span>
            </a>
        </c:when>
        <c:otherwise>
            <a id="cd-sign-trigger" class="cd-signin" href="javascript:void(0);">
                <span class="cd-menu-text">Login</span>
            </a>
        </c:otherwise>
    </c:choose>


</header>

<%--<main class="cd-main-content">--%>