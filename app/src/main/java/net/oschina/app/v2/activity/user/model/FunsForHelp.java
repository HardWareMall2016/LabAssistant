package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.io.Serializable;

import net.oschina.app.v2.AppException;

import org.json.JSONObject;

public class FunsForHelp implements Serializable {
	private static final long serialVersionUID = 2108698718855107102L;

	private int id;
	private int catid;
	private int uid;
	private String content;
	private String lable;
	private String reward;
	private String superlist;
	private int issolveed;
	private int allow_comment;
	private int intputtime;
	private int anum;
	private String nickname;
	private String head;
	private int rank;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getSuperlist() {
		return superlist;
	}

	public void setSuperlist(String superlist) {
		this.superlist = superlist;
	}

	public int getIssolveed() {
		return issolveed;
	}

	public void setIssolveed(int issolveed) {
		this.issolveed = issolveed;
	}

	public int getAllow_comment() {
		return allow_comment;
	}

	public void setAllow_comment(int allow_comment) {
		this.allow_comment = allow_comment;
	}

	public int getIntputtime() {
		return intputtime;
	}

	public void setIntputtime(int intputtime) {
		this.intputtime = intputtime;
	}

	public int getAnum() {
		return anum;
	}

	public void setAnum(int anum) {
		this.anum = anum;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public static FunsForHelp parse(JSONObject response) throws IOException,
			AppException {
		FunsForHelp forHelp = new FunsForHelp();
		forHelp.setId(response.optInt("id"));
		forHelp.setCatid(response.optInt("catid"));
		forHelp.setUid(response.optInt("uid"));
		forHelp.setContent(response.optString("content"));
		forHelp.setLable(response.optString("lable"));
		forHelp.setReward(response.optString("reward"));
		forHelp.setSuperlist(response.optString("superlist"));
		forHelp.setIssolveed(response.optInt("issolveed"));
		forHelp.setAllow_comment(response.optInt("allow_comment"));
		forHelp.setIntputtime(response.optInt("intputtime"));
		forHelp.setAnum(response.optInt("anum"));
		forHelp.setNickname(response.optString("nickname"));
		forHelp.setHead(response.optString("head"));
		forHelp.setRank(response.optInt("rank"));
		return forHelp;
	}

}
