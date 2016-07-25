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
	private String type;
	private String status;
	private String qid;
	private String quid;
	private String aid;
	private String auid;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getQuid() {
		return quid;
	}

	public void setQuid(String quid) {
		this.quid = quid;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAuid() {
		return auid;
	}

	public void setAuid(String auid) {
		this.auid = auid;
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
		xiTongXiaoXi.setType(response.optString("type"));
		xiTongXiaoXi.setStatus(response.optString("status"));
		xiTongXiaoXi.setQid(response.optString("qid"));
		xiTongXiaoXi.setQuid(response.optString("quid"));
		xiTongXiaoXi.setAid(response.optString("aid"));
		xiTongXiaoXi.setAuid(response.optString("auid"));
		return xiTongXiaoXi;
	}
}
