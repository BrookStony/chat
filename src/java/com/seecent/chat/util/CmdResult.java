package com.seecent.chat.util;

import java.io.Serializable;

public class CmdResult implements Serializable {

    public static final int OK = 0;
    public static final int FAIL = 1;
    public static final int TIMEOUT = 2;
    public static final int EXCEPTION = 3;
    public static final int ERROR = 4;

    private int code = FAIL;
    private String message;
    private String result;
    private long startTime;
    private long endTime;

    public CmdResult() {
        this.startTime = System.currentTimeMillis();
    }

    public CmdResult(int code, String message, String result) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.startTime = System.currentTimeMillis();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.endTime = System.currentTimeMillis();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        this.endTime = System.currentTimeMillis();
        if(null != result && !result.trim().equals("")){
            this.code = OK;
        }
    }

    public boolean isOk() {
        return OK == code;
    }

    public boolean isTimeout() {
        return TIMEOUT == code;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CmdResult{");
        sb.append("code: ");
        sb.append(code);
        if(null == result || result.trim().equals("")) {
            sb.append(", message: ");
            sb.append(message);
        }
        sb.append(", result: ");
        sb.append(result);
        sb.append(", costTime: ");
        sb.append(endTime - startTime);
        sb.append("}");
        return sb.toString();
    }
}
