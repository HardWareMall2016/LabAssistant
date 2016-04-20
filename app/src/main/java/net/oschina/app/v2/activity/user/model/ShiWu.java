package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ShiWu implements Serializable {
	private static final long serialVersionUID = -3109268294644824027L;
	private int code;
	private String desc;
	private boolean data;
	private int id;
	private int proid;// 礼物id
	private String name;// 名称
	private String thumb;// 礼物图片
	private int integral;// 所需积分
	private int inputtime;// 添加时间
	private int status;//状态

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

	public int getProid() {
		return proid;
	}

	public void setProid(int proid) {
		this.proid = proid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getInputtime() {
		return inputtime;
	}

	public void setInputtime(int inputtime) {
		this.inputtime = inputtime;
	}

	public static ShiWu parse(JSONObject response) throws IOException,
			AppException {
		ShiWu shiWu = new ShiWu();
		try {
			// 问题ID
			shiWu.setName(response.getString("name"));// 问题的内容
			shiWu.setThumb(response.getString("thumb"));// 图片路径
			shiWu.setIntegral(response.getInt("integral"));// 所需要的积分
			shiWu.setInputtime(response.getInt("inputtime"));// 时间
			shiWu.setProid(response.optInt("proid"));
			shiWu.setId(response.optInt("id"));
			shiWu.setStatus(response.optInt("status"));
			
			String statusResult="成功";
			switch(shiWu.getStatus()){
			case 0:
				statusResult="处理中";
				break;
			case 1:
				statusResult="成功";
				break;
			case 2:
				statusResult="未通过";
				break;
			}
			String desc = String.format("于%s",
					DateUtil.getCnFormatTime(shiWu.getInputtime()))+"申请兑换 "+statusResult;
			shiWu.setDesc(desc);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return shiWu;
	}
}
