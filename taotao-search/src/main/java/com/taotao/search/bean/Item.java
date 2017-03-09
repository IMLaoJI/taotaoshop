package com.taotao.search.bean;

import org.apache.solr.client.solrj.beans.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)//忽略为知（未匹配）的属性
public class Item{

    @Field("id")
    private Long id;

    @Field
    private String title;

    @Field
    private String sellPoint;

    @Field
    private Long price;

    private Integer num;

    private String barcode;

    @Field
    private String image;

    @Field
    private Long cid;

    @Field
    private Integer status;
    
   

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", title=" + title + ", sellPoint=" + sellPoint + ", price=" + price
                + ", num=" + num + ", barcode=" + barcode + ", image=" + image + ", cid=" + cid + ", status="
                + status + "]";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
