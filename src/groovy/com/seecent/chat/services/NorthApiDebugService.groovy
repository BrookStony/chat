package com.seecent.chat.services

import com.seecent.chat.services.exception.ApiErrorException

public interface NorthApiDebugService {
    def post(String url, String json, params) throws ApiErrorException
    def get(String url, params) throws ApiErrorException
}