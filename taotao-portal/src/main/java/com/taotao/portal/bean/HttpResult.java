package com.taotao.portal.bean;

public class HttpResult {
    
    //响应状态码
    private Integer code;
    
    //响应内容
    private String content;
    
    public HttpResult(){
        
    }

    public HttpResult(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    

}
