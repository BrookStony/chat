package com.trunksoft.chat.assets

class PictureManageService {

    static boolean transactional = false

    def materialManageService
    def wechatMaterialService

    /**
     * 图片完整URL
     * @param url
     * @return
     */
    String serverUrl(String url) {
        return materialManageService.serverUrl(url)
    }

    /**
     * 图片完整路径
     * @param path
     * @return
     */
    String completeFilePath(String path) {
        return materialManageService.completeFilePath(path)
    }

    /**
     *
     * @param picture
     * @param article
     * @return
     */
    def delete(Picture picture, boolean article) {
        if(picture && picture.article == article) {
            picture.delete(flush: true)
            deleteFile(picture.path)
        }
    }

    /**
     *
     * @param picture
     * @return
     */
    def delete(Picture picture) {
        if(picture) {
            picture.delete(flush: true)
            deleteFile(picture.path)
            deleteMaterialFile(picture)
        }
    }

    /**
     * 删除永久图片素材
     * @param picture
     * @return
     */
    def deleteMaterialFile(Picture picture) {
        if(picture.mediaId) {
            wechatMaterialService.delete(picture.account, picture.mediaId) //删除永久图片素材
        }
    }

    def delete(Long id, boolean article) {
        if(null != id){
            def picture = Picture.get(id)
            if(picture && picture.article == article) {
                picture.delete(flush: true)
                deleteFile(picture.path)
            }
        }
    }

    def delete(Long id) {
        if(null != id){
            def picture = Picture.get(id)
            if(picture) {
                picture.delete(flush: true)
                deleteFile(picture.path)
            }
        }
    }

    /**
     * 删除图片文件
     * @param fileName
     * @return
     */
    def deleteFile(String fileName) {
        materialManageService.deleteFile(fileName)
    }
}
