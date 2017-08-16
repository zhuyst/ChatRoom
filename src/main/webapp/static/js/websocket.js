var echo_websocket;

function connect(url) {

    function writeMainMessage(data) {
        var type = data.type;
        var message = data.message;

        var p;
        if(type === "in"){
            p = $("<p class='text-success'></p>").text(message);
        }
        else if(type === "out"){
            p = $("<p class='text-danger'></p>").text(message);
        }

        var panel = $("div#main-message");
        panel.append(p);
        scrollRun(panel);
    }

    function writeMessage(data) {
        var username = data.username;
        var message = data.message;
        var type = data.type;
        var date = data.date;
        date = timeStampToString(date);

        var p_bold;
        if(type === "my" || type === "other"){
            p_bold = $("<p class='bolder' style='margin-bottom: 3px'></p>").append(username + "\t说：\t");
        }
        else if(type === "state"){
            p_bold = $("<p class='bolder' style='margin-bottom: 3px'></p>").append(username + "\t");
        }

        var p_message = $("<p></p>").append(message);

        var div1;
        var p_date;
        if(type === "my" || type === "state"){
            p_date = $("<p class='text-right date'></p>").append(date);
            div1 = $("<div class='pull-right well well-sm text-left text-info' style='max-width: 80%'></div>").append(p_bold,p_message,p_date);
        }
        else if(type === "other"){
            p_date = $("<p class='text-left date'></p>").append(date);
            div1 = $("<div class='pull-left well well-sm text-left text-info' style='max-width: 80%'></div>").append(p_bold,p_message,p_date);
        }

        var div2 = $("<div class='col-sm-12'></div>").append(div1);

        var panel = $("div#output");
        panel.append(div2);
        scrollRun(panel);
    }

    function scrollRun(panel) {
        var beforeHeight = panel.scrollTop();
        panel.scrollTop(beforeHeight + 10000);
    }

    function timeStampToString(time){
        var datetime = new Date();
        datetime.setTime(time);
        var year = datetime.getFullYear();
        var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
        var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
        var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
        var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
        var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
        return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
    }

    echo_websocket = new WebSocket(url);
    echo_websocket.onopen = function () {
        var data = {
            type:"in",
            message:"连接聊天室成功！"};

        writeMainMessage(data);
    };
    echo_websocket.onmessage = function (evt) {
        var data = $.parseJSON(evt.data);
        if(data.type === "in" || data.type === "out" ){
            writeMainMessage(data);
        }
        else if(data.type === "my" || data.type === "other" || data.type === "state"){
            writeMessage(data);
        }

    };
    echo_websocket.onerror = function (evt) {
        var data = {
            type:"out",
            message:evt.data};

        writeMainMessage(data);
        echo_websocket.close();
    };
}

function doSend() {
    var textarea = $("#message");
    var message = textarea.val();
    if($.trim(message) === ""){
        textarea.attr("placeholder","不能发送空的信息！");
        $("#alert").html("不能发送空的信息！");
    }
    else {
        textarea.attr("placeholder","请输入内容...");
        $("#alert").html("");
        echo_websocket.send($.trim(message));
    }
    textarea.val('');
}