package com.trunksoft.chat.services

class ListOpenIdsResult {
    Integer total
    Integer count
    List<String> openIds
    String nextOpenId

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("{total: " + total)
        sb.append(",count: " + count)
        sb.append(",openIds: " + openIds?.toString())
        sb.append(",nextOpenId: " + nextOpenId)
        sb.append("}")
    }
}
