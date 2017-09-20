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
                            <c:when test="${workerGroupVo==null}">
                                新增worker group
                            </c:when>
                            <c:otherwise>
                                编辑worker group
                            </c:otherwise>
                        </c:choose>
                    </em></li>
                </ol>
            </nav>
        </div>
    </div>


    <input id="workerGroupId" type="hidden" value="${workerGroupVo.id}">

    <div class="row top-buffer">
        <div class="col-md-6 col-md-offset-3">
            <div class="input-group" style="width:100%">
                <span class="input-group-addon" style="width:35%">workerGroup名称</span>
                <input class="form-control" id="name" value="${workerGroupVo.name}" onblur="checkWorkerGroupName()"/>
            </div>
        </div>


        <div class="col-md-6 col-md-offset-3 top-buffer text-center">
            <div class="input-group" style="width:100%">
                <c:choose>
                    <c:when test="${workerGroupVo!=null}">
                        <button type="button" class="btn btn-default" onclick="updateWorkerGroup()">更新</button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-default" onclick="addWorkerGroup()">新增</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>



<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/manage/workerGroupAddOrEdit.js"></script>