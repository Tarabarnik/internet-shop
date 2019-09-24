<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="items" scope="request" type="java.util.List<mate.academy.internetshop.model.Item>"/>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 17.09.2019
  Time: 20:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Catalogue</title>
</head>
<body>
Catalogue:

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
    </tr>
    <c:forEach var="order" items="${items}">
        <tr>
            <td>
                <c:out value="${order.id}" />
            </td>
            <td>
                <c:out value="${order.name}" />
            </td>
            <td>
                <c:out value="${order.price}" />
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
