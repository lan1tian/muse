<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="uname" value="${user.uname}"/>
    <jsp:param name="platform" value="${platform}"/>
    <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<link type="text/css" rel="stylesheet" href="${contextPath}/assets/css/dashboard.css" charset="utf-8">


<div class="container">

    <div class="row">
        <div class="col-md-4">
            <nav>
                <ol class="cd-breadcrumb triangle">
                    <li><a href="${contextPath}/">首页</a></li>
                    <li class="current"><em>我的任务</em></li>
                </ol>
            </nav>
        </div>
    </div>

    <hr>

    <h4 style="color:#73879C;">任务状态汇总</h4>

    <div class="row" style="padding-left:15px;padding-right:15px;">
        <div class="col-md-2 thin-padding">
            <div class="dashboard-card">
                <a href="javascript:void(0)" onclick="dashBoard.refreshTasks(1)">
                    <div class="status-text">
                    <span>
                        <i class="glyphicon glyphicon-question-sign wait"></i>等待
                    </span>
                    </div>

                    <div class="num-text">12</div>
                </a>
            </div>
        </div>
        <div class="col-md-2 thin-padding">
            <div class="dashboard-card">
                <a href="javascript:void(0)" onclick="dashBoard.refreshTasks(2)">
                    <div class="status-text">
                        <span>
                            <i class="glyphicon glyphicon-info-sign ready"></i>准备
                        </span>
                    </div>
                    <div class="num-text">1</div>
                </a>
            </div>
        </div>
        <div class="col-md-2 thin-padding">
            <div class="dashboard-card">
                <a href="javascript:void(0)" onclick="dashBoard.refreshTasks(3)">
                    <div class="status-text">
                        <span>
                            <i class="glyphicon glyphicon-menu-right runnning"></i>运行
                        </span>
                    </div>

                    <div class="num-text">25</div>
                </a>
            </div>
        </div>
        <div class="col-md-2 thin-padding">
            <div class="dashboard-card">
                <a href="javascript:void(0)" onclick="dashBoard.refreshTasks(4)">
                    <div class="status-text">
                        <span>
                            <i class="glyphicon glyphicon-ok success"></i>成功
                        </span>
                    </div>


                    <div class="num-text">129</div>
                </a>
            </div>
        </div>
        <div class="col-md-2 thin-padding">
            <div class="dashboard-card">
                <a href="javascript:void(0)" onclick="dashBoard.refreshTasks(5)">
                    <div class="status-text">
                        <span>
                            <i class="glyphicon glyphicon-remove failed"></i>失败
                        </span>
                    </div>
                    <div class="num-text">16</div>
                </a>
            </div>
        </div>
        <div class="col-md-2 thin-padding">
            <div class="dashboard-card">
                <a href="javascript:void(0)" onclick="dashBoard.refreshTasks(6)">
                    <div class="status-text">
                        <span>
                            <i class="glyphicon glyphicon-pause killed"></i>终止
                        </span>
                    </div>

                    <div class="num-text">5</div>
                </a>
            </div>
        </div>
    </div>

    <!-- 显示某状态的任务列表 -->
    <div id="statusTaskList" class="top-buffer" style="display: none">
        <div class="text-center">
            <a  href="javascript:void(0)" onclick="dashBoard.hideTaskList()">
                <i class="glyphicon glyphicon-chevron-up"></i>
            </a>
        </div>


        <div id="toolBar">
            <span><i class="fa fa-circle fa-2x" style="color: #FF851B"></i>等待</span>
            <span><i class="fa fa-circle fa-2x" style="color: #FFDC00"></i>准备</span>
            <span><i class="fa fa-circle fa-2x" style="color: #0074D9"></i>运行</span>
            <span><i class="fa fa-circle fa-2x" style="color: #2ECC40"></i>成功</span>
            <span><i class="fa fa-circle fa-2x" style="color: #FF4136"></i>失败</span>
            <span><i class="fa fa-circle fa-2x" style="color: #111111"></i>终止</span>

        </div>
        <table id="tasks" data-toolbar="#toolBar">

        </table>
    </div>

    <hr>

</div>




<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>

<script type="text/javascript">

</script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/dashboard/index.js"></script>
