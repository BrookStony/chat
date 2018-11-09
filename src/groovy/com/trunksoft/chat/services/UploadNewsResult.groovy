package com.trunksoft.chat.services

class UploadNewsResult {
    String mediaId
    Long createTime
    String type

    public UploadNewsResult(String mediaId, Long createTime, String type){
        this.mediaId = mediaId
        this.createTime = createTime
        this.type = type
    }

    public static UploadNewsResult create(mediaId, createTime, type){
        if(null!=mediaId){
            return new UploadNewsResult(mediaId as Long,createTime as Long, type?.toString())
        }
        return  new UploadNewsResult(null,0,null)
    }

    boolean isOk(){
        if(null == mediaId || "".equals(mediaId)){
            return false
        }
        return true
    }
}
