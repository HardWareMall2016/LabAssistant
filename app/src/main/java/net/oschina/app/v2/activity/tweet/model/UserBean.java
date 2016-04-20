package net.oschina.app.v2.activity.tweet.model;

import net.oschina.app.v2.model.Base;

import org.json.JSONObject;

public class UserBean extends Base {

	private int id;
	private int uid;
	private int aid;
	private int intputtime;
	private String nickname;
	private String head;
	private String company;
	private int realname_status;
	private int rank;
	private int supporNum;

	private int type;
	private int same ;
	
	

	public int getSame() {
		return same;
	}

	public void setSame(int same) {
		this.same = same;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getIntputtime() {
		return intputtime;
	}

	public void setIntputtime(int intputtime) {
		this.intputtime = intputtime;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public int getSupporNum() {
		return supporNum;
	}
	public void setSupporNum(int supporNum) {
		this.supporNum = supporNum;
	}

	public static UserBean parse(JSONObject userinfo) {
		UserBean user = new UserBean();
		user.setId(userinfo.optInt("id"));
		user.setUid(userinfo.optInt("uid"));
		user.setAid(userinfo.optInt("aid"));
		user.setIntputtime(userinfo.optInt("intputtime"));
		user.setNickname(userinfo.optString("nickname"));
		user.setHead(userinfo.optString("head"));
		user.setCompany(userinfo.optString("company"));
		user.setRealname_status(userinfo.optInt("realname_status"));
		user.setRank(userinfo.optInt("rank"));
		user.setType(userinfo.optInt("type"));
		user.setSupporNum(userinfo.optInt("supportnum"));
		user.setSame(userinfo.optInt("same"));
		return user;
	}
}
