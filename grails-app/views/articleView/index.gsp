<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<head>
	<title>${article?.title}</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon-chat.ico')}">
    <link rel="stylesheet css" href="${resource(dir: 'css', file: 'article.css')}">
	<link rel="stylesheet css" href="${resource(dir: 'css/praise/css', file: 'article-praise.css')}">
	<script src="${resource(dir: 'js/lib/jquery', file: 'jquery.min.js')}"></script>
	<script>
		 $(document).ready(function () {
            try {
                $("#js_praise").click(function () {
                    var id = ${article?.id};
                    var type = "${article?.type}";
                    var url = "${createLink(controller: 'articleView', action: 'praise')}";
                    if("Article" == type) {
                        url = url + "?id=" + id;
                    }
                    else {
                        url = url + "?item_id=" + id;
                    }
                    $.ajax({
                        type: "get",
                        url: url,
                        success: function (data) {
                            $("#praiseCount").html(data);
                        },
                        async: false,
                        dataType: "text"
                    });
                });
            } catch (e) {
                console.log(e);
            }
        });
	</script>
</head>
<body id="activity-detail" class="zh_CN " style="background-color: #e7e8eb;">
    <div id="js_article" class="rich_media">
        <div class="rich_media_inner">
            <div id="page-content">
                <div id="img-content" class="rich_media_area_primary">
                    <h2 class="rich_media_title" id="activity-name">
						${article?.title}
                    </h2>
                    <div class="rich_media_meta_list">
                        <em id="post-date" class="rich_media_meta rich_media_meta_text">${article?.dateCreated}</em>
                        <em class="rich_media_meta rich_media_meta_text">${article?.author}</em>
                        <a class="rich_media_meta rich_media_meta_link rich_media_meta_nickname" href="javascript:void(0);" id="post-user">${article?.account?.name}</a>
                    </div>
                    <div class="rich_media_thumb_wrp" id="media">
                        <img class="rich_media_thumb" src="${article?.coverImage}" onerror="this.parentNode.removeChild(this)">
                    </div>
                    <div class="rich_media_content" id="js_content">
                        <chat:stringToHtml content="${article?.content}"></chat:stringToHtml>
                    </div>
                    <div class="rich_media_tool" id="js_toobar">
                        <g:if test="${article?.originalUrl}">
                            <a class="media_tool_meta meta_primary" id="js_view_source" href="${article?.originalUrl}" target="_blank">阅读原文</a>
                        </g:if>
						<em class="rich_media_meta rich_media_meta_text">阅读 ${article?.readCount}</em>&nbsp;&nbsp;
						<a class="rich_media_meta meta_praise" id="js_praise" href="javascript:void(0);">
							<img class="meta_praise_img" src="${resource(dir: 'images/icons', file: 'praise.png')}"></img>
						</a>
						<em class="rich_media_meta rich_media_meta_text" id="praiseCount">${article?.praiseCount}</em>
                    </div>
                </div>
                <div id="js_bottom_ad_area"></div>
            </div>
            </div>
        </div>
    </div>
</body>
</html>