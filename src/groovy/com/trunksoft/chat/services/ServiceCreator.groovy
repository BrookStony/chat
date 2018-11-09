package com.trunksoft.chat.services

public interface ServiceCreator {

    AccountService getAccountService()

    ChatGroupService getGroupService()

    ChatUserService getUserService()

    ChatMenuService getMenuService()

    ChatMaterialService getMaterialService()

    ChatMessageService getMessageService()

    SynchDomainService getSynchAllService()

}