package com.trunksoft.platform.common

import com.trunksoft.platform.type.LocationType
import com.trunksoft.platform.type.LocationKind
import com.trunksoft.platform.type.LocationGrade

class LocationInitService {

    static transactional = false

    def init() {
        log.info(" initLocations start...")
        try {
            if (!Location.count()) {
                def datas = []
                getClass().getResourceAsStream('/data/location.csv').text.eachLine {
                    String[] split = it.split(',')
                    if (split.length == 7) {
                        datas << [code: split[0] , name: split[1], fullname: split[2], type: split[3], kind: split[4], grade: split[5], parentId: split[6]]
                    }
                }

                initCountries(datas)
                initAreas(datas)
                initProvinces(datas)
                initCities(datas)
                initDistricts(datas)
            }
        }
        catch (Exception e) {
            println e.message
//            e.printStackTrace()
        }
        log.info(" initLocations end.")
    }

    private void initCountries(datas) {
        log.info(" initCountries start...")
        for(def data : datas) {
            if(data.type == "CA"){
                saveLocation(data, LocationType.COUNTRY)
                return
            }
        }
    }

    private void initAreas(datas) {
        log.info(" initAreas start...")
        for(def data : datas) {
            if(data.type == "A"){
                saveLocation(data, LocationType.AREA)
            }
            else if(data.type == "P"){
                return
            }
        }
    }

    private void initProvinces(datas) {
        log.info(" initProvinces start...")
        for(def data : datas) {
            if(data.type == "P"){
                saveLocation(data, LocationType.PROVINCE)
            }
        }
    }

    private void initCities(datas) {
        log.info(" initCities start...")
        for(def data : datas) {
            if(data.type == "C"){
                saveLocation(data, LocationType.CITY)
            }
        }
    }

    private void initDistricts(datas) {
        log.info(" initDistricts start...")
        for(def data : datas) {
            if(data.type == "D"){
                saveLocation(data, LocationType.DISTRICT)
            }
        }
    }

    private void saveLocation(data, LocationType type){
        def location = new Location(code: data.code, name: data.name, fullname: data.fullname)
        location.id = data.code as Long
        location.sortno = data.code as Integer
        location.type = type
        location.kind = toKind(data.kind)
        location.grade = LocationGrade.codeOf(data.grade as Integer)
        if(data.parentId && "null" != data.parentId.trim()){
            location.parent = Location.proxy(data.parentId as Long)
        }
        if(LocationType.DISTRICT == type){
            location.save(failOnError: true)
        }
        else {
            location.save(flush: true, failOnError: true)
        }
    }

    private LocationKind toKind(kind) {
        if("C".equals(kind)){
            return LocationKind.CAPITAL
        }
        else if("M".equals(kind)){
            return LocationKind.MAJOR
        }
        return LocationKind.COMMON
    }

}
