
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
</main>


<div class="cd-cover-layer"></div>
<div class="cd-loading-bar"></div>

<div id="cd-shadow-layer"></div>
<div id="cd-cart">
    <h2>偏好设置</h2>
    <ul class="cd-cart-items">
        <li>
            获取最新公告
            <div class="cd-price">接收网站最新公告</div>
            <div  class="switch switch-mini cd-item-remove" >
                <input type="checkbox" data-size="mini"/>
            </div>
        </li>
    </ul>
    <a href="${adminServletPath}/logout" class="checkout-btn">Logout</a>
</div>


<a href="javascript:void(0);" class="cd-top">Top</a>

<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-3.3.5/js/bootstrap.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/js/login.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-switch/js/bootstrap-switch.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/back-to-top/js/main.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/js/const.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/js/cart.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/js/pager.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/select2/4.0.1/js/select2.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/select2/4.0.1/js/i18n/zh-CN.js"></script>

<%--<script type="text/javascript" src="${contextPath}/assets/plugins/form/jquery.form.min.js" charset="UTF-8"></script>--%>
<script type="text/javascript" src="${contextPath}/assets/plugins/qtip2/jquery.qtip.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/pnotify/pnotify.custom.min.js" charset="UTF-8"></script>
<%--<script type="text/javascript" src="${contextPath}/assets/plugins/echarts-2.2.7/echarts-all.js" charset="UTF-8"></script>--%>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-table/bootstrap-table.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-table/extensions/export/bootstrap-table-export.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/tableExport.jquery.plugin/tableExport.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/jstree/3.2.1/jstree.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/momentjs/moment.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/d3/d3.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/d3/d3.layout.js" charset="UTF-8"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/bootstrap-list-filter/bootstrap-list-filter.src.js"></script>

<script type="text/javascript" src="${contextPath}/assets/js/jarvis.js" charset="UTF-8"></script>

</body>
</html>
