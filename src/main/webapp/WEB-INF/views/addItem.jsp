<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 18.09.2019
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Item</title>
</head>
<body>
<form action="addItem" method="post">
    <div class="container">
        <h1>add item</h1>
        <p>Please fill in this form to add item to your bucket.</p>
        <hr>

        <label for="user_ID"><b>user ID</b></label>
        <input type="number" placeholder="user ID" name="user_ID" required>

        <label for="item_ID"><b>item ID</b></label>
        <input type="number" placeholder="item ID" name="item_ID" required>

        <hr>

        <button type="submit" class="add_item_btn">Add</button>
    </div>

</form>
</body>
</html>
