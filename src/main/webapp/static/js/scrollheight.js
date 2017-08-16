function resize() {
    function setSendPanelHeight() {
        var height = $("#message").outerHeight();
        $("#send-button").css("height",height);
    }

    function setMessagePanelHeight() {
        var main = $("#main-message");
        var mainTop = main.offset().top;
        var mainHeight = main.outerHeight(true);
        var sendTop = $("#send-panel").offset().top;
        $("#output").css("height",sendTop - mainTop - mainHeight);
    }

    setSendPanelHeight();
    setMessagePanelHeight();
}

function setMainMessagePanelHeight() {
    var panel = $("#main-message");
    var height = panel.outerHeight();
    panel.css("height", height);
    panel.empty();
}
