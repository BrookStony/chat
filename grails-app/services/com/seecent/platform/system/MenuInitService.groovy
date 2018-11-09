package com.seecent.platform.system

import com.seecent.chat.util.XmlUtil

class MenuInitService {

    static transactional = false

    public void initFromXml(xmlName) {
        try {
            def menusNode = new XmlSlurper().parse(MenuInitService.class.getResourceAsStream("/data/${xmlName}"))

            menusNode.menu?.each { menuNode ->
                def menu = createMenu(null, menuNode)
                if(menu && menuNode.menuitem){
                    initSubMenus(menu, menuNode)
                }
            }
        } catch (Exception e) {
            log.error(" init error with xml:" + xmlName, e)
            e.printStackTrace()
        }
    }

    private void initSubMenus(Menu parent, parentMenuNode) {
        try {
            parentMenuNode.menuitem?.each { menuNode ->
                def menu = createMenu(parent, menuNode)
                if (parent.level < 3 && menu && menuNode.menuitem) {
                    initSubMenus(menu, menuNode)
                }
            }
        } catch (Exception e) {
            log.error(" initSubMenus error with xml:" + parent.code, e)
            e.printStackTrace()
        }
    }

    private Menu createMenu(Menu parent, menuNode) {
        Long id = XmlUtil.attrLongValue(menuNode, "id")
        String code = XmlUtil.attrValue(menuNode, "code")
        if (null != id) {
            def menu = Menu.get(id)
            if (null == menu) {
                menu = new Menu(code: code)
                menu.id = id
            }
            menu.parent = parent
            if (null == parent) {
                menu.level = 1
            } else {
                menu.level = parent.level + 1
            }
            menu.code = code
            menu.sortno = XmlUtil.intValue(menuNode, "no", 1)
            menu.name = XmlUtil.attrValue(menuNode, "name")
            menu.url = XmlUtil.attrValue(menuNode, "url")
            menu.image = XmlUtil.attrValue(menuNode, "image")
            menu.cssicon = XmlUtil.attrValue(menuNode, "icon")
            menu.save(failOnError: true)
            return menu
        }
        return null
    }
}
