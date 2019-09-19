<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="users" scope="request" type="java.util.List<mate.academy.internetshop.model.User>"/>
<jsp:useBean id="greeting" scope="request" type="java.lang.String"/>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 17.09.2019
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Users</title>
</head>
<body>
Hello, ${greeting}, Welcome to All Users page!

Users:
<table border="1">
    <tr>
        <th>ID</th>
        <th>Login</th>
        <th>Name</th>
        <th>Surname</th>
        <th>DELETE</th>
        <th>Orders</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>
                <c:out value="${user.id}" />
            </td>
            <td>
                <c:out value="${user.login}" />
            </td>
            <td>
                <c:out value="${user.name}" />
            </td>
            <td>
                <c:out value="${user.surname}" />
            </td>
            <td>
                <a href="/internet_shop_war_exploded/deleteUser?user_id=${user.id}">Delete</a>
            </td>
            <td>
                <a href="/internet_shop_war_exploded/userOrders?user_id=${user.id}">click</a>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
<a href="/internet_shop_war_exploded/register" >Add new user</a>
</p>
</body>
</html>
