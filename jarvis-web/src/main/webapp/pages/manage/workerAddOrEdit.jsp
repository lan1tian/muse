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
                    <li><a href="${contextPath}/manage/worker">worker管理</a></li>
                    <li class="current"><em>
                        <c:choose>
                            <c:when test="${workerVo==null}">
                                新增worker
                            </c:when>
                            <c:otherwise>
                                编辑worker
                            </c:otherwise>
                        </c:choose>
                    </em></li>
                </ol>
            </nav>
        </div>
    </div>


    <input id="workerId" type="hidden" value="${workerVo.id}"/>

    <div class="row top-buffer">
        <div class="col-md-6 col-md-offset-3 top-buffer">
            <div class="input-group" style="width:100%">
                <span class="input-group-addon" style="width:35%">workerGroupId</span>
                <select id="workerGroupId">
                    <c:forEach items="${workerGroupVoList}" var="workerGroupVo">
                        <option value="${workerGroupVo.id}"
                                <c:choose>
                                    <c:when test="${workerVo.workerGroupId==workerGroupVo.id}">selected</c:when>
                                </c:choose> >${workerGroupVo.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="col-md-6 col-md-offset-3 top-buffer">
            <div class="input-group" style="width:100%">
                <span class="input-group-addon" style="width:35%">IP</span>
                <input class="form-control" id="ip" value="${workerVo.ip}"/>
            </div>
        </div>
        <div class="col-md-6 col-md-offset-3 top-buffer">
            <div class="input-group" style="width:100%">
                <span class="input-group-addon" style="width:35%">端口</span>
                <input class="form-control" id="port" value="${workerVo.port}"/>
            </div>
        </div>

        <div class="col-md-6 col-md-offset-3 top-buffer">
            <div class="input-group" style="width:100%">
                <span class="input-group-addon" style="width:35%">状态</span>
                <select id="status">
                    <option value="0"
                            <c:choose>
                                <c:when test="${workerVo!=null&&workerVo.status==0}">selected</c:when>
                            </c:choose> >停用
                    </option>
                    <option value="1"
                            <c:choose>
                                <c:when test="${workerVo!=null&&workerVo.status==1}">selected</c:when>
                            </c:choose> >启用
                    </option>
                </select>
            </div>
        </div>

        <div class="col-md-6 col-md-offset-3 top-buffer text-center">
            <div class="input-group" style="width:100%">
                <c:choose>
                    <c:when test="${workerVo!=null}">
                        <button type="button" class="btn btn-default" onclick="updateWorker()">更新</button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-default" onclick="addWorker()">新增</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>


<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/manage/workerAddOrEdit.js"></script>