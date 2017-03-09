package com.taotao.common.pojo;
/**
 * easyUI树形控件节点格式
 * <p>Title: EUTreeNode</p>
 * <p>Description: </p>
 * @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */
public class EUTreeNode {

	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
