<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="orders" scope="request" type="java.util.List<mate.academy.internetshop.model.Order>"/>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 19.09.2019
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My orders</title>
</head>
<body>

Orders:
<table border="1">
    <tr>
        <th>ID</th>
        <th>Total price</th>
        <th>DELETE</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.id}" />
            </td>
            <td>
                <c:out value="${order.totalPrice()}" />
            </td>
            <td>
                <a href="/internet_shop_war_exploded/deleteOrder?order_id=${order.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
