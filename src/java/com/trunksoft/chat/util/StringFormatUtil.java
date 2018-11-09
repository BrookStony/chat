package com.trunksoft.chat.util;

public class StringFormatUtil {

    private static final int DAY_SECONDS = 24 * 3600;
    private static final int HOUR_SECONDS = 3600;
    private static final int MINUTE_SECONDS = 60;

    public static String formatCountTime(long times) {
        int seconds = Long.valueOf(times).intValue() / 1000;
        StringBuilder sb = new StringBuilder();
        int days = seconds / DAY_SECONDS;
        if (days > 0) {
            sb.append(days);
            sb.append("天");
            seconds = seconds % DAY_SECONDS;
        }
        int hours = seconds / HOUR_SECONDS;
        if (hours > 0) {
            sb.append(hours);
            sb.append("小时");
            seconds = seconds % HOUR_SECONDS;
        }
        int minutes = seconds / MINUTE_SECONDS;
        if (minutes > 0) {
            sb.append(minutes);
            sb.append("分钟");
            seconds = seconds % MINUTE_SECONDS;
        }
        if (seconds >= 0) {
            sb.append(seconds);
            sb.append("秒");
        }
        return sb.toString();
    }

    public static String formatMemberNo(Integer no) {
        if(null != no) {
            if(no / 10 < 1){
                return "00000" + no;
            }
            else if(no / 100 < 1){
                return "0000" + no;
            }
            else if(no / 1000 < 1){
                return "000" + no;
            }
            else if(no / 10000 < 1){
                return "00" + no;
            }
            else if(no / 100000 < 1){
                return "0" + no;
            }
            else {
                return no.toString();
            }
        }
        return "";
    }
}
