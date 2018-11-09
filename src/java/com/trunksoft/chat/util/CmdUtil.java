package com.trunksoft.chat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CmdUtil {

    private static final int SLEEP_TIME = 1000;
    private static final int READ_BUFFER_SIZE = 1024;

    public static CmdResult cmd(String command, String waitfor, int timeout) {
        return cmd(command, waitfor, null, timeout, null);
    }

    public static CmdResult cmd(String command, String waitfor, int timeout, String charset) {
        return cmd(command, waitfor, null, timeout, charset);
    }

    public static CmdResult cmd(String command, String waitfor, List<String> endList, int timeout) {
        return cmd(command, waitfor, endList, timeout, null);
    }

    public static CmdResult cmd(String command, String waitfor, List<String> endList, int timeout, String charset) {

        CmdResult cmdResult = new CmdResult();
        try {
            Process process;
            String osname = System.getProperty("os.name");
            if(osname.contains("Windows")){
                command = "cmd /k " + command;
                process = Runtime.getRuntime().exec(command);
            }
            else {
                command = command.replaceAll("\"", "");
                String[] commands = command.split("\\s+");
                process = Runtime.getRuntime().exec(commands);
            }

            InputStream in = process.getInputStream();
            InputStream errorIn = process.getErrorStream();

            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            long start = System.currentTimeMillis();
            while (true) {
                long used = System.currentTimeMillis() - start;
                if (used > timeout) {
                    cmdResult.setCode(CmdResult.TIMEOUT);
                    String error = readError(errorIn, charset);
                    cmdResult.setMessage(error);
                    cmdResult.setResult(sb.toString());
                    return cmdResult;
                }

                int num = -1;
                try {
                    num = in.available();
                } catch (IOException ioex) {
                    cmdResult.setCode(CmdResult.EXCEPTION);
                    cmdResult.setMessage(ioex.getMessage());
                    return cmdResult;
                }

                // 检查是否有数据
                if (num <= 0) {
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException ex) {
                        cmdResult.setCode(CmdResult.EXCEPTION);
                        cmdResult.setMessage(ex.getMessage());
                        return cmdResult;
                    }
                    continue;
                }

                int len = -1;
                try {
                    len = in.read(buffer);
                    // 判断读取状态
                    if (-1 == len) {
                        break;
                    }
                } catch (IOException ioex) {
                    cmdResult.setCode(CmdResult.EXCEPTION);
                    cmdResult.setMessage(ioex.getMessage());
                    ioex.printStackTrace();
                }

                // 添加返回数据
                if(null != charset && charset.length() > 0){
                    String str = new String(buffer, 0, len, charset);
                    sb.append(str);
                }
                else {
                    String str = new String(buffer, 0, len);
                    sb.append(str);
                }

                if(errorIn.available() > 0){
                    String error = readError(errorIn, charset);
                    cmdResult.setCode(CmdResult.ERROR);
                    cmdResult.setMessage(error);
                }

                // 检查结束waitfor
                if (-1 != sb.lastIndexOf(waitfor)) {
                    break;
                }

                if(null != endList) {
                    boolean isEnd = false;
                    for(String endFlag : endList){
                        if (-1 != sb.lastIndexOf(endFlag)) {
                            isEnd = true;
                            break;
                        }
                    }
                    if(isEnd) {
                        break;
                    }
                }
            }
            cmdResult.setCode(CmdResult.OK);
            cmdResult.setResult(sb.toString());
            return cmdResult;
        }
        catch (IOException ioex) {
            cmdResult.setCode(CmdResult.EXCEPTION);
            cmdResult.setMessage(ioex.getMessage());
            ioex.printStackTrace();
        }
        return cmdResult;
    }

    public static String readError(InputStream in, String charset) {
        try {
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            int len = in.read(buffer);
            if (len > 0) {
                if(null != charset && charset.length() > 0){
                    String str = new String(buffer, 0, len, charset);
                    sb.append(str);
                }
                else {
                    String str = new String(buffer, 0, len);
                    sb.append(str);
                }
                return sb.toString();
            }
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> endList = new ArrayList<String>();
        endList.add("errcode");
        endList.add("media_id");
        CmdResult cmdResult = CmdUtil.cmd("curl -F media=@D:\\soft\\tomcat-sem\\webapps\\chat-assets\\IDNMS-SIN\\images\\20141106\\1321301W2-4.jpg \"http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=Vku0vdlZiOthkX888nLlBhEXvoZpWmx1Ms-onmGoo6LSu6pkKTA4b0_Bp2V2UYK8_XzXOFRd8uAQzaxfI_X_KyHdjYXnXE6hBWaY41b5YZQ&type=image\"", ">", 50000, "GBK");
        System.out.println(cmdResult.toString());
    }

}

