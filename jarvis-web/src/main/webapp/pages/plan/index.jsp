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
                    <li class="current"><em>执行计划</em></li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">


            <div class="row top-buffer">
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">任务ID</span>
                        <select id="jobId" multiple>
                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">任务名称</span>
                        <select id="jobName" multiple>
                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">任务类型</span>
                        <select id="jobType" multiple></select>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">优先级</span>
                        <select id="priority" multiple></select>
                    </div>
                </div>

            </div>

            <div class="row top-buffer">

                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">执行用户</span>
                        <select id="executeUser" multiple>
                        </select>
                    </div>
                </div>

                <%--<div class="col-md-6">--%>
                    <%--<div class="input-group" style="width:100%">--%>
                        <%--<span class="input-group-addon" style="width:16.5%">状态</span>--%>

                        <%--<div class="form-control" id="taskStatus">--%>
                            <%--<input type="checkbox" name="taskStatus" onclick="chooseStatus(this)" value="all">全部--%>
                            <%--<input type="checkbox" name="taskStatus" onclick="chooseStatus(this)" value="0">未初始化--%>
                            <%--<input type="checkbox" name="taskStatus" onclick="chooseStatus(this)" value="1">等待--%>
                            <%--<input type="checkbox" name="taskStatus" onclick="chooseStatus(this)" value="2">准备--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <div class="col-md-3 pull-right">
                    <div class="row">
                        <div class="col-md-6 col-md-offset-6">
                            <div class="input-group pull-right">
                                <button type="button" class="btn btn-primary " onclick="search()"
                                        style="margin-right: 3px">查询
                                </button>
                                <button type="button" class="btn btn-primary " onclick="reset()">重置</button>
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

            <%--<div id="toolBar">--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #c9c9c9"></i>未初始化</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #FF851B"></i>等待</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #FFDC00"></i>准备</span>--%>

                <%--<span><i class="fa fa-circle fa-2x" style="color: #0074D9"></i>运行</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #2ECC40"></i>成功</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #FF4136"></i>失败</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #111111"></i>终止</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #ab279d"></i>删除</span>--%>
            <%--</div>--%>

            <table id="content" data-toolbar="#toolBar">

            </table>

        </div>

    </div>


</div>


<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript">
    var planQo = ${planQo};
</script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/plan/plan.js"></script>
