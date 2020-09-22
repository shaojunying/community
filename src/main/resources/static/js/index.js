$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    // 隐藏新帖发布的弹出框
    $("#publishModal").modal("hide");
    // 发送发帖的请求, 显示请求结果
    let title = $("#recipient-name").val();
    let content = $("#message-text").val();
    $.post(
        "/discuss-post",
        {"title": title, "content": content},
        function (data) {
            data = $.parseJSON(data);
            $("#hintBody").text(data.message);
            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                if (data.code === 0) {
                    window.location.reload();
                }
            }, 2000);
        }
    )
}
