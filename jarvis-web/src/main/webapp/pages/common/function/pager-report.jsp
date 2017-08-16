<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: bitrho
  Date: 15/1/8
  Time: 下午2:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<nav class="pull-right">
  <ul class="pagination">
    <%
        String totalCount = request.getParameter("totalCount");
        String pageSize = request.getParameter("pageSize");
        String pageNo = request.getParameter("pageNo");
        String outputCount = request.getParameter("outputCount");

        Integer totalItems = (int) Math.ceil(Double.parseDouble(totalCount)/ Double.parseDouble(pageSize));
        Integer start = Math.max(Integer.parseInt(pageNo) - (int) Math.ceil(Double.parseDouble(outputCount)/2), 1);
        Integer end = Math.min(start + Integer.parseInt(outputCount) - 1, totalItems);
    %>



      <% if (start == 1) { %>
        <li class="disabled"><a href="javascript:void(0);" aria-label="Previous"><span aria-hidden="true">«</span></a></li>
      <% } %>
      <% if (start > 1) { %>
        <li ><a pageNo="1" class="page-link"><span aria-hidden="true">«</span></a></li>
      <% }%>

      <%
        for(Integer i = start; i <= end; i++) {
      %>
        <li <% if (Integer.parseInt(pageNo) == i){%> class="active" <% } %> >
        <a pageNo="<% out.print(i.toString()); %>" class="page-link"><% out.print(i);%><span class="sr-only">(current)</span></a>
        </li>
    <%
    }
    if (end < totalItems) {
    %>
      <li><a>...</a></li>
      <li>
        <a pageNo="<% out.print(totalItems.toString()); %>" class="page-link"><% out.print(totalItems);%><span class="sr-only">(current)</span></a>
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
