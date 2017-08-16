<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登陆界面</title>

    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/static/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>

    <style>
        body{
            background-color: #eeeeee;
        }
        .text-danger{
            margin: 8px 0 0 15px;
        }
    </style>
</head>
<body>
    <div class="main center-block">
        <h1 class="center-block">WebSocket聊天室Demo</h1>
        <div class="col-sm-8 col-sm-offset-2 col-lg-4 col-lg-offset-4">
            <div class="col-sm-12 center-block text-center login-panel">
                <h3 style="font-weight: bold;margin-bottom: 30px;">登陆</h3>
                <form role="form" action="${pageContext.request.contextPath}/login" method="post">
                    <div class="col-sm-12 text-left form-group">
                        <div class="col-sm-12">
                            <label class="col-sm-12" for="username">用户名：</label>
                            <input class="form-control" type="text" name="username" id="username" placeholder="请输入用户名">
                            <c:if test="${msg != null}">
                                <p class="text-danger text-left" >${msg}</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="col-sm-12 form-group" style="margin-top: 10px">
                        <div class="text-right" style="margin-right: 7%">
                            <input class="btn btn-primary" type="submit" value="进入聊天室">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
