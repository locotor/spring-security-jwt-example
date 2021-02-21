package cn.locotor.springsecurityjwtexample.common;

public class ResponseData {

    private String code = null;

    private String message = null;

    private Object data;

    public ResponseData() {
        this.code = CodeMessage.SUCCESS.getCode();
        this.message = CodeMessage.SUCCESS.getMessage();
        this.data = null;
    }

    public ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public ResponseData(CodeMessage message, Object data) {
        this.code = message.getCode();
        this.message = message.getMessage();
        this.data = data;
    }

    public ResponseData(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseData(Object data) {
        this.code = CodeMessage.SUCCESS.getCode();
        this.message = CodeMessage.SUCCESS.getMessage();
        this.data = data;
    }

    public static ResponseData build(CodeMessage message, Object data) {
        return new ResponseData(message, data);
    }

    public static ResponseData build(String code, String message, Object data) {
        return new ResponseData(code, message, data);
    }

    public static ResponseData build(CodeMessage message) {
        return new ResponseData(message, null);
    }

    public static ResponseData build(String code, String message) {
        return new ResponseData(code, message, null);
    }

    public static ResponseData ok(Object data) {
        return new ResponseData(data);
    }

    public static ResponseData ok() {
        return new ResponseData(null);
    }

    /**
     * 将返回数据设置为系统错误状态
     */
    public void change2SysError() {
        this.code = CodeMessage.SYS_ERROR.getCode();
        this.message = CodeMessage.SYS_ERROR.getMessage();
    }

    public void fail() {
        this.code = CodeMessage.FAIL.getCode();
        this.message = CodeMessage.FAIL.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCodeMessage(CodeMessage CodeMessage) {
        this.code = CodeMessage.getCode();
        this.message = CodeMessage.getMessage();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
