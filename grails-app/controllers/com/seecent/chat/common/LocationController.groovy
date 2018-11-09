package com.seecent.chat.common

import com.seecent.platform.common.Location
import com.seecent.platform.type.LocationType
import grails.converters.JSON

class LocationController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = Location.createCriteria().list(params) {
            if(params.name) {
                like("name", "%${params.name}%".toString())
            }
        }
        def locationCount = result.totalCount
        def locationList = result.collect {
            toLocationMap(it)
        }
        response.setHeader('totalItems', locationCount as String)
        respond locationList
    }

    def search() {
        def type = params.type as String
        def locationType = LocationType.COUNTRY
        if(type) {
            locationType = LocationType.valueOf(type)
        }
        def parentId = params.long("parentId")
        def result = Location.createCriteria().list(params) {
            if(type) {
                eq("type", locationType)
            }
            if(parentId) {
                if(LocationType.PROVINCE == locationType) {
                    parent {
                        parent {
                            eq("id", parentId)
                        }
                    }
                }
                else {
                    parent {
                        eq("id", parentId)
                    }
                }
            }
            order("code")
        }
        def locationCount = result.totalCount
        def locationList = result.collect {
            toLocationMap(it)
        }
        response.setHeader('totalItems', locationCount as String)
        respond locationList
    }

    private def toLocationMap(Location it) {
        return [id: it.id, name: it.name, fullName: it.fullname,
                code: it.code, type: it.type.name()]
    }

}
