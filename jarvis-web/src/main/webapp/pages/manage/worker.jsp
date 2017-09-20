<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


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
                    <li class="current"><em>Worker管理</em></li>
                </ol>
            </nav>
        </div>
    </div>


    <ul class="nav nav-tabs">
        <li role="presentation" class="active"><a href="#worker" data-toggle="tab">Worker</a></li>
        <li role="presentation"><a href="#workerGroup" data-toggle="tab">Worker Group</a></li>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="worker">
            <div class="row top-buffer">
                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="input-group" style="width:100%">
                                <span class="input-group-addon" style="width:35%">Worker Group 名</span>
                                <select id="workerGroupId">
                                    <option value="all">全部</option>
                                    <c:forEach items="${workerGroupVoList}" var="workerGroup">
                                        <option value="${workerGroup.id}">${workerGroup.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>


                        <div class="col-md-3">
                            <div class="input-group" style="width:100%">
                                <span class="input-group-addon" style="width:35%">IP</span>
                                <select id="ip">
                                    <option value="all">全部</option>
                                    <c:forEach items="${ipList}" var="ip">
                                        <option value="${ip}">${ip}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-2">
                            <div class="input-group" style="width:100%">
                                <span class="input-group-addon" style="width:35%">port</span>
                                <select id="port">
                                    <option value="all">全部</option>
                                    <c:forEach items="${portList}" var="port">
                                        <option value="${port}">${port}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-2">
                            <div class="input-group" style="width:100%">
                                <span class="input-group-addon" style="width:35%">启用状态</span>
                                <select id="workerStatus">
                                    <option value="all">全部</option>
                                    <option value="1">启用</option>
                                    <option value="2">停用</option>
                                </select>
                            </div>
                        </div>


                        <div class="col-md-2 pull-right">
                            <div class="row">
                                <div class="col-md-10 col-lg-offset-2">
                                    <div class="input-group">
                                        <button type="button" class="btn btn-primary" onclick="searchWorker()">查询
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="resetWorker()"
                                                style="margin-left: 3px">重置
                                        </button>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>


                    <div class="row top-buffer">
                        <div class="col-md-12">

                            <table id="workerContent">

                            </table>

                        </div>

                    </div>

                </div>
            </div>
        </div>
        <div class="tab-pane" id="workerGroup">
            <div class="row top-buffer">
                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="input-group" style="width:100%">
                                <span class="input-group-addon" style="width:35%">Worker Group名称</span>
                                <select id="name">
                                    <option value="all">全部</option>
                                    <c:forEach items="${workerGroupVoList}" var="workerGroupVo">
                                        <option value="${workerGroupVo.name}">${workerGroupVo.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-3 pull-right">
                            <div class="row">
                                <div class="col-md-6 col-lg-offset-6">
                                    <div class="input-group">
                                        <button type="button" class="btn btn-primary" onclick="searchWorkerGroup()">查询
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="resetWorkerGroup()"
                                                style="margin-left: 3px">重置
                                        </button>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>


                    <div class="row top-buffer">
                        <div class="col-md-12">
                            <div id="toolbar">
                                <a class="btn btn-primary" href="${contextPath}/manage/workerGroupAddOrEdit">新增Worker Group</a>
                            </div>
                            <table id="workerGroupContent" data-toolbar="#toolbar">

                            </table>

                        </div>

                    </div>

                </div>
            </div>
        </div>
    </div>


</div>

<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/manage/worker.js"></script>