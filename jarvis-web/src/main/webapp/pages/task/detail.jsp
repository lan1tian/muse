<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="uname" value="${user.uname}"/>
    <jsp:param name="platform" value="${platform}"/>
    <jsp:param name="platforms" value="${platforms}"/>
</jsp:include>

<link type="text/css" rel="stylesheet" href="${contextPath}/assets/plugins/d3/d3-collapsible-tree.css"/>

<style>
    .modal-dialog {
        width: 900px;
    }
    td>pre {
        white-space: pre-wrap;
        word-wrap: break-word;
    }
    pre{
        font-size: 13px;
    }
</style>




<div class="container">

    <div class="row">
        <div class="col-md-4">
            <nav>
                <ol class="cd-breadcrumb triangle">
                    <li><a href="${contextPath}/">首页</a></li>
                    <li><a href="${contextPath}/task">执行流水</a></li>
                    <li class="current"><em>执行详情</em></li>
                </ol>
            </nav>
        </div>


        <div class="col-md-2 pull-right top-buffer" style="margin-top: 70px">
            <a href="javascript:void(0)" onclick="showTaskHistory('${taskVo.taskId}')">
                <h4>查看重试记录</h4>
            </a>
        </div>

    </div>

    <div class="row top-buffer">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered">
                        <tbody>
                        <tr>
                            <td class=" bg-warning">任务ID</td>
                            <td>${jobVo.jobId}</td>
                            <td class=" bg-warning">任务名称</td>
                            <td>
                                <a href="${contextPath}/job/detail?jobId=${jobVo.jobId}">${jobVo.jobName}</a>
                            </td>
                            <td class=" bg-warning">参数</td>
                            <td >
                                <pre style="max-width:500px">${taskVo.params}</pre>
                            </td>
                        </tr>
                        <tr>
                            <td class=" bg-warning">执行ID</td>
                            <td>${taskVo.taskId}</td>
                            <td class=" bg-warning">执行者</td>
                            <td>${taskVo.executeUser}</td>
                            <td class=" bg-warning">状态</td>
                            <td>
                                <c:choose>
                                    <c:when test="${taskVo.status==1}">
                                        等待
                                    </c:when>
                                    <c:when test="${taskVo.status==2}">
                                        准备
                                    </c:when>
                                    <c:when test="${taskVo.status==3}">
                                        执行
                                    </c:when>
                                    <c:when test="${taskVo.status==4}">
                                        成功
                                    </c:when>
                                    <c:when test="${taskVo.status==5}">
                                        失败
                                    </c:when>
                                    <c:when test="${taskVo.status==6}">
                                        终止
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class=" bg-warning">调度时间</td>
                            <td><fmt:formatDate value="${taskVo.scheduleTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td class=" bg-warning">执行进度</td>
                            <td>
                                <div class="progress progress-sm mbn" style="width: 160px;display: inline-block;">
                                    <div role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100"
                                         style="width: <fmt:formatNumber type="number" value="${taskVo.progress*100}" maxIntegerDigits="3"></fmt:formatNumber>%;" class="progress-bar progress-bar-success">
                                    </div>
                                </div>
                            <span>
                                <fmt:formatNumber type="number" value="${taskVo.progress*100}" maxIntegerDigits="3"></fmt:formatNumber>%
                            </span>
                            </td>
                            <td class=" bg-warning">最近30次平均耗时</td>
                            <td>${taskVo.avgExecuteTime}秒</td>
                        </tr>
                        <tr>
                            <td class=" bg-warning">开始时间</td>
                            <td><fmt:formatDate value="${taskVo.executeStartTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td class=" bg-warning">结束时间</td>
                            <td><fmt:formatDate value="${taskVo.executeEndTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td class=" bg-warning">耗时</td>
                            <td>${taskVo.executeTime}秒</td>
                        </tr>
                        <tr>
                            <td class=" bg-warning">worker IP</td>
                            <td>${taskVo.ip}</td>
                        </tr>

                        <c:choose>
                            <c:when test="${taskVo.status==5}">

                                <tr id="errorNotify">
                                    <td colspan="6">
                                        推测原因:
                            <pre style="color:red" id="errorNotifyMsg">
                            </pre>
                                    </td>
                                </tr>
                            </c:when>
                        </c:choose>
                        <tr>
                            <td colspan="6">
                                <div id="container" style="height:400px;width:100%"></div>
                            </td>
                        </tr>


                        <tr>

                            <td colspan="6">
                                <div>
                                    <ul id="tabNav" class="nav nav-tabs">
                                        <li role="presentation" class="active">
                                            <a href="#executeContent" data-toggle="tab">执行内容</a>
                                        </li>
                                        <li role="presentation">
                                            <a href="#log" data-toggle="tab">日志</a>
                                        </li>
                                        <c:if test="${debug == 1}">
                                            <li role="presentation">
                                                <a href="#logMore" data-toggle="tab">日志(标准输出)</a>
                                            </li>
                                        </c:if>
                                    </ul>

                                    <div class="tab-content">
                                        <div id="executeContent" class="tab-pane active" style="width: 1150px">
                                            <pre>${taskVo.content}</pre>
                                        </div>

                                        <pre id="log" class="tab-pane" style="width: 1150px"></pre>
                                        <a id="moreLog" style="display: none" href="javascript:void(0)" class="pull-right"><i class="glyphicon glyphicon-arrow-down text-primary"></i></a>
                                        <c:if test="${debug == 1}">
                                            <pre id="logMore" class="tab-pane"></pre>
                                        </c:if>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <hr>
    <h4>执行依赖详情</h4>
    <div >
        <span><i class="fa fa-circle fa-2x" style="color: #FF851B"></i>等待</span>
        <span><i class="fa fa-circle fa-2x" style="color: #FFDC00"></i>准备</span>
        <span><i class="fa fa-circle fa-2x" style="color: #0074D9"></i>运行</span>
        <span><i class="fa fa-circle fa-2x" style="color: #2ECC40"></i>成功</span>
        <span><i class="fa fa-circle fa-2x" style="color: #FF4136"></i>失败</span>
        <span><i class="fa fa-circle fa-2x" style="color: #111111"></i> Killed</span>
        <%--<span><i class="fa fa-circle fa-2x" style="color: #ab279d"></i>删除</span>--%>
    </div>
    <div id="popoverContainer" ></div>

    <div id="dependTree"></div>


    <!-- 显示 task history的模态框 -->
    <div id="taskHistoryModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">执行流水重试记录</h4>
                </div>
                <div class="modal-body">
                    <table id="taskHistory">

                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>

</div>


<jsp:include page="../common/footer.jsp">
    <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>
<script type="text/javascript" src="${contextPath}/assets/plugins/echarts-2.2.7/echarts-all.js" charset="UTF-8"></script>

<script type="text/javascript">
    var taskVoList =${taskVoList};
    var taskId = '${taskVo.taskId}';
    var jobId = '${taskVo.jobId}';
    var attemptId = '${taskVo.attemptId}';
    var page = {
        jobType: '${taskVo.jobType}'
    }
</script>
<script type="text/javascript" src="${contextPath}/assets/js/jarvis/task/detail.js"></script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/task/concept-graph.js" charset="UTF-8"></script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis/task/dependency.js"></script>
