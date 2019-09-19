<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 18.09.2019
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bucket</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/bucketItems" method="get">
    <div class="container">
        <h1>open bucket</h1>
        <p>Please fill in this form to open your bucket.</p>
        <hr>
        <label for="bucket_id"><b>bucket ID</b></label>
        <input  type="text" placeholder="bucket ID" name="bucket_id" required>
        <button type="submit">Open</button>
        <hr>
    </div>
</form>
</body>
</html>
