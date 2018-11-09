package com.seecent.chat.services

class MaterialResult extends ChatApiResult {
    String mediaId
    Long createTime
    String filename
    Long costTime
    String url

    public MaterialResult(Integer code, String message) {
        super(code, message)
    }

    public MaterialResult(Integer code, String message, String mediaId) {
        super(code, message)
        this.mediaId = mediaId
    }

    public MaterialResult(Integer code, String message, String filename, Long costTime) {
        super(code, message)
        this.filename = filename
        this.costTime = costTime
    }

    public MaterialResult(Integer code, String message, String mediaId, Long createTime, Long costTime) {
        super(code, message)
        this.mediaId = mediaId
        this.createTime = createTime
        this.costTime = costTime
    }

    public MaterialResult(Integer code, String message, String mediaId, String url, Long costTime) {
        super(code, message)
        this.mediaId = mediaId
        this.url = url
        this.costTime = costTime
    }

    public String toString() {
        StringBuffer sb = new StringBuffer()
        sb.append("MaterialResult{")
        sb.append("mediaId: ")
        sb.append(mediaId)
        sb.append("url: ")
        sb.append(url)
        sb.append(", createTime: ")
        sb.append(createTime)
        sb.append(", filename: ")
        sb.append(filename)
        sb.append(", costTime: ")
        sb.append(costTime)
        sb.append("}")
    }
}
