<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="uname" value="${user.uname}"/>
    <jsp:param name="platform" value="${platform}"/>
    <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<style>
    *.icon-green {
        color: #2ca02c
    }
</style>

<div class="container">

    <div class="row">
        <div class="col-md-4">
            <nav>
                <ol class="cd-breadcrumb triangle">
                    <li><a href="${contextPath}/">首页</a></li>
                    <li class="current"><em>重跑任务</em></li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row top-buffer">
        <div class="col-md-12">

            <div class="row top-buffer">
                <div class="col-md-6 col-md-offset-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">选择任务</span>
                        <select id="originJobId" multiple>
                            <option value="">无</option>
                            <c:forEach items="${jobVoList}" var="job" varStatus="status">
                                <option value="${job.jobId}" appName="${job.appName}"
                                        appKey="${job.appKey}">${job.jobName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <div class="row top-buffer">
                <div class="col-md-6 col-md-offset-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">开始日期</span>
                        <input id="startTime" class="form-control"/>
                    </div>
                </div>
            </div>

            <div class="row top-buffer">
                <div class="col-md-6 col-md-offset-3">
                    <div class="input-group" style="width:100%">
                        <span class="input-group-addon" style="width:35%">结束日期</span>
                        <input id="endTime" class="form-control"/>
                    </div>
                </div>
            </div>

            <div class="row top-buffer">
                <div class="col-md-6 col-md-offset-3">
                    <div class="input-group">
                        <span class="input-group-addon" style="border-radius:4px;border:1px solid #ccc">选择重跑任务</span>
                    </div>

                    <div class="center">
                        <div id="reRunJobs"></div>
                    </div>
                </div>
            </div>

            <div class="row top-buffer">
                <div class="col-md-4 col-md-offset-4 text-center">
                    <button type="button" class="btn btn-primary" onclick="submit()">提交</button>
                    <button type="button" class="btn btn-primary" onclick="reset()">重置</button>
                </div>

            </div>

        </div>
    </div>
</div>


<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>
<script type="text/javascript" src="${contextPath}/assets/js/jarvis/trigger/trigger.js"></script>
