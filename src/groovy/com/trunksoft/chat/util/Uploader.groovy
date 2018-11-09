package com.trunksoft.chat.util

import sun.misc.BASE64Decoder

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat

class Uploader {
    // 输出文件地址
    private String url = "";
    // 上传文件名
    private String fileName = "";
    // 状态
    private String state = "";
    // 文件类型
    private String type = "";
    // 原始文件名
    private String originalName = "";
    // 文件大小
    private long size = 0;

    private HttpServletRequest request = null;
    private String title = "";

    // 保存路径
    private String savePath = "upload";
    // 文件允许格式
    private String[] allowFiles = [".rar", ".doc", ".docx", ".zip", ".pdf",".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp" ];
    // 文件大小限制，单位KB
    private int maxSize = 10000;

    private HashMap<String, String> errorInfo = new HashMap<String, String>();

    public Uploader() {
        HashMap<String, String> tmp = this.errorInfo;
        tmp.put("SUCCESS", "SUCCESS"); //默认成功
        tmp.put("NOFILE", "未包含文件上传域");
        tmp.put("TYPE", "不允许的文件格式");
        tmp.put("SIZE", "图片大小超出300K");
        tmp.put("ENTYPE", "请求类型ENTYPE错误");
        tmp.put("REQUEST", "上传请求异常");
        tmp.put("IO", "IO异常");
        tmp.put("DIR", "目录创建失败");
        tmp.put("UNKNOWN", "未知错误");
    }

    /**
     * 接受并保存以base64格式上传的文件
     * @param fieldName
     */
    public void uploadBase64(String fieldName){
        String savePath = this.getFolder(this.savePath);
        String base64Data = this.request.getParameter(fieldName);
        this.fileName = this.getName("test.png");
        this.url = savePath + "/" + this.fileName;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            File outFile = new File(this.getPhysicalPath(this.url));
            OutputStream ro = new FileOutputStream(outFile);
            byte[] b = decoder.decodeBuffer(base64Data);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            ro.write(b);
            ro.flush();
            ro.close();
            this.state=this.errorInfo.get("SUCCESS");
        } catch (Exception e) {
            this.state = this.errorInfo.get("IO");
        }
    }

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 依据原始文件名生成新文件名
     * @return
     */
    private String getName(String fileName) {
        Random random = new Random();
        return this.fileName = "" + random.nextInt(10000) + System.currentTimeMillis() + this.getFileExt(fileName);
    }

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     * @param path
     * @return
     */
    private String getFolder(String path) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        path += "/" + formater.format(new Date());
        return path;
    }

    /**
     * 根据传入的虚拟路径获取物理路径
     *
     * @param path
     * @return
     */
    private String getPhysicalPath(String path) {
        String servletPath = this.request.getServletPath();
        String realPath = this.request.getSession().getServletContext()
                .getRealPath(servletPath);
        return new File(realPath).getParent() +"/" +path;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setAllowFiles(String[] allowFiles) {
        this.allowFiles = allowFiles;
    }

    public void setMaxSize(int size) {
        this.maxSize = size;
    }

    public long getSize() {
        return this.size;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getState() {
        return this.state;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getOriginalName() {
        return this.originalName;
    }
}
