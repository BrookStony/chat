package com.seecent.chat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.HttpStatus;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpClientUtil {

    public static String post(String host, String url, String data) throws Exception {
        String result = null;
        if(null != data) {
            HttpClient client = new HttpClient();
            PostMethod post = new PostMethod(url);
            post.setRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0");
            post.setRequestHeader("Host", host);
            post.setRequestHeader("Connection", "Keep-Alive");
            post.setRequestHeader("Cache-Control", "no-cache");

            post.setRequestBody(data);
            int status = client.executeMethod(post);
            System.out.println("status: " + status);
            if (status == HttpStatus.SC_OK)
            {
                String responseContent = post.getResponseBodyAsString();
                System.out.println("responseContent: " + responseContent);
                result = responseContent;
            }
        }

        return result;
    }

    public static String upload(String host, String url, String name, File file, String contentType, String charset) throws Exception {
        String result = null;
        if(null != file && file.exists()) {
            HttpClient client = new HttpClient();
            PostMethod post = new PostMethod(url);
            post.setRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0");
            post.setRequestHeader("Host", host);
            post.setRequestHeader("Connection", "Keep-Alive");
            post.setRequestHeader("Cache-Control", "no-cache");

            FilePart filepart = new FilePart(name, file, contentType,
                    charset);
            Part[] parts = new Part[] { filepart };
            MultipartRequestEntity entity = new MultipartRequestEntity(
                    parts, post.getParams());
            post.setRequestEntity(entity);
            int status = client.executeMethod(post);
            System.out.println("status: " + status);
            if (status == HttpStatus.SC_OK)
            {
                String responseContent = post.getResponseBodyAsString();
                System.out.println("responseContent: " + responseContent);
                result = responseContent;
            }
        }

        return result;
    }

    public static boolean download(String url, String fileName) {
        boolean result = false;
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(url);
        try {
            client.executeMethod(get);
            File storeFile = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(storeFile);
            FileOutputStream output = fileOutputStream;
            output.write(get.getResponseBody());
            output.close();
            result = true;
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {

        String token = "N38O-j46LfcrjzNqzVBwjpt-269CMdjzQi-qQDnmix39yXb_0IjqCG_1RdvDoBzFKDUiqAgipohtZqXx-HxpeKJDiyCrH2GbdDRjG5H2ls8";
        String host = "api.weixin.qq.com";
        String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + token + "&type=image";
        File file = new File("d:\\test" + File.separator + "123456.jpg");
        try {
            String result = HttpClientUtil.upload(host, url, "media", file, "image/jpeg", "UTF-8");

//			String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + token;
//			String mediaId = "XMS4Rk5PcYILMiXW-MVsdi4dw_MecMFM1mlmmb4fxoQ";
//			String result = HttpClientUtil.post(host, url, "{\"media_id\":\"XMS4Rk5PcYILMiXW-MVsdi4dw_MecMFM1mlmmb4fxoQ\"}");
            System.out.println("result: " + result);
            if(null != result) {
                JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
                JsonObject json = jsonparer.parse(result)
                        .getAsJsonObject();

                // {"errcode":40004,"errmsg":"invalid media type"}
                if (json.get("errcode") == null)
                {
                    result = json.get("media_id").getAsString(); // 上传成功  {"type":"TYPE","media_id":"MEDIA_ID","created_at":123456789}
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		String imageUrl = "https://mmbiz.qlogo.cn/mmbiz/qL1I9TcJN24PJX2eIXVNWDAEwNwLoo2BXibn8biaddknrMGC59Z3meSDzQ8Ngj4qNgF6t5Xv9LGJNFLa1v24hNOg/0?wx_fmt=jpeg";
//		HttpClientUtil.download(imageUrl, "d:\\test\\123.jpg");
    }

}
