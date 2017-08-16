
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="header.jsp">
  <jsp:param name="uname" value="${user.uname}"/>
</jsp:include>

<div class="container top-buffer">
  <div class="alert alert-danger alert-dismissible fade in" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
    <h4>Error!</h4>
    <hr/>
    ${message}
  </div>
</div>



<jsp:include page="login.jsp">
  <jsp:param name="uname" value="${user.uname}"/>
</jsp:include>

<jsp:include page="footer.jsp">
  <jsp:param name="menuMap" value="${menuMap}"/>
</jsp:include>
