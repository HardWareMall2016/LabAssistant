package net.oschina.app.v2.activity.user.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author acer
 * 关注消息
 */
public class Attention {
	private int code;
	private String desc;
	private boolean data;
	private int id;
	private int tuid;
	private int inputtime;
	private String nickname;
	private String head;
	private String company;
	private int realname_status;
	private int rank;
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
	public int getTuid() {
		return tuid;
	}
	public void setTuid(int tuid) {
		this.tuid = tuid;
	}
	public int getInputtime() {
		return inputtime;
	}
	public void setInputtime(int inputtime) {
		this.inputtime = inputtime;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getRealname_status() {
		return realname_status;
	}
	public void setRealname_status(int realname_status) {
		this.realname_status = realname_status;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

	public static Attention parse(JSONObject response) throws IOException, AppException {
		/*Answer answer=new Answer();*/
		Attention attention=new Attention();
		try {
			//问题ID
		attention.setCode(response.getInt("code"));
		attention.setDesc(response.getString("desc"));
		attention.setData(response.getBoolean("data"));
		attention.setId(response.getInt("id"));
		attention.setTuid(response.getInt("tuid"));
		attention.setInputtime(response.getInt("inputtime"));
		attention.setNickname(response.getString("nickname"));
		attention.setHead(response.getString("head"));
		attention.setCompany(response.getString("company"));
		attention.setRealname_status(response.getInt("realname_status"));
		attention.setRank(response.getInt("rank"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return attention;       
	}
}
