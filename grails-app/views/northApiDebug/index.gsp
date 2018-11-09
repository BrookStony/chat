<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>开放接口测试</title>
    <r:require modules="jquery-latest, json2"/>
    <r:layoutResources/>
    <r:script>
        $(document).ready(function () {
            try {
                $("#submit").click(function () {
                    var accessToken = $("#accessToken").val();
                    var proxyUrl= $("#proxyUrl").val() + "?access_token=" + accessToken;
                    var method = $("#method").children('option:selected').val();
                    var url = $("#url").val();
                    var json = $("#json").val();
                    if (url) {
                        $.ajax({
                            type: "post",
                            url: proxyUrl,
                            data: {method: method, url: url, data: json},
                            success: function (result) {
                                $("#result").html(result);
                            },
                            async: false,
                            dataType: "text"
                        });
                    }
                });
            } catch (e) {
                console.log(e);
            }
        });
    </r:script>
    <style>
        div {
            display: block;
        }
        .content {
            padding: 10px 15px;
            border: 1px solid #ccc;
            color: #333;
            background-color: #f8f8f8;
            border-radius: 3px;
            margin-bottom: 20px;
        }
        .frm_control_group {
            margin-bottom: 20px;
        }
        .frm_control_group label {
            margin-right: 48px;
            display: inline-block;
            vertical-align: top;
            zoom: 1;
        }
        .frm_label {
            width: 80px;
            float: left;
            margin-right: 5px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="content">
        <input type="hidden" id="proxyUrl" name="url" value="${createLink(controller: 'northApiDebug', action: 'debug')}" style="width: 360px"/>
        <div class="frm_control_group">
            <label class="frm_label">AccessToken:</label>
            <input type="text" id="accessToken" name="accessToken" value="66668888" style="width: 360px"/>
        </div>
        <div class="frm_control_group">
            <label class="frm_label">方法:</label>
            <select id="method">
                <option value="GET">GET</option>
                <option value="POST" selected="selected">POST</option>
            </select>
        </div>
        <div class="frm_control_group">
            <label class="frm_label">URL:</label>
            <input type="text" id="url" name="url" value="http://127.0.0.1:8080/chat/rest/message/alarm" style="width: 360px"/>
        </div>
        <div class="frm_control_group">
            <label class="frm_label">Body:</label>
            <textarea id="json" name="json" cols="100" rows="10">{"tousers":[8], "url":"http://soso.com", "data":{"title":"测试告警通知","content":"10.221.44.198 磁盘分区0使用90%, 剩余0.7G.","occurtime":"2014-04-08 17:00:02","remark":"请尽快处理（您可以点击这里参考告警类型说明及处理建议）."}}</textarea>
        </div>
        <div class="frm_control_group">
            <label class="frm_label" >&nbsp;</label>
            <button id="submit" type="submit">提交</button>
        </div>
        <div class="frm_control_group">
            <label class="frm_label">结果:</label>
            <textarea id="result" name="result" cols="100" rows="5"></textarea>
        </div>
    </div>
    <r:layoutResources/>
</body>
</html>