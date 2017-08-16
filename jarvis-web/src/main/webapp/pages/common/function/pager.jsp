<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: bitrho
  Date: 15/1/8
  Time: 下午2:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mogu.bigdata.admin.util.misc.Util" %>

<div class="pull-right" style="margin:25px 0;">
    <select class="form-control" onchange="changeUrl(this,'pageSize')">
        <option value="20" <c:choose><c:when test="${pageSize==20}">selected</c:when></c:choose>>20</option>
        <option value="30" <c:choose><c:when test="${pageSize==30}">selected</c:when></c:choose>>30</option>
        <option value="50" <c:choose><c:when test="${pageSize==50}">selected</c:when></c:choose>>50</option>
        <option value="100" <c:choose><c:when test="${pageSize==100}">selected</c:when></c:choose>>100</option>
    </select>
</div>

<nav id="pager"   class="pull-right" style="margin:5px 0;">

  <ul class="pagination">
    <%
        String totalCount = request.getParameter("totalCount");
        String pageSize = request.getParameter("pageSize");
        String pageNo = request.getParameter("pageNo");
        String outputCount = request.getParameter("outputCount");
        String url = request.getParameter("url");

        Integer totalItems = (int) Math.ceil(Double.parseDouble(totalCount)/ Double.parseDouble(pageSize));
        Integer start = Math.max(Integer.parseInt(pageNo) - (int) Math.ceil(Double.parseDouble(outputCount)/2), 1);
        Integer end = Math.min(start + Integer.parseInt(outputCount) - 1, totalItems);

    %>



      <% if (start == 1) { %>
        <li class="disabled"><a href="javascript:void(0);" aria-label="Previous"><span aria-hidden="true">«</span></a></li>
      <% } %>
      <% if (start > 1) { %>
        <li ><a href="<% out.print(Util.replaceUrlQuery(url, "pageNo", "1")); %>"><span aria-hidden="true">«</span></a></li>
      <% }%>

      <%
        for(Integer i = start; i <= end; i++) {
      %>
        <li <% if (Integer.parseInt(pageNo) == i){%> class="active" <% } %> >
        <a  href="<% out.print(Util.replaceUrlQuery(url, "pageNo", i.toString())); %>"><% out.print(i);%><span class="sr-only">(current)</span></a>
        </li>
    <%
    }
    if (end < totalItems) {
    %>
      <li><a>...</a></li>
      <li>
        <a  href="<% out.print(Util.replaceUrlQuery(url, "pageNo", totalItems.toString()));%>"><% out.print(totalItems);%><span class="sr-only">(current)</span></a>
      </li>
      <li class="disabled"><a href="javascript:void(0);" aria-label="Next"><span aria-hidden="true">»</span></a></li>
    <%
  } else { %>
      <li class="disabled"><a href="javascript:void(0);" aria-label="Next"><span aria-hidden="true">»</span></a></li>
 <% }
%>
      <li ><a href="javascript:void(0);"><%=totalCount%></a></li>
  </ul>
</nav>
