package com.seecent.chat.util

import com.seecent.chat.assets.Article
import com.seecent.chat.assets.ArticleItem

import org.apache.commons.lang.StringEscapeUtils

class ArticleJsonUtil {

    static String toUpdateJson(Article article) {
        StringBuilder sb = new StringBuilder()
        sb.append("{\"media_id\":\"")
        sb.append(article.mediaId)
        sb.append("\",\"index\":")
        sb.append(0)
        sb.append(",\"articles\":")
        sb.append(toJson(article))
        sb.append("}")
        return sb.toString()
    }

    static String toUpdateJson(ArticleItem articleItem) {
        StringBuilder sb = new StringBuilder()
        sb.append("{\"media_id\":\"")
        sb.append(articleItem.article.mediaId)
        sb.append("\",\"index\":")
        sb.append(articleItem.no + 1)
        sb.append(",\"articles\":")
        sb.append(toJson(articleItem))
        sb.append("}")
        return sb.toString()
    }

    static String toJson(List<String> articles) {
        StringBuilder sb = new StringBuilder()
        sb.append("{\"articles\":[")
        for(int i=0; i<articles.size();i++) {
            if(i > 0) {
                sb.append(",")
            }
            sb.append(articles.get(i))
        }
        sb.append("]}")
        return sb.toString()
    }

    static String toJson(Article it) {
        StringBuilder sb = new StringBuilder()
        sb.append("{")
        sb.append("\"title\":\"")
        sb.append(it.title)
        sb.append("\",\"thumb_media_id\":\"")
        sb.append(it.coverImage.mediaId)
        sb.append("\",\"show_cover_pic\":")
        sb.append(it.coverDisplayInText ? 1 : 0)
        sb.append(",\"author\":\"")
        sb.append(it.author)
        if(it.description) {
            sb.append("\",\"digest\":\"")
            sb.append(it.description)
        }
        if(it.originalUrl && it.originalUrl.trim().toLowerCase().startsWith("http")) {
            sb.append("\",\"content_source_url\":\"")
            sb.append(it.originalUrl)
        }
        sb.append("\",\"content\":\"")
        sb.append(formatJsonContent(it.content))
        sb.append("\"}")
        return sb.toString()
    }

    static String toJson(ArticleItem it) {
        StringBuilder sb = new StringBuilder()
        sb.append("{")
        sb.append("\"title\":\"")
        sb.append(it.title)
        sb.append("\",\"thumb_media_id\":\"")
        sb.append(it.coverImage.mediaId)
        sb.append("\",\"show_cover_pic\":")
        sb.append(it.coverDisplayInText ? 1 : 0)
        sb.append(",\"author\":\"")
        sb.append(it.author)
        if(it.description) {
            sb.append("\",\"digest\":\"")
            sb.append(it.description)
        }
        if(it.originalUrl && it.originalUrl.trim().toLowerCase().startsWith("http")) {
            sb.append("\",\"content_source_url\":\"")
            sb.append(it.originalUrl)
        }
        sb.append("\",\"content\":\"")
        sb.append(formatJsonContent(it.content))
        sb.append("\"}")
        return sb.toString()
    }

    private static String formatJsonContent(String content) {
        String formatContent = content.replaceAll("\"", "\\\\\"")
//        formatContent = formatContent.replaceAll(",", "\\\\,")
//        formatContent = formatContent.replaceAll(":", "\\\\:")
//        formatContent = formatContent.replaceAll("\\{", "\\\\{")
//        formatContent = formatContent.replaceAll("\\}", "\\\\}")
//        formatContent = formatContent.replaceAll("\\[", "\\\\[")
//        formatContent = formatContent.replaceAll("\\]", "\\\\]")
        return formatContent
//        return StringEscapeUtils.escapeHtml(content)
    }
}
