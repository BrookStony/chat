<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon-chat.ico')}">
    <link rel="stylesheet css" href="${resource(dir: 'css', file: 'article.css')}">
    <title>${article?.title}</title>
</head>
<body id="activity-detail" class="zh_CN " style="background-color: #e7e8eb;">
    <div id="js_article" class="rich_media">
        <div class="rich_media_inner">
            <h2 class="rich_media_title" id="activity-name">
                ${article?.title}
            </h2>
            <div class="rich_media_meta_list">
                <em id="post-date" class="rich_media_meta rich_media_meta_text">${article?.dateCreated}</em>
                <em class="rich_media_meta rich_media_meta_text">${article?.author}</em>
                <a class="rich_media_meta rich_media_meta_link rich_media_meta_nickname" href="javascript:void(0);" id="post-user">${article?.account?.name}</a>
            </div>

            <div id="page-content">
                <div id="img-content">
                    <div class="rich_media_thumb_wrp" id="media">
                        <img class="rich_media_thumb" src="${article?.coverImage}" onerror="this.parentNode.removeChild(this)">
                    </div>
                    <div class="rich_media_content" id="js_content">
                        <chat:stringToHtml content="${article?.content}"></chat:stringToHtml>
                    </div>
                    <div class="rich_media_tool" id="js_toobar">
                        <g:if test="${article?.originalUrl}">
                            <a class="media_tool_meta meta_primary" id="js_view_source" href="http://${article?.originalUrl}/" target="_blank">阅读原文</a>
                        </g:if>
                    </div>
                </div>
                <div id="js_bottom_ad_area"></div>
            </div>
        </div>
    </div>

    %{--<div class="rich_media">--}%
        %{--<div class="rich_media_inner">--}%
            %{--<h2 class="rich_media_title">${article?.title}</h2>--}%
            %{--<div class="rich_media_meta_list">--}%
                %{--<em class="rich_media_meta text">${article?.dateCreated}</em>--}%
                %{--<em class="rich_media_meta text">${article?.title}</em>--}%
                %{--<a class="rich_media_meta" href="javascript:void (0)">${article?.account?.name}</a>--}%
            %{--</div>--}%
            %{--<div id="page-content">--}%
                %{--<div class="rich_media_thumb" id="media">--}%
                    %{--<img src="${article?.coverImage}" onerror="this.parentNode.removeChild(this)">--}%
                %{--</div>--}%
                %{--<div class="rich_media_content">--}%
                    %{--<chat:stringToHtml content="${article?.content}"></chat:stringToHtml>--}%
                %{--</div>--}%
            %{--</div>--}%
            %{--<div style="margin-top: 10px;margin-bottom: 30px">--}%
                %{--<a href="http://${article?.originalUrl}/" target="_blank">阅读原文</a>--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</div>--}%
</body>
</html>