function like(btn, entityType, entityId, entityUserId) {
    $.post(
        "like",
        {
            "entityType": entityType,
            "entityId": entityId,
            "entityUserId": entityUserId
        },
        function (data) {
            data = $.parseJSON(data);
            if (data.code === 0) {
                data = data.data;
                // 点赞成功
                $(btn).children("b").text(data["likeStatus"] ? "已赞" : "赞");
                $(btn).children("i").text(data["likeCount"]);
            } else {
                // 点赞失败
                alert(data.message)
            }
        }
    )
}
