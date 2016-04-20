package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

/**
 * @author acer 系统消息实体
 */
public class XiTongXiaoXi implements Serializable {
	private static final long serialVersionUID = 5879049647887930690L;
	private int code;
	private String desc;
	private boolean data;
	private int id;
	private String valname;
	private String content;
	private String inputtime;
	private String description;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getValname() {
		return valname;
	}

	public void setValname(String valname) {
		this.valname = valname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	// 解析json得到一个实体的对象
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static XiTongXiaoXi parse(JSONObject response) throws IOException,
			AppException {
		XiTongXiaoXi xiTongXiaoXi = new XiTongXiaoXi();
		xiTongXiaoXi.setId(response.optInt("id"));
		xiTongXiaoXi.setData(response.optBoolean("data"));
		xiTongXiaoXi.setValname(response.optString("valname"));
		xiTongXiaoXi.setContent(response.optString("content"));
		xiTongXiaoXi.setInputtime(response.optString("inputtime"));
		xiTongXiaoXi.setDescription(response.optString("description"));
		return xiTongXiaoXi;
	}
}
