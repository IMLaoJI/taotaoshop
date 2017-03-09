package com.taotao.portal.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class demo {
    
    String keyWords;
    Integer page; 
    public String getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    @Override
    public String toString() {
        return "demo [keyWords=" + keyWords + ", page=" + page + ", rows=" + rows + "]";
    }
    Integer rows;

}
