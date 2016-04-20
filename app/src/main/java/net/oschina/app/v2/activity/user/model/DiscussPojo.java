package net.oschina.app.v2.activity.user.model;

import org.json.JSONObject;

/**
 * 讨论
 */
public class DiscussPojo {

	private int id;
	private int uid;
	private String content;
	private String inputtime;
	private String userIcon;
	private String userName;
	private int hits;

	private int rank;
	private int ispraise;
	private int type;
	
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getIspraise() {
		return ispraise;
	}

	public void setIspraise(int ispraise) {
		this.ispraise = ispraise;
	}

	public static DiscussPojo parse(JSONObject response) {
		DiscussPojo intersted = new DiscussPojo();

		intersted.setId(response.optInt("id"));
		intersted.setContent(response.optString("content"));
		intersted.setInputtime(response.optString("inputtime"));
		intersted.setUid(response.optInt("uid"));
		intersted.setUserIcon(response.optString("head"));
		intersted.setUserName(response.optString("nickname"));
		intersted.setRank(response.optInt("rank"));
		intersted.setHits(response.optInt("hits"));
		intersted.setIspraise(response.optInt("ispraise"));
		intersted.setType(response.optInt("type"));
		return intersted;
	}
}
