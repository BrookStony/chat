
class BootStrap {

    def initService

    def init = { servletContext ->

        initService.init()

    }

    def destroy = {
    }
}
