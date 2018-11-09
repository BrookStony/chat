package chat

class ArticleTagLib {

    static namespace = "chat"

    def stringToHtml = { attr, body ->
        out << """${attr.content}"""
    }
}
