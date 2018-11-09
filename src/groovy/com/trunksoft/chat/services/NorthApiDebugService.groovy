package com.trunksoft.chat.services

import com.trunksoft.chat.services.exception.ApiErrorException

public interface NorthApiDebugService {
    def post(String url, String json, params) throws ApiErrorException
    def get(String url, params) throws ApiErrorException
}