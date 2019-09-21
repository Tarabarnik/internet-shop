<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 18.09.2019
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bucket</title>
</head>
<body>
Items in your bucket:

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>DELETE</th>
    </tr>
    <c:forEach var="order" items="${bucket.items}">
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
            <td>
                <a href="/internet_shop_war_exploded/deleteItem?item_id=${order.id}&bucket_id=${bucket.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<p>
    <a href="/internet_shop_war_exploded/order?bucket_id=${bucket.id}">Order that!</a>
</p>
</body>
</html>
