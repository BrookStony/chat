package com.trunksoft.chat.module.autoreply

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.type.MessageType
import com.trunksoft.util.MathUtil
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AutoReplyRuleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def autoReplyService
    def articleService
    def materialManageService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = AutoReplyRule.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
        }
        def autoReplyRuleCount = result.totalCount
        def autoReplyRuleList = result.collect {
            return toAutoReplyRuleMap(it)
        }
        response.setHeader('totalItems', autoReplyRuleCount as String)
        respond autoReplyRuleList
    }

    private def toAutoReplyRuleMap(AutoReplyRule it) {
        def map = [id: it.id, name: it.name, keyword: it.keyword,
                   matchType: it.matchType.name(), messageType: it.messageType.name(),
                   msg: it.msg,
                   materialId: it.materialId, material: [:],
                   articleId: it.articleId, article: [:],
                   type: it.type.name(), status: it.status.name()]
        if(MessageType.IMAGE == it.messageType) {
            def pic = Picture.get(it.materialId)
            if(pic) {
                map.material = [name: pic.name, url: materialManageService.serverUrl(pic.url)]
            }
        }
        else if(MessageType.NEWS == it.messageType) {
            def article = Article.get(it.articleId)
            if(article) {
                map.article = articleService.toArticle(article, false)
            }
        }
        return map
    }

    def show(AutoReplyRule autoReplyRuleInstance) {
        def map = [id: autoReplyRuleInstance.id, name: autoReplyRuleInstance.name, keyword: autoReplyRuleInstance.keyword,
                   msg: autoReplyRuleInstance.msg, materialId: autoReplyRuleInstance.materialId, articleId: autoReplyRuleInstance.articleId,
                   matchType: autoReplyRuleInstance.matchType.name(), messageType: autoReplyRuleInstance.messageType.name(),
                   type: autoReplyRuleInstance.type.name(), status: autoReplyRuleInstance.status.name()]
        def messageType = autoReplyRuleInstance.messageType
        if(MessageType.IMAGE == messageType) {
            def materialId = autoReplyRuleInstance.materialId
            if(materialId) {
                def picture = Picture.get(materialId)
                if(picture) {
                    map.image = toPicture(picture)
                }
            }
        }
        else if(MessageType.NEWS == messageType) {
            def articleId = autoReplyRuleInstance.articleId
            if(articleId) {
                def article = Article.get(articleId)
                if(article) {
                    map.article = articleService.toArticle(article, false)
                }
            }
        }
        respond map
    }

    private def toPicture(Picture it){
        [id: it.id, name: it.name, path: it.path,
         url: materialManageService.serverUrl(it.url),
         type: it.type, description: it.description]
    }


    def create() {
        respond new AutoReplyRule(params)
    }

    @Transactional
    def save(AutoReplyRule autoReplyRuleInstance) {
        if (autoReplyRuleInstance == null) {
            notFound()
            return
        }
        if (autoReplyRuleInstance.hasErrors()) {
            respond autoReplyRuleInstance.errors, view: 'create'
            return
        }
        autoReplyRuleInstance.save failOnError: true
        if(AutoReplyRuleStatus.ACTIVATE == autoReplyRuleInstance.status) {
            autoReplyService.add(autoReplyRuleInstance)
        }
        respond autoReplyRuleInstance, [status: CREATED]
    }

    def edit(AutoReplyRule autoReplyRuleInstance) {
        respond autoReplyRuleInstance
    }

    @Transactional
    def update(AutoReplyRule autoReplyRuleInstance) {
        if (autoReplyRuleInstance == null) {
            notFound()
            return
        }
        if (autoReplyRuleInstance.hasErrors()) {
            respond autoReplyRuleInstance.errors, view: 'edit'
            return
        }
        autoReplyRuleInstance.save flush: true
        autoReplyService.refresh()
        respond autoReplyRuleInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def autoReplyRuleInstance = AutoReplyRule.get(params.id)
        if (autoReplyRuleInstance == null) {
            notFound()
            return
        }
        autoReplyService.remove(autoReplyRuleInstance)
        autoReplyRuleInstance.delete()
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}