<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 24.09.2019
  Time: 11:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
Login page!
<div>${errorMsg}</div>
<form action="login" method="post">
    <div class="container">
        <h1>Login</h1>
        <p>Please fill in this form to log into account.</p>
        <hr>
        <label for="login"><b>Login</b></label>
        <input type="text" placeholder="Enter login" name="login" required>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="psw" required>

        <button type="submit" class="signinbtn">Sign in</button>
    </div>

    <div class="container signup">
        <p>Don't have an account? <a href="register">Sign up</a>.</p>
    </div>
</form>

</body>
</html>
