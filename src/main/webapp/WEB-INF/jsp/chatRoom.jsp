<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>聊天室界面</title>

    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/static/js/jquery-3.2.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/sockjs.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/websocket.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/scrollheight.js"></script>
    <script>
        $(document).ready(function () {
            setMainMessagePanelHeight();
            setSendPanelHeight();
            setMessagePanelHeight();

            connect("ws://" + "${url}" + "websocket/chatRoom");

            $("#message").keydown(function (event) {
                if(event.which == "13"){
                    if(event.ctrlKey){
                        var message = $(this).val();
                        $(this).val(message + "\n");
                    }
                    else {
                        event.preventDefault();
                        doSend();
                    }
                }
            })
        });
    </script>
    <style>
        p{
            margin: 0;
            font-size: 20px;
        }
        p.date{
            font-size: 18px;
            margin-left: 30px;
            margin-top: 5px;
        }
        .well{
            padding-left: 15px;
            padding-right: 15px;
        }
    </style>
</head>
<body>
<div class="main" style="padding-top: 1%">
    <div class="well well-sm  bolder text-center col-sm-12 col-lg-6 col-lg-offset-3" id="main-message">
        <p>1</p>
        <p>2</p>
    </div>
    <div class="col-sm-12" id="output"></div>
    <div class="col-sm-12" id="send-panel">
        <form class="form-inline" role="form">
            <div class="form-group col-sm-9 col-lg-5 col-lg-offset-3" id="message-panel">
                <textarea id="message" class="input-lg form-control" rows="3" placeholder="请输入内容..." style="width: 100%"></textarea>
                <div class="col-sm-12" style="margin-top: 10px">
                    <p class="col-sm-6 text-left text-danger" id="alert"></p>
                    <p class="col-sm-6 text-right text-muted">Enter键发送信息</p>
                    <p class="col-sm-12 text-right text-muted">Ctrl + Enter键进行换行</p>
                </div>
            </div>
            <div class="form-group col-sm-3 col-lg-1">
                <button class="btn btn-primary" id="send-button" type="button" style="width: 100%" onclick="doSend()">发送信息</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>