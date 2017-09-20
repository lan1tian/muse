<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="uname" value="${user.uname}"/>
    <jsp:param name="platform" value="${platform}"/>
    <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<style>
    .fixed-table-body{
        padding-bottom: 80px;
    }
</style>

<div class="container">

    <div class="row">
        <div class="col-md-4">
            <nav>
                <ol class="cd-breadcrumb triangle">
                    <li><a href="${contextPath}/">首页</a></li>
                    <li class="current"><em>执行流水</em></li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">

            <div class="row">
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">执行Id</span>

                        <select id="taskId" multiple>

                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">任务Id</span>

                        <select id="jobId" multiple>

                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">任务名称</span>
                        <select id="jobName" multiple>

                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">任务类型</span>
                        <select id="jobType" multiple></select>
                    </div>
                </div>

            </div>

            <div class="row top-buffer">

                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">执行人</span>
                        <select id="executeUser" multiple>
                        </select>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:23.5%">状态</span>

                        <div class="form-control" id="taskStatus">
                        </div>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">是否临时任务</span>

                        <div class="form-control" id="isTemp">
                            <input type="checkbox" value="1"> 是
                            &nbsp;<input type="checkbox" value="0"> 否
                        </div>
                    </div>
                </div>
            </div>

            <div class="row top-buffer">

                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">调度日期</span>
                        <input id="scheduleDate" class="form-control"/>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">执行日期</span>
                        <input id="executeDate" class="form-control"/>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">执行开始日期>=</span>
                        <input id="startDate" class="form-control"/>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:50%">执行结束日期<</span>
                        <input id="endDate" class="form-control"/>
                    </div>
                </div>

            </div>



            <div class="row top-buffer">
                <div class="col-md-3 pull-right">
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
            <div id="toolBar">
                <span><i class="fa fa-circle fa-2x" style="color: #FF851B"></i>等待</span>
                <span><i class="fa fa-circle fa-2x" style="color: #FFDC00"></i>准备</span>
                <span><i class="fa fa-circle fa-2x" style="color: #0074D9"></i>运行</span>
                <span><i class="fa fa-circle fa-2x" style="color: #2ECC40"></i>成功</span>
                <span><i class="fa fa-circle fa-2x" style="color: #FF4136"></i>失败</span>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #111111"></i> Killed</span>--%>
                <%--<span><i class="fa fa-circle fa-2x" style="color: #ab279d"></i>删除</span>--%>
            </div>
            <table id="content" data-toolbar="#toolBar">

            </table>

        </div>

    </div>

</div>


<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript">
    <%--var taskQo = ${taskQo};--%>
    var taskQo = 42600;
</script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/task/task.js"></script>
